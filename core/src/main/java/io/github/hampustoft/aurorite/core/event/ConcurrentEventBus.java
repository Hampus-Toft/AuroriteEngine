package io.github.hampustoft.aurorite.core.event;

import io.github.hampustoft.aurorite.api.event.EventBus;
import io.github.hampustoft.aurorite.api.event.EventPhase;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class ConcurrentEventBus implements EventBus {
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<EventPhase, CopyOnWriteArrayList<Consumer<Object>>>>
            handlers = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> eventType, EventPhase phase, Consumer<T> handler) {
        var phaseMap = this.handlers.computeIfAbsent(eventType, k -> new ConcurrentHashMap<>());
        var list = phaseMap.computeIfAbsent(phase, k -> new CopyOnWriteArrayList<>());
        list.add((Consumer<Object>) handler);
    }

    @Override
    public void post(Object event) {
        var phaseMap = this.handlers.get(event.getClass());
        if (phaseMap == null) {
            return;
        }

        for (var phase : EventPhase.values()) {
            var listeners = phaseMap.get(phase);
            if (listeners == null) {
                continue;
            }
            for (var listener : listeners) {
                listener.accept(event);
            }
        }
    }
}
