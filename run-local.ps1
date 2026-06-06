<#
.SYNOPSIS
    Run TrimQ services locally for development

.DESCRIPTION
    This script starts TrimQ services locally using Java.
    Requires PostgreSQL, Redis, and Kafka to be running.

.PARAMETER Service
    Service to run

.PARAMETER Profile
    Spring profile to use (default: local)

.EXAMPLE
    .\run-local.ps1 -Service api-gateway
    .\run-local.ps1 -Service user-service -Profile dev
#>

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("api-gateway", "user-service", "shop-service", 
                 "booking-service", "payment-service", "notification-service")]
    [string]$Service,
    
    [Parameter(Mandatory=$false)]
    [string]$Profile = "local"
)

$portMappings = @{
    "api-gateway" = 8080
    "user-service" = 8081
    "shop-service" = 8082
    "booking-service" = 8083
    "payment-service" = 8084
    "notification-service" = 8085
}

$port = $portMappings[$Service]

Write-Host ""
Write-Host "Starting $Service on port $port..." -ForegroundColor Cyan
Write-Host "Profile: $Profile" -ForegroundColor Yellow
Write-Host ""

$jarPath = "services/$Service/target/*.jar"
$jars = Get-ChildItem -Path $jarPath -ErrorAction SilentlyContinue

if ($jars.Count -eq 0) {
    Write-Host "[ERROR] JAR file not found. Run .\build-local.ps1 -Service $Service first" -ForegroundColor Red
    exit 1
}

$jarFile = $jars[0].FullName

$env:SPRING_PROFILES_ACTIVE = $Profile
$env:SERVER_PORT = $port

# Default local environment variables
$env:POSTGRES_HOST = "localhost"
$env:POSTGRES_PORT = "5432"
$env:POSTGRES_DB = "trimq"
$env:POSTGRES_USER = "trimq"
$env:POSTGRES_PASSWORD = "trimq123"
$env:REDIS_HOST = "localhost"
$env:REDIS_PORT = "6379"
$env:KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
$env:JWT_SECRET = "trimq-super-secret-key-for-jwt-token-generation-min-256-bits-required"

Write-Host "Running: java -jar $jarFile" -ForegroundColor Gray
Write-Host ""

java -jar $jarFile

