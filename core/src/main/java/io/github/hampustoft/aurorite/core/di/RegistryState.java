package io.github.hampustoft.aurorite.core.di;

public enum RegistryState {
    /** Full modification allowed (Core initialization / Startup Mods). */
    ENGINE_LAUNCH,

    /** Only content/lobby-level services can be modified (Lobby creation). */
    LOBBY_INIT,

    /** Registries are locked. Only additive new services allowed. Transient state. */
    GAME_ACTIVE
}
