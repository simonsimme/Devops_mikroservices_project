# Kubernetes Deployment Manifests

Kubernetes deployment configurations for the DevOps Microservices Project, enabling container orchestration and production deployment.

## 🎯 Purpose

This directory is designed to contain Kubernetes manifests for:
- **Service Deployments** - Containerized microservice deployments
- **Database StatefulSets** - PostgreSQL database deployments
- **ConfigMaps & Secrets** - Configuration and sensitive data management
- **Ingress Controllers** - External traffic routing
- **Service Discovery** - Internal service communication
- **Auto-scaling** - Horizontal Pod Autoscaler configurations

## 📁 Planned Structure

```
infra/k8s/
├── README.md                    # This file - K8s documentation
├── namespace/                   # Namespace definitions
├── databases/                   # Database StatefulSets and PVCs
│   ├── user-db.yaml            # User Service PostgreSQL
│   └── scheduler-db.yaml       # Scheduler Service PostgreSQL
├── services/                    # Microservice deployments
│   ├── user-service.yaml       # User authentication service
│   ├── scheduler-service.yaml  # Scheduler management service  
│   └── gateway.yaml            # API Gateway service
├── frontend/                    # Frontend deployment
│   └── react-app.yaml          # React application deployment
├── networking/                  # Network configurations
│   ├── ingress.yaml            # External traffic routing
│   └── services.yaml           # Service definitions
├── config/                      # Configuration management
│   ├── configmaps.yaml         # Application configuration
│   └── secrets.yaml            # Sensitive data (JWT keys, DB passwords)
└── monitoring/                  # Observability stack
    ├── prometheus.yaml         # Metrics collection
    └── grafana.yaml            # Monitoring dashboards
```

## 🚀 Getting Started

### Prerequisites
- **Kubernetes Cluster** - Local (minikube, kind) or cloud (EKS, GKE, AKS)
- **kubectl** - Kubernetes CLI tool
- **Docker Images** - Built and pushed to a container registry
- **Persistent Storage** - For database data persistence

### Basic Deployment

#### 1. Create Namespace
```bash
kubectl create namespace devops-microservices
kubectl config set-context --current --namespace=devops-microservices
```

#### 2. Deploy Databases (StatefulSets)
```bash
# Deploy PostgreSQL databases
kubectl apply -f databases/

# Wait for databases to be ready
kubectl wait --for=condition=ready pod -l app=user-db --timeout=300s
kubectl wait --for=condition=ready pod -l app=scheduler-db --timeout=300s
```

#### 3. Deploy Configuration
```bash
# Apply ConfigMaps and Secrets
kubectl apply -f config/
```

#### 4. Deploy Services
```bash
# Deploy microservices
kubectl apply -f services/

# Wait for services to be ready
kubectl wait --for=condition=ready pod -l app=user-service --timeout=300s
kubectl wait --for=condition=ready pod -l app=scheduler-service --timeout=300s
kubectl wait --for=condition=ready pod -l app=gateway --timeout=300s
```

#### 5. Deploy Frontend
```bash
# Deploy React application
kubectl apply -f frontend/
```

#### 6. Configure Networking
```bash
# Set up ingress and service exposure
kubectl apply -f networking/
```

## 📊 Service Architecture

### Database Layer
```yaml
# Example StatefulSet for User Database
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: user-db
spec:
  serviceName: user-db
  replicas: 1
  selector:
    matchLabels:
      app: user-db
  template:
    spec:
      containers:
      - name: postgres
        image: postgres:16
        env:
        - name: POSTGRES_DB
          value: users_db
        - name: POSTGRES_USER
          valueFrom:
            secretKeyRef:
              name: db-secrets
              key: user-db-username
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secrets
              key: user-db-password
        ports:
        - containerPort: 5432
        volumeMounts:
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
  volumeClaimTemplates:
  - metadata:
      name: postgres-storage
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 10Gi
```

### Application Layer
```yaml
# Example Deployment for User Service
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: user-service
  template:
    spec:
      containers:
      - name: user-service
        image: user-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_URL
          value: jdbc:postgresql://user-db:5432/users_db
        - name: DB_USER
          valueFrom:
            secretKeyRef:
              name: db-secrets
              key: user-db-username
        - name: DB_PASS
          valueFrom:
            secretKeyRef:
              name: db-secrets
              key: user-db-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: jwt-secret
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## 🔧 Configuration Management

### Secrets Management
```yaml
# Example secrets configuration
apiVersion: v1
kind: Secret
metadata:
  name: db-secrets
type: Opaque
data:
  user-db-username: <base64-encoded-username>
  user-db-password: <base64-encoded-password>
  scheduler-db-username: <base64-encoded-username>
  scheduler-db-password: <base64-encoded-password>

---
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
type: Opaque
data:
  jwt-secret: <base64-encoded-jwt-secret>
```

### ConfigMaps
```yaml
# Example application configuration
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  user-service.yml: |
    server:
      port: 8080
    spring:
      application:
        name: user-service
      jpa:
        hibernate:
          ddl-auto: validate
        show-sql: false
    management:
      endpoints:
        web:
          exposure:
            include: health,info,metrics
```

## 🌐 Networking & Service Discovery

### Service Definitions
```yaml
# Example service for User Service
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
  ports:
  - port: 8080
    targetPort: 8080
  type: ClusterIP

---
# Database service
apiVersion: v1
kind: Service
metadata:
  name: user-db
spec:
  selector:
    app: user-db
  ports:
  - port: 5432
    targetPort: 5432
  type: ClusterIP
```

### Ingress Configuration
```yaml
# Example ingress for external access
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: devops-microservices-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: api.devops-microservices.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: gateway
            port:
              number: 8080
  - host: app.devops-microservices.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend
            port:
              number: 80
```

## 📈 Scaling & Auto-scaling

### Horizontal Pod Autoscaler
```yaml
# Example HPA for User Service
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: user-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: user-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

## 🔍 Monitoring & Observability

### Health Checks
```bash
# Check pod status
kubectl get pods -w

# Check service endpoints
kubectl get endpoints

# View logs
kubectl logs -f deployment/user-service
kubectl logs -f deployment/scheduler-service
kubectl logs -f deployment/gateway
```

### Port Forwarding (Development)
```bash
# Access services locally
kubectl port-forward service/gateway 8080:8080
kubectl port-forward service/user-db 5433:5432
kubectl port-forward service/scheduler-db 5434:5432
kubectl port-forward service/frontend 3000:80
```



## 🛠️ Development Workflow

### Local Development with Kubernetes
1. **Build Images**: Build and push Docker images to registry
2. **Deploy to Dev**: Apply manifests to development cluster
3. **Test**: Verify functionality with port-forwarding
4. **Debug**: Use kubectl logs and exec for troubleshooting
5. **Iterate**: Update images and redeploy

### CI/CD Integration
```yaml
# Example GitHub Actions workflow
- name: Deploy to Kubernetes
  run: |
    kubectl apply -f infra/k8s/
    kubectl rollout status deployment/user-service
    kubectl rollout status deployment/scheduler-service
    kubectl rollout status deployment/gateway
```

## 📚 Related Documentation

- **[Main Project README](../../README.md)** - Project overview and architecture
- **[Docker Setup](../docker/README.md)** - Local development environment
- **[CI/CD Pipeline](../ci-cd/README.md)** - Automated deployment pipeline

---

**Status**: 🚧 **Under Development** - Manifests not yet implemented  
**Cluster Requirements**: Kubernetes 1.20+  
**Storage**: Persistent volumes required for databases  
**Networking**: Ingress controller recommended for external access