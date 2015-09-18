package main;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction getNewDirection(CellColor cellColor) {
        switch (this) {
            case UP:
                if (cellColor == CellColor.BLACK) {
                    return RIGHT;
                } else {
                    return LEFT;
                }
            case DOWN:
                if (cellColor == CellColor.BLACK) {
                    return LEFT;
                } else {
                    return RIGHT;
                }
            case LEFT:
                if (cellColor == CellColor.BLACK) {
                    return UP;
                } else {
                    return DOWN;
                }
            case RIGHT:
                if (cellColor == CellColor.BLACK) {
                    return DOWN;
                } else {
                    return UP;
                }
            default:
                return UP;
        }
    }
}
