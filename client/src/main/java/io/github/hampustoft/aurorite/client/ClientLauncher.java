package io.github.hampustoft.aurorite.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class ClientLauncher {
    public static void main(String[] args) {
        if (needsMacReboot()) {
            try {
                rebootJVMWithFirstThreadFlag(args);
            } catch (Exception e) {
                System.err.println("Failed to automatically spawn macOS First Thread JVM subprocess.");
            }
            return;
        }

        // Hand off control to the actual desktop app orchestrator
        Application.launch();
    }

    private static boolean needsMacReboot() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("mac") && !osName.contains("darwin")) {
            return false;
        }
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);
        return !"1".equals(env);
    }

    private static void rebootJVMWithFirstThreadFlag(String[] args) throws Exception {
        String jvmBinary = System.getProperty("java.home") + "/bin/java";
        String classpath = System.getProperty("java.class.path");

        List<String> command = new ArrayList<>();
        command.add(jvmBinary);
        command.add("-XstartOnFirstThread");
        command.add("-cp");
        command.add(classpath);
        command.add(ClientLauncher.class.getName());
        command.addAll(List.of(args));

        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        process.waitFor();
    }
}
