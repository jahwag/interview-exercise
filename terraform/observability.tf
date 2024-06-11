resource "kubernetes_namespace" "tools_namespace" {
  metadata {
    name = var.tools_namespace
  }
}

resource "helm_release" "kube-prometheus-stack" {
  name       = "kube-prometheus-stack"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  version    = "58.7.2"
  namespace  = var.tools_namespace

  values = [
    file("${path.module}/configs/kube-prometheus-stack.yaml")
  ]
}

# loki
resource "helm_release" "loki" {
  name       = "loki"
  repository = "https://grafana.github.io/helm-charts/"
  chart      = "loki"
  version    = "5.48.0"
  namespace  = var.tools_namespace

  values = [
    file("${path.module}/configs/loki.yaml")
  ]
  depends_on = [
    helm_release.kube-prometheus-stack
  ]
}

# promtail
resource "helm_release" "promtail" {
  name       = "promtail"
  repository = "https://grafana.github.io/helm-charts/"
  chart      = "promtail"
  version    = "6.15.5"
  namespace  = var.tools_namespace

  values = [
    file("${path.module}/configs/promtail.yaml")
  ]

  depends_on = [
    helm_release.kube-prometheus-stack
  ]
}

# Adding a null resource with delay
resource "null_resource" "wait_for_crds" {
  provisioner "local-exec" {
    command = "sleep 60"
  }
  depends_on = [helm_release.kube-prometheus-stack]
}
