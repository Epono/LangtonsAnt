package main;

// TODO: add constraints according to grid
public class Coordinate2D {
    private int x;
    private int y;

    public Coordinate2D() {
        this.x = 0;
        this.y = 0;
    }

    public Coordinate2D(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Coordinate2D(int value) {
        super();
        this.x = value;
        this.y = value;
    }

    public Coordinate2D(Coordinate2D coordinate2D) {
        super();
        this.x = coordinate2D.getX();
        this.y = coordinate2D.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void clampPosition(Coordinate2D newAntPosition, int width, int height) {
        if (newAntPosition.getX() >= width) {
            newAntPosition.setX(0);
        } else if (newAntPosition.getX() < 0) {
            newAntPosition.setX(width - 1);
        } else if (newAntPosition.getY() >= height) {
            newAntPosition.setY(0);
        } else if (newAntPosition.getY() < 0) {
            newAntPosition.setY(height - 1);
        }
    }
}