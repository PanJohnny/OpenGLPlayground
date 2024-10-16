package me.panjohnny;

import me.panjohnny.api.SceneObject;

public class Triangle extends SceneObject {
    public Triangle(float x, float y, float z, float width, float height, float depth, float rotationX, float rotationY, float rotationZ, int vbo, int ibo, int texture, int angles) {
        super(x, y, z, width, height, depth, rotationX, rotationY, rotationZ, vbo, ibo, texture, angles);
    }
}
