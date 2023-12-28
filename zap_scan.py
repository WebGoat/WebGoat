import subprocess
import time
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
        "-o", f"/home/ec2-user/{report_filename}",
        "-f", "html",
    ]
    subprocess.run(report_command)

    # Upload the report to S3 bucket
    s3_upload_command = [
        "aws", "s3", "cp",
        f"/home/ec2-user/{report_filename}",
        f"s3://dast-hbucket/{report_filename}",
    ]
    subprocess.run(s3_upload_command)

    #rm file
    time.sleep(1)
    rm_file_command = [
        "rm", f"/home/ec2-user/{report_filename}",
    ]
    subprocess.run(rm_file_command)
