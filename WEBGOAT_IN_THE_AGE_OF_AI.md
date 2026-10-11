# WebGoat in the age of AI

## Background

AI-assisted development is changing how people write software and find security vulnerabilities. Given an endpoint, source code, and an objective such as "perform an SQL injection" or "exploit this XXE vulnerability," an AI assistant can often produce a working payload before the learner understands the flaw.

This raises a question for WebGoat: if AI can perform much of the technical work, what should WebGoat teach?

WebGoat still has an important role, but its exercises should account for the tools that developers and security practitioners now use. WebGoat should teach how vulnerable systems behave, how weaknesses combine, and how to verify a repair. Remembering the syntax of a particular exploit remains useful, but it should not be the final learning objective.

## The original WebGoat philosophy still applies

This problem is not new. WebGoat has sometimes randomized exercise values so automated security tools could not solve a lesson by replaying a fixed answer. The underlying principle was simple:

> It is your responsibility to learn. It is not WebGoat's responsibility to prevent you from automating the exercise.

A learner can point a scanner at an SQL injection lesson and complete it without learning much. AI creates a similar problem, though it can also explain code, investigate several components, and propose a repair.

Trying to stop AI from solving WebGoat exercises would lead to an arms race that does little to teach application security. WebGoat should not become an anti-AI CTF. Learners should be free to use the tools they would use in practice, including:

- Browser developer tools
- Intercepting proxies such as Burp Suite
- Scripts and security scanners
- IDEs
- AI assistants and coding agents

Completing an exercise and understanding it are different outcomes. WebGoat should design for understanding.

## Keep the focused lessons

The existing lessons remain important. They give learners the vocabulary and technical foundation needed to understand application security. These lessons cover topics such as SQL injection, cross-site scripting, XXE, SSRF, JWT, access control, insecure deserialization, password hashing, timing side channels, and HTTP parameter pollution.

A learner asking "How does XXE work?" should be able to open the XXE lesson and study that vulnerability in a controlled setting.

The focused lessons are the textbook. They explain one security concept at a time.

## Add advanced scenarios

An advanced section could build on the focused lessons. Each scenario would give the learner an objective without naming the vulnerability needed to reach it.

Instead of this instruction:

> Perform an XXE attack against this endpoint.

The learner might receive this objective:

> An administrative endpoint cannot be accessed externally. Retrieve the information it exposes.

The learner would investigate the application and discover the attack path:

```text
XML input
    |
    v
External entity processing
    |
    v
Server-side request
    |
    v
Internal service
    |
    v
Trusted internal request
    |
    v
Sensitive response
```

An AI assistant may generate the XML payload in seconds. The learner still needs to understand:

- Which components exist and how they communicate
- Where the trust boundaries are
- Which assumptions make the system vulnerable
- How separate weaknesses form a complete attack
- Why the attack succeeds

## Do not reveal the vulnerability classes

An advanced exercise named "XXE and SSRF challenge" gives away much of the investigation. Scenario-oriented names preserve the discovery work. Possible names include:

- Internal services
- Trust boundaries
- Account recovery
- Administrative access
- Unexpected parameters

The learner should identify the relevant vulnerability classes while investigating the scenario.

## Link back to focused lessons

Advanced scenarios do not need to explain every vulnerability again. They can offer optional links to the focused lessons:

> Not familiar with XML external entities? Complete the XXE lesson first.
>
> This scenario involves server-side requests. The SSRF lesson explains the related trust boundaries.

This creates a clear progression:

```text
Learn a security concept
          |
          v
Exploit it in isolation
          |
          v
Recognize it in a larger system
          |
          v
Combine it with other weaknesses
          |
          v
Explain and repair the complete attack
```

The existing lessons become building blocks for more complex scenarios.

## Candidate scenarios

### XXE, SSRF, and an internal service

Objective: retrieve information from an administrative endpoint that cannot be accessed externally.

```text
XML parser
    |
    v
External entity processing
    |
    v
Server-side request forgery
    |
    v
Internal service endpoint
    |
    v
Internal authentication or trusted-header behavior
    |
    v
Sensitive information
```

This is a good first scenario because it combines several security concepts in an architecture that remains small enough to understand.

### Open redirect and OAuth

Objective: obtain access to a protected resource belonging to another user.

```text
Open redirect
    |
    v
Weak redirect URI validation
    |
    v
OAuth authorization flow
    |
    v
Authorization code or token exposure
    |
    v
Protected resource
```

An open redirect can look harmless in isolation. Placing it in an authorization flow shows how another system can turn it into an account security problem.

### HTTP parameter pollution and authorization

Objective: perform an operation on an account that the current user does not control.

```text
Duplicate parameters
       |
       v
Different parser interpretations
       |
       +---- Authorization component reads Alice
       |
       +---- Business logic reads Bob
                         |
                         v
               Unauthorized operation
```

This scenario teaches the effect of parser disagreement across a trust boundary, not merely the existence of duplicate HTTP parameters.

### Stored XSS and a privileged action

Objective: change a protected application setting without the required privileges.

```text
Attacker-controlled content
            |
            v
         Stored XSS
            |
            v
Privileged user views the content
            |
            v
Authenticated action runs in that user's session
            |
            v
Protected setting changes
```

This shows why stored XSS matters beyond producing `alert(1)`.

### IDOR, information disclosure, and credential reuse

Objective: access protected administration functionality.

```text
Insecure direct object reference
                |
                v
Information disclosure
                |
                v
Credential or token exposure
                |
                v
Credential reuse
                |
                v
Administrative functionality
```

This scenario shows why the severity of a weakness depends on the surrounding system and the other information an attacker can reach.

### Path traversal and configuration disclosure

Objective: retrieve a protected customer record.

```text
Path traversal
      |
      v
Application configuration
      |
      v
Credentials or connection information
      |
      v
Protected component
      |
      v
Customer data
```

Path traversal becomes one step in compromising a larger system instead of the final objective.

### JWT and information disclosure

Objective: access a resource unavailable to the current user.

The learner receives a valid JWT but is not told that the token is relevant. Elsewhere in the application, the learner can discover configuration or cryptographic information. The existing JWT lessons provide the knowledge needed to recognize why that information matters.

### Race condition and a business rule

Objective: obtain something the account should receive only once.

The application may enforce its checks when it handles requests one at a time. Concurrent requests break an application rule. AI can generate the concurrency script, but the learner must identify the rule and the timing window that breaks it.

## Continue beyond exploitation

Exploitation does not need to end an advanced lesson. A complete exercise can follow this sequence:

```text
Discover
   |
   v
Exploit
   |
   v
Explain
   |
   v
Repair
   |
   v
Verify
```

After the learner completes an attack, WebGoat could present a proposed developer fix. The learner would determine whether it removes the underlying weakness.

### Example: an incomplete SSRF fix

Suppose the learner uses XXE to make the server request:

```text
http://localhost:8080/internal
```

The development team blocks the string `localhost` and claims the problem is fixed. WebGoat then asks the learner to verify the repair.

The learner discovers that the patch blocks one representation of the destination but leaves the trust problem in place. A proper repair might disable external entity processing, restrict outbound requests, or break the attack chain at another appropriate trust boundary.

The lesson teaches the difference between blocking one demonstrated payload and removing the vulnerability.

## Review proposed security fixes

Developers will review and maintain code they did not write themselves. Some of that code will come from AI coding assistants. An advanced WebGoat exercise could present several plausible patches and ask the learner to determine whether each patch fixes the problem.

A patch might:

- Block only the demonstrated payload
- Deny one dangerous value while allowing equivalent values
- Validate input in the wrong component
- Introduce another vulnerability
- Remove the root cause

The learner's task is to understand the system well enough to judge the proposed code. This skill remains useful whether a person or an AI assistant wrote the patch.

## Require evidence of understanding

If AI can supply an exploit, an advanced lesson should require more than the final payload. The learner could be asked to:

- Identify the failed trust boundary
- Reconstruct the attack chain
- Explain why each step enables the next
- Choose between several proposed repairs
- Predict whether a modified exploit will still work
- Verify the repair with a regression test

These tasks make the learning objective explicit and give WebGoat a way to check understanding.

## A small advanced application

The advanced section could eventually use a small fictional application rather than a set of unrelated challenges. The learner would become familiar with its architecture over several exercises.

```text
Browser
   |
   v
Frontend and API
   |
   +-------- Authentication service
   |
   +-------- Document service
   |
   +-------- Internal admin service
   |
   +-------- External integrations
```

Early exercises could reveal information about the architecture that becomes useful later. This would let vulnerabilities combine through normal component interactions. The learner would reason about a system instead of treating every vulnerability as an isolated puzzle.

A shared application also costs more to build and maintain. It needs reliable state reset, stable dependencies, progressive hints, and tests for every supported attack path. It makes sense as a later step after WebGoat validates the lesson format with one smaller scenario.

## The role of AI

WebGoat should assume that learners have access to AI. The design question for each advanced lesson should be:

> If an AI assistant generates the exploit in 20 seconds, what does the learner still need to understand?

If the answer is "nothing," the exercise may still demonstrate a basic vulnerability, but it does not belong in the advanced section.

An advanced lesson remains useful when the learner must reason about application architecture, protocol behavior, parser differences, authorization decisions, attack composition, root causes, and verification.

## Proposed learning model

```text
+---------------------------------+
|          Focused lessons        |
|                                 |
|  Learn individual security      |
|  concepts and vulnerabilities   |
+----------------+----------------+
                 |
                 v
+---------------------------------+
|        Advanced scenarios       |
|                                 |
|  Discover which concepts apply  |
|  and combine them               |
+----------------+----------------+
                 |
                 v
+---------------------------------+
|       Remediation and review    |
|                                 |
|  Fix the root cause and verify  |
|  that the attack chain is gone  |
+---------------------------------+
```

Focused lessons are the textbook. Advanced scenarios are the lab.

## A practical first version

WebGoat can test this approach with one advanced scenario before building a larger application:

1. Build one attack chain from concepts already taught in focused lessons.
2. Give the learner an outcome-based objective without vulnerability names.
3. Add optional hints that point back to the relevant focused lessons.
4. Require the learner to explain the attack chain after exploitation.
5. Present several repairs and ask the learner to test them.
6. Check whether the learner can identify the root cause and verify the final repair.

The XXE and SSRF scenario is a good candidate because it has a clear objective, visible trust boundaries, and several places where an incomplete fix can fail.

## Design principle

WebGoat should teach security rather than test whether somebody can avoid automation. Learners should be free to use the tools they would use in practice, including AI. WebGoat's responsibility is to provide exercises that still require something worth learning.

The learning path is:

```text
Learn the concepts
       |
       v
Exploit them in isolation
       |
       v
Combine them in a system
       |
       v
Explain why the attack works
       |
       v
Fix the root cause and verify the repair
```

WebGoat should not focus on teaching people to type exploits. It should teach them how vulnerable software behaves and how to determine whether it has been fixed.
