# TrimQ Kubernetes Manifests

This directory contains all Kubernetes manifests for deploying the TrimQ platform.

## Directory Structure

```
k8s/
├── namespace.yaml           # Namespace definitions
├── kustomization.yaml       # Kustomize configuration
├── secrets/
│   └── secrets.yaml         # Database, API keys, JWT secrets
├── configmaps/
│   └── app-config.yaml      # Service configurations
├── data/
│   ├── postgresql.yaml      # PostgreSQL deployment
│   ├── redis.yaml           # Redis deployment
│   └── kafka.yaml           # Kafka (KRaft mode) deployment
├── deployments/
│   ├── api-gateway.yaml     # API Gateway
│   ├── user-service.yaml    # User Service
│   ├── shop-service.yaml    # Shop Service
│   ├── booking-service.yaml # Booking Service
│   ├── payment-service.yaml # Payment Service
│   ├── notification-service.yaml # Notification Service
│   └── nginx.yaml           # Nginx reverse proxy
└── monitoring/
    ├── prometheus.yaml      # Prometheus
    ├── grafana.yaml         # Grafana
    └── loki.yaml            # Loki (centralized logging)
```

## Namespaces

| Namespace | Purpose |
|-----------|---------|
| `trimq-app` | Application services (API Gateway, microservices, Nginx) |
| `trimq-data` | Data services (PostgreSQL, Redis, Kafka) |
| `trimq-monitoring` | Monitoring stack (Prometheus, Grafana, Loki) |

## Quick Start (Docker Desktop Kubernetes)

### 1. Enable Kubernetes in Docker Desktop

Settings → Kubernetes → Enable Kubernetes

### 2. Deploy using Kustomize

```powershell
# Deploy all resources
kubectl apply -k k8s/

# Or deploy step by step:
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secrets/
kubectl apply -f k8s/configmaps/
kubectl apply -f k8s/data/
kubectl apply -f k8s/deployments/
kubectl apply -f k8s/monitoring/
```

### 3. Wait for pods to be ready

```powershell
# Check data layer
kubectl get pods -n trimq-data -w

# Check application layer
kubectl get pods -n trimq-app -w

# Check monitoring
kubectl get pods -n trimq-monitoring -w
```

### 4. Access the application

| Service | URL |
|---------|-----|
| TrimQ App (Nginx) | http://localhost:30080 |
| API Gateway | http://localhost:30080/api/ |
| Prometheus | http://localhost:30090 |
| Grafana | http://localhost:30030 (admin/admin123) |

## Production Deployment (AWS EKS)

### Prerequisites

- AWS CLI configured
- eksctl installed
- kubectl configured for EKS

### Create EKS Cluster

```bash
eksctl create cluster \
  --name trimq-cluster \
  --region ap-south-1 \
  --nodegroup-name workers \
  --node-type t3.medium \
  --nodes 3 \
  --nodes-min 2 \
  --nodes-max 5
```

### Update secrets for production

Before deploying, update `k8s/secrets/secrets.yaml` with:
- Real database credentials
- Production JWT secret
- Razorpay live keys
- Twilio production credentials
- SendGrid production API key

### Deploy to EKS

```bash
kubectl apply -k k8s/
```

## Useful Commands

```powershell
# View all pods across namespaces
kubectl get pods -A | grep trimq

# View logs
kubectl logs -n trimq-app deployment/api-gateway -f
kubectl logs -n trimq-app deployment/user-service -f

# Describe pod (for debugging)
kubectl describe pod -n trimq-app -l app=user-service

# Port forward (for local access)
kubectl port-forward -n trimq-app svc/api-gateway 8080:8080

# Scale a deployment
kubectl scale deployment -n trimq-app user-service --replicas=3

# Delete all resources
kubectl delete -k k8s/
```

## Resource Requirements

### Minimum (Development)

- CPU: 4 cores
- Memory: 8GB RAM
- Storage: 20GB

### Recommended (Production)

- CPU: 8+ cores
- Memory: 16GB+ RAM
- Storage: 100GB+ SSD

## Troubleshooting

### Pods stuck in Pending

```powershell
kubectl describe pod <pod-name> -n <namespace>
```

Check for resource constraints or PVC binding issues.

### Database connection errors

```powershell
# Check PostgreSQL is running
kubectl get pods -n trimq-data -l app=postgresql

# Check PostgreSQL logs
kubectl logs -n trimq-data deployment/postgresql
```

### Kafka connection issues

```powershell
# Kafka takes ~60 seconds to start
kubectl logs -n trimq-data deployment/kafka -f
```

### Services not responding

```powershell
# Check service endpoints
kubectl get endpoints -n trimq-app

# Test connectivity
kubectl run curl --rm -it --image=curlimages/curl -- sh
curl http://api-gateway.trimq-app:8080/actuator/health
```

