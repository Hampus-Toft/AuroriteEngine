dependencies {
    implementation(project(":api"))

    // Core networking dependencies or concurrency utils can go here
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.fusesource.jansi:jansi:2.4.1")
}
