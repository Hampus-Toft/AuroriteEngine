package io.github.hampustoft.aurorite.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import io.github.hampustoft.aurorite.api.event.EventBus;
import io.github.hampustoft.aurorite.core.di.AuroriteEngineContext;
import io.github.hampustoft.aurorite.core.event.ConcurrentEventBus;
import org.lwjgl.opengl.GL;

public final class Application {
    private long window;

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

    private void loop() {
        GL.createCapabilities();
        var context = AuroriteEngineContext.getInstance();

        while (!glfwWindowShouldClose(this.window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (!BackgroundInitializationTask.isReady()) {
                glClearColor(0.08f, 0.09f, 0.12f, 1.0f);
            } else {
                glClearColor(0.15f, 0.17f, 0.23f, 1.0f);
                // The graphics pipeline can safely fetch the active UI screen components here
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
