package me.panjohnny;

import me.panjohnny.api.app.Application;
import me.panjohnny.api.glfw.GLFWWindow;
import me.panjohnny.api.opengl.GLObjectHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.function.BiConsumer;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Application {
    private static final System.Logger LOGGER = System.getLogger(Main.class.getName());
    private int jeff;
    private Triangle triangle;


    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void init() {
    }

    @Override
    public void loadGraphics() {
//        int shader;
//        try {
//            shader = loadShaderResources("/vertex.glsl", "/fragment.glsl");
//            // useShader(shader);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //Create VBO and IBO for a 3d triangle
        float[] vertices = {
                0, 0, 0,
                1, 0, 0,
                1, 1, 0,

                0, 1, 0,
                0, 0, 1,
                1, 0, 1,

                1, 1, 1,
                0, 1, 1
        };

        float[] texCoords = {
                0, 0,
                1, 0,
                1, 1,

                0, 1,
                0, 0,
                1, 0,

                1, 1,
                0, 1
        };

        int vbo = GLObjectHelper.createVBO(vertices);
        int ibo = GLObjectHelper.createTexCoordsVBO(texCoords);

        jeff = loadTexture("bezos.png", (w, h) -> {
            System.out.println("Loaded texture with size: " + w + "x" + h);
        });

        triangle = new Triangle(0, 1, 0, 1, 1, 1, 0, 0, 0, vbo, ibo, jeff, vertices.length);
    }

    @Override
    public void draw() {
        triangle.draw();
    }

    /**
     * Loads texture using following parameters:
     * <ul>
     *     <li>GL_TEXTURE_WRAP_S, GL_REPEAT</li>
     *     <li>GL_TEXTURE_WRAP_T, GL_REPEAT</li>
     *     <li>GL_TEXTURE_MIN_FILTER, GL_NEAREST</li>
     *     <li>GL_TEXTURE_MAG_FILTER, GL_NEAREST</li>
     * </ul>
     *
     * @param file path to file
     * @return texture id
     * @see #loadTexture(String, int, int, int, int, BiConsumer)
     */
    public static int loadTexture(String file, BiConsumer<Integer, Integer> sizeConsumer) {
        return loadTexture(file, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_NEAREST, GL11.GL_NEAREST, sizeConsumer);
    }

    /**
     * Loads texture from filesystem using STBImage
     *
     * @param file       path to file
     * @param min_filter GL_TEXTURE_MIN_FILTER
     * @param mag_filter GL_TEXTURE_MAG_FILTER
     * @param wrap_s     GL_TEXTURE_WRAP_S
     * @param wrap_t     GL_TEXTURE_WRAP_T
     * @return texture id
     */
    public static int loadTexture(String file, int wrap_s, int wrap_t, int min_filter, int mag_filter, BiConsumer<Integer, Integer> sizeConsumer) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load the image data using STB
        ByteBuffer imageBuffer = STBImage.stbi_load(file, width, height, channels, 0);
        if (imageBuffer == null) {
            throw new RuntimeException("Failed to load image: " + file + ", Reason: " + STBImage.stbi_failure_reason());
        }

        if (GLFW.glfwGetCurrentContext() == 0) {
            LOGGER.log(System.Logger.Level.ERROR, "OpenGL context is not initialized, loading textures is supported after window opens");
            throw new RuntimeException("OpenGL context not initialized");
        }

        // Generate a new OpenGL texture
        int textureId = GL11.glGenTextures();
        glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        int w = width.get();
        int h = height.get();

        if (sizeConsumer != null)
            sizeConsumer.accept(w, h);

        // Create a new buffer to hold the flipped image data
        ByteBuffer flippedBuffer = BufferUtils.createByteBuffer(w * h * 4); // Assuming RGBA format

        for (int y = 0; y < h; y++) {
            int rowStart = (h - 1 - y) * w * 4; // Each row is w pixels, 4 bytes per pixel
            for (int x = 0; x < w; x++) {
                int pixelStart = rowStart + x * 4; // Calculate the starting index for the pixel
                flippedBuffer.put(imageBuffer.get(pixelStart));       // R
                flippedBuffer.put(imageBuffer.get(pixelStart + 1));   // G
                flippedBuffer.put(imageBuffer.get(pixelStart + 2));   // B
                flippedBuffer.put(imageBuffer.get(pixelStart + 3));   // A
            }
        }
        flippedBuffer.flip(); // Prepare the buffer for reading

        // Upload the flipped image data to the texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, flippedBuffer);

        // Set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap_s);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap_t);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, min_filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mag_filter);

        // Free the image data buffer
        STBImage.stbi_image_free(imageBuffer);

        glBindTexture(GL_TEXTURE_2D, 0); // Unbind the current 2D texture

        return textureId;
    }

}