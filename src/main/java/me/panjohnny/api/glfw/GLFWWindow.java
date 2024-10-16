package me.panjohnny.api.glfw;

import me.panjohnny.api.wrap.PointerWrapper;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Provides options to manipulate GLFW windows.
 */
public class GLFWWindow extends PointerWrapper {
    public GLFWWindow(long pointer) {
        super(pointer);
    }

    /**
     * Makes the window visible.
     */
    public void show() {
        glfwShowWindow(pointer);
    }

    /**
     * Hides the window.
     */
    public void hide() {
        glfwHideWindow(pointer);
    }

    /**
     * Sets the window's title.
     * @param title The new title.
     */
    public void setTitle(String title) {
        glfwSetWindowTitle(pointer, title);
    }

    /**
     * Sets the window's size.
     * @param width The new width.
     * @param height The new height.
     */
    public void setSize(int width, int height) {
        glfwSetWindowSize(pointer, width, height);
    }

    /**
     * Sets the window's position.
     * @param x The new x position.
     * @param y The new y position.
     */
    public void setPosition(int x, int y) {
        glfwSetWindowPos(pointer, x, y);
    }

    /**
     * Sets the window's icon.
     * @param images The images to set as the icon (each should represent a pointer to an image in memory).
     */
    public void setIcon(ImageData[] images) {
        // Create a buffer to hold GLFWImage objects
        GLFWImage.Buffer buffer = GLFWImage.malloc(images.length);

        // Loop through each image and add it to the buffer
        for (ImageData image : images) {
            // Create a new GLFWImage instance for each image
            GLFWImage glfwImage = GLFWImage.malloc();

            // Set image width, height, and pixel data in the GLFWImage
            glfwImage.set(image.getWidth(), image.getHeight(), image.getPixelData());

            // Add the GLFWImage to the buffer
            buffer.put(glfwImage);
        }

        // Flip the buffer for reading
        buffer.flip();

        // Set the window icon using the buffer of images
        glfwSetWindowIcon(pointer, buffer);

        // Free the buffer when done to avoid memory leaks
        buffer.free();
    }

    public void setKeyCallback(GLFWKeyCallback callback) {
        glfwSetKeyCallback(pointer, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallback callback) {
        glfwSetMouseButtonCallback(pointer, callback);
    }

    public void setCursorCallback(GLFWCursorPosCallback callback) {
        glfwSetCursorPosCallback(pointer, callback);
    }

    public int[] getSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(pointer, width, height);
        return new int[] {width.get(), height.get()};
    }

    /**
     * Simple class to represent the image data needed for GLFWImage
     * (This is just an example. You can modify the class or the data source as needed)
     */
    public static class ImageData {
        private final int width;
        private final int height;
        private final ByteBuffer pixelData;

        public ImageData(int width, int height, ByteBuffer pixelData) {
            this.width = width;
            this.height = height;
            this.pixelData = pixelData;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getPixelData() {
            return pixelData;
        }

        /**
         * Loads a file and converts it into ImageData.
         * @return An ImageData containing the pixel data.
         */
        public static ImageData fromFile(String path, int width, int height) throws IOException {
            BufferedImage bi = ImageIO.read(new File(path));
            bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            WritableRaster raster = bi.getRaster();
            DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer();

            byte[] data = dataBufferByte.getData();
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);

            buffer.put(data);
            buffer.flip();
            return new ImageData(width, height, buffer);
        }
    }
}
