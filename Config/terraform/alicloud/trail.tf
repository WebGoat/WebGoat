resource "alicloud_actiontrail_trail" "fail" {
  # Action Trail not Logging for all regions
  # Action Trail not Logging for all events
  trail_name         = "action-trail"
  oss_write_role_arn = alicloud_ram_role.trail.arn
  oss_bucket_name    = alicloud_oss_bucket.trail.bucket
  event_rw           = "Read"
  trail_region       = "cn-hangzhou"
}

resource "alicloud_oss_bucket" "trail" {

  tags = {
    git_commit           = "c2ff052009d49c65e6af551eb1506abc73b5976c"
    git_file             = "terraform/alicloud/trail.tf"
    git_last_modified_at = "2022-04-05 15:14:28"
    git_last_modified_by = "james.woolfenden@gmail.com"
    git_modifiers        = "james.woolfenden"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "9ce7077b-8195-4e71-aec6-ed1f769555dc"
  }
}

resource "alicloud_ram_role" "trail" {
  name     = "trail"
  document = <<EOF
  {
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Effect": "Allow",
        "Principal": {
          "Service": [
            "actiontrail.aliyuncs.com"
          ]
        }
      }
    ],
    "Version": "1"
  }
  EOF
  force    = true
}