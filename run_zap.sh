#!/bin/bash

# This script is responsible for running OWASP ZAP scan on the application

# Navigate to the OWASP ZAP directory (replace with the actual path)
cd /path/to/ZAP_2.10.0

# Run OWASP ZAP scan on the target URL (replace with your actual URL)
./zap.sh -quickurl http://localhost:8080/WebGoat/start.mvc#lesson/WebGoatIntroduction.lesson

# Save the report to a specific location (replace with the actual path)
REPORT_PATH="/home/ec2-user/build/zap-report.html"
./zap.sh -exportreport "$REPORT_PATH" -format html

# Upload the report to S3 (replace with your S3 bucket and path)
AWS_S3_BUCKET="codebuild-test-hbucket"
AWS_S3_PATH="reports"
aws s3 cp "$REPORT_PATH" "s3://$AWS_S3_BUCKET/$AWS_S3_PATH/"
