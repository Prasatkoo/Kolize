import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String colis = text.load("lvl1.txt");

        String[] linesOfMaze = colis.split("\n");

        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        // TODO: Add support for macOS

        long window = GLFW.glfwCreateWindow(800, 600, "Maze", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("Can't open window");
        }
        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);

        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });

        Shaders.initShaders();

        ArrayList<Square> squares = new ArrayList<>();

        {
            int i = 0;
            while (i < linesOfMaze.length) {
                String[] coords = linesOfMaze[i].split(";");
                Square square = new Square(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]));
                squares.add(square);
                i++;
            }
        }

        Square moove = new Square(0f, 0f, 0.25f);

        while (!GLFW.glfwWindowShouldClose(window)) {
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true); // Send a shutdown signal...


            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            {
                int i = 0;
                while (i < squares.size()) {
                    squares.get(i).render();
                    i++;
                }
            }

            for (int i = 0; i < squares.size(); i++) {
                if (ifcollide(moove, squares.get(i))) break;
            }
            moove.update(window);
            moove.render();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }
    public static boolean ifcollide(Square movingSquare, Square square) {
        if (in(movingSquare, square)) {
            movingSquare.purple();
            return true;
        }
        else movingSquare.yellow();
        return false;
    }
    public static boolean in(Square movingSquare, Square square) {
        return (((movingSquare.getX() < (square.getX() + square.getSize()) && movingSquare.getX() > square.getX())
                || (((movingSquare.getX() + movingSquare.getSize()) < (square.getX() + square.getSize()) && (movingSquare.getX() + movingSquare.getSize()) > square.getX())))
                && ((movingSquare.getY() < (square.getY()) && movingSquare.getY() > square.getY() - square.getSize())
                || (((movingSquare.getY() - movingSquare.getSize()) > (square.getY() - square.getSize()) && (movingSquare.getY() - movingSquare.getSize()) < square.getY()))));
    }





}
