variable "tools_namespace" {
  description = "Namespace for monitoring, registry etc"
  type        = string
  default     = "monitoring"
}

variable "app_namespace" {
  description = "Namespace for backend/frontend"
  type        = string
  default     = "paf"
}

variable "github_username" {
  description = "GitHub username for authenticating with GHCR"
  type        = string
  default     = null
}

variable "github_pat" {
  description = "GitHub Personal Access Token for authenticating with GHCR"
  type        = string
  default     = null
}
