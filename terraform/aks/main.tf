# Copyright (c) HashiCorp, Inc.
# SPDX-License-Identifier: MPL-2.0
# Terraform is inspired by https://github.com/hashicorp/learn-terraform-provision-aks-cluster

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "rg" {
  name     = "webgoat-rg"
  location = "canadacentral"
}

resource "azurerm_kubernetes_cluster" "webgoat-cluster" {
  name                = "webgoat-aks"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  dns_prefix          = "webgoat-k8s"
  kubernetes_version  = "1.29"

  default_node_pool {
    name            = "default"
    node_count      = 2
    vm_size         = "Standard_D2_v2"
    os_disk_size_gb = 30
  }

  service_principal {
    client_id     = var.appId
    client_secret = var.password
  }

  role_based_access_control_enabled = true
}
