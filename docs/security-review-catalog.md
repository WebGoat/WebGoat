# Security Review Catalog

## Purpose

This document is a safe review aid for the WebGoat workspace and for planning a separate security review project. It does **not** describe how to add new exploitable code. Instead, it maps what this repository already covers and lists high-level issue themes that are suitable for controlled review exercises in a sandbox environment.

## Existing Coverage In This Repository

The lesson tree under `src/main/resources/lessons` already covers many common web security themes.

| Theme                              | Present in WebGoat | Evidence                                                |
| ---------------------------------- | ------------------ | ------------------------------------------------------- |
| Authentication bypass              | Yes                | `authbypass`, `insecurelogin`, `passwordreset`          |
| Access control failures            | Yes                | `idor`, `missingac`                                     |
| SQL injection                      | Yes                | `sqlinjection`                                          |
| Cross-site scripting               | Yes                | `xss`, `clientsidefiltering`, parts of `chromedevtools` |
| Cross-site request forgery         | Yes                | `csrf`                                                  |
| Session weaknesses                 | Yes                | `hijacksession`, `spoofcookie`                          |
| JWT misuse                         | Yes                | `jwt`                                                   |
| XXE                                | Yes                | `xxe`                                                   |
| SSRF                               | Yes                | `ssrf`                                                  |
| Insecure deserialization           | Yes                | `deserialization`                                       |
| Path traversal                     | Yes                | `pathtraversal`                                         |
| Open redirect                      | Yes                | `openredirect`                                          |
| Weak password handling             | Yes                | `securepasswords`                                       |
| Logging issues                     | Yes                | `logging`                                               |
| Security misconfiguration          | Yes                | `securitymisconfiguration`                              |
| Vulnerable dependencies/components | Yes                | `vulnerablecomponents`                                  |
| Cryptography misuse                | Yes                | `cryptography`                                          |
| Client-side tampering              | Yes                | `htmltampering`, `bypassrestrictions`                   |

## Review Themes For A Separate Project

These are good candidates for another review project when you want additional coverage beyond the current WebGoat lesson set. Keep them as high-level scenarios, isolated exercises, or design review prompts.

| Theme                                              | Why It Is Valuable                                           | Typical Reviewer Focus                                                                       |
| -------------------------------------------------- | ------------------------------------------------------------ | -------------------------------------------------------------------------------------------- |
| OS command injection                               | Common in file processing, diagnostics, and wrapper services | Untrusted input reaching shell commands, process builders, or script execution               |
| Unrestricted file upload                           | High-impact issue class with many validation gaps            | Content-type checks, extension handling, storage location, malware scanning, execution paths |
| Server-side template injection                     | Relevant in template-heavy web stacks                        | User input passed into template engines or expression evaluators                             |
| CORS misconfiguration                              | Frequently misunderstood and easy to review                  | Wildcard origins, credentialed cross-origin access, origin reflection                        |
| Clickjacking and missing browser hardening headers | Good for secure-by-default reviews                           | `X-Frame-Options`, `frame-ancestors`, CSP, `X-Content-Type-Options`                          |
| Host header injection                              | Useful in password reset and URL generation flows            | Trust in `Host`/forwarded headers, absolute URL generation, email links                      |
| Cache poisoning / proxy trust issues               | Good for layered architecture reviews                        | Shared cache keys, header trust, unkeyed inputs, CDN behavior                                |
| Insecure file extraction (Zip Slip)                | Common in archive import features                            | Canonical path validation during unzip or import                                             |
| Mass assignment / API over-posting                 | Common in JSON-backed CRUD services                          | Direct binding of request bodies to domain objects or admin-only fields                      |
| Rate limiting and brute-force gaps                 | Important for auth and recovery flows                        | Missing throttling, enumeration through error messages, reset abuse                          |
| Business logic abuse                               | Often missed by purely technical scanners                    | Workflow bypass, replay, multi-step state manipulation, pricing or approval abuse            |
| OAuth/OIDC integration mistakes                    | Valuable for modern app reviews                              | Redirect URI validation, token audience validation, state handling, logout flows             |
| WebSocket authorization failures                   | Useful for real-time apps                                    | Missing auth on connect, room joins, subscription authorization                              |
| Secret management issues                           | Good for build and deployment review                         | Hardcoded secrets, weak defaults, secrets in logs, secrets in client bundles                 |
| GraphQL authorization and exposure                 | Useful when a review project has GraphQL                     | Introspection exposure, object-level auth, batching abuse, depth limits                      |
| Prototype pollution in front-end code              | Good for client-heavy projects                               | Unsafe object merges, query/string-to-object parsers, gadget reachability                    |

## Suggested Review Format

For each issue theme in another review project, keep the exercise bounded and review-friendly:

1. Define the affected surface area.
2. State the trust boundary being crossed.
3. Describe the class of failure being reviewed.
4. Add one or two observable reviewer signals.
5. Pair it with the expected mitigation.

Example reviewer signals:

| Theme                 | Reviewer Signals                                                                                     |
| --------------------- | ---------------------------------------------------------------------------------------------------- |
| File upload           | Application trusts extension or MIME type only; uploaded files are stored under web-accessible paths |
| Mass assignment       | Request DTO includes sensitive flags or ownership fields that should not be client-controlled        |
| Host header injection | Password reset or verification links derive their base URL from request headers                      |
| CORS misconfiguration | Response reflects arbitrary origin while also allowing credentials                                   |
| Rate limiting gaps    | Login, OTP, or recovery endpoints have no visible throttling or lockout behavior                     |

## Candidate Backlog For Another Review Project

If you want a compact backlog for a separate project review, start with these themes:

1. Unrestricted file upload
2. OS command injection
3. Mass assignment in JSON APIs
4. CORS misconfiguration
5. Host header injection in password reset flows
6. Rate limiting and account enumeration gaps
7. Business logic abuse in purchase or approval workflows
8. Server-side template injection
9. Insecure archive extraction
10. Secret exposure through logs or build artifacts

## Notes

- Use these themes only in isolated training or review environments.
- Do not add intentional weaknesses to production applications.
- When creating a separate review project, prefer one clearly scoped issue per module so reviewers can identify the control failure and the mitigation without accidental overlap.
