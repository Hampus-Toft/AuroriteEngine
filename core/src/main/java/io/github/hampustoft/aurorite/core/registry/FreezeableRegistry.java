package io.github.hampustoft.aurorite.core.registry;

import io.github.hampustoft.aurorite.api.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FreezeableRegistry<T> implements Registry<T> {
    private final String name;
    private final ConcurrentHashMap<String, T> entries = new ConcurrentHashMap<>();
    private final AtomicBoolean frozen = new AtomicBoolean(false);

    public FreezeableRegistry(String name) {
        this.name = name;
    }

    @Override
    public String getRegistryName() {
        return this.name;
    }

    @Override
    public void register(String id, T entry) {
        if (this.frozen.get()) {
            throw new IllegalStateException("Registry " + this.name + " is frozen.");
        }
        this.entries.put(id, entry);
    }

    @Override
    public T get(String id) {
        return this.entries.get(id);
    }

    public void freeze() {
        this.frozen.set(true);
    }

    public void clear() {
        this.frozen.set(false);
        this.entries.clear();
    }
}
