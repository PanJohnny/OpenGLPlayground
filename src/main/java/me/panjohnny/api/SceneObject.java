package me.panjohnny.api;

import me.panjohnny.api.capabilities.Drawable;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class SceneObject implements Drawable {
    protected float x, y, z;
    protected float width, height, depth;
    protected float rotationX, rotationY, rotationZ;
    protected int vbo, ibo, texture;

    protected int angles;

    public SceneObject(float x, float y, float z, float width, float height, float depth, float rotationX, float rotationY, float rotationZ, int vbo, int ibo, int texture, int angles) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.vbo = vbo;
        this.ibo = ibo;
        this.texture = texture;
        this.angles = angles;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    @Override
    public void draw() {
        // Bind the VBO and IBO
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindTexture(GL_TEXTURE_2D, texture);

        // Transform and rotate the object
        glPushMatrix();
        glTranslatef(x, y, z);
        glRotatef(rotationX, 1, 0, 0);
        glRotatef(rotationY, 0, 1, 0);
        glRotatef(rotationZ, 0, 0, 1);

        // Draw the object
        glDrawElements(GL_TRIANGLES, angles, GL_UNSIGNED_INT, 0);

        // Restore the model-view matrix
        glPopMatrix();

        // Unbind the buffers and texture
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
