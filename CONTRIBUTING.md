# Contributing to Tabletop Nexus

Thank you for your interest in contributing! We want to keep this engine fast, clean, and community-focused.

## 🤝 How to Contribute

### 1. Reporting Bugs & Feature Requests
* Check the GitHub Issues tab to ensure it hasn't been reported yet.
* Open a new issue with clear steps to reproduce the bug or a detailed layout of the feature request.

### 2. Code Contributions
1. Fork the repository and create your branch from `main`.
2. Ensure your code compiles cleanly using Java 21 and `./gradlew build`.
3. Adhere to standard Java code styling rules (consistent indentation, clear variable names).
4. Submit a Pull Request (PR) describing what you changed and why.

## 🏗️ Development Guidelines
* **Thread Safety:** The core engine relies on an event-driven loop. Ensure any modifications to the networking or state processing handlers do not block the main application loop.
* **Asset Clean-up:** If you are modifying the client rendering pipeline, ensure all native LWJGL bindings/buffers are explicitly freed to avoid memory leaks.