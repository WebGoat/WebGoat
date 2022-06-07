resource random_string "password" {
  length      = 16
  special     = false
  min_lower   = 1
  min_numeric = 1
  min_upper   = 1
}

resource azurerm_linux_virtual_machine "linux_machine" {
  admin_username                  = "terragoat-linux"
  admin_password                  = random_string.password.result
  location                        = var.location
  name                            = "terragoat-linux"
  network_interface_ids           = [azurerm_network_interface.ni_linux.id]
  resource_group_name             = azurerm_resource_group.example.name
  size                            = "Standard_F2"
  disable_password_authentication = false
  source_image_reference {
    publisher = "Canonical"
    offer     = "UbuntuServer"
    sku       = "16.04-LTS"
    version   = "latest"
  }
  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  tags = merge({
    terragoat   = true
    environment = var.environment
    }, {
    git_commit           = "de3bb777f967989c2c3332faea16cb614c55ccc9"
    git_file             = "terraform/azure/instance.tf"
    git_last_modified_at = "2020-06-17 15:48:15"
    git_last_modified_by = "nimrodkor@gmail.com"
    git_modifiers        = "nimrodkor"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "736ef713-51b8-4178-ad69-406be81f6ef2"
  })
}

resource azurerm_windows_virtual_machine "windows_machine" {
  admin_password        = random_string.password.result
  admin_username        = "tg-${var.environment}"
  location              = var.location
  name                  = "tg-win"
  network_interface_ids = [azurerm_network_interface.ni_win.id]
  resource_group_name   = azurerm_resource_group.example.name
  size                  = "Standard_F2"
  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "MicrosoftWindowsServer"
    offer     = "WindowsServer"
    sku       = "2016-Datacenter"
    version   = "latest"
  }

  tags = merge({
    terragoat   = true
    environment = var.environment
    }, {
    git_commit           = "de3bb777f967989c2c3332faea16cb614c55ccc9"
    git_file             = "terraform/azure/instance.tf"
    git_last_modified_at = "2020-06-17 15:48:15"
    git_last_modified_by = "nimrodkor@gmail.com"
    git_modifiers        = "nimrodkor"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "41938587-bdfc-4234-b941-560f1ae7d6c0"
  })
}