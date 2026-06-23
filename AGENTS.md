# 🤖 AI Agent Guidelines & Architecture (`AGENTS.md`)

Welcome! This repository supports and welcomes development aided by autonomous AI agents and coding assistants (e.g., GitHub Copilot, Cursor, Windsurf, or specialized custom agent workflows). 

To maintain the architectural integrity of the **Aurorite Engine**, ensure compliance with our split-licensing model, and keep the codebase clean, all AI agents and users driving them **must** adhere to the instructions outlined below.

---

## General Core Directives

1. **Disclosure Requirement:** Every AI agent must disclose its involvement. When creating pull requests or committing code, the description or commit message should clearly state that AI assistance was utilized (e.g., `[AI Assisted] Added event listener pipeline`).
2. **Style Guide Compliance:** All generated code must adhere strictly to the repository's formatting specification. Agents must run automated formatting tools (`spotlessApply`) before staging or delivering changes.
3. **Licensing Context Awareness:** Agents must be context-aware of *where* they are writing code to avoid legal contamination across our split-licensing boundaries.

---

## License & Contribution Compliance

Aurorite Engine uses a **split-licensing model**. Agents must be explicitly instructed on these boundaries before generating code:

| Directory | Scope | License | Agent Instruction |
| :--- | :--- | :--- | :--- |
| `/core`, `/client`, `/server` | Engine Core | **LGPL-3.0** | Code written here becomes part of the core engine. Ensure no strictly proprietary or incompatible copyleft snippets are introduced. |
| `/api`, `/mods` | Modding API & Examples | **MIT** | Code here must remain completely permissive. Permissive MIT code can interact with LGPL core interfaces via the public API. |

> **User Responsibility Disclaimer:** Users utilizing AI agents to generate contributions bear full legal and structural responsibility for the code committed. Ensure the agent does not ingest or hallucinate copyrighted or improperly licensed code snippets.

---

## Performance & Code Quality Guardrails

Because Aurorite is a real-time, event-driven game engine, code must remain highly performant, predictable, and maintainable. AI agents must respect the following strict software metrics:

### 1. Cyclomatic Complexity & Clean Code Rules
* **Method Length:** Keep methods small and modular. Methods must not exceed **60 lines of code** (excluding comments).
* **Nesting Limit:** Avoid nesting logic deeper than **3 levels** for `if` statements (`NestedIfDepth`) and **2 levels** for loops (`NestedForDepth`). Use guard clauses and early returns to flatten out logic.
* **Brace Requirements:** Standardized structural blocks are mandatory. All control structures (`if`, `else`, `for`, `while`) **must** use explicit curly braces `{}`—even for single-line statements.
* **Documentation:** If an algorithm naturally demands complex mathematical logic (e.g., custom rendering algorithms or network serialization logic), the agent *must* add detailed, step-by-step documentation comments explaining its behavior.

### 2. Thread Safety & The Event Loop
* **Non-Blocking Architecture:** Do not introduce heavy blocking operations (`Thread.sleep()`, synchronous network/disk I/O) inside the core engine loop or event dispatcher.
* **Concurrency:** When modifying core state across threads (network threads vs. render threads), agents must utilize appropriate synchronization primitives or thread-safe data structures.

### 3. Mandatory Verification & Automated Formatting
Before an agent considers a task complete, the following workflow must be executed locally:

1. **Auto-Format Code:** Run `./gradlew spotlessApply` to automatically format all Java/Kotlin/Markdown files according to project styles (enforces standard spacing, brackets, and unused import cleanup).
2. **Quality Check:** Run `./gradlew check` to trigger local unit tests and **Checkstyle** metric evaluations. If the code breaks complexity constraints or missing overrides, the build will fail and must be refactored by the agent.
3. **Regression & Coverage Testing:** If an agent introduces a new event listener, API endpoint, or utility logic, it **must** write corresponding unit tests to verify its execution.

> **Performance Note:** This repository has the **Gradle Configuration Cache** enabled. Ensure any custom build scripts or Gradle tasks you generate are configuration-cache compatible (i.e., they do not access the mutable `Project` instance during task execution).

---

## Agent Roles & System Prompts

When configuring specialized AI agents or setting up custom workspace rules (like `.cursorrules`), utilize the following scoping boundaries:

### 1. Core Engine Agent (`/core`, `/client`, `/server`)
* **Focus:** LWJGL 3 rendering, low-latency P2P/Dedicated server networking, event loops, asset pipeline caching, and memory management.
* **Constraint:** Must use **LGPL-3.0** compliance rules. Focus heavily on performant, non-blocking Java/Kotlin code suitable for game engine architectures. Do not leak API/Mod implementation details into the Core.

### 2. API & Modding Agent (`/api`, `/mods`)
* **Focus:** Designing clean, intuitive lifecycle hooks, event subscriptions (e.g., card flips, dice rolls), and developer-facing APIs.
* **Constraint:** Must use **MIT** compliance rules. This agent must strictly use the public interfaces exposed by the `/api` layer and should never modify code inside `/core` or `/client`.

### 3. DevOps & Build Agent (Root, `.github/`, Gradle configurations)
* **Focus:** Managing the Gradle multi-project monorepo structure, `build.gradle.kts` dependencies, Spotless setups, Checkstyle XML rules, and CI/CD pipelines.