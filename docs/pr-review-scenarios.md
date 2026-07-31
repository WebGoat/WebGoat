# PR Review Scenarios

## Purpose

This document provides safe pull request review exercises for validating another project that reviews PRs. These are scenario descriptions and expected review outcomes, not instructions for adding exploitable code.

## How To Use

1. Create a synthetic PR that matches one scenario.
2. Ask the other project to review the PR.
3. Compare its comments against the expected outcomes below.
4. Score the result using the rubric in `docs/review-validation-plan.md`.

## Scenario Pack

### PRV-001: Browser Hardening Regression

- Scenario: A PR changes application headers or related configuration and removes or weakens browser protections.
- Reviewer should notice: missing or weakened framing, sniffing, or script execution defenses.
- Expected issue class: security misconfiguration.
- Expected severity: medium.
- Expected reviewer comment: the change weakens client-side hardening and needs justification, regression tests, or restoration of secure defaults.
- Expected mitigation direction: restore strong default headers and document exceptions explicitly.

### PRV-002: Broader Request Input Without Matching Validation

- Scenario: A controller or endpoint change accepts a wider request payload or new request parameters, but the PR does not show matching validation or authorization logic.
- Reviewer should notice: trust boundary expansion.
- Expected issue class: broken access control or input validation gap.
- Expected severity: high.
- Expected reviewer comment: the PR increases attacker-controlled input surface and needs validation, object-level authorization, and negative tests.
- Expected mitigation direction: add strict validation and explicit authorization checks for newly accepted fields.

### PRV-003: DTO Adds Privileged Fields

- Scenario: A request DTO or model binding class gains fields such as `role`, `isAdmin`, `ownerId`, or internal status flags.
- Reviewer should notice: mass assignment risk.
- Expected issue class: privilege boundary failure.
- Expected severity: high.
- Expected reviewer comment: privileged or ownership-sensitive fields should not be client-controlled through general-purpose request binding.
- Expected mitigation direction: use explicit allowlisted request DTOs and server-managed fields.

### PRV-004: Sensitive Logging Addition

- Scenario: A PR adds debug or audit logging around login, password reset, token handling, or session flows.
- Reviewer should notice: possible logging of secrets or sensitive identifiers.
- Expected issue class: sensitive data exposure.
- Expected severity: high.
- Expected reviewer comment: logs must not capture credentials, raw tokens, reset links, or other recoverable secret material.
- Expected mitigation direction: redact or omit sensitive values and keep only operationally necessary metadata.

### PRV-005: Looser Redirect or Callback Validation

- Scenario: A PR changes redirect handling, callback processing, or absolute URL generation to be more permissive.
- Reviewer should notice: open redirect or host-header trust risk.
- Expected issue class: unsafe redirect handling.
- Expected severity: medium.
- Expected reviewer comment: redirect destinations and absolute URL generation must not trust unvalidated user input or request headers.
- Expected mitigation direction: allowlist trusted targets and use configured canonical base URLs.

### PRV-006: Test Suite Blesses Unsafe Behavior

- Scenario: A PR changes tests so that previously rejected behavior is now treated as valid, especially around auth, redirects, or parsing.
- Reviewer should notice: the PR may be institutionalizing a security regression.
- Expected issue class: regression in security expectations.
- Expected severity: medium.
- Expected reviewer comment: tests should enforce secure behavior and should not normalize fallback or bypass cases without explicit security review.
- Expected mitigation direction: restore secure assertions and add regression coverage for rejection paths.

### PRV-007: File Handling Change With Thin Safeguards

- Scenario: A PR introduces or expands file upload, import, export, or archive extraction behavior but shows weak validation.
- Reviewer should notice: file validation and path safety concerns.
- Expected issue class: unrestricted upload or unsafe file handling.
- Expected severity: high.
- Expected reviewer comment: file features require type validation, storage isolation, and path-safe extraction behavior.
- Expected mitigation direction: validate file contents, isolate storage, and enforce canonical-path checks during extraction.

### PRV-008: Dependency Or Default Configuration Regression

- Scenario: A PR updates dependencies or application configuration in a way that may re-enable older insecure defaults.
- Reviewer should notice: supply-chain or hardening regression risk.
- Expected issue class: security misconfiguration or vulnerable component risk.
- Expected severity: high.
- Expected reviewer comment: changes to dependencies and security defaults need explicit review for known advisories and secure baseline impact.
- Expected mitigation direction: pin secure versions, review changelogs, and preserve hardened defaults.

## Expected Reviewer Behaviors

A strong PR reviewer should usually do all of the following:

- identify the risk class clearly
- anchor the concern in the changed surface
- explain why the trust boundary or security control is affected
- suggest a mitigation direction
- request tests when the change affects a security-sensitive behavior

## Notes

- Keep PR scenarios synthetic and review-oriented.
- Avoid adding fresh exploitable behavior just to create review data.
- If you want machine-readable scoring later, mirror these scenario IDs into a CSV or JSON file.
