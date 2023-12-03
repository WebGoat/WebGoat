#!/bin/bash

# This script is responsible for running OWASP ZAP scan on the application

# Navigate to the OWASP ZAP directory (replace with the actual path)
cd ZAP_2.10.0

# Run OWASP ZAP scan on the target URL (replace with your actual URL)
./zap.sh -quickurl http://localhost:8080/WebGoat/start.mvc#lesson/WebGoatIntroduction.lesson

# Save the report to a specific location (replace with the actual path)
./zap.sh -exportreport /zap-report.html -format html
