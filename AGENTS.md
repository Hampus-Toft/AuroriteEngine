# Instructions for AI Coding Agents

This file outlines the architecture, constraints, and programming patterns required when writing or refactoring code for this repository.

## 🤖 Context & Stack
* **Language:** Java 21 (Utilize modern patterns: Switch Expressions, Records, and Virtual Threads where appropriate).
* **Build System:** Gradle with Kotlin DSL (`.kts`).
* **Graphics/Windowing:** LWJGL 3 (OpenGL).

## 📐 Architecture Constraints
* **Monorepo Separation:** * Code in `client/` must not leak into `server/`.
  * Heavy networking wrappers and shared event models belong strictly in `shared/`.
* **Event-Driven Architecture:** Do not write polling systems for game states. Rely entirely on the custom `EventBus` implemented in the `shared` module.
* **Memory Management:** LWJGL 3 utilizes off-heap native memory. Avoid creating memory leaks; always prefer utilizing try-with-resources blocks with `MemoryStack.stackPush()` for native allocations.

## 📝 Code Style Preferred
* Prefer structured concurrency using Java 21 virtual threads for P2P networking handlers.
* Annotate mod event handlers strictly using `@ModEventHandler`.