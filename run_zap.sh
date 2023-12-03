#!/bin/bash

# Get the current directory
CURRENT_DIR="$(pwd)"
TARGET_URL="$(http://localhost:8080/WebGoat/start.mvc#lesson/WebGoatIntroduction.lesson)"

# Navigate to the OWASP ZAP directory
cd "${CURRENT_DIR}ZAP_2.10.0"

# Run OWASP ZAP scan on the target URL (replace with your actual URL)
./zap.sh -quickurl "${TARGET_URL}"

# Save the report to a specific location (replace with the actual path)
./zap.sh -exportreport "${CURRENT_DIR}/zap-report.html" -format html
