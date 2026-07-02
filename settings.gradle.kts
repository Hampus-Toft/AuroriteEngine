plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

// WIP name (Might change "engine" to forge or other terms)
rootProject.name = "aurorite-engine"

include("api")
include("core")
include("client")
include("server")
include("mods:example-cards")
include("mods:example-dice")