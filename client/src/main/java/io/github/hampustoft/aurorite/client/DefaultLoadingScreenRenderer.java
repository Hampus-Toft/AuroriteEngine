package io.github.hampustoft.aurorite.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import io.github.hampustoft.aurorite.api.loading.InitializationProgress;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DefaultLoadingScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger("Load");
    private int customTextureId = -1;
    private int spinnerTextureId = -1;

    // Animation settings
    private static final int SPINNER_FRAMES = 8;
    private static final float ANIMATION_SPEED_SECONDS = 0.1f;
    private double lastFrameTime = 0.0;
    private int currentFrame = 0;

    void init() {
        this.lastFrameTime = glfwGetTime();

        File customBg = new File("gameData/branding/loading_bg.png");
        if (customBg.exists()) {
            // this.customTextureId = ClientTextureEngine.loadImmediate(customBg);
        }

        File spinnerFile = new File("gameData/branding/loading_spinner.png");
        if (spinnerFile.exists()) {
            // Load a horizontal sprite sheet containing 8 frames
            // this.spinnerTextureId = ClientTextureEngine.loadImmediate(spinnerFile);
        }
    }

    private boolean firstDraw = true;

    void render(InitializationProgress report) {
        if (firstDraw) {
            firstDraw = false;
            LOGGER.info("First Draw Call made");
        }
        if (this.customTextureId != -1) {
            this.drawCustomBackground();
        } else {
            this.drawFallbackBackground();
        }

        this.drawProgressBar(report.percentage());
        this.drawAnimatedSpinner();
    }

    void cleanup() {
        this.deleteTexture(this.customTextureId);
        this.deleteTexture(this.spinnerTextureId);
        this.customTextureId = -1;
        this.spinnerTextureId = -1;
    }

    private void updateAnimation() {
        double currentTime = glfwGetTime();
        if (currentTime - this.lastFrameTime >= ANIMATION_SPEED_SECONDS) {
            this.currentFrame = (this.currentFrame + 1) % SPINNER_FRAMES;
            this.lastFrameTime = currentTime;
        }
    }

    private void drawProgressBar(float percentage) {
        // Render background bar slot (e.g., dark gray)
        this.drawQuad(100, 500, 600, 20, 0.2f, 0.2f, 0.2f);

        // Render filled progress bar mapping width to the percentage
        float filledWidth = 600f * percentage;
        this.drawQuad(100, 500, filledWidth, 20, 0.0f, 0.6f, 1.0f);
    }

    private void drawAnimatedSpinner() {
        if (this.spinnerTextureId == -1) {
            return;
        }
        this.updateAnimation();

        // Calculate horizontal UV coordinates for the current frame
        float frameWidthUV = 1.0f / SPINNER_FRAMES;
        float uMin = this.currentFrame * frameWidthUV;
        float uMax = uMin + frameWidthUV;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, this.spinnerTextureId);

        glBegin(GL_QUADS);
        glTexCoord2f(uMin, 0.0f);
        glVertex2f(700, 530);
        glTexCoord2f(uMax, 0.0f);
        glVertex2f(732, 530);
        glTexCoord2f(uMax, 1.0f);
        glVertex2f(732, 562);
        glTexCoord2f(uMin, 1.0f);
        glVertex2f(700, 562);
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawQuad(float x, float y, float width, float height, float r, float g, float b) {
        glColor3f(r, g, b);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();
        glColor3f(1.0f, 1.0f, 1.0f); // Reset color context
    }

    private void drawCustomBackground() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, this.customTextureId);
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f);
        glVertex2f(0, 0);
        glTexCoord2f(1f, 0f);
        glVertex2f(800, 0);
        glTexCoord2f(1f, 1f);
        glVertex2f(800, 600);
        glTexCoord2f(0f, 1f);
        glVertex2f(0, 600);
        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    private void drawFallbackBackground() {
        // Use glfwGetTime() to create a smooth, oscillating factor between 0.0 and 1.0
        double time = glfwGetTime();
        float wave = (float) (Math.sin(time * 3.0) * 0.5 + 0.5);

        // Mix a dynamic color scheme: base dark slate pulsing into deep purple/blue
        float r = 0.08f + (wave * 0.15f);
        float g = 0.09f;
        float b = 0.12f + (wave * 0.30f);

        // Option A: Explicitly set the clear color context
        glClearColor(r, g, b, 1.0f);
    }

    private void deleteTexture(int textureId) {
        if (textureId != -1) {
            glDeleteTextures(textureId);
        }
    }
}
