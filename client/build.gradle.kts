plugins {
    java
}

// 1. Determine the host OS and architecture dynamically
val osName = System.getProperty("os.name").lowercase()
val osArch = System.getProperty("os.arch").lowercase()

val platformClassifier = when {
    osName.contains("windows") -> "natives-windows"
    osName.contains("mac") || osName.contains("darwin") -> {
        if (osArch.contains("aarch64") || osArch.contains("arm64")) "natives-macos-arm64" else "natives-macos"
    }
    osName.contains("linux") || osName.contains("unix") -> {
        if (osArch.contains("aarch64") || osArch.contains("arm64")) "natives-linux-arm64" else "natives-linux"
    }
    else -> throw GradleException("Unsupported operating system: $osName")
}

dependencies {
    implementation(project(":core"))

    // Base LWJGL API libraries (Platform independent jars)
    implementation(libs.lwjgl)
    implementation(libs.lwjgl.glfw)
    implementation(libs.lwjgl.opengl)
    implementation(libs.lwjgl.openal)
    implementation(libs.lwjgl.stb)

    // 2. Inject ONLY the matching native modules for the current OS instance
    runtimeOnly("org.lwjgl:lwjgl:${libs.versions.lwjgl.get()}:$platformClassifier")
    runtimeOnly("org.lwjgl:lwjgl-glfw:${libs.versions.lwjgl.get()}:$platformClassifier")
    runtimeOnly("org.lwjgl:lwjgl-opengl:${libs.versions.lwjgl.get()}:$platformClassifier")
    runtimeOnly("org.lwjgl:lwjgl-openal:${libs.versions.lwjgl.get()}:$platformClassifier")
    runtimeOnly("org.lwjgl:lwjgl-stb:${libs.versions.lwjgl.get()}:$platformClassifier")
}

tasks.register<JavaExec>("run") {
    mainClass.set("com.tabletop.client.ClientLauncher")
    classpath = sourceSets["main"].runtimeClasspath
    
    // Smooth rendering flag required specifically for modern macOS environments
    if (System.getProperty("os.name").lowercase().contains("mac")) {
        jvmArgs("-XstartOnFirstThread")
    }
}