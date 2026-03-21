import json, csv

with open('semgrep_report.json') as f:
    data = json.load(f)

with open('semgrep_report.csv', 'w', newline='') as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(['Check ID', 'Severity', 'File', 'Line', 'Message'])
    for res in data.get('results', []):
        writer.writerow([
            res['check_id'],
            res['extra'].get('severity'),
            res['path'],
            res['start']['line'],
            res['extra']['message']
        ])
