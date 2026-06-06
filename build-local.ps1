<#
.SYNOPSIS
    Build TrimQ services locally without Docker

.DESCRIPTION
    This script builds all TrimQ services using Maven.
    Useful for local development and testing.

.PARAMETER Service
    Specific service to build (optional, builds all if not specified)

.PARAMETER SkipTests
    Skip running tests

.EXAMPLE
    .\build-local.ps1
    .\build-local.ps1 -Service user-service
    .\build-local.ps1 -SkipTests
#>

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("common", "api-gateway", "user-service", "shop-service", 
                 "booking-service", "payment-service", "notification-service", "all")]
    [string]$Service = "all",
    
    [Parameter(Mandatory=$false)]
    [switch]$SkipTests
)

$ErrorActionPreference = "Stop"

function Write-Header($message) {
    Write-Host ""
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host " $message" -ForegroundColor Cyan
    Write-Host "=============================================" -ForegroundColor Cyan
}

function Write-Success($message) {
    Write-Host "[OK] $message" -ForegroundColor Green
}

function Write-Info($message) {
    Write-Host "[INFO] $message" -ForegroundColor Yellow
}

# Check Java version
Write-Header "Checking Java Version"
$javaVersion = java -version 2>&1 | Select-String -Pattern "version"
Write-Info $javaVersion

# Build command
$skipTestsArg = if ($SkipTests) { "-DskipTests" } else { "" }

if ($Service -eq "all") {
    Write-Header "Building All Services"
    
    # Build common first
    Write-Info "Building common module..."
    & ./mvnw install -pl common -am $skipTestsArg -B
    if ($LASTEXITCODE -ne 0) { throw "Build failed for common" }
    Write-Success "Common module built"
    
    # Build all services
    $services = @("api-gateway", "user-service", "shop-service", 
                  "booking-service", "payment-service", "notification-service")
    
    foreach ($svc in $services) {
        Write-Info "Building $svc..."
        & ./mvnw package -pl "services/$svc" -am $skipTestsArg -B
        if ($LASTEXITCODE -ne 0) { 
            Write-Host "[FAILED] $svc build failed" -ForegroundColor Red
        } else {
            Write-Success "$svc built successfully"
        }
    }
} elseif ($Service -eq "common") {
    Write-Header "Building Common Module"
    & ./mvnw install -pl common $skipTestsArg -B
    if ($LASTEXITCODE -eq 0) { Write-Success "Common module built" }
} else {
    Write-Header "Building $Service"
    & ./mvnw package -pl "services/$Service" -am $skipTestsArg -B
    if ($LASTEXITCODE -eq 0) { Write-Success "$Service built successfully" }
}

Write-Header "Build Complete"
Write-Info "JAR files are in target/ directories"

