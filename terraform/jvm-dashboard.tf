resource "kubernetes_config_map" "jvm_dashboard" {
  metadata {
    name = "jvm-dashboard"
    labels = {
      grafana_dashboard = "1"
    }
  }

  data = {
    "jvm-dashboard.json" = file("${path.module}/dashboards/4701_rev10.json")
  }
}
