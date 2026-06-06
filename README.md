# TrimQ - Salon Booking Platform

<div align="center">

![TrimQ Logo](docs/images/logo.png)

### Skip the Wait. Own Your Style.

[![Build Status](https://github.com/your-username/trimq/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/your-username/trimq/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green.svg)](https://spring.io/projects/spring-boot)

</div>

---

## 🎯 Overview

**TrimQ** is an enterprise-grade salon booking platform designed for barber shops across India. Users can discover shops, view real-time availability, book slots, make payments, and receive a digital pass with QR code.

### Key Features

- 🔍 **Shop Discovery** - Search by location, filter by rating & price
- 📅 **Real-time Booking** - View slot availability, book instantly
- 💳 **Secure Payments** - Razorpay integration (UPI, Cards, Wallets)
- 🎫 **Digital Pass** - QR code pass for seamless check-in
- 📊 **Shop Dashboard** - Manage bookings, services, staff
- 📧 **Notifications** - Email & SMS for booking updates

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         NGINX (80/443)                          │
│              Reverse Proxy | CORS | Rate Limiting               │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│                    API GATEWAY (8080)                           │
│         Spring Cloud Gateway | JWT | Circuit Breaker            │
└──────────────────────────────┬──────────────────────────────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ User Service  │    │ Shop Service  │    │Booking Service│
│    (8081)     │    │    (8082)     │    │    (8083)     │
└───────────────┘    └───────────────┘    └───────────────┘
        │                      │                      │
        │            ┌─────────┴─────────┐            │
        │            ▼                   ▼            │
        │    ┌───────────────┐   ┌───────────────┐    │
        │    │Payment Service│   │ Notification  │    │
        │    │    (8084)     │   │   (8085)      │    │
        │    └───────────────┘   └───────────────┘    │
        │            │                   │            │
        └────────────┼───────────────────┼────────────┘
                     │                   │
                     ▼                   ▼
        ┌─────────────────────────────────────────────┐
        │               DATA LAYER                    │
        │  PostgreSQL │ Redis │ Kafka (KRaft)         │
        └─────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| **Frontend** | Next.js 14, TailwindCSS, Framer Motion, shadcn/ui |
| **Backend** | Java 21, Spring Boot 3.3, Spring Cloud Gateway |
| **Database** | PostgreSQL 16 |
| **Cache** | Redis 7 |
| **Messaging** | Apache Kafka (KRaft) |
| **Payments** | Razorpay |
| **Notifications** | SendGrid (Email), Twilio (SMS) |
| **Monitoring** | Prometheus, Grafana, Loki |
| **Orchestration** | Kubernetes (Docker Desktop / AWS EKS) |
| **CI/CD** | GitHub Actions |

---

## 📁 Project Structure

```
TrimQ/
├── common/                    # Shared module (DTOs, Exceptions, Events)
├── services/
│   ├── api-gateway/          # Spring Cloud Gateway (Port 8080)
│   ├── user-service/         # Auth, JWT, OTP (Port 8081)
│   ├── shop-service/         # Shop, Services, Staff (Port 8082)
│   ├── booking-service/      # Slots, Bookings, Pass (Port 8083)
│   ├── payment-service/      # Razorpay integration (Port 8084)
│   └── notification-service/ # Kafka consumer, Email/SMS (Port 8085)
├── frontend/                  # Next.js application
├── k8s/                       # Kubernetes manifests
│   ├── namespace.yaml
│   ├── secrets/
│   ├── configmaps/
│   ├── data/                 # PostgreSQL, Redis, Kafka
│   ├── deployments/          # All microservices
│   └── monitoring/           # Prometheus, Grafana, Loki
├── .github/workflows/         # CI/CD pipelines
├── deploy-k8s.ps1            # Local deployment script
├── build-local.ps1           # Local build script
└── pom.xml                   # Parent POM
```

---

## 🚀 Quick Start

### Prerequisites

- Java 21 (Temurin/OpenJDK)
- Maven 3.9+
- Docker Desktop with Kubernetes enabled
- Node.js 18+ (for frontend)

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/trimq.git
cd trimq
```

### 2. Build Services

```powershell
# Build all services
.\build-local.ps1

# Or build specific service
.\build-local.ps1 -Service user-service
```

### 3. Deploy to Kubernetes

```powershell
# Deploy entire platform
.\deploy-k8s.ps1 -Action deploy

# Check status
.\deploy-k8s.ps1 -Action status

# View logs
.\deploy-k8s.ps1 -Action logs -Service api-gateway
```

### 4. Access the Application

| Service | URL |
|---------|-----|
| TrimQ App | http://localhost:30080 |
| API | http://localhost:30080/api/v1/ |
| Prometheus | http://localhost:30090 |
| Grafana | http://localhost:30030 (admin/admin123) |

---

## 📡 API Endpoints

### Authentication
```
POST /api/v1/auth/register     # User registration
POST /api/v1/auth/login        # Login (returns JWT)
POST /api/v1/auth/verify-otp   # OTP verification
POST /api/v1/auth/refresh      # Refresh token
```

### Shops
```
GET  /api/v1/shops             # List all shops
GET  /api/v1/shops/search      # Search shops
GET  /api/v1/shops/{id}        # Shop details
POST /api/v1/shops             # Create shop (owner)
```

### Bookings
```
GET  /api/v1/slots             # Get available slots
POST /api/v1/slots/lock        # Lock slot (before payment)
POST /api/v1/bookings          # Create booking
GET  /api/v1/bookings/user     # User's bookings
POST /api/v1/bookings/validate-pass  # Validate QR pass
```

### Payments
```
POST /api/v1/payments/create-order  # Create Razorpay order
POST /api/v1/payments/verify        # Verify payment
POST /api/v1/payments/refund        # Initiate refund
```

---

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `POSTGRES_HOST` | PostgreSQL host | localhost |
| `POSTGRES_PORT` | PostgreSQL port | 5432 |
| `REDIS_HOST` | Redis host | localhost |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | localhost:9092 |
| `JWT_SECRET` | JWT signing key | (required) |
| `RAZORPAY_KEY_ID` | Razorpay key | (required) |
| `RAZORPAY_KEY_SECRET` | Razorpay secret | (required) |

---

## 📊 Monitoring

### Prometheus Metrics

All services expose metrics at `/actuator/prometheus`:
- JVM metrics (memory, GC, threads)
- HTTP request metrics
- Database connection pool
- Kafka consumer lag

### Grafana Dashboards

Pre-configured dashboards for:
- Service Health Overview
- API Response Times
- Database Performance
- Kafka Consumer Metrics

---

## 🧪 Testing

```powershell
# Run all tests
./mvnw test

# Run specific service tests
./mvnw test -pl services/user-service

# Run with coverage
./mvnw test jacoco:report
```

---

## 🚢 Deployment

### Local (Docker Desktop)

```powershell
.\deploy-k8s.ps1 -Action deploy
```

### Production (AWS EKS)

```bash
# Configure kubectl for EKS
aws eks update-kubeconfig --name trimq-cluster --region ap-south-1

# Deploy
kubectl apply -k k8s/
```

---

## 📝 Phase Roadmap

### Phase 1 (Current) ✅
- User authentication & profile
- Shop discovery & booking
- Payment integration
- Digital pass & QR code

### Phase 2 (Planned)
- Mobile app (React Native)
- Push notifications
- Reviews & ratings
- Loyalty program

### Phase 3 (Future)
- AI style recommendations
- Smart scheduling
- Multi-language support
- Analytics dashboard

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Built with ❤️ for the Indian Salon Industry**

[Report Bug](https://github.com/your-username/trimq/issues) · [Request Feature](https://github.com/your-username/trimq/issues)

</div>
