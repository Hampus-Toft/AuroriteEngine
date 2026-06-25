package io.github.hampustoft.aurorite.api.loading;

/**
 * An immutable snapshot of the current engine initialization state.
 * Using a Java 25 record for clean data modeling.
 */
public record InitializationProgress(
        float percentage, // 0.0f to 1.0f
        int tasksCompleted,
        int totalTasks,
        String currentTaskDescription) {
    public static InitializationProgress initial() {
        return new InitializationProgress(0.0f, 0, 1, "Initializing background workers...");
    }
}
