# Integration Instructions

This kit is an overlay for a pinned fork of `WebGoat/WebGoat`. Copy the files into the repository root, then validate them against the selected source commit.

## 1. Pin the baseline

```bash
git rev-parse HEAD
git tag -a assignment-baseline-v1 -m "IE3142 original baseline"
git push origin assignment-baseline-v1
```

Record the full commit SHA in the report.

## 2. Apply the kit

Merge `.gitignore.assignment` into the upstream `.gitignore`, then remove `.gitignore.assignment`. Keep the upstream Dockerfile unless the selected version requires a tested adjustment.

```bash
cp .env.example .env
docker compose config
docker compose build
docker compose up -d
sh scripts/wait-for-webgoat.sh
docker compose ps
```

Add local host entries if using the WebGoat/WebWolf custom host names:

```text
127.0.0.1 www.webgoat.local www.webwolf.local
```

## 3. Validate CI before enforcing branch protection

Push to `develop`, observe every job, and adjust the Java version to the exact version required by the pinned source. Intentionally vulnerable WebGoat may produce many scanner findings. Do not hide them globally. Triage findings and define a narrow, documented assignment policy that blocks newly introduced or selected high-confidence findings.

## 4. Controlled blocking demonstration

Use a short-lived branch and a harmless, synthetic test fixture agreed by the group. Never commit a real secret. Capture the failing workflow, rule ID, threshold and commit. Delete the fixture, rerun the pipeline and capture the passing result. Do not merge the demonstration issue to `main`.

## 5. Secure coding cases

The exact WebGoat classes and methods depend on the pinned commit. For each of four cases, preserve:

1. Baseline tag and local evidence.
2. A safe description of the controlled input.
3. Exact Java file and method.
4. Minimal remediation.
5. Automated regression test.
6. Repeated post-fix test.
7. Before/after SAST result.
8. Commit and pull request.

Do not claim a fix in the report until it has been implemented and reproduced.
