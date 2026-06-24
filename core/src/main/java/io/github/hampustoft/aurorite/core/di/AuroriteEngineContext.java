package io.github.hampustoft.aurorite.core.di;

import io.github.hampustoft.aurorite.api.internal.ServiceRegistry;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class AuroriteEngineContext implements ServiceRegistry {
    private static AuroriteEngineContext instance;

    private final ConcurrentHashMap<Class<?>, Object> services = new ConcurrentHashMap<>();
    private final Set<Class<?>> transientServices = new HashSet<>();
    private RegistryState currentState = RegistryState.ENGINE_LAUNCH;

    // Services only moddable at engine cold-start (e.g., Network, AssetPipeline)
    private final Set<Class<?>> launchOnlyServices = Set.of(
            // TODO: Fix whitelist
            // NetworkHandler.class, ShaderRenderer.class
            );

    private AuroriteEngineContext() {}

    public static synchronized AuroriteEngineContext getInstance() {
        if (instance == null) {
            instance = new AuroriteEngineContext();
        }
        return instance;
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, T implementation) {
        if (this.currentState == RegistryState.GAME_ACTIVE) {
            // Active gameplay: Only completely new services can be added; no overrides
            if (this.services.containsKey(serviceClass)) {
                throw new IllegalStateException(
                        "Cannot override existing service during active game: " + serviceClass.getName());
            }
            this.transientServices.add(serviceClass);
        } else if (this.currentState == RegistryState.LOBBY_INIT && this.launchOnlyServices.contains(serviceClass)) {
            // Lobby phase: Fundamental launch-only services are now locked out from overrides
            throw new IllegalStateException(
                    "Service " + serviceClass.getName() + " can only be replaced during engine launch.");
        }

        this.services.put(serviceClass, implementation);
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        var service = this.services.get(serviceClass);
        if (service == null) {
            throw new IllegalArgumentException("No implementation found for: " + serviceClass.getName());
        }
        return serviceClass.cast(service);
    }

    /** Called when transitioning from Main Menu to a Lobby. */
    public void setLobbyInitState() {
        this.currentState = RegistryState.LOBBY_INIT;
    }

    /** Called when the host triggers the match start. */
    public void setGameActiveState() {
        this.currentState = RegistryState.GAME_ACTIVE;
    }

    /** * Called at the end of a lobby session. Clears out lobby-specific extensions
     * and resets the state to prepare for the next lobby.
     */
    public void teardownLobbySession() {
        for (var transientClass : this.transientServices) {
            this.services.remove(transientClass);
        }
        this.transientServices.clear();
        this.currentState = RegistryState.LOBBY_INIT;
    }
}
