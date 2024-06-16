resource "kubernetes_secret" "ghcr" {
  metadata {
    name      = "ghcr-secret"
    namespace = kubernetes_namespace.application_namespace.metadata[0].name
  }

  data = {
    ".dockerconfigjson" = jsonencode({
      auths = {
        "ghcr.io" = {
          auth = base64encode("${var.github_username}:${var.github_pat}")
        }
      }
    })
  }

  type = "kubernetes.io/dockerconfigjson"
}

output "encoded_dockerconfigjson" {
  value = base64encode(jsonencode({
    auths = {
      "ghcr.io" = {
        auth = base64encode("${var.github_username}:${var.github_pat}")
      }
    }
  }))
}

resource "kubernetes_secret" "postgresql" {
  metadata {
    name      = "postgresql-secret"
    namespace = kubernetes_namespace.application_namespace.metadata[0].name
  }

  data = {
    postgres-user     = "postgres"
    postgres-password = "postgres"
  }
}
