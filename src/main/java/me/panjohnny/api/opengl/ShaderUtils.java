package me.panjohnny.api.opengl;

import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ShaderUtils {

    public static int loadShaderFromFile(String filePath, int type) throws IOException {
        // Read shader (can be inside the jar or outside)
        String shaderSource = Files.readString(Paths.get(filePath));
        return createShader(type, shaderSource);
    }

    public static int loadShaderFromResource(String filePath, int type) throws IOException {
        // Read shader (can be inside the jar or outside)
        String shaderSource = new String(Objects.requireNonNull(ShaderUtils.class.getResource(filePath)).openStream().readAllBytes());
        return createShader(type, shaderSource);
    }

    private static int createShader(int type, String shaderSource) {
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + GL20.glGetShaderInfoLog(shaderId));
        }

        return shaderId;
    }

    public static int createShaderProgramFromResource(String vertexShaderPath, String fragmentShaderPath) throws IOException {
        int vertexShader = loadShaderFromResource(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        int fragmentShader = loadShaderFromResource(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);

        return createShaderProgram(vertexShader, fragmentShader);
    }

    public static int createShaderProgramFromFile(String vertexShaderPath, String fragmentShaderPath) throws IOException {
        int vertexShader = loadShaderFromFile(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        int fragmentShader = loadShaderFromFile(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);

        return createShaderProgram(vertexShader, fragmentShader);
    }

    private static int createShaderProgram(int vertexShader, int fragmentShader) {
        int programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader);
        GL20.glAttachShader(programId, fragmentShader);
        GL20.glLinkProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("Error linking shader program: " + GL20.glGetProgramInfoLog(programId));
        }

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        return programId;
    }
}