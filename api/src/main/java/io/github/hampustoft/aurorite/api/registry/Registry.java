package io.github.hampustoft.aurorite.api.registry;

public interface Registry<T> {
    String getRegistryName();

    void register(String id, T entry);

    T get(String id);
}
