package io.github.hampustoft.aurorite.client;

import io.github.hampustoft.aurorite.core.di.AuroriteEngineContext;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BackgroundInitializationTask implements Runnable {
    private static final AtomicBoolean READY = new AtomicBoolean(false);

    public static boolean isReady() {
        return READY.get();
    }

    @Override
    public void run() {
        try {
            // High-throughput asset stream allocations, directory scanning, and reflection analysis goes here
            Thread.sleep(2500);

            // Freeze basic startup modules and flip registration limits
            AuroriteEngineContext.getInstance().setLobbyInitState();

            // Allow graphics context iteration execution pathways
            READY.set(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
