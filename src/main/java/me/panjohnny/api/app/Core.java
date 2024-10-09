package me.panjohnny.api.app;

import me.panjohnny.api.glfw.GLFWWindow;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Core {
    private Thread thread;
    private long window;
    private Application master;

    public Core(Application master) {
        this.master = master;
        thread = new Thread(this::run, "LWJGL Application");
    }

    public void start() {
        thread.start();
    }

    public void run() {
        init();
        loop();
    }

    public void init() {
        // Create the window
        glfwSetErrorCallback((error, description) -> {
            throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
        });

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 800, "Jeff simulator 3000", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        master.createPointerWrapper(GLFWWindow.class, window);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
    }

    public void loop() {
        // Initialize OpenGL capabilities
        GL.createCapabilities();

        // Load master resources (shaders, textures, etc.)
        master.loadGraphics();

        // Show the window after loading graphics
        glfwShowWindow(window);

        // Set up the initial viewport and projection once
        setupViewport(800, 800);

        // Handle window resizing
        glfwSetWindowSizeCallback(window, (w, width, height) -> {
            setupViewport(width, height);  // Recalculate viewport and projection on resize
        });

        // Set OpenGL state that doesn't change per frame
        initializeOpenGLState();

        // Main rendering loop
        while (!glfwWindowShouldClose(window) && !Thread.interrupted()) {
            // Clear buffers (color and depth)
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Draw your scene
            master.draw();

            // Swap buffers and poll for events
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // Cleanup resources after window is closed
        cleanup();
    }

    private void setupViewport(int width, int height) {
        // Set the viewport to the new window dimensions
        glViewport(0, 0, width, height);

        // Switch to the projection matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Set an orthographic projection (or use perspective if needed)
        float aspectRatio = (float) width / (float) height;
        glOrtho(-aspectRatio, aspectRatio, -1.0, 1.0, -1.0, 1.0);

        // Switch back to the model-view matrix
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void initializeOpenGLState() {
        // Set clear color (background)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  // Black background

        // Enable important OpenGL features
        glEnable(GL_TEXTURE_2D);               // Enable 2D textures
        glEnable(GL_BLEND);                    // Enable blending
        glEnable(GL_DEPTH_TEST);               // Enable depth testing

        // Set blending function for transparency
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable client-side arrays (for VBOs)
        glEnableClientState(GL_VERTEX_ARRAY);
        //glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private void cleanup() {
        // Free GLFW resources and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
