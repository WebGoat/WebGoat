
My primary objective is to systematically eliminate all security vulnerabilities identified by CodeQL while ensuring the project remains in a **stable, compilable, and testable state**. I will treat the entire repository, including all lessons and tests, as in-scope for fixes.

<key_knowledge>
- The user wants to avoid introducing new vulnerabilities, especially hardcoded keys.
- Tests should be regenerated or fixed correctly, not just modified to pass without understanding the underlying code.
- Dependencies should be updated based on Dependabot alerts.
- The project is a Maven project.
- The user prefers using  tools for GitHub operations.
</key_knowledge>

## 1. Environment and Build Hygiene

**This is the most critical section for ensuring successful builds.** I will adhere to these steps for all build and compilation tasks.

* **Java Configuration:**
  * The project requires **Java 23**.
  * **CRITICAL:** The  in  does not reliably use the  or  environment variables for forked processes. I must ensure the  explicitly uses  to prevent .
  * Before running , I will verify the active Java version with .
* **Pre-commit and Python Environment:**
  * Code quality and formatting are managed by .
  * **I must always activate the Python virtual environment before running it.**
  * **Command:** 
* **Maven Build Process:**
  * **Always use :** Start builds with  to avoid stale artifacts.
  * **Spotless Formatting:** If a build fails due to formatting errors from the  plugin, the fix is to run .
* **Process Management:**
  * When starting the application server, I will do so as a background process using .
  * I will capture the **PID** from the  output.
  * To stop the application, I will use the command .

## 2. Core Remediation Workflow

1. **Analyze Reports:** I will begin by parsing all provided SARIF reports () to identify all unique vulnerability types ().
2. **Isolate and Conquer:** I will tackle one vulnerability class at a time, querying the SARIF file for all instances of a specific rule.
3. **Context-Aware Fixing:** For each vulnerability, I will:
   a.  **Read First:** Always use  to understand the current state of the source code before attempting any modification.
   b.  **Check Dependencies:** Before applying a fix that uses new APIs, I will check  or  to ensure the change is compatible with the project's library versions.
   c.  **Apply Precise Fix:** Use  or  to apply a targeted, best-practice fix.
4. **Verify and Iterate:** After every modification, I will run  (following the hygiene rules above) using JAVA_HOME.
   * If the build fails, I will enter a **debugging phase**: analyze the error, read the affected files, and apply corrections until the build passes.
   * If a security fix is incompatible with existing libraries, I will report this and, with your approval, revert the change to maintain a stable build.
5. **Re-evaluate:** After fixing a class of vulnerabilities, I will ask you to provide updated SARIF reports to re-evaluate the security posture.

## 3. Tool & Command Strategy

* **GitHub Operations:** I will use  tools for all GitHub interactions, such as downloading SARIF artifacts from workflow runs.
* **Build & Compilation:** I will use  to execute  and , strictly following the hygiene rules.
* **File Operations:** I will rely on , , and  for all direct code modifications.

## 4. Guiding Principles

* **Stability is as Important as Security:** I will not consider my job done until the project is both secure and the build is passing.
* **Don't Patch, Upgrade:** For vulnerabilities in third-party libraries (e.g., ), my default strategy is to upgrade the dependency in the  or , not to patch the library code directly.
* **Embrace the Debugging Loop:** I understand that my fixes can introduce errors. I will systematically debug these issues by analyzing logs and iterating on solutions.
* **User Guidance is Key:** I will rely on your expertise for strategic direction, such as providing updated scan results, clarifying scope, and assisting with complex debugging scenarios.

## 5. Vulnerability-Specific Strategies

| Rule ID                            | Description                         | Preferred Fix                                                                                                                                      |
|:-----------------------------------|:------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------|
|       | Unsafe deserialization of user data | Use a whitelisting .                                                                                                            |
|                | User data in SQL queries            | Use  with parameterized queries.                                                                                                |
|               | Uncontrolled data in file paths     | Validate paths against a base directory.                                                                                                           |
|                            | Cross-site scripting                | Use context-aware output encoding.                                                                                                                 |
|           | Vulnerable third-party library      | Upgrade the dependency version in .                                                                                                       |
|        | Overly permissive GitHub Actions    | Add explicit, least-privilege  to workflows.                                                                                          |
|  | Missing JWT signature check         | Add  to JWT parsing calls, but verify compatibility with the  library version in . If incompatible, report it. |
|             | Regular Expression DoS              | Rewrite inefficient regex. If not possible, use Google's RE2J library.   EOF
