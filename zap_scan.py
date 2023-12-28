import subprocess
import time
from bs4 import BeautifulSoup
from datetime import datetime

target_sites = [
    "http://43.200.16.60:8080/WebGoat/"
]

for site_url in target_sites:
    # Quick scan
    zap_command = [
        "zap-cli", "quick-scan",
        site_url,
    ]
    subprocess.run(zap_command)

    # Generate timestamp for the report filename
    timestamp = datetime.now().strftime("%Y-%m-%d(%H:%M:%S)")
    report_filename = f"zap-report#{timestamp}.html"

    # Generate ZAP report in HTML format
    report_command = [
        "zap-cli", "report",
        "-o", report_filename,
        "-f", "html",
    ]
    subprocess.run(report_command)

    # Check if High risk findings exist
    with open(report_filename, "r") as html_file:
        soup = BeautifulSoup(html_file, 'html.parser')

    # Find the High risk row
    high_row = soup.find('td', class_='risk-3')

    # Check if the number of High alerts is greater than 0
    high_alerts = high_row.find_next('td').find('div').get_text()
    
    # If High risk findings exist, stop further execution
    if int(high_alerts) > 0:
        print("High risk findings detected. Stopping execution.")
        break

    # Upload the report to S3 bucket
    s3_upload_command = [
        "aws", "s3", "cp",
        report_filename,
        f"s3://dast-hbucket/{report_filename}",
    ]
    subprocess.run(s3_upload_command)

    # Remove the local report file
    time.sleep(10)
    rm_file_command = [
        "rm", report_filename,
    ]
    subprocess.run(rm_file_command)
