package io.github.hampustoft.aurorite.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import io.github.hampustoft.aurorite.api.event.EventBus;
import io.github.hampustoft.aurorite.api.loading.InitializationProgress;
import io.github.hampustoft.aurorite.core.di.AuroriteEngineContext;
import io.github.hampustoft.aurorite.core.event.ConcurrentEventBus;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {
    private long window;

    public static Logger LOGGER = LoggerFactory.getLogger("App");

    public static void launch() {
        new Application().run();
    }

    private void run() {
        this.bootstrapCoreServices();
        this.initGlfw();

        // Spawn async background loader using a Java 25 Virtual Thread
        Thread.ofVirtual().start(new BackgroundInitializationTask());

        this.loop();
        this.cleanup();
    }

    private void bootstrapCoreServices() {
        var context = AuroriteEngineContext.getInstance();
        context.registerService(EventBus.class, new ConcurrentEventBus());
    }

    private void initGlfw() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        this.window = glfwCreateWindow(800, 600, "Aurorite Engine", 0L, 0L);
        if (this.window == 0L) {
            throw new RuntimeException("Failed to create window");
        }
        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);
    }

    private float lastProgress = -1f;

    private void loop() {
        GL.createCapabilities();

        while (!glfwWindowShouldClose(this.window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (!BackgroundInitializationTask.isReady()) {

                glClearColor(0.08f, 0.09f, 0.12f, 1.0f);

                // Fetch the atomic snapshot cleanly with zero thread contention
                InitializationProgress report = BackgroundInitializationTask.getProgress();
                if (lastProgress != report.percentage()) {
                    LOGGER.trace(String.format(
                            "Progress %.0f%% '%s'", (report.percentage() * 100), report.currentTaskDescription()));
                    lastProgress = report.percentage();
                }

                // 1. Draw your loading slider/panorama background here
                // 2. Pass data to your immediate loading graphics renderer:
                //    renderProgressBar(report.percentage());
                //    renderLoadingText(String.format("[%d/%d] Working on: %s",
                //         report.tasksCompleted(), report.totalTasks(), report.currentTaskDescription()));
            } else {
                glClearColor(0.15f, 0.17f, 0.23f, 1.0f);
                // Background initialization finished! Run standard Main Menu logic
            }

            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwDestroyWindow(this.window);
        glfwTerminate();
    }
}
