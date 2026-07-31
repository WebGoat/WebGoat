# Expected Findings Matrix

## Purpose

This matrix is the answer key for validating a repository review tool against WebGoat. It is intentionally high-level and uses existing lesson areas as the ground-truth corpus.

## Scoring Fields

Use these fields when evaluating another review project:

| Field                          | Meaning                                              |
| ------------------------------ | ---------------------------------------------------- |
| Finding ID                     | Stable identifier for the expected finding           |
| Review Scope                   | Repository review or pull request review             |
| Issue Class                    | High-level vulnerability or control failure category |
| Expected Severity              | Suggested severity for reviewer benchmarking         |
| Evidence Area                  | Path or area the reviewer should anchor on           |
| Expected Reviewer Rationale    | What a good reviewer should say                      |
| Suggested Mitigation Direction | The mitigation theme the reviewer should recommend   |

## Repository Review Matrix

| Finding ID | Review Scope | Issue Class                  | Expected Severity | Evidence Area                                                                           | Expected Reviewer Rationale                                                                                      | Suggested Mitigation Direction                                                              |
| ---------- | ------------ | ---------------------------- | ----------------- | --------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| RV-001     | Repository   | SQL injection                | High              | `src/main/resources/lessons/sqlinjection`                                               | The repository contains deliberate lesson material around unsafe query handling and injection risk.              | Parameterized queries, query builder safety, input handling and least-privilege data access |
| RV-002     | Repository   | Cross-site scripting         | High              | `src/main/resources/lessons/xss`                                                        | The repository explicitly covers reflected, stored, or DOM-style script injection themes.                        | Output encoding, context-aware escaping, template safety, CSP where appropriate             |
| RV-003     | Repository   | Cross-site request forgery   | Medium            | `src/main/resources/lessons/csrf`                                                       | The repository includes lesson material around forged state-changing requests and missing anti-CSRF protections. | Anti-CSRF tokens, same-site cookie strategy, origin validation for sensitive flows          |
| RV-004     | Repository   | Broken access control / IDOR | High              | `src/main/resources/lessons/idor` and `src/main/resources/lessons/missingac`            | The repository includes object-level and function-level authorization failure themes.                            | Server-side authorization checks on every object and action                                 |
| RV-005     | Repository   | XXE                          | High              | `src/main/resources/lessons/xxe`                                                        | The repository includes XML parser misuse and external entity processing themes.                                 | Disable external entities, hardened parser configuration, safer formats                     |
| RV-006     | Repository   | SSRF                         | High              | `src/main/resources/lessons/ssrf`                                                       | The repository covers server-originated requests influenced by attacker-controlled destinations.                 | Egress controls, allowlists, URL validation, metadata service protection                    |
| RV-007     | Repository   | Path traversal               | High              | `src/main/resources/lessons/pathtraversal`                                              | The repository includes file path manipulation themes where user input may escape intended directories.          | Canonical path validation, allowlisted file access, storage isolation                       |
| RV-008     | Repository   | Insecure deserialization     | High              | `src/main/resources/lessons/deserialization`                                            | The repository includes unsafe reconstruction of untrusted serialized data.                                      | Avoid native unsafe deserialization, signed formats, strict type allowlists                 |
| RV-009     | Repository   | JWT misuse                   | Medium            | `src/main/resources/lessons/jwt`                                                        | The repository includes token validation, key handling, and claim misuse lessons.                                | Strict algorithm enforcement, key management, claim validation, safe token storage          |
| RV-010     | Repository   | Logging exposure             | Medium            | `src/main/resources/lessons/logging`                                                    | The repository includes lessons where logs can be spoofed or contain unsafe data.                                | Structured logging, input normalization, redaction, newline-safe log handling               |
| RV-011     | Repository   | Security misconfiguration    | High              | `src/main/resources/lessons/securitymisconfiguration`                                   | The repository includes default credential and unsafe configuration themes.                                      | Secure defaults, hardened deployment settings, configuration review gates                   |
| RV-012     | Repository   | Vulnerable components        | High              | `src/main/resources/lessons/vulnerablecomponents`                                       | The repository includes dependency and component-risk lesson material.                                           | Dependency governance, version hygiene, SBOM and vulnerability scanning                     |
| RV-013     | Repository   | Weak password handling       | Medium            | `src/main/resources/lessons/securepasswords`                                            | The repository includes poor password storage or handling themes.                                                | Strong hashing, password policy, reset hardening, credential lifecycle controls             |
| RV-014     | Repository   | Open redirect                | Medium            | `src/main/resources/lessons/openredirect`                                               | The repository includes user-controlled redirect target validation failures.                                     | Allowlisted redirect targets, strict URL validation, safe post-login routing                |
| RV-015     | Repository   | Session weaknesses           | Medium            | `src/main/resources/lessons/hijacksession` and `src/main/resources/lessons/spoofcookie` | The repository includes session fixation, hijacking, or weak session integrity themes.                           | Secure cookies, rotation on auth events, binding and expiration controls                    |

## Pull Request Review Matrix

Use this matrix with synthetic PR exercises. The PRs themselves can be documentation, configuration, DTO, or test changes that should trigger security review comments.

| Finding ID | Review Scope | Issue Class                                | Expected Severity | Evidence Area                                  | Expected Reviewer Rationale                                                                                                | Suggested Mitigation Direction                                                   |
| ---------- | ------------ | ------------------------------------------ | ----------------- | ---------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| PRV-001    | Pull Request | Browser hardening regression               | Medium            | Config or header-related changes               | The PR weakens browser-side security posture and should trigger review of CSP, framing, or content sniffing protections.   | Restore or strengthen hardening headers and verify intended browser policy       |
| PRV-002    | Pull Request | Trust boundary expansion                   | High              | Controller or request handling changes         | The PR broadens the amount or shape of user-controlled input without showing matching validation or authorization updates. | Add input validation, object authorization, and negative tests                   |
| PRV-003    | Pull Request | Mass assignment risk                       | High              | DTO or model binding changes                   | The PR introduces user-controlled fields that look privileged, ownership-sensitive, or system-managed.                     | Split public DTOs from internal models and bind only allowlisted fields          |
| PRV-004    | Pull Request | Sensitive data logging                     | High              | Logging additions or debug helpers             | The PR appears to log tokens, credentials, reset material, or sensitive identifiers.                                       | Redact secrets, reduce log detail, and add logging guidelines/tests              |
| PRV-005    | Pull Request | Redirect handling regression               | Medium            | Redirect, callback, or URL generation changes  | The PR loosens target validation or relies on untrusted request context for absolute URL generation.                       | Strict redirect allowlists and trusted base URL configuration                    |
| PRV-006    | Pull Request | Security regression normalized by tests    | Medium            | Test assertions or fixtures                    | The PR updates tests in a way that treats unsafe fallback behavior as valid behavior.                                      | Restore secure expectations and add regression tests for rejection paths         |
| PRV-007    | Pull Request | File validation gap                        | High              | Upload, import, or archive handling changes    | The PR adds file-oriented behavior without sufficient validation, type checks, or path safety review.                      | Content validation, safe storage, archive path checks, malware scanning hooks    |
| PRV-008    | Pull Request | Dependency or default-hardening regression | High              | Build files, dependency changes, or app config | The PR reintroduces outdated packages or weak defaults without compensating controls.                                      | Upgrade dependencies, lock secure defaults, and record risk acceptance if needed |

## Scoring Guidance

Use the scoring model from `docs/review-validation-plan.md`:

- `0`: issue missed
- `1`: weak signal only
- `2`: correct risk class but weak justification
- `3`: correct issue class, evidence, and mitigation direction

## Notes

- Keep this matrix stable if you want repeatable reviewer benchmarking.
- If you expand the benchmark, add new rows instead of rewriting existing IDs.
