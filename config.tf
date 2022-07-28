variable acl {
  type    = string
  default = "public-read-write"
}

variable versioning_enabled {
  default = false
}

locals {
  enable_log_file_validation = true
}

resource "aws_s3_bucket" cloudtrail {
  bucket        = "my-cloudtrail-bucket"
  acl           = var.acl
  force_destroy = true

  versioning {
    enabled = var.versioning_enabled
  }

  policy = data.aws_iam_policy_document.default.json

  tags = {
    Environment = "dev"
  }
}

resource "aws_cloudtrail" "cloudtrail" {
  name                       = "tf-trail"
  s3_bucket_name             = aws_s3_bucket.cloudtrail.id
  is_multi_region_trail      = true
  enable_log_file_validation = local.enable_log_file_validation
}

data "aws_iam_policy_document" "default" {

  statement {
    sid = "AWSCloudTrailAclCheck"

    principals {
      type = "Service"
      identifiers = [
      "cloudtrail.amazonaws.com"]
    }

    actions = [
      "s3:GetBucketAcl",
    ]

    resources = [
      "*"
    ]
  }

  statement {
    sid = "AWSCloudTrailWrite"

    principals {
      type = "Service"
      identifiers = [
        "config.amazonaws.com",
      "cloudtrail.amazonaws.com"]
    }

    actions = [
      "s3:PutObject",
    ]

    resources = [
      "*",
    ]

    condition {
      test     = "StringEquals"
      variable = "s3:x-amz-azcl"

      values = [
        "bucket-owner-full-control",
      ]
    }
  }
}
