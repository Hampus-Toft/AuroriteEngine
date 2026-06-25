plugins {
    id("java-library") // Enables the 'api' dependency configuration
    // Keep your other plugins like spotless here
}

dependencies {
    // Now 'api' will resolve perfectly
    api("org.slf4j:slf4j-api:2.0.13")
}
