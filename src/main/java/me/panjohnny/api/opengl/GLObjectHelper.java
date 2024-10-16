package me.panjohnny.api.opengl;

import static org.lwjgl.opengl.GL15.*;

public class GLObjectHelper {
    public static int createVBO(float[] vertices) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        return vbo;
    }

    public static int createIBO(int[] indices) {
        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        return ibo;
    }

    public static int createTexCoordsVBO(float[] texCoords) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        return vbo;
    }

    public static void deleteVBO(int vbo) {
        glDeleteBuffers(vbo);
    }

    public static void deleteIBO(int ibo) {
        glDeleteBuffers(ibo);
    }
}
