resource azurerm_monitor_log_profile "logging_profile" {
  storage_account_id = azurerm_storage_account.example.id
  categories         = ["Action"]
  locations          = [var.location]
  name               = "terragoat-${var.environment}"
  retention_policy {
    enabled = true
    days    = 30
  }
}