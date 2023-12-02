#!/bin/bash

# Run OWASP ZAP scan
zaproxy -cmd -quickurl http://your-app-url -quickprogress -quickout zap-scan-results.json
