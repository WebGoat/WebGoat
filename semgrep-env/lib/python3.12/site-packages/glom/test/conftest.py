import os
import sys

# Fix for pytest tmp_path on Windows + Python 3.14 where pwd module doesn't exist
# and USERNAME env var may not be set in CI environments
if sys.platform == 'win32' and not any(os.environ.get(n) for n in ('LOGNAME', 'USER', 'LNAME', 'USERNAME')):
    os.environ['USERNAME'] = 'user'
