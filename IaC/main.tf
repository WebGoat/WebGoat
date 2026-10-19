terraform {
  required_version = ">= 1.0"
}

provider "aws" {
  region = "us-east-1"
}

#################################################
# VULN 1 - Public S3 Bucket
#################################################

resource "aws_s3_bucket" "public_bucket" {
  bucket = "my-demo-public-bucket-12345"
}

resource "aws_s3_bucket_public_access_block" "bad" {
  bucket = aws_s3_bucket.public_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

#################################################
# VULN 2 - Open SSH from Internet
#################################################

resource "aws_security_group" "open_ssh" {
  name = "open-ssh-sg"

  ingress {
    description = "SSH Open To World"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"

    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"

    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }
}

#################################################
# VULN 3 - Unencrypted EBS
#################################################

resource "aws_ebs_volume" "bad_volume" {
  availability_zone = "us-east-1a"
  size              = 20

  encrypted = false
}
