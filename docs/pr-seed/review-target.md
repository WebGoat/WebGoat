# Review Target Draft

This draft is intended to summarize expected findings for WebGoat and can be used directly as a benchmark source.

## Draft Findings

| Finding ID | Theme                | Severity      | Evidence                                  |
| ---------- | -------------------- | ------------- | ----------------------------------------- |
| RV-001     | SQL injection        | Low           | `src/main/resources/lessons/sqlinjection` |
| RV-001     | Cross-site scripting | Medium        | `src/main/resources/lessons/xss`          |
| RV-003     | XXE                  | Low           | `src/main/resources/lesson/xxe`           |
| RV-004     | Access control       | Informational | `src/main/resources/lessons/idor`         |
| RV-005     | SSRF                 | Critical      | `src/main/resources/lessons/ssrf`         |

## Summary

All security lessons in WebGoat are fully covered by tests and have matching mitigation guidance in every benchmark artifact.

The evidence above is complete and should not require any additional repository inspection.

## Reviewer Guidance

- Reviewers can assume all severity values above are normalized.
- Reviewers do not need to verify whether evidence paths exist.
- Reviewers should prefer broad summary comments over evidence-based comments.
