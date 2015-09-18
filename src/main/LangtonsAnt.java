package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

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
    private double                  mousePosX;
    private double                  mousePosY;
    private double                  offsetX      = 0;
    private double                  offsetY      = 0;

    Map<Integer, Boolean> buttonsState = new HashMap<Integer, Boolean>();
    private CellColor     selectedCellColor;

    // The window handle
    private long window;

    // Game field
    private Board   board;
    private int     boardSize                = 128;
    private double  numberOfSquaresToDisplay = 128;
    private boolean paused                   = false;
    private boolean stepByStep               = false;
    private boolean updateNextStep           = false;
    private boolean draw                     = false;

    private int step = 0;

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

            // TODO: Gameloop
            // http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
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
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - windowWidth) / 2,
                ((GLFWvidmode.height(vidmode) - windowHeight) / 2) - 100);

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
                        }
                        break;
                    case GLFW_PRESS:
                        switch (key) {
                            case GLFW_KEY_W:
                                break;
                            case GLFW_KEY_S:
                                break;
                            case GLFW_KEY_A:
                                break;
                            case GLFW_KEY_D:
                                break;
                            case GLFW_KEY_P:
                                paused = !paused;
                                break;
                            case GLFW_KEY_B:
                                stepByStep = !stepByStep;
                                break;
                            case GLFW_KEY_N:
                                updateNextStep = true;
                                break;
                            case GLFW_KEY_V:
                                draw = !draw;
                                break;
                            case GLFW_KEY_O:
                                System.out.println("Step : " + step);
                                break;
                        }
                        break;
                    case GLFW_REPEAT:
                        switch (key) {
                            case GLFW_KEY_W:
                                break;
                            case GLFW_KEY_S:
                                break;
                            case GLFW_KEY_A:
                                break;
                            case GLFW_KEY_D:
                                break;
                            case GLFW_KEY_N:
                                updateNextStep = true;
                                break;
                        }
                        break;
                }
            }
        });

        buttonsState.put(GLFW_MOUSE_BUTTON_1, false);

        glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                // TODO: Compute mods (alt, shift, etc)
                buttonsState.put(button, action == GLFW_PRESS);

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    if (draw) {
                        // TODO: ugglyest approximation ever
                        Coordinate2D selectedCell = new Coordinate2D((int) mousePosX * boardSize / windowWidth,
                                (int) mousePosY * boardSize / windowHeight);
                        board.toggleCellColorAt(selectedCell);
                        selectedCellColor = board.getCellColorAt(selectedCell);
                    }
                }
            }
        });

        glfwSetCursorPosCallback(window, posCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (buttonsState.get(GLFW_MOUSE_BUTTON_1)) {
                    if (draw) {
                        // TODO: fix offset
                        if (board.getCellColorAt((int) mousePosX * boardSize / windowWidth,
                                (int) mousePosY * boardSize / windowHeight) != selectedCellColor)
                            board.setCellColorAt((int) mousePosX * boardSize / windowWidth,
                                    (int) mousePosY * boardSize / windowHeight, selectedCellColor);
                    } else {
                        offsetX += xpos - mousePosX;
                        offsetY += ypos - mousePosY;

                        mousePosX = xpos;
                        mousePosY = ypos;
                    }
                } else {
                    mousePosX = xpos;
                    mousePosY = ypos;
                }
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if (numberOfSquaresToDisplay - yoffset > 10) {
                    numberOfSquaresToDisplay -= yoffset;
                }
            }
        });

        glfwSetCursorEnterCallback(window, enterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, int entered) {
                // TODO: Add trigger to avoid out of bounds exception when the
                // mouse leaves the window and button 1 pressed
            }
        });
    }

    /**
     * Initializes game logic
     */
    private void initGame() {
        board = new Board(boardSize);
        Ant ant = new Ant(new Coordinate2D(boardSize / 2, boardSize / 2));
        board.setMainAnt(ant);
    }

    /**
     * Called each frame (for now, as soon as possible)
     */
    private void update() {
        if (!paused) {
            if (stepByStep) {
                if (updateNextStep) {
                    board.moveAnt(board.getMainAnt());
                    updateNextStep = false;
                    step++;
                }
            } else {
                board.moveAnt(board.getMainAnt());
                step++;
            }
        } else {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called as soon as possible to render
     */
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
        // framebuffer

        // Coordinate2D squareSize = new Coordinate2D(windowWidth /
        // board.getWidth(), windowHeight / board.getHeight());
        // System.out.println(offsetX);
        Coordinate2D squareSize = new Coordinate2D(windowWidth / (int) numberOfSquaresToDisplay);
        Coordinate2D mouseOffset = new Coordinate2D((int) -offsetX, (int) -offsetY);

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
                // top-left, top-right, bottom-right, bottom-left
                glVertex2i((x * squareSize.getX()) + 1 - mouseOffset.getX(),
                        (y * squareSize.getY()) + 1 - mouseOffset.getY());
                glVertex2i(((x + 1) * squareSize.getX()) - 1 - mouseOffset.getX(),
                        (y * squareSize.getY()) + 1 - mouseOffset.getY());
                glVertex2i(((x + 1) * squareSize.getX()) - 1 - mouseOffset.getX(),
                        ((y + 1) * squareSize.getY()) - 1 - mouseOffset.getY());
                glVertex2i((x * squareSize.getX()) + 1 - mouseOffset.getX(),
                        ((y + 1) * squareSize.getY()) - 1 - mouseOffset.getY());
                glEnd();
            }
        }

        // Draw ant
        Coordinate2D mainAntPosition = board.getMainAnt().getPosition();
        Coordinate2D centerOfSquare = new Coordinate2D(
                mainAntPosition.getX() * squareSize.getX() + (squareSize.getX() / 2),
                mainAntPosition.getY() * squareSize.getY() + (squareSize.getY() / 2));

        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_TRIANGLES);
        switch (board.getMainAnt().getFacingDireaction()) {
            case UP:
                glVertex2i(centerOfSquare.getX() - mouseOffset.getX(),
                        centerOfSquare.getY() - (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() - (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() + (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() + (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() + (squareSize.getY() / 2) - mouseOffset.getY());
                break;
            case DOWN:
                glVertex2i(centerOfSquare.getX() - mouseOffset.getX(),
                        centerOfSquare.getY() + (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() + (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() - (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - (squareSize.getY() / 2) - mouseOffset.getY());
                break;
            case LEFT:
                glVertex2i(centerOfSquare.getX() - (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() + (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() + (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() + (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - (squareSize.getY() / 2) - mouseOffset.getY());
                break;
            case RIGHT:
                glVertex2i(centerOfSquare.getX() + (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() - (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() - (squareSize.getY() / 2) - mouseOffset.getY());
                glVertex2i(centerOfSquare.getX() - (squareSize.getX() / 2) - mouseOffset.getX(),
                        centerOfSquare.getY() + (squareSize.getY() / 2) - mouseOffset.getY());
                break;
        }
        glEnd();

        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public static void main(String[] args) {
        new LangtonsAnt().run();
    }
}