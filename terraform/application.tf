resource "kubernetes_namespace" "application_namespace" {
  metadata {
    name = var.app_namespace
  }
}

// Define the Kubernetes deployment for your application
resource "kubernetes_deployment" "application" {
  metadata {
    name      = "interview-exercise-deployment"
    namespace = kubernetes_namespace.application_namespace.metadata[0].name
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "interview-exercise"
      }
    }

    template {
      metadata {
        labels = {
          app = "interview-exercise"
        }
      }

      spec {
        image_pull_secrets {
          name = kubernetes_secret.ghcr.metadata[0].name
        }

        container {
          name  = "interview-exercise"
          image = "ghcr.io/jahwag/interview-exercise"
          port {
            container_port = 8080
          }
          // Ensure metrics endpoint
          liveness_probe {
            http_get {
              path = "/actuator/prometheus"
              port = 8080
            }
            initial_delay_seconds = 60
            period_seconds        = 3
          }

          // Add environment variables to use k8s profile and secret values
          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "k8s"
          }
          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.postgresql.metadata[0].name
                key  = "postgres-user"
              }
            }
          }
          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.postgresql.metadata[0].name
                key  = "postgres-password"
              }
            }
          }
        }
      }
    }
  }

  depends_on = [
    helm_release.postgresql
  ]
}


// Define the Kubernetes service for your application
resource "kubernetes_service" "application" {
  metadata {
    name      = "interview-exercise-service"
    namespace = kubernetes_namespace.application_namespace.metadata[0].name
    labels = {
      "app.selector" = "interview-exercise"
    }
  }

  spec {
    selector = {
      app = "interview-exercise"
    }

    port {
      name        = "prometheus"
      protocol    = "TCP"
      port        = 8080
      target_port = 8080
      node_port   = 30036
    }

    type = "NodePort"
  }
}
