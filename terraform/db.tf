resource "helm_release" "postgresql" {
  name       = "postgresql"
  repository = "https://charts.bitnami.com/bitnami"
  chart      = "postgresql"
  version    = "15.1.0"

  namespace = kubernetes_namespace.application_namespace.metadata[0].name

  set {
    name  = "global.postgresql.auth.database"
    value = "interview-exercise"
  }

  set_sensitive {
    name  = "global.postgresql.auth.existingSecret"
    value = kubernetes_secret.postgresql.metadata[0].name
  }

  set {
    name  = "service.port"
    value = "5432"
  }

  set {
    name  = "service.type"
    value = "ClusterIP"
  }
}
