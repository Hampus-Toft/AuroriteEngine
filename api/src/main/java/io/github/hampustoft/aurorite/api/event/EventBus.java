package io.github.hampustoft.aurorite.api.event;

import java.util.function.Consumer;

public interface EventBus {
    <T> void register(Class<T> eventType, EventPhase phase, Consumer<T> handler);

    void post(Object event);
}
