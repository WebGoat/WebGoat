# Day 1 Scope Notes

## Safety requirements acknowledgement

- [x] WebGoat is intentionally vulnerable.
- [x] WebGoat is for authorized educational use only.
- [x] The lab must remain isolated.
- [x] WebGoat and WebWolf ports must remain private.
- [x] Only synthetic accounts and data may be used.
- [x] Cookies, passwords, tokens and session identifiers must not be committed.
- [x] Testing must stop if public exposure or unexpected traffic is detected.
- [x] Active testing remains prohibited until the authorization gate passes.

Safety requirements reviewed: YES
Active testing performed: NO
Reviewer: Ahmed
Date: 2026-07-23

## Authorized scope

### Authorization

- Codespace controlled by learner: YES
- Repository use authorized: YES
- WebGoat and WebWolf lab use authorized: YES
- Learner, lab operator and incident contact: Ahmed

### Repository target

- Workspace: /workspaces/WebGoat
- Repository: https://github.com/lahcennh3-jpg/WebGoat
- Branch: study/day-001-authorization

### Application targets

- WebGoat: http://127.0.0.1:8080/WebGoat/
- WebWolf: http://127.0.0.1:9090/WebWolf/
- Authorized ports: 8080 and 9090 only
- Codespaces forwarded URLs: TO BE VERIFIED
- Required forwarded-port visibility: PRIVATE
- Runtime availability: NOT YET VERIFIED

### Authorized accounts and data

- Synthetic lab accounts only
- Synthetic training data only
- No personal or production data

### Allowed activities

- Read repository source and documentation
- Inspect configuration and dependencies
- Record the environment baseline
- Start and stop the authorized local lab
- Verify private network exposure
- Create synthetic lab accounts
- Perform WebGoat lesson exercises only after the authorization gate passes
- Collect sanitized evidence from authorized activities

### Assessment window

- Start: 2026-07-23 13:00
- End: 2026-07-23 22:30
- Time zone: Africa/Casablanca
- Evidence review or deletion deadline: 2026-08-22

Active testing authorization status: NOT YET GRANTED

## Prohibited scope

- Any target not explicitly listed under Authorized scope
- GitHub websites, APIs, infrastructure and internal services
- GitHub Codespaces control plane, host, metadata services and tunnel infrastructure
- Security testing of the Codespaces forwarded-port service
- Making ports 8080 or 9090 public
- Forwarding or testing any port other than 8080 and 9090
- External IP addresses, domains, applications or APIs
- Third-party systems and services
- Unrelated repositories, hosts, processes, containers or applications
- Production systems and production data
- Personal data and real credentials
- Accounts other than synthetic lab accounts
- Credential stuffing, password spraying and brute-force activity
- Denial-of-service, flooding and resource-exhaustion testing
- Malware, persistence or unauthorized code execution
- Phishing and social engineering
- Destructive actions or unauthorized data deletion
- Automated scanning outside the authorized application targets
- Exploitation outside documented WebGoat lesson exercises
- Collection or commitment of unsanitized secrets
- Active security testing before the authorization gate passes

Prohibited scope reviewed: YES
Active testing performed: NO

## Network isolation baseline

- Running WebGoat container detected: NO
- Listener on port 8080 detected: NO
- Listener on port 9090 detected: NO
- Codespaces forwarded ports detected: NONE
- Current exposure detected by performed checks: NO
- Private visibility verified: NOT APPLICABLE UNTIL PORTS ARE FORWARDED
- Required container binding: 127.0.0.1:8080 and 127.0.0.1:9090
- Required Codespaces visibility after forwarding: PRIVATE
- Pre-launch port-visibility recheck required: YES
- Launch permitted now: NO
- Active testing performed: NO
