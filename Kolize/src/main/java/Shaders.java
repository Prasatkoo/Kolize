import org.lwjgl.opengl.GL33;

public class Shaders {
    private static final String vertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 vertexColors;\n" +

            "uniform mat4 matrix;" +

            "out vec4 myColors;\n" +

            "void main()\n" +
            "{\n" +
            "gl_Position = matrix *  vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "myColors = vertexColors;\n" +
            "}";


    private static final String fragmentShaderSource = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec4 myColors;\n" +
            "void main()\n" +
            "{\n" +
            "   FragColor = myColors;\n" +
            "}\n";

    public static int vertexShaderId;
    public static int fragmentShaderId;
    public static int shaderProgramId;

    public static void initShaders() {

        vertexShaderId = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);
        fragmentShaderId = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER);


        GL33.glShaderSource(vertexShaderId, vertexShaderSource);
        GL33.glCompileShader(vertexShaderId);


        System.out.println(GL33.glGetShaderInfoLog(vertexShaderId));

        GL33.glShaderSource(fragmentShaderId, fragmentShaderSource);
        GL33.glCompileShader(fragmentShaderId);


        System.out.println(GL33.glGetShaderInfoLog(fragmentShaderId));

        shaderProgramId = GL33.glCreateProgram();
        GL33.glAttachShader(shaderProgramId, vertexShaderId);
        GL33.glAttachShader(shaderProgramId, fragmentShaderId);
        GL33.glLinkProgram(shaderProgramId);

        System.out.println(GL33.glGetProgramInfoLog(shaderProgramId));

        GL33.glDeleteShader(vertexShaderId);
        GL33.glDeleteShader(fragmentShaderId);
    }

}

