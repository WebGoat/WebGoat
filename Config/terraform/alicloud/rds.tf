resource "alicloud_db_instance" "seeme" {
  # Is public due to Security IPS 0.0.0.0/0
  engine           = "MySQL"
  engine_version   = "5.6"
  instance_type    = "rds.mysql.t1.small"
  instance_storage = "10"
  tde_status       = "Enabled"
  security_ips = [
    "0.0.0.0",
    "10.23.12.24/24"
  ]
  parameters {
    name  = "innodb_large_prefix"
    value = "ON"
  }
  parameters {
    name  = "connect_timeout"
    value = "50"
  }
  tags = {
    git_commit           = "c2ff052009d49c65e6af551eb1506abc73b5976c"
    git_file             = "terraform/alicloud/rds.tf"
    git_last_modified_at = "2022-04-05 15:14:28"
    git_last_modified_by = "james.woolfenden@gmail.com"
    git_modifiers        = "james.woolfenden"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "7bd1394e-0a79-4288-8f83-5abcca5bf1ba"
  }
}
