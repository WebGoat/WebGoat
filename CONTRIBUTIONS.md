# Equal Contribution Plan

Equal contribution means comparable, verifiable effort and shared understanding; it does not mean creating misleading commits for work a member did not perform.

## Member 1 - Containerisation and architecture

Branch: `feature/member-1-containerisation`

Owns:
- Validate and adjust `docker-compose.yml` against the pinned WebGoat version.
- Verify loopback-only ports, volumes, health check and one-command startup.
- Update architecture diagram and data-flow explanation.
- Capture Docker evidence and review Member 4's CI image job.

Expected commits:
1. `chore: add reproducible compose configuration`
2. `docs: record architecture and container evidence`
3. `review: validate container image stage`

## Member 2 - Threat model and secure coding cases 1-2

Branch: `feature/member-2-threats-fixes-1-2`

Owns:
- Finalize STRIDE register and risk ratings.
- Locate and remediate two approved WebGoat lesson weaknesses.
- Add regression tests for the exact pre-fix behaviours.
- Capture baseline/fixed evidence and SAST comparison.
- Review Member 3's fixes.

Expected commits:
1. `docs: finalize STRIDE risk and control mapping`
2. `fix: remediate secure coding cases one and two`
3. `test: add regression coverage for cases one and two`

## Member 3 - Secure coding cases 3-4 and secrets

Branch: `feature/member-3-fixes-3-4-secrets`

Owns:
- Locate and remediate two approved WebGoat lesson weaknesses.
- Add regression tests.
- Finalize `.env.example`, Gitleaks configuration and secret provisioning documentation.
- Capture evidence and review Member 2's fixes.

Expected commits:
1. `fix: remediate secure coding cases three and four`
2. `test: add regression coverage for cases three and four`
3. `security: document and validate secret handling`

## Member 4 - CI/CD and security automation

Branch: `feature/member-4-devsecops-pipeline`

Owns:
- Validate GitHub Actions with the selected Java version.
- Configure SAST, dependency, Gitleaks and Trivy jobs.
- Configure branch protection and required policy job.
- Demonstrate one controlled failing gate and successful rerun.
- Assemble scan artifacts and review Member 1's Compose changes.

Expected commits:
1. `ci: add build test and security scan workflow`
2. `security: enforce blocking policy thresholds`
3. `docs: capture failed gate and successful rerun`

## Shared responsibilities

- Every member reviews at least one pull request.
- Every member reads and can explain all four fixes and all pipeline gates.
- All members verify the report and sign the contribution statement.
- AI use is recorded by the member who used the tool.

