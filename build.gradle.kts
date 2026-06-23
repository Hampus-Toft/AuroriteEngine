plugins {
    // Apply the Java library plugin logic globally to all subprojects
    id("com.diffplug.spotless") version "6.25.0"
    java
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "com.diffplug.spotless")

    // ----------------------------------------------------
    // Checkstyle Configuration (Code Quality & Complexity)
    // ----------------------------------------------------
    configure<CheckstyleExtension> {
        toolVersion = "10.12.4"
        // Looks for your rules file at root/config/checkstyle/checkstyle.xml
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = false // Fails the build if complexity/rules are violated
        isShowViolations = true
    }

    // ----------------------------------------------------
    // Spotless Configuration (Automated Formatting)
    // ----------------------------------------------------
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        // Java Formatting Rules
        java {
            target("src/**/*.java")
            palantirJavaFormat() // Enforces standard Google style
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }

        // Kotlin Formatting Rules (For build scripts or Kotlin mods)
        kotlin {
            target("src/**/*.kt", "*.gradle.kts")
            ktlint("0.50.0") // Enforces standard Kotlin style
            trimTrailingWhitespace()
            endWithNewline()
        }

        // Markdown Formatting (Optional - great for keeping AGENTS.md clean!)
        format("misc") {
            target("*.md", ".gitignore")
            trimTrailingWhitespace()
            indentWithSpaces(2)
            endWithNewline()
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}