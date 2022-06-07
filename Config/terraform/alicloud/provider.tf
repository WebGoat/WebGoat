terraform {
  required_providers {
    alicloud = {
      source  = "aliyun/alicloud"
      version = "1.162.0"
    }
  }
}

provider "alicloud" {
  region = "cn-hangzhou"
}