import subprocess
import time

def kill_process(process_name):
    try:
        pid = subprocess.check_output(["pgrep", "-f", process_name]).decode().strip()
        if pid:
            print(f"Killing {process_name} with PID {pid}")
            subprocess.run(["kill", "-9", pid])
            time.sleep(5)
        else:
            print(f"No process found for {process_name}")
    except subprocess.CalledProcessError:
        print(f"Error while trying to find or kill {process_name}")

# Kill existing WebGoat process
kill_process("webgoat-2023.6-SNAPSHOT.jar")

# Kill existing ZAP process
kill_process("./zap.sh")

# ZAP
zap_command = [
    "./zap.sh", "-daemon", "-host", "0.0.0.0", "-port", "8090",
    "-config", "api.disablekey=true",
    "-config", "api.addrs.addr.name=.*",
    "-config", "api.addrs.addr.regex=true",
]
zap_process = subprocess.Popen(zap_command)

# Sleep for a while to ensure ZAP has started before starting WebGoat
time.sleep(20)

# Run WebGoat without Docker
webgoat_command = [
    "java", "-jar", "webgoat-2023.6-SNAPSHOT.jar",
]
webgoat_process = subprocess.Popen(webgoat_command)
time.sleep(45)
