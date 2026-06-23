dependencies {
    implementation(project(":core"))
}

// Allow execution via './gradlew :server:run'
tasks.register<JavaExec>("run") {
    mainClass.set("com.tabletop.server.ServerLauncher") // Change to your actual main class
    classpath = sourceSets["main"].runtimeClasspath
}