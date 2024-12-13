# https://medium.com/rahasak/terraform-kubernetes-integration-with-minikube-334c43151931
variable "kubeconfig" {
  description = "Path to kubeconfig file"
  type        = string
}

terraform {
  required_providers {
    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "2.17.0"
    }
  }
}

provider "kubernetes" {
    config_path    = var.kubeconfig
    config_context = "minikube"
}
