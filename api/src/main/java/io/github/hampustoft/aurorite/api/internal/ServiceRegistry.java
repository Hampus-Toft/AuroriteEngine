package io.github.hampustoft.aurorite.api.internal;

import io.github.hampustoft.aurorite.api.di.EngineContext;

/**
 * Advanced interface for platform extensions to override or register core subsystems.
 */
public interface ServiceRegistry extends EngineContext {
    /**
     * Registers or replaces an implementation of a given service class.
     */
    <T> void registerService(Class<T> serviceClass, T implementation);
}
