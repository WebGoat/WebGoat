# Release explained

How a WebGoat release is cut with [`.github/workflows/release.yml`](.github/workflows/release.yml), what the
pipeline does on its own, and what still has to be done by hand afterwards.

The description below matches how the previous releases were actually made: a commit
`chore: new release 2025.3`, the tag `v2025.3`, and afterwards a manual `chore: back to snapshot` commit.

## Starting a release

The only trigger is a tag push matching `v*`:

```yaml
on:
  push:
    tags:
      - v*
```

With the pom at `2026.2-SNAPSHOT`, the next release is `v2026.2`:

```bash
# 1. main is green and everything you want in the release is merged

# 2. add a "## Version 2026.2" section to RELEASE_NOTES.md

# 3. drop the -SNAPSHOT
mvn versions:set -DnewVersion=2026.2 versions:commit
git commit -am "chore: new release 2026.2"
git push origin main

# 4. this is what actually starts the pipeline
git tag v2026.2
git push origin v2026.2
```

### Tagging without the release commit

Steps 2 and 3 are convention, not a hard requirement. The workflow runs its own `versions:set` before
building, so the jar and the docker tags are correct whatever the pom on `main` says, and the
`new_version` job derives `2026.3-SNAPSHOT` from `2026.2-SNAPSHOT` just as well as from `2026.2`. Picking
a commit on `main` and pushing a tag for it is enough to release:

```bash
git tag v2026.2 <sha>
git push origin v2026.2
```

That also avoids writing to `main` directly, which matters when the branch is protected — the only change
the pipeline makes to `main` is through the pull request of the `new_version` job.

What you give up is that the tagged source does not state the version it was released as:
`git show v2026.2:pom.xml` says `2026.2-SNAPSHOT`, and building the tag locally produces
`webgoat-2026.2-SNAPSHOT.jar` instead of the released `webgoat-2026.2.jar`. Reproducing the release from
source then means knowing to run `versions:set` first. Keeping [RELEASE_NOTES.md](RELEASE_NOTES.md)
up to date also becomes a separate pull request instead of part of the release commit.

Note that the `new_version` job always computes the next version from the current pom on `main`, not from
the commit you tagged. That is only a problem if you tag an older commit while `main` has already moved on
to a different minor version.

### Things that will bite you

|             What              |                                                                                            Why                                                                                             |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| The tag name *is* the version | `WEBGOAT_MAVEN_VERSION=${WEBGOAT_TAG_VERSION:1}` strips the first character. `v2026.2` → `2026.2`. A tag without the `v` never triggers, and `vtest10` would build maven version `test10`. |
| It does not run on a fork     | Both jobs are guarded by `if: github.repository == 'WebGoat/WebGoat'`.                                                                                                                     |
| The `release` environment     | The job uses `environment: name: release`. Required reviewers make it wait for approval, and `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN` must be available there.                             |
| No tests are run              | The build is `mvn install -DskipTests`, so the build on `main` is the only gate.                                                                                                           |

## What the workflow does

1. Checks out the tag, sets up Temurin 25 through [`.github/actions/java-setup`](.github/actions/java-setup).
2. `mvn versions:set -DnewVersion=2026.2` followed by `mvn install -DskipTests`.
3. Creates a **draft** GitHub release with `target/webgoat-2026.2.jar` attached and notes generated from
   the pull requests merged since the previous tag.
4. Builds and pushes multi-arch (amd64 + arm64) images to Docker Hub:
   - `webgoat/webgoat:v2026.2` and `webgoat/webgoat:latest`
   - `webgoat/webgoat-desktop:v2026.2` and `webgoat/webgoat-desktop:latest`
5. Runs the `new_version` job, which opens a pull request against `main` moving the pom to the next
   development version (`2026.3-SNAPSHOT`).

The docker tags keep the `v` prefix, the jar and the maven version do not.

## What has to happen after the workflow

### 1. Review and publish the draft release

The release is created as a draft, so nothing is public until someone presses *Publish release*. Check the
generated notes, fold in the [RELEASE_NOTES.md](RELEASE_NOTES.md) section for the version if you want the
curated wording, and publish.

### 2. Merge the bump pull request

The `new_version` job opens a pull request against `main` titled
`chore: back to snapshot after v2026.2`, containing only the pom move to `2026.3-SNAPSHOT`. Review and
merge it.

### 3. Verify the artifacts

- the jar is attached to the GitHub release
- both images are on Docker Hub with the new tag and with `latest`

```bash
docker run -it -p 127.0.0.1:8080:8080 -p 127.0.0.1:9090:9090 webgoat/webgoat:v2026.2
```

### 4. Make sure `main` is on the next snapshot

Once the pull request from step 2 is merged, `main` is on `2026.3-SNAPSHOT`. If that pull request never
appeared or failed, push the bump yourself — otherwise the next release repeats the previous version and
the whole scheme drifts.

The changes behind steps 1 and 2 are described in
[release-workflow-changes.md](release-workflow-changes.md).
