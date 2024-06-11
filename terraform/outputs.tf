output "namespace" {
  value = kubernetes_namespace.application_namespace.metadata[0].name
}

output "deployment_name" {
  value = kubernetes_deployment.application.metadata[0].name
}

output "service_name" {
  value = kubernetes_service.application.metadata[0].name
}

output "service_url" {
  value = "http://${kubernetes_service.application.metadata[0].name}.${kubernetes_namespace.application_namespace.metadata[0].name}.svc.cluster.local:${kubernetes_service.application.spec[0].port[0].port}"
}
