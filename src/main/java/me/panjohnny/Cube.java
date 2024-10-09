package me.panjohnny;

import me.panjohnny.api.capabilities.Drawable;
import org.lwjgl.BufferUtils;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Cube implements Drawable {
    final float[] faceVertices = {
            // Front face
            -1f, -1f, 1f,  // Bottom-left
            1f, -1f, 1f,   // Bottom-right
            1f, 1f, 1f,    // Top-right
            -1f, -1f, 1f,  // Bottom-left
            -1f, 1f, 1f,   // Top-left
            1f, 1f, 1f,    // Top-right

            // Back face
            -1f, -1f, -1f, // Bottom-left
            1f, -1f, -1f,  // Bottom-right
            1f, 1f, -1f,   // Top-right
            -1f, -1f, -1f, // Bottom-left
            1f, 1f, -1f,   // Top-right
            -1f, 1f, -1f,  // Top-left

            // Top face
            -1f, 1f, -1f,  // Back-left
            -1f, 1f, 1f,   // Front-left
            1f, 1f, 1f,    // Front-right
            -1f, 1f, -1f,  // Back-left
            1f, 1f, 1f,    // Front-right
            1f, 1f, -1f,   // Back-right

            // Bottom face
            -1f, -1f, -1f, // Back-left
            1f, -1f, -1f,  // Back-right
            1f, -1f, 1f,   // Front-right
            -1f, -1f, -1f, // Back-left
            1f, -1f, 1f,   // Front-right
            -1f, -1f, 1f,  // Front-left

            // Right face
            1f, -1f, -1f,  // Back-bottom
            1f, 1f, -1f,   // Back-top
            1f, 1f, 1f,    // Front-top
            1f, -1f, -1f,  // Back-bottom
            1f, -1f, 1f,   // Front-bottom
            1f, 1f, 1f,    // Front-top

            // Left face
            -1f, -1f, -1f, // Back-bottom
            -1f, -1f, 1f,  // Front-bottom
            -1f, 1f, 1f,   // Front-top
            -1f, -1f, -1f, // Back-bottom
            -1f, 1f, 1f,   // Front-top
            -1f, 1f, -1f,  // Back-top
    };


    final float[] texCoords = {
            // Front face
            0f, 0f,   // Bottom left
            1f, 0f,   // Bottom right
            1f, 1f,   // Top right
            0f, 0f,   // Bottom left
            0f, 1f,   // Top left
            1f, 1f,   // Top right

            // Back face
            0f, 0f,   // Bottom left
            1f, 0f,   // Bottom right
            1f, 1f,   // Top right
            0f, 0f,   // Bottom left
            1f, 1f,   // Top right
            1f, 0f,   // Top left

            // Top face
            0f, 0f,   // Bottom left
            1f, 0f,   // Bottom right
            1f, 1f,   // Top right
            0f, 0f,   // Bottom left
            1f, 1f,   // Top right
            0f, 1f,   // Top left

            // Bottom face
            0f, 1f,   // Top left
            1f, 1f,   // Top right
            1f, 0f,   // Bottom right
            0f, 1f,   // Top left
            0f, 0f,   // Bottom left
            1f, 0f,   // Bottom right

            // Right face
            0f, 0f,   // Bottom left
            1f, 0f,   // Top left
            1f, 1f,   // Top right
            0f, 0f,   // Bottom left
            0f, 1f,   // Bottom right
            1f, 1f,   // Top right

            // Left face
            0f, 0f,   // Bottom left
            0f, 1f,   // Bottom right
            1f, 1f,   // Top right
            0f, 0f,   // Bottom left
            1f, 1f,   // Top right
            1f, 0f,   // Top left
    };

    int vboVertices;
    int vboTexCoords;

    float motion = 0f;

    public void prepare() {
        // VBO for vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(faceVertices.length);
        vertexBuffer.put(faceVertices);
        vertexBuffer.flip();

        vboVertices = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertices);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // VBO for texture coordinates
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texCoordBuffer.put(texCoords);
        texCoordBuffer.flip();

        vboTexCoords = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords);
        glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);

        // Enable the vertex array once
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    @Override
    public void draw() {
        applyMotion();
        // Bind the vertex VBO
        // Bind the vertex VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboVertices);
        glVertexPointer(3, GL_FLOAT, 0, 0);  // 3 components (x, y, z) per vertex

// Bind the texture coordinates VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);  // 2 components (s, t) per vertex

// Draw the object (6 vertices / 2 triangles)
        glDrawArrays(GL_TRIANGLES, 0, faceVertices.length / 3);

        // Optional cleanup (disabling after every frame might not be necessary)
        // glDisableClientState(GL_VERTEX_ARRAY);
        // glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void move(float x, float y, float z) {
        // Use translation matrix to move the cube
        glTranslatef(x, y, z);
    }

    public void shoot() {
        // start the motion
        motion = 1.2f;
    }

    public void applyMotion() {
        // This is a shoot effect, it should use linear interpolation to move the cube
        float velocity = (float) (Math.log(motion + 1) * 0.1f);
        motion += velocity;

        glTranslatef(0, 0, motion);
    }
}
