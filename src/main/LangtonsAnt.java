package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class LangtonsAnt {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback       errorCallback;
    private GLFWKeyCallback         keyCallback;
    private GLFWMouseButtonCallback mouseCallback;
    private GLFWCursorPosCallback   posCallback;
    private GLFWScrollCallback      scrollCallback;
    private GLFWCursorEnterCallback enterCallback;
    private int                     windowWidth  = 640;
    private int                     windowHeight = 640;
    private int                     windowXPos;
    private int                     windowYPos;
    private int                     moveStep     = 10;
    private double                  mousePosX;
    private double                  mousePosY;

    // The window handle
    private long window;

    // Game field
    private Board board;

    public void run() {
        // System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

        try {
            initGL();
            initGame();

            // This line is critical for LWJGL's interoperation with GLFW's
            // OpenGL context, or any context that is managed externally.
            // LWJGL detects the context that is current in the current thread,
            // creates the ContextCapabilities instance and makes the OpenGL
            // bindings available for use.
            GLContext.createFromCurrent(); // use this line instead with the
                                           // 3.0.0a
                                           // build

            // Set the clear color
            glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

            glMatrixMode(GL11.GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, windowWidth, windowHeight, 0, 1, -1);
            glMatrixMode(GL11.GL_MODELVIEW);

            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while (glfwWindowShouldClose(window) == GL_FALSE) {
                update();
                render();
            }

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
            mouseCallback.release();
            posCallback.release();
            scrollCallback.release();
            enterCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void initGL() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are
                                  // already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
                                                // after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(windowWidth, windowHeight, "Langton's Ant", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        setupCallbacks();

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center our window
        windowXPos = (GLFWvidmode.width(vidmode) - windowWidth) / 2;
        windowYPos = ((GLFWvidmode.height(vidmode) - windowHeight) / 2) - 100;
        glfwSetWindowPos(window, windowXPos, windowYPos);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void setupCallbacks() {
        // Setup a key callback. It will be called every time a key is pressed,
        // repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                switch (action) {
                    case GLFW_RELEASE:
                        switch (key) {
                            case GLFW_KEY_ESCAPE:
                                // We will detect in our rendering loop
                                glfwSetWindowShouldClose(window, GL_TRUE);
                                break;
                            default:
                                break;
                        }
                        break;
                    case GLFW_PRESS:
                        switch (key) {
                            case GLFW_KEY_W:
                                windowYPos -= moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_S:
                                windowYPos += moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_A:
                                windowXPos -= moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_D:
                                windowXPos += moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            default:
                                break;
                        }
                        break;
                    case GLFW_REPEAT:
                        switch (key) {
                            case GLFW_KEY_W:
                                windowYPos -= moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_S:
                                windowYPos += moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_A:
                                windowXPos -= moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            case GLFW_KEY_D:
                                windowXPos += moveStep;
                                glfwSetWindowPos(window, windowXPos, windowYPos);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                // GLFW_PRESS
                // GLFW_RELEASE
                switch (button) {
                    case GLFW_MOUSE_BUTTON_1:
                        break;
                    case GLFW_MOUSE_BUTTON_2:
                        break;
                    case GLFW_MOUSE_BUTTON_3:
                        break;
                    case GLFW_MOUSE_BUTTON_4:
                        break;
                    case GLFW_MOUSE_BUTTON_5:
                        break;
                    default:
                        break;
                }
            }
        });

        glfwSetCursorPosCallback(window, posCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mousePosX = xpos;
                mousePosY = ypos;
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {

            }
        });

        glfwSetCursorEnterCallback(window, enterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, int entered) {
            }
        });
    }

    private void initGame() {
        board = new Board(64, 64);
        Ant ant = new Ant(new Coordinate2D(32, 32));
        board.setAnt(ant);
    }

    private void update() {
        board.moveAnt();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
        // framebuffer

        Coordinate2D squareSize = new Coordinate2D(windowWidth / board.getWidth(), windowHeight / board.getHeight());

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                // Gradient
                // glColor3f((float) x / board.getWidth(), (float) y /
                // board.getHeight(), 1.0f);

                // Real color
                if (board.getCellColorAt(x, y) == CellColor.BLACK) {
                    glColor3f(0.0f, 0.0f, 0.0f);
                } else {
                    glColor3f(1.0f, 1.0f, 1.0f);
                }

                glBegin(GL_QUADS);
                glVertex2i((x * squareSize.getX()) + 1, (y * squareSize.getY()) + 1); // top-left
                // vertex
                glVertex2i(((x + 1) * squareSize.getX()) - 1, (y * squareSize.getY()) + 1); // top-right
                // vertex
                glVertex2i(((x + 1) * squareSize.getX()) - 1, ((y + 1) * squareSize.getY()) - 1); // bottom-right
                // vertex
                glVertex2i((x * squareSize.getX()) + 1, ((y + 1) * squareSize.getY()) - 1); // bottom-left
                // vertex
                glEnd();
            }
        }

        // Draw ant

        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public static void main(String[] args) {
        new LangtonsAnt().run();
    }
}