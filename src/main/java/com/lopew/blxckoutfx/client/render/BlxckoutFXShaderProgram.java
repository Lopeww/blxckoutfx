package com.lopew.blxckoutfx.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class BlxckoutFXShaderProgram {
    private final int programId;

    private BlxckoutFXShaderProgram(int programId) {
        this.programId = programId;
    }

    public static BlxckoutFXShaderProgram compile(String vertexSource, String fragmentSource) {
        int vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);

        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            String log = GL20.glGetProgramInfoLog(program, 4096);
            throw new IllegalStateException("BlxckoutFX shader program failed to link: " + log);
        }

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        return new BlxckoutFXShaderProgram(program);
    }

    private static int compileShader(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            String log = GL20.glGetShaderInfoLog(shader, 4096);
            throw new IllegalStateException("BlxckoutFX shader failed to compile: " + log);
        }

        return shader;
    }

    public void bind() {
        GL20.glUseProgram(programId);
    }

    public static void unbind() {
        GL20.glUseProgram(0);
    }

    public void setUniform1i(String name, int value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) {
            GL20.glUniform1i(location, value);
        }
    }

    public void setUniform1f(String name, float value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) {
            GL20.glUniform1f(location, value);
        }
    }

    public void setUniform3f(String name, float x, float y, float z) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) {
            GL20.glUniform3f(location, x, y, z);
        }
    }

    public void setUniform4f(String name, float x, float y, float z, float w) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) {
            GL20.glUniform4f(location, x, y, z, w);
        }
    }
}
