package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int width;
    private int height;

    private CellColor[][] board;

    private Ant       mainAnt;
    private List<Ant> ants;

    public Board() {
        super();
        this.width = 64;
        this.height = 64;
        board = new CellColor[this.width][this.height];
        ants = new ArrayList<Ant>();
        fillInWhite();
    }

    public Board(int size) {
        super();
        this.width = size;
        this.height = size;
        board = new CellColor[this.width][this.height];
        ants = new ArrayList<Ant>();
        fillInWhite();
    }

    public Board(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        board = new CellColor[this.width][this.height];
        ants = new ArrayList<Ant>();
        fillInWhite();
    }

    private void fillInWhite() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                board[x][y] = CellColor.WHITE;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CellColor[][] getBoard() {
        return board;
    }

    public List<Ant> getAnts() {
        return ants;
    }

    public void setAnts(List<Ant> ants) {
        this.ants = ants;
    }

    public CellColor getCellColorAt(Coordinate2D coordinate2d) {
        return board[coordinate2d.getX()][coordinate2d.getY()];
    }

    public CellColor getCellColorAt(int x, int y) {
        return board[x][y];
    }

    public void setCellColorAt(Coordinate2D coordinate2d, CellColor cellColor) {
        board[coordinate2d.getX()][coordinate2d.getY()] = cellColor;
    }

    public void setCellColorAt(int x, int y, CellColor cellColor) {
        board[x][y] = cellColor;
    }

    public void toggleCellColorAt(Coordinate2D coordinate2d) {
        if (board[coordinate2d.getX()][coordinate2d.getY()] == CellColor.BLACK) {
            board[coordinate2d.getX()][coordinate2d.getY()] = CellColor.WHITE;
        } else {
            board[coordinate2d.getX()][coordinate2d.getY()] = CellColor.BLACK;
        }
    }

    public void toggleCellColorAt(int x, int y) {
        if (board[x][y] == CellColor.BLACK) {
            board[x][y] = CellColor.WHITE;
        } else {
            board[x][y] = CellColor.BLACK;
        }
    }

    public Ant getMainAnt() {
        return mainAnt;
    }

    public void setMainAnt(Ant mainAnt) {
        this.mainAnt = mainAnt;
    }

    public void moveAnt(Ant ant) {
        Coordinate2D initialAntPosition = new Coordinate2D(mainAnt.getPosition());
        Coordinate2D newAntPosition = new Coordinate2D(mainAnt.getPosition());

        // Rotate and move the ant
        Direction newFacingDirection = mainAnt.rotate(getCellColorAt(initialAntPosition));
        newAntPosition.move(newFacingDirection);
        // Way around out of bounds exception
        // Coordinate2D.clampPosition(newAntPosition, width, height);
        //
        mainAnt.setPosition(newAntPosition);

        // Change the color of the cell the ant juste left
        toggleCellColorAt(initialAntPosition);
    }

    public void moveAnts() {
        for (Ant ant : ants) {
            moveAnt(ant);
        }
    }

    @Override
    public String toString() {
        return "Board [width=" + width + ", height=" + height + ", board=" + Arrays.toString(board) + "]";
    }
}
