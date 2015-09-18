package main;

public class Ant {
    private Coordinate2D position;
    private Direction    facingDireaction;

    public Ant(Coordinate2D position) {
        super();
        this.position = position;
        this.facingDireaction = Direction.LEFT;
    }

    public Ant(Coordinate2D position, Direction facingDirection) {
        super();
        this.position = position;
        this.facingDireaction = facingDirection;
    }

    public Coordinate2D getPosition() {
        return position;
    }

    public void setPosition(Coordinate2D position) {
        this.position = position;
    }

    public Direction getFacingDireaction() {
        return facingDireaction;
    }

    public void setFacingDireaction(Direction facingDireaction) {
        this.facingDireaction = facingDireaction;
    }

    public Direction rotate(CellColor currentCellColor) {
        facingDireaction = facingDireaction.getNewDirection(currentCellColor);
        return facingDireaction;
    }
}