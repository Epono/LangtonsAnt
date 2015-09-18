package main;

import java.util.Arrays;

public class Board {
    private int width;
    private int height;

    CellColor[][] board;

    private Ant ant;

    public Board() {
        super();
        this.width = 64;
        this.height = 64;
        board = new CellColor[this.width][this.height];
    }

    public Board(int size) {
        super();
        this.width = size;
        this.height = size;
        board = new CellColor[this.width][this.height];
    }

    public Board(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        board = new CellColor[this.width][this.height];
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

    public Ant getAnt() {
        return ant;
    }

    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    public void moveAnt() {
        Coordinate2D initialAntPosition = new Coordinate2D(ant.getPosition());
        Coordinate2D newAntPosition = new Coordinate2D(ant.getPosition());

        // Rotate and move the ant
        Direction newFacingDirection = ant.rotate(getCellColorAt(initialAntPosition));
        newAntPosition.move(newFacingDirection);
        ant.setPosition(newAntPosition);

        // Change the color of the cell the ant juste left
        toggleCellColorAt(initialAntPosition);
    }

    @Override
    public String toString() {
        return "Board [width=" + width + ", height=" + height + ", board=" + Arrays.toString(board) + "]";
    }
}
