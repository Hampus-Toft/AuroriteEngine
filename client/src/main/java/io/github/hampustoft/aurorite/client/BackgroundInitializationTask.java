package io.github.hampustoft.aurorite.client;

import io.github.hampustoft.aurorite.api.loading.InitializationProgress;
import io.github.hampustoft.aurorite.core.di.AuroriteEngineContext;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class BackgroundInitializationTask implements Runnable {
    private static final AtomicBoolean READY = new AtomicBoolean(false);
    private static final AtomicReference<InitializationProgress> PROGRESS =
            new AtomicReference<>(InitializationProgress.initial());

    public static boolean isReady() {
        return READY.get();
    }

    public static InitializationProgress getProgress() {
        return PROGRESS.get();
    }

    @Override
    public void run() {
        final int totalSteps = 3;

        this.executeStep(1, totalSteps, "Scanning local mod directories...");
        this.executeStep(2, totalSteps, "Verifying API layer dependency trees...");
        this.executeStep(3, totalSteps, "Freezing core engine subsystem configurations...");

        AuroriteEngineContext.getInstance().setLobbyInitState();
        READY.set(true);
    }

    private void executeStep(int step, int total, String description) {
        float percent = (float) step / total;
        PROGRESS.set(new InitializationProgress(percent, step, total, description));
        try {
            // Simulating physical I/O processing overhead
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
