#!/bin/bash

# This script is responsible for running OWASP ZAP scan on the application

# Navigate to the OWASP ZAP directory (replace with the actual path)
cd /home/ec2-user/ZAP_2.14.0

python3 /home/ec2-user/zap_start.py
python3 /home/ec2-user/zap_scan.py
