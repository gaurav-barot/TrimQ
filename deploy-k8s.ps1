<#
.SYNOPSIS
    TrimQ Kubernetes Deployment Script for Docker Desktop

.DESCRIPTION
    This script deploys the TrimQ platform to a local Kubernetes cluster
    running on Docker Desktop.

.PARAMETER Action
    The action to perform: deploy, delete, status, logs, build

.PARAMETER Service
    Specific service to target (optional)

.PARAMETER BuildLocal
    Build Docker images locally instead of pulling from GHCR

.EXAMPLE
    .\deploy-k8s.ps1 -Action deploy
    .\deploy-k8s.ps1 -Action delete
    .\deploy-k8s.ps1 -Action status
    .\deploy-k8s.ps1 -Action logs -Service api-gateway
    .\deploy-k8s.ps1 -Action build -Service user-service

.NOTES
    Author: TrimQ Team
    Requires: Docker Desktop with Kubernetes enabled
#>

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("deploy", "delete", "status", "logs", "build", "restart", "port-forward")]
    [string]$Action,
    
    [Parameter(Mandatory=$false)]
    [ValidateSet("api-gateway", "user-service", "shop-service", "booking-service", 
                 "payment-service", "notification-service", "nginx", "all")]
    [string]$Service = "all",
    
    [Parameter(Mandatory=$false)]
    [switch]$BuildLocal
)

# Configuration
$NAMESPACE_APP = "trimq-app"
$NAMESPACE_DATA = "trimq-data"
$NAMESPACE_MONITORING = "trimq-monitoring"
$IMAGE_PREFIX = "trimq"

# Colors for output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Header($message) {
    Write-Host ""
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host " $message" -ForegroundColor Cyan
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host ""
}

function Write-Success($message) {
    Write-Host "[OK] $message" -ForegroundColor Green
}

function Write-Info($message) {
    Write-Host "[INFO] $message" -ForegroundColor Yellow
}

function Write-Error($message) {
    Write-Host "[ERROR] $message" -ForegroundColor Red
}

# Check prerequisites
function Test-Prerequisites {
    Write-Header "Checking Prerequisites"
    
    # Check kubectl
    if (!(Get-Command kubectl -ErrorAction SilentlyContinue)) {
        Write-Error "kubectl is not installed or not in PATH"
        exit 1
    }
    Write-Success "kubectl is available"
    
    # Check Docker
    if (!(Get-Command docker -ErrorAction SilentlyContinue)) {
        Write-Error "Docker is not installed or not in PATH"
        exit 1
    }
    Write-Success "Docker is available"
    
    # Check Kubernetes context
    $context = kubectl config current-context
    Write-Info "Current Kubernetes context: $context"
    
    # Check if Kubernetes is running
    $nodes = kubectl get nodes 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Kubernetes cluster is not running. Please start Docker Desktop and enable Kubernetes."
        exit 1
    }
    Write-Success "Kubernetes cluster is running"
}

# Build Docker images locally
function Build-LocalImages {
    param([string]$ServiceName)
    
    Write-Header "Building Docker Images Locally"
    
    $services = @()
    if ($ServiceName -eq "all") {
        $services = @("api-gateway", "user-service", "shop-service", 
                      "booking-service", "payment-service", "notification-service")
    } else {
        $services = @($ServiceName)
    }
    
    foreach ($svc in $services) {
        Write-Info "Building $svc..."
        
        # Build with Maven first
        Write-Info "Compiling $svc with Maven..."
        & ./mvnw package -pl "services/$svc" -am -DskipTests -B
        if ($LASTEXITCODE -ne 0) {
            Write-Error "Maven build failed for $svc"
            continue
        }
        
        # Build Docker image
        Write-Info "Building Docker image for $svc..."
        docker build -t "${IMAGE_PREFIX}/${svc}:latest" -f "services/$svc/Dockerfile" .
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Built ${IMAGE_PREFIX}/${svc}:latest"
        } else {
            Write-Error "Docker build failed for $svc"
        }
    }
}

# Deploy to Kubernetes
function Deploy-TrimQ {
    Write-Header "Deploying TrimQ Platform"
    
    if ($BuildLocal) {
        Build-LocalImages -ServiceName $Service
        
        # Update deployments to use local images
        Write-Info "Note: Using locally built images. Make sure imagePullPolicy is set to 'Never' or 'IfNotPresent'"
    }
    
    # Apply namespaces first
    Write-Info "Creating namespaces..."
    kubectl apply -f k8s/namespace.yaml
    Start-Sleep -Seconds 2
    
    # Apply secrets
    Write-Info "Applying secrets..."
    kubectl apply -f k8s/secrets/
    
    # Apply configmaps
    Write-Info "Applying configmaps..."
    kubectl apply -f k8s/configmaps/
    
    # Deploy data layer
    Write-Header "Deploying Data Layer"
    kubectl apply -f k8s/data/
    
    Write-Info "Waiting for data services to be ready..."
    kubectl wait --for=condition=ready pod -l app=postgresql -n $NAMESPACE_DATA --timeout=120s 2>$null
    kubectl wait --for=condition=ready pod -l app=redis -n $NAMESPACE_DATA --timeout=60s 2>$null
    kubectl wait --for=condition=ready pod -l app=kafka -n $NAMESPACE_DATA --timeout=120s 2>$null
    
    # Deploy application layer
    Write-Header "Deploying Application Layer"
    kubectl apply -f k8s/deployments/
    
    # Deploy monitoring
    Write-Header "Deploying Monitoring Stack"
    kubectl apply -f k8s/monitoring/
    
    Write-Success "Deployment initiated!"
    Write-Info "Run '.\deploy-k8s.ps1 -Action status' to check deployment status"
}

# Delete deployment
function Remove-TrimQ {
    Write-Header "Removing TrimQ Platform"
    
    $confirm = Read-Host "Are you sure you want to delete all TrimQ resources? (yes/no)"
    if ($confirm -ne "yes") {
        Write-Info "Deletion cancelled"
        return
    }
    
    Write-Info "Deleting application layer..."
    kubectl delete -f k8s/deployments/ --ignore-not-found
    
    Write-Info "Deleting monitoring..."
    kubectl delete -f k8s/monitoring/ --ignore-not-found
    
    Write-Info "Deleting data layer..."
    kubectl delete -f k8s/data/ --ignore-not-found
    
    Write-Info "Deleting configmaps and secrets..."
    kubectl delete -f k8s/configmaps/ --ignore-not-found
    kubectl delete -f k8s/secrets/ --ignore-not-found
    
    $deleteNamespaces = Read-Host "Delete namespaces as well? (yes/no)"
    if ($deleteNamespaces -eq "yes") {
        kubectl delete -f k8s/namespace.yaml --ignore-not-found
    }
    
    Write-Success "TrimQ platform removed"
}

# Show status
function Show-Status {
    Write-Header "TrimQ Platform Status"
    
    Write-Host "`n--- Data Layer ($NAMESPACE_DATA) ---" -ForegroundColor Cyan
    kubectl get pods -n $NAMESPACE_DATA -o wide
    
    Write-Host "`n--- Application Layer ($NAMESPACE_APP) ---" -ForegroundColor Cyan
    kubectl get pods -n $NAMESPACE_APP -o wide
    
    Write-Host "`n--- Monitoring ($NAMESPACE_MONITORING) ---" -ForegroundColor Cyan
    kubectl get pods -n $NAMESPACE_MONITORING -o wide
    
    Write-Host "`n--- Services ---" -ForegroundColor Cyan
    kubectl get svc -n $NAMESPACE_APP
    kubectl get svc -n $NAMESPACE_MONITORING
    
    Write-Host "`n--- Access URLs ---" -ForegroundColor Green
    Write-Host "  TrimQ App:    http://localhost:30080"
    Write-Host "  API Gateway:  http://localhost:30080/api/"
    Write-Host "  Prometheus:   http://localhost:30090"
    Write-Host "  Grafana:      http://localhost:30030  (admin/admin123)"
}

# Show logs
function Show-Logs {
    param([string]$ServiceName)
    
    if ($ServiceName -eq "all") {
        Write-Error "Please specify a service name with -Service parameter"
        return
    }
    
    Write-Header "Logs for $ServiceName"
    kubectl logs -n $NAMESPACE_APP -l app=$ServiceName --tail=100 -f
}

# Restart deployment
function Restart-Deployment {
    param([string]$ServiceName)
    
    Write-Header "Restarting Deployment(s)"
    
    if ($ServiceName -eq "all") {
        Write-Info "Restarting all deployments..."
        kubectl rollout restart deployment -n $NAMESPACE_APP
    } else {
        Write-Info "Restarting $ServiceName..."
        kubectl rollout restart deployment/$ServiceName -n $NAMESPACE_APP
    }
    
    Write-Success "Restart initiated"
}

# Port forward for debugging
function Start-PortForward {
    param([string]$ServiceName)
    
    $portMappings = @{
        "api-gateway" = "8080:8080"
        "user-service" = "8081:8081"
        "shop-service" = "8082:8082"
        "booking-service" = "8083:8083"
        "payment-service" = "8084:8084"
        "notification-service" = "8085:8085"
    }
    
    if ($ServiceName -eq "all" -or !$portMappings.ContainsKey($ServiceName)) {
        Write-Error "Please specify a valid service name"
        Write-Info "Available services: api-gateway, user-service, shop-service, booking-service, payment-service, notification-service"
        return
    }
    
    $ports = $portMappings[$ServiceName]
    Write-Info "Port forwarding $ServiceName on $ports..."
    Write-Info "Press Ctrl+C to stop"
    kubectl port-forward -n $NAMESPACE_APP svc/$ServiceName $ports
}

# Main execution
Test-Prerequisites

switch ($Action) {
    "deploy" {
        Deploy-TrimQ
    }
    "delete" {
        Remove-TrimQ
    }
    "status" {
        Show-Status
    }
    "logs" {
        Show-Logs -ServiceName $Service
    }
    "build" {
        Build-LocalImages -ServiceName $Service
    }
    "restart" {
        Restart-Deployment -ServiceName $Service
    }
    "port-forward" {
        Start-PortForward -ServiceName $Service
    }
}

