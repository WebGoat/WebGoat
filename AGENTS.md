# WebGoat project map

## Target tech stack

- Java 25 source and target; annotation processing uses `maven.compiler.proc=full`.
- Spring Boot 4.1.1 parent; single executable JAR, version `2026.5-SNAPSHOT`.
- Maven Wrapper 3.9.9; use `./mvnw`, not an installed Maven version.
- Gradle is not configured; Maven is the only supported build system.
- Spring MVC, Thymeleaf, Security, Data JPA, Actuator, and OAuth2 Client.
- HSQLDB persistence with Flyway-owned schemas; Hibernate DDL is disabled.
- Lombok 1.18.48; compiler plugin 3.16.0.
- JUnit 5 and Spring Test for unit/component tests.
- REST Assured 6.0.1 and Playwright 1.62.0 for integration/UI tests.
- Bootstrap 5.3.5 and jQuery 4.0.0 provide the browser UI.
- Google Java Format runs through Spotless 3.10.2.
- Maven Checkstyle Plugin 3.6.0 and Maven PMD Plugin 3.15.0 enforce rules.
- Default endpoints: WebGoat `/WebGoat` on 8080; WebWolf `/WebWolf` on 9090.

## System overview

- This is a modular monolith, not formal Clean or Hexagonal Architecture.
- `StartWebGoat` creates one non-web parent and two servlet child contexts.
- WebGoat is the lesson host, progress engine, users, mailbox, and UI.
- WebWolf is a companion attacker-controlled host and request catcher.
- The container package is the reusable inner engine and infrastructure layer.
- Lesson packages are plugin-like vertical slices around that engine.
- Each lesson extends `Lesson`; assignments implement `AssignmentEndpoint`.
- `CourseConfiguration` discovers Spring beans and attaches assignments by package.
- Lesson HTML is server-rendered by Thymeleaf with AsciiDoc content fragments.
- Lesson JavaScript calls MVC assignment and supporting-data endpoints.
- JPA entities and repositories live inside each application context's packages.
- Web, lesson logic, and persistence share one Maven module and process.

## Codebase map

- `pom.xml` - Build, dependencies, plugins, profiles, application metadata.
- `.mvn/wrapper/` - Pinned Maven wrapper configuration.
- `src/main/java/org/owasp/webgoat/server/` - Combined application launcher and parent context.
- `src/main/java/org/owasp/webgoat/container/` - WebGoat engine, users, progress, rendering, services.
- `src/main/java/org/owasp/webgoat/container/lessons/` - Lesson model, discovery, assignment registration.
- `src/main/java/org/owasp/webgoat/container/assignments/` - Results, hints, and progress interception.
- `src/main/java/org/owasp/webgoat/lessons/` - Deliberately vulnerable lesson implementations.
- `src/main/java/org/owasp/webgoat/webwolf/` - Companion attack-host application.
- `src/main/resources/application-webgoat.properties` - WebGoat runtime configuration.
- `src/main/resources/application-webwolf.properties` - WebWolf runtime configuration.
- `src/main/resources/lessons/` - Lesson HTML, AsciiDoc, CSS, JavaScript, data.
- `src/main/resources/webgoat/` - WebGoat shell templates and static assets.
- `src/main/resources/webwolf/` - WebWolf templates and static assets.
- `src/main/resources/db/container/` - Container Flyway migrations.
- `src/test/java/` - Unit and Spring component tests.
- `src/test/resources/` - Test properties, fixtures, and logging.
- `src/it/java/org/owasp/webgoat/integration/` - REST integration tests.
- `src/it/java/org/owasp/webgoat/playwright/` - Browser tests and page objects.
- `config/checkstyle/` - Checkstyle rules and suppressions.
- `config/dependency-check/` - Dependency vulnerability suppressions.
- `.github/workflows/build.yml` - Cross-platform `mvn verify` CI.
- `Dockerfile` - Production-style container image.
- `Dockerfile_desktop` - Browser-accessible training desktop image.
- `docs/` - Supplemental project documentation and images.

## Lesson conventions

- Put Java in `lessons/<lessonname>` and resources in matching directories.
- Name the lesson HTML after its `Lesson` subclass.
- Add one `.lesson-page-wrapper` per navigable lesson page.
- Return `AttackResult` from the tracked assignment mapping.
- Keep support endpoints outside `AssignmentEndpoint` implementations when practical.
- Put feedback and hints in the lesson `WebGoatLabels.properties` file.
- Scope lesson state per user or HTTP session when state can change.
- Bound deliberate cracking, timing, polling, and resource-exhaustion exercises.
- Add unit or component coverage and an integration or Playwright test.

## Strict architectural constraints

- WebGoat is intentionally vulnerable training software.
- NEVER fix, sanitize, remove, or refactor a lesson vulnerability by default.
- This prohibition includes SQL injection, XSS, XXE, SSRF, CSRF, deserialization,
  path traversal, command injection, weak cryptography, and access-control flaws.
- Change vulnerable behavior only when the user explicitly requests that lesson change.
- Preserve the exploit path, learning objective, hints, and completion signal together.
- Do not apply lesson vulnerabilities to container, authentication, or admin code.
- Isolate new intentional vulnerabilities inside the relevant lesson package.
- Do not expose the host filesystem, real credentials, or unrestricted execution.
- Do not replace bounded simulations with unbounded attacks or expensive loops.
- Do not globally suppress security tooling to hide a lesson vulnerability.
- Do not upgrade dependencies marked `do not update necessary for lesson`.
- Preserve unrelated user changes; keep pull requests limited to one issue.
- New or changed lessons require a corresponding integration or UI test.
- Tests may assert an exploit succeeds when that is the lesson's purpose.
- Support Windows, Linux, and macOS for application and test code.
- Use `Path`, `Files`, and platform-neutral path resolution for filesystem access.
- Never assume `/`, drive letters, case sensitivity, or Unix line endings.
- Use JUnit `@TempDir`; never depend on absolute or shared writable paths.

## Testing model

- Surefire runs unit and Spring component tests from `src/test/java`.
- Failsafe runs REST and Playwright tests from `src/it/java`.
- The `start-server` profile starts WebGoat and WebWolf during integration tests.
- GitHub Actions runs `mvn verify` on Windows, Ubuntu, and macOS Intel.
- Filesystem changes need tests covering paths, cleanup, and line endings.
- Linux/macOS use `./mvnw`; Windows PowerShell uses `.\mvnw.cmd`.

## Verification commands

```bash
# Compile main sources
./mvnw -DskipTests compile

# Run unit and Spring component tests
./mvnw test

# Run one unit test class
./mvnw -Dtest=ClassName test

# Format and verify formatting
./mvnw spotless:apply
./mvnw spotless:check

# Build and run unit, REST integration, and Playwright tests
./mvnw verify

# Full clean local build
./mvnw clean install

# Start WebGoat and WebWolf locally
./mvnw spring-boot:run

# Remove stale WebGoat runtime/test data when required
./mvnw clean -Pcleanall
```
