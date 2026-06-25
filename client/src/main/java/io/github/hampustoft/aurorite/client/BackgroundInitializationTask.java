package io.github.hampustoft.aurorite.client;

import io.github.hampustoft.aurorite.api.loading.InitializationProgress;
import io.github.hampustoft.aurorite.core.di.AuroriteEngineContext;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackgroundInitializationTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger("Back");
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
        final int totalSteps = 5;

        LOGGER.trace("Starting step 1");
        this.executeStep(1, totalSteps, "Scanning local mod directories...");
        LOGGER.trace("Starting step 2");
        this.executeStep(2, totalSteps, "Verifying API layer dependency trees...");
        LOGGER.trace("Starting step 3");
        this.executeStep(3, totalSteps, "Freezing core engine subsystem configurations...");
        LOGGER.trace("Starting step 4");
        this.executeStep(4, totalSteps, "Random Stuff...");
        LOGGER.trace("Starting step 5");
        this.executeStep(5, totalSteps, "Almost ready now...");
        LOGGER.trace("All steps are done");

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
