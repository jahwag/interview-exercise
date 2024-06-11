resource "kubernetes_config_map" "springboot_dashboard" {
  metadata {
    name = "springboot-dashboard"
    labels = {
      grafana_dashboard = "1"
    }
  }

  data = {
    "springboot-dashboard.json" = file("${path.module}/dashboards/17175_rev2.json")
  }
}
