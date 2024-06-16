# Interview Exercise
[![Docker](https://github.com/jahwag/interview-exercise/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/jahwag/interview-exercise/actions/workflows/docker-publish.yml)

This repository showcases a solution to upgrade the original application, bringing it closer to production-ready standards.

## Features
- **Ready Deployment**: Kubernetes setup with two replicas for fault tolerance.
- **Observability**: Integrated with Prometheus for monitoring.
- **Monitoring & Alerting**: Configured with Grafana dashboards.
- **Security**: Role-based access control implemented.
- **Modern Technologies**: Built with Spring Boot 3 and Java 21.

## Areas not included
- Advanced authentication mechanisms like Single Sign-On (SSO) or OAuth2.
- New backoffice UI

## Prerequisites
Ensure you have the following tools installed on your system:

### For Windows Users
- [Chocolatey](https://chocolatey.org/install)
- [Msysgit](https://chocolatey.org/packages/git)

### For MacOS Users
- [Homebrew](https://brew.sh/)

### For Linux Users
- Appropriate package manager (APT/YUM/RPM, etc.)

## Usage
### Quick Start
1. To quickly deploy the application, run the following command:
 ```sh
 ./minikube.sh
 ```

2. Application is served here:

http://localhost:8080/swagger-ui.html

You may authenticate with admin:admin(to be permitted) or user:user(to be denied).
