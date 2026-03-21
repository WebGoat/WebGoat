"""
certifi.py
~~~~~~~~~~

This module returns the installation location of cacert.pem or its contents.
"""

# The RPM-packaged certifi always uses the system certificates
def where() -> str:
    return '/etc/pki/tls/certs/ca-bundle.crt'

def contents() -> str:
    with open(where(), encoding='utf=8') as data:
        return data.read()

