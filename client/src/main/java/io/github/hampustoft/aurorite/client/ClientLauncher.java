package io.github.hampustoft.aurorite.client;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
// src/main/java/io/github/hampustoft/aurorite
public class ClientLauncher {
    private long window;

    public static void main(String[] args) {
        // 1. Intercept execution on macOS to check if we need a reboot
        if (needsMacReboot()) {
            try {
                rebootJVMWithFirstThreadFlag(args);
            } catch (Exception e) {
                System.err.println("Failed to automatically spawn macOS First Thread JVM subprocess.");
                e.printStackTrace();
            }
            return; // Terminate this initial wrapper thread safely
        }

        // 2. If we are on Windows/Linux, or successfully on macOS Thread 0, boot normally
        new ClientLauncher().run();
    }

    private static boolean needsMacReboot() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("mac") && !osName.contains("darwin")) {
            return false; // Windows and Linux are clear
        }

        // Cocoa updates an environment variable tracking whether the JVM started on Thread 0
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);
        
        // If the environment variable isn't "1", we are running on a secondary thread!
        return !"1".equals(env);
    }

    private static void rebootJVMWithFirstThreadFlag(String[] args) throws Exception {
        System.out.println("macOS environment detected without Thread 0 context. Relaunching engine pipeline...");

        String jvmBinary = System.getProperty("java.home") + "/bin/java";
        String classpath = System.getProperty("java.class.path");

        List<String> command = new ArrayList<>();
        command.add(jvmBinary);
        command.add("-XstartOnFirstThread");
        command.add("-cp");
        command.add(classpath);
        command.add(ClientLauncher.class.getName()); // Calls this exact main method again
        command.addAll(List.of(args));

        // Start the sub-process
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();

        // Pipe the output streams so logging logs straight to your IDE/console seamlessly
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        process.waitFor();
    }

    public void run() {
        System.out.println("Launching Tabletop Nexus Engine using LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        try {
            glfwSetErrorCallback(null).free();
        } catch (NullPointerException e) {}
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "Tabletop Engine Test Window", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode != null) {
                glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
            }
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.15f, 0.17f, 0.23f, 1.0f);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}