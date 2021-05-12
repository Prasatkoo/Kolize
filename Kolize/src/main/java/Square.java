import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {
    private float[] vertices;

    private final int[] indices = {
            0, 1, 3,
            1, 2, 3
    };
    public int squareVaoId;
    public int squareVboId;
    private int squareEboId;
    private int squareColorId;
    public static int uniformMatrixLocation;
    public Matrix4f matrix;
    public FloatBuffer matrixFloatBuffer;
    public float[] yellow;
    public float[] purple;
    private float x;
    private float y;
    private float size;
    public FloatBuffer floatBufferColors;

    public Square(float x, float y, float leng) {
        this.x = x;
        this.y = y;
        this.size = leng;
        matrix = new Matrix4f().identity();
        matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

        float longest = (float) Math.sqrt(2);
        float c1 = (float) Math.sqrt(((x + leng) * (x + leng)) + (y * y));
        float c2 = (float) Math.sqrt(((x + leng) * (x + leng)) + ((y - leng) * (y - leng)));
        float c3 = (float) Math.sqrt((x * x) + ((y - leng) * (y - leng)));
        float c4 = (float) Math.sqrt((x * x) + (y * y));
        float percent1 = (c1 / longest);
        float percent2 = (c2 / longest);
        float percent3 = (c3 / longest);
        float percent4 = (c4 / longest);

        float[] colors = {
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
        };

        yellow = new float[]{
                1f, 1f, 0f, 1f,
                1f, 1f, 0f, 1f,
                1f, 1f, 0f, 1f,
                1f, 1f, 0f, 1f,
        };

        purple = new float[]{
                1f, 0f, 1f, 1f,
                1f, 0f, 1f, 1f,
                1f, 0f, 1f, 1f,
                1f, 0f, 1f, 1f,
        };

        float[] vertices = {
                x + leng, y, 0.0f,
                x + leng, y - leng, 0.0f,
                x, y - leng, 0.0f,
                x, y, 0.0f,
        };
        floatBufferColors = BufferUtils.createFloatBuffer(yellow.length).put(yellow).flip();
        this.vertices = vertices;

        squareVaoId = GL33.glGenVertexArrays();
        squareEboId = GL33.glGenBuffers();
        squareVboId = GL33.glGenBuffers();
        squareColorId = GL33.glGenBuffers();

        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);


        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        FloatBuffer cfb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();


        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        MemoryUtil.memFree(cfb);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glUseProgram(Shaders.shaderProgramId);
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(ib);
    }

    public void render () {
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);


        GL33.glUseProgram(Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }


    public void update(long window) {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0.01f, 0f, 0f);
            this.x = x + 0.01f;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(-0.01f, 0f, 0f);
            this.x = x - 0.01f;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0f, 0.01f, 0f);
            this.y = y + 0.01f;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0, -0.01f, 0f);
            this.y = y - 0.01f;
        }

        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
    }

    public void yellow() {
        GL33.glBindVertexArray(squareVaoId);
        floatBufferColors.clear().put(yellow).flip();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, floatBufferColors, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public void purple() {
        GL33.glBindVertexArray(squareVaoId);
        floatBufferColors.clear().put(purple).flip();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, floatBufferColors, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }


}
