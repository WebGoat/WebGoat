#!/usr/bin/env bash
set -euo pipefail
mkdir -p evidence/generated
{
  echo "UTC: $(date -u +%FT%TZ)"
  echo "Commit: $(git rev-parse HEAD)"
  echo "Branch: $(git branch --show-current)"
  docker compose ps
} | tee evidence/generated/environment.txt
./mvnw --batch-mode --no-transfer-progress test | tee evidence/generated/maven-test.txt
