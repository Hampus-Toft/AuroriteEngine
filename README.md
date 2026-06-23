# Tabletop Nexus (Replace with your actual Engine Name)

An open-source, event-driven 2D/3D tabletop game engine built on top of **LWJGL 3**. This engine provides a robust base framework for building, hosting, and playing modded tabletop games via Peer-to-Peer (P2P) or dedicated servers.

The core philosophy of this engine is a "blank slate" client. The base engine handles the main menu, core loop, asset management, and networking, while the entire game logic, rules, and visual assets are streamed dynamically from the host—similar to a Steam Workshop experience.

---

## 🚧 Development Status (Work in Progress)

This project is actively under development. Below is the current roadmap and feature status:

* **[🟢 Done]** Core Multi-Project Gradle Monorepo Setup
* **[🟡 In Progress]** Shared Framework Core Event Loop & P2P Networking Base
* **[🔴 Planned]** Dynamic Mod Asset/Script Syncing Pipeline
* **[🔴 Planned]** LWJGL 3 Client Renderer & UI Layer
* **[🔴 Planned]** Example Mods (`core-cards` and `core-dice`)

---

## 🚀 Key Features

* **Event-Driven Core Loop:** Fully extensible event system allowing modders to subscribe to turns, dice rolls, card flips, and game state changes.
* **Seamless Dynamic Mod Streaming:** Connect to any host and download necessary assets directly into the game session without needing a full client restart.
* **P2P & Dedicated Networking:** Integrated robust networking framework tailored for low-latency state synchronization.
* **Asset & Resource Management:** Centralized asset pipeline for textures, models, shaders, and audio with automatic caching.
* **Modding API:** Comprehensive API designed for developers to build standalone tabletop modules (e.g., card games, board games, wargames).

---

## 📂 Repository Structure

This project is organized as a Gradle monorepo:

```text
root/
├── core/               # Engine Core (Client & Server common engine logic) [LGPL-3.0]
├── client/             # LWJGL 3 Client application [LGPL-3.0]
├── server/             # Dedicated Server application [LGPL-3.0]
│
├── api/                # Public Modding API (Events, hooks, data types) [MIT]
├── mods/               # Included default/example game modules [MIT]
│   ├── example-cards/     
│   └── example-dice/      
│   
├── LICENSE             # Core Engine License (LGPL-3.0)
├── LICENSE-API         # Modding API & Mods License (MIT)
├── README.md           
├── AGENTS.md           
├── CONTRIBUTING.md           
├── build.gradle.kts    
└── settings.gradle.kts
```

## 📄 License

This project features a split-licensing model to protect the core framework while giving modders total freedom:

* **Core Engine (`client/`, `server/`, `shared/`):** Licensed under the **GNU Lesser General Public License v3.0 (LGPL-3.0)**. See `LICENSE` for details.
* **Modding API & Example Mods (`mods/`, `api/`):** Licensed under the **MIT License**. See `LICENSE-API` for details. You are free to use the API to build commercial, closed-source, or open-source games.