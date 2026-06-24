package io.github.hampustoft.aurorite.api.di;

/**
 * The standard, read-only contract that all mods use to fetch engine systems.
 */
public interface EngineContext {
    <T> T getService(Class<T> serviceClass);
}
