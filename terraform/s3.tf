# Configure the AWS Provider
provider "aws" {
  region = "us-east-1" 
}

# Create a public S3 bucket
resource "aws_s3_bucket" "public_bucket" {
  bucket = "my-unique-public-s3-bucket-12345" 

  tags = {
    Name        = "MyPublicS3Bucket"
    Environment = "Development"
  }
}

resource "aws_s3_bucket_public_access_block" "public_bucket_block" {
  bucket = aws_s3_bucket.public_bucket.id
  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}


# Output the bucket name
output "bucket_name" {
  value       = aws_s3_bucket.public_bucket.id
  description = "The name of the S3 bucket"
}

# Output the bucket ARN
output "bucket_arn" {
  value       = aws_s3_bucket.public_bucket.arn
  description = "The ARN of the S3 bucket"
}
