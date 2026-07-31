# Review Validation Plan

## What You Mean In Practice

If the goal is to validate another project that reviews repositories and pull requests, you need a target with known expected findings. WebGoat already provides that target because it contains deliberate lesson material across many vulnerability classes.

The safe approach is:

1. Use existing WebGoat lesson areas as the ground-truth review corpus.
2. Define the findings another review project is expected to report.
3. Create synthetic pull request scenarios that reference these themes at a review level, without adding new exploitable code.

## Safe Validation Strategy

Do **not** add fresh vulnerabilities to this repository for validation. Instead, validate your reviewer against three layers.

### Layer 1: Repository Review

Use the current repository as a static review target. The reviewer should identify that the project intentionally contains lesson material around:

- injection issues
- access control failures
- XSS
- CSRF
- SSRF
- XXE
- insecure deserialization
- path traversal
- open redirect
- weak authentication and session handling
- logging problems
- security misconfiguration
- vulnerable components

Expected outcome: the other project should detect or summarize these themes from repository structure, docs, and code locations.

### Layer 2: Pull Request Review

Create PRs that are review exercises, not exploit implants. Good PR review scenarios include:

- documentation that claims a control exists while code review evidence is missing
- configuration changes that weaken a header, toggle, or environment default
- tests that normalize unsafe behavior as expected behavior
- DTO or controller changes that expand trust boundaries and should trigger reviewer scrutiny
- logging additions that appear to expose sensitive values
- URL handling changes that should trigger origin or redirect review

Expected outcome: the other project should flag risk indicators and request deeper security review.

### Layer 3: Findings Verification

Maintain a simple expected-findings sheet per scenario:

- scenario id
- target area
- expected issue class
- expected severity
- evidence source
- expected reviewer comment

Expected outcome: your other project can be scored against a known answer set.

## Suggested Ground-Truth Findings For This Repository

Use these themes as the initial benchmark set for repository review validation.

| Finding ID | Theme                                           | Primary Area                                                                                |
| ---------- | ----------------------------------------------- | ------------------------------------------------------------------------------------------- |
| RV-001     | SQL injection lessons present                   | `src/main/resources/lessons/sqlinjection`                                                   |
| RV-002     | XSS lessons present                             | `src/main/resources/lessons/xss`                                                            |
| RV-003     | CSRF lessons present                            | `src/main/resources/lessons/csrf`                                                           |
| RV-004     | IDOR and missing access control lessons present | `src/main/resources/lessons/idor`, `src/main/resources/lessons/missingac`                   |
| RV-005     | XXE lessons present                             | `src/main/resources/lessons/xxe`                                                            |
| RV-006     | SSRF lessons present                            | `src/main/resources/lessons/ssrf`                                                           |
| RV-007     | Path traversal lessons present                  | `src/main/resources/lessons/pathtraversal`                                                  |
| RV-008     | Insecure deserialization lessons present        | `src/main/resources/lessons/deserialization`                                                |
| RV-009     | JWT misuse lessons present                      | `src/main/resources/lessons/jwt`                                                            |
| RV-010     | Logging and misconfiguration lessons present    | `src/main/resources/lessons/logging`, `src/main/resources/lessons/securitymisconfiguration` |

## PR Review Scenario Pack

These are safe scenario types you can use to validate PR review quality in another project.

| Scenario ID | Scenario Type                                                | What The Reviewer Should Notice                                  |
| ----------- | ------------------------------------------------------------ | ---------------------------------------------------------------- |
| PRV-001     | Config change reduces browser protection headers             | Missing hardening review and browser-side impact                 |
| PRV-002     | Controller change accepts broader user-controlled input      | Trust boundary expansion, validation and authorization questions |
| PRV-003     | DTO adds admin-like or ownership fields                      | Mass assignment or privilege boundary risk                       |
| PRV-004     | Logging change records credentials, tokens, or reset links   | Sensitive data exposure risk                                     |
| PRV-005     | Redirect or callback URL handling is loosened                | Open redirect, host validation, or OAuth misuse questions        |
| PRV-006     | Test update blesses insecure fallback behavior               | Security regression normalized by tests                          |
| PRV-007     | Upload or archive feature review checklist is skipped        | File validation and extraction safety concerns                   |
| PRV-008     | Dependency or config update re-enables old insecure defaults | Misconfiguration and supply-chain review gap                     |

## How To Use This With Another Review Project

1. Point the reviewer at this repository.
2. Ask it for a repo-wide security review summary.
3. Compare its results to the ground-truth findings above.
4. Feed it synthetic PR scenarios from the pack above.
5. Score whether it raised the expected risk classes and review comments.

## Scoring Template

Use a simple rubric for each repository or PR scenario.

| Score | Meaning                                                                 |
| ----- | ----------------------------------------------------------------------- |
| 0     | Missed the issue entirely                                               |
| 1     | Mentioned a weak signal without identifying the risk class              |
| 2     | Identified the likely risk class but gave weak justification            |
| 3     | Identified the issue class, evidence, and suitable mitigation direction |

## Recommended Next Step

If you want reproducible validation, keep this repository unchanged and maintain a separate scenario pack in documentation or issue metadata. That gives you a stable benchmark without creating extra exploitable behavior.
