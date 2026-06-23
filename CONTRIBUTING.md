# Contributing to Aurorite Engine

Thank you for your interest in contributing! We want to keep this engine fast, clean, and community-focused. By contributing to this project, you help build a robust, open-source framework for tabletop gaming.

---

## Core Prerequisites & Licensing

Before writing any code, please be aware that Aurorite Engine utilizes a **split-licensing model**:
* **Engine Core (`/core`, `/client`, `/server`):** Licensed under the **GNU Lesser General Public License v3.0 (LGPL-3.0)**.
* **Modding API & Example Mods (`/api`, `/mods`):** Licensed under the **MIT License**.

By submitting a Pull Request, you agree that your contributions will be licensed under the respective directory's license. If you use AI assistants (e.g., Cursor, Copilot) to generate code, you retain full responsibility for ensuring the code complies with these licenses.

---

## How to Contribute

### 1. Reporting Bugs & Feature Requests
* Check the GitHub Issues tab to ensure it hasn't been reported yet.
* Open a new issue with clear steps to reproduce the bug, expected vs. actual behavior, or a detailed layout of the feature request.

### 2. Code Contributions Workflow
1. Fork the repository and create your feature branch from `main`.
2. Implement your changes following our **Code Quality Guardrails** below.
3. **Format your code:** Run `./gradlew spotlessApply` to automatically align your code with our repository style guidelines.
4. **Verify your code:** Run `./gradlew check` to execute all unit tests and run **Checkstyle** metric validation. Your build must pass cleanly before submission.
5. Submit a Pull Request (PR) describing what you changed and why.

---

## AI Agent Guidelines

We explicitly welcome contributions aided by autonomous AI agents or LLM coding assistants. However, any AI-assisted contributions must adhere to the rules outlined in our [AGENTS.md](AGENTS.md) file, including:
* **Mandatory Disclosure:** Mention AI assistance in your PR description or commit messages (e.g., `[AI Assisted] Refactored event listener loop`).
* **Strict Complexity Compliance:** AI-generated code must be automatically formatted via Spotless and verified via Checkstyle to ensure it passes nesting, line length, and cyclomatic complexity gates.

---

## Development & Performance Guidelines

To maintain a high-performance, real-time tabletop engine, all manual and automated contributions must respect these engineering standards:

* **No Blocking Logic:** The core engine relies on a high-throughput, event-driven loop. Never introduce heavy blocking operations (`Thread.sleep()`, synchronous network/disk I/O) inside the core engine loop or event dispatcher.
* **Cyclomatic & Structural Complexity:** Keep methods modular and single-purpose (under 60 lines of code). Avoid deeply nested logic (max 3 deep for `if` statements, max 2 deep for loops). Use early returns and guard clauses.
* **Brace Requirements:** All control structures (`if`, `else`, `for`, `while`) **must** use explicit curly braces `{}`—even for single-line statements.
* **Native Memory Management:** If you are modifying the client rendering pipeline (`LWJGL 3`), ensure all native bindings, textures, and buffers are explicitly freed to avoid memory leaks.
* **Configuration Cache:** This repository utilizes the Gradle Configuration Cache for rapid builds. Ensure any build script alterations do not break configuration cache compatibility.