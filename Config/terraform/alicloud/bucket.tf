resource "alicloud_oss_bucket" "bad_bucket" {
  # Public and writeable bucket 
  # Versioning isn't enabled
  # Not Encrypted with a Customer Master Key and no Server side encryption
  # Doesn't have access logging enabled" 
  bucket = "wildwestfreeforall"
  acl    = "public-read-write"
  tags = {
    git_commit           = "9c114f23d311f787c137723e1f71b27a52f0adec"
    git_file             = "terraform/alicloud/bucket.tf"
    git_last_modified_at = "2022-04-05 15:17:55"
    git_last_modified_by = "james.woolfenden@gmail.com"
    git_modifiers        = "james.woolfenden"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "80373049-248d-4f5e-9d25-740c3e80f2b9"
  }
}
