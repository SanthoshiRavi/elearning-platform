<div align="center">

# 📚 YDEMY E-Learning Platform

### Production-Style Full Stack DevOps Project on AWS

A cloud-native E-Learning Platform built using **React, Spring Boot, PostgreSQL, Docker, Jenkins, AWS CloudFormation, Prometheus, Grafana, and Amazon S3** demonstrating modern DevOps practices including Infrastructure as Code, CI/CD, Monitoring, Containerization, and Cloud Deployment. The detailed steps of how to use the project is included.

</div>

---

# 📑 Table of Contents

- Project Overview
- Features
- Technology Stack
- Architecture
- Infrastructure
- Project Structure
- CI/CD Pipeline
- Monitoring
- Screenshots
- Getting Started
- CloudFormation Deployment
- Docker Deployment
- Future Improvements
- Learning Outcomes

---

# 🚀 Project Overview

This project demonstrates a **production-inspired deployment** of an E-Learning Platform on AWS.

The primary objective was not only to develop the application but also to build an end-to-end DevOps pipeline including:

- Infrastructure as Code
- Dockerized Applications
- CI/CD Automation
- Cloud Deployment
- Secure File Storage
- Monitoring & Observability

---

# ✨ Features

### 👨‍🎓 Student

- Register/Login
- Browse Courses
- Enroll Courses
- View Course Contents
- Access PDF & Video Lessons

### 👨‍💼 Admin

- JWT Authentication
- Create Courses
- Upload PDFs
- Upload Videos
- Manage Course Contents

### ☁ AWS

- Amazon S3 Storage
- EC2 Deployment
- IAM Roles
- CloudFormation
- Security Groups

### ⚙ DevOps

- Docker
- Jenkins Pipeline
- Docker Hub
- GitHub Integration

### 📊 Monitoring

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana
- Node Exporter

---

# 🛠 Technology Stack

| Layer | Technologies |
|---------|-------------|
| Frontend | React, Vite, Axios |
| Backend | Java 21, Spring Boot, Spring Security |
| Database | PostgreSQL |
| Storage | Amazon S3 |
| Authentication | JWT |
| Containerization | Docker |
| CI/CD | Jenkins |
| IaC | AWS CloudFormation |
| Monitoring | Prometheus, Grafana |
| Cloud | AWS EC2 |

---

# 🏗 Architecture

![Architecture](docs/architecture.png)

---

## Architecture Flow

```text
                    GitHub
                       │
                       ▼
                  Jenkins CI/CD
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼

 Backend Image    Frontend Image

        │              │
        └──────┬───────┘
               ▼

          Docker Hub

               │

    ┌──────────┼──────────────┐

    ▼          ▼              ▼

 Backend     Frontend     Monitoring

 SpringBoot   React        Grafana
 PostgreSQL   Nginx        Prometheus
 NodeExporter              NodeExporter

          │

          ▼

      Amazon S3
```

---

# ☁ AWS Infrastructure

| Resource | Purpose |
|-----------|----------|
| VPC | Networking |
| Public Subnets | EC2 Deployment |
| Internet Gateway | Internet Access |
| Security Groups | Network Security |
| EC2 | Application Hosting |
| IAM Role | Secure AWS Access |
| Amazon S3 | Course Content Storage |

---

# ⚡ CI/CD Pipeline

```text
GitHub Push

      │

      ▼

Jenkins

      │

Checkout

      │

Build Backend

      │

Build Frontend

      │

Docker Build

      │

Push Docker Images

      │

Deploy Backend

      │

Health Check

      │

Deploy Frontend

      │

Health Check

      │

Deployment Complete
```

---

# 📈 Monitoring

### Prometheus

- Spring Boot Metrics
- Node Exporter Metrics
- Prometheus Self Monitoring

### Grafana Dashboards

- Application Dashboard
- Infrastructure Dashboard
---

# 📷 Screenshots

## Jenkins Pipeline

![Jenkins](docs/jenkins.png)

---

## Frontend

![Frontend-Login](docs/frontend1.png)
![Frontend-Enroll](docs/frontend2.png)
![Frontend-MyCourses](docs/frontend3.png)
![Frontend-CourseContent](docs/frontend4.png)
![Frontend-AdminPage](docs/frontend5.png)

---

## Grafana Dashboard

![Grafana](docs/grafana-dashboard.png)

---

## Prometheus Targets

![Prometheus](docs/prometheus.png)

---

# 📦 Docker

<details>

<summary>Backend Stack</summary>

- Spring Boot
- PostgreSQL
- Node Exporter

</details>

<details>

<summary>Frontend Stack</summary>

- React
- Nginx

</details>

<details>

<summary>Monitoring Stack</summary>

- Prometheus
- Grafana
- Node Exporter

</details>

---

# 🚀 Deployment

## 1. Provision Infrastructure

```bash
aws cloudformation deploy ...
```

---

## 2. Configure Jenkins

- DockerHub Credentials
- SSH Credentials
- GitHub Webhook

---

## 3. Push Code

```bash
git add .

git commit -m "Deploy"

git push
```

Jenkins automatically:

- Builds
- Tests
- Creates Docker Images
- Pushes Images
- Deploys Backend
- Deploys Frontend

---

# 🔒 Security

- JWT Authentication
- IAM Roles
- Security Groups
- Least Privilege Access
- Amazon S3 IAM Permissions
- Docker Isolation

---

# 🎯 Learning Outcomes

✔ Infrastructure as Code

✔ Docker

✔ Jenkins

✔ AWS

✔ CloudFormation

✔ Monitoring

✔ Observability

✔ DevOps

✔ CI/CD

✔ Spring Boot

✔ React

---

# 🔮 Future Improvements

- Kubernetes
- Helm
- ArgoCD
- Terraform
- Alertmanager
- SonarQube
- Trivy
- AWS ECR
- ECS
- EKS
- HTTPS
- Route53
- Application Load Balancer

---
# E-Learning Platform - Setup & Deployment Guide

Below are detailed steps to clone and run this project locally, along with a troubleshooting guide. If you have any questions, feedback, or suggestions, feel free to leave a comment or connect with me on **LinkedIn:** [LinkedIn](https://www.linkedin.com/in/santhoshi-ravi-845b77229). Contributions are always welcome—feel free to open a pull request!
# Prerequisites

Before starting, ensure you have:

- AWS Account
- Docker Hub Account
- GitHub Account
- AWS CLI Installed / AWS account
- SSH Key Pair (`.pem`)
- Jenkins Server (created through CloudFormation)

---

[Login to your AWS account and in your console account generate SSH Key pair (.pem file). If you have doubts refer to internet resources/chatgpt/claude ai for generating it.]
> ⚠️ **Important Notice on AWS Billing & Costs**  
> Running AWS infrastructure may incur costs depending on your usage and active services.
>
> To avoid unexpected charges:
> - **Enable Cost Alerts:** Set up an [AWS Billing Alarm](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/monitor_billing_community.html) via CloudWatch or AWS Budgets.
> - **Clean Up Resources:** Always terminate or delete unused resources (EC2 instances, databases, S3 buckets, etc.) when you are done testing.
> - **Check Free Tier:** Ensure your configurations fall within the AWS Free Tier limits if applicable.

# Step 1: Clone Repository

```bash
git clone <repository-url>

cd <repository-name>
```

---

# Step 2: Deploy Infrastructure

Deploy CloudFormation stacks in the following order:

```text
1. 01-network.yaml

2. 02-security-groups.yaml

3. 03-ec2.yaml

4. 04-jenkins.yaml

5. 05-storage.yaml
```

After successful deployment, note:

```text
Frontend Elastic IP

Jenkins Elastic IP

Monitoring Elastic IP
```

---

# Step 3: Verify SSM Parameters

AWS Systems Manager → Parameter Store

Verify following parameters exist:

```text
/elearning/infrastructure/frontend-private-ip

/elearning/infrastructure/backend-private-ip

/elearning/infrastructure/monitoring-private-ip

/elearning/storage/course-content-bucket
```

---

# Step 4: Configure Jenkins

Open Jenkins:

```text
http://<JENKINS_ELASTIC_IP>:8080
```

Example:

```text
http://13.xx.xx.xx:8080
```

---

# Jenkins Plugins Required

Install:

```text
Git

Docker

Docker Pipeline

SSH Agent

Credentials Binding

Pipeline

Pipeline Stage View
```

---

# Jenkins Credentials Setup

Navigate:

```text
Manage Jenkins

→ Credentials

→ System

→ Global Credentials
```

Create the following credentials.

---

## 1. Docker Hub Username

Type:

```text
Username with Password
```

ID:

```text
dockerhub-creds
```

Value:

```text
Docker Hub Username
Docker Hub Password / Token
```

---

## 2. EC2 SSH Key

Type:

```text
SSH Username with private key
```

ID:

```text
ec2-ssh-key
```

Username:

```text
ec2-user
```

Private Key:

```text
Paste your .pem file content
```

---

## 3. PostgreSQL Password

Type:

```text
Secret Text
```

ID:

```text
postgres-password
```

---

## 4. JWT Secret

Type:

```text
Secret Text
```

ID:

```text
jwt-secret
```

---

## 5. Admin Password

Type:

```text
Secret Text
```

ID:

```text
admin-password
```

---

## 6. Grafana Password

Type:

```text
Secret Text
```

ID:

```text
grafana-admin-password
```

---

# Step 5: Create Jenkins Pipeline

Create:

```text
New Item

→ Pipeline
```

Pipeline Definition:

```text
Pipeline script from SCM
```

SCM:

```text
Git
```

Repository URL:

```text
<github-repository-url>
```

Script Path:

```text
Jenkinsfile
```

Save.

---

# Step 6: Configure GitHub Webhook

GitHub Repository:

```text
Settings

→ Webhooks

→ Add Webhook
```

Payload URL:

```text
http://<JENKINS_ELASTIC_IP>:8080/github-webhook/
```

Example:

```text
http://13.xx.xx.xx:8080/github-webhook/
```

Content Type:

```text
application/json
```

Events:

```text
Just the push event
```

Save.

---

# Step 7: Run Jenkins Pipeline

Trigger:

```text
Build Now
```

Pipeline will automatically:

```text
Checkout Source Code

Build Backend

Build Frontend

Build Docker Images

Push Images To Docker Hub

Generate Runtime Configurations

Deploy Backend

Deploy Frontend

Deploy Monitoring

Perform Health Checks
```

---

# Application URLs

Replace placeholders with your Elastic IPs.

---

## E-Learning Application

```text
http://<FRONTEND_ELASTIC_IP>
```

Example:

```text
http://13.xx.xx.xx
```

---

## Jenkins Dashboard

```text
http://<JENKINS_ELASTIC_IP>:8080
```

Example:

```text
http://13.xx.xx.xx:8080
```

---

## Grafana Dashboard

```text
http://<MONITORING_ELASTIC_IP>:3000
```

Example:

```text
http://13.xx.xx.xx:3000
```

Login:

```text
Username: admin

Password: <grafana-admin-password credential>
```

---

## Prometheus Dashboard

```text
http://<MONITORING_ELASTIC_IP>:9090
```

Example:

```text
http://13.xx.xx.xx:9090
```

---

# Default Admin User

Created automatically during backend startup.

Login:

```text
Email:
admin@elearning.com

Password:
<admin-password credential>
```

---

# Monitoring

Prometheus collects:

```text
Frontend Node Exporter Metrics

Backend Node Exporter Metrics

Monitoring Node Exporter Metrics

Spring Boot Actuator Metrics
```

Grafana visualizes:

```text
CPU Usage

Memory Usage

Disk Usage

Network Metrics

Application Metrics
```

---

# S3 Storage

Course content is stored in:

```text
Amazon S3
```

Supported content:

```text
PDF Files

Videos

Course Documents
```

Application uses:

```text
Pre-Signed URLs
```

for secure file access.

---

# Security

This project follows the following security practices:

```text
No hardcoded passwords in source code

No AWS Access Keys stored in GitHub

IAM Role based S3 access

Runtime secret generation through Jenkins

Security Group restricted access

JWT based authentication

Grafana credentials externalized

Database credentials externalized
```

---

# Troubleshooting

## Cannot SSH To EC2

Verify:

```text
Security Group Port 22

Your current public IP

EC2 is running
```

Update:

```text
AdminIpCidr
```

if your internet IP changes.

---

## Jenkins Cannot Deploy

Verify:

```text
ec2-ssh-key credential exists

Docker Hub credential exists

EC2 reachable
```

---

## Grafana Not Opening

Verify:

```text
Monitoring EC2 Running

Port 3000 Allowed

Grafana Container Running
```

Check:

```bash
docker ps
```

---

## Prometheus Targets Down

Verify:

```text
Node Exporter Running

Security Group Rules

Backend Actuator Endpoint
```

Check:

```text
Status → Targets
```

inside Prometheus UI.

---

# Project Features

✅ Infrastructure as Code using CloudFormation

✅ Automated CI/CD using Jenkins

✅ Containerized Deployment using Docker

✅ Secure Storage using Amazon S3

✅ Monitoring using Prometheus & Grafana

✅ Dynamic Configuration using AWS Parameter Store

✅ Secret Management through Jenkins Credentials

✅ Fully Automated Deployment Pipeline

---

# Deployment Flow

```text
Developer Pushes Code
│
▼
GitHub Webhook
│
▼
Jenkins Pipeline
│
▼
Build Docker Images
│
▼
Push To Docker Hub
│
▼
Deploy To EC2 Instances
│
▼
Application Available
```
# 👩‍💻 Author

## Santhoshi R

Software Engineer - [LinkedIn](https://www.linkedin.com/in/santhoshi-ravi-845b77229)

Java • Spring Boot • React • Docker • Jenkins • AWS • PostgreSQL • Prometheus • Grafana

---

<div align="center">

⭐ If you found this project interesting, consider giving it a star!

</div>
