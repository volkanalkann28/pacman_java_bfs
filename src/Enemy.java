import java.util.ArrayList;

public abstract class Enemy {
    protected Position pos;
    protected Game.Direction direction;
    protected BFSPathFinder finder;
    protected double visualRow;
    protected double visualCol;

    public Enemy(Position pos){
        this.pos = pos;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.finder = new BFSPathFinder();
    }

    public Enemy(Position pos, BFSPathFinder finder) {
        this.pos = pos;
        this.finder = finder;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    public double getVisualRow() { return visualRow; }
    public double getVisualCol() { return visualCol; }
    public void setVisualRow(double visualRow) { this.visualRow = visualRow; }
    public void setVisualCol(double visualCol) { this.visualCol = visualCol; }
    public Position getPos() { return pos; }
    public void setPos(Position pos) { this.pos = pos; }
    public Game.Direction getDirection() { return direction; }
    public void setDirection(Game.Direction direction) { this.direction = direction; }

    // Every ghost has its own target logic
    public abstract Position selectTarget(Player player, MapData mapData);

    public void move(Player player, MapData mapData) {
        Position target = selectTarget(player, mapData);
        if (target != null && finder != null) {
            // Find path using BFS
            ArrayList<Position> path = finder.getFullShortestPath(this.pos, target, mapData);
            if (path != null && path.size() > 1) {
                Position nextStep = path.get(1);
                this.direction = getDirectionFromPositions(this.pos, nextStep);
                this.pos = nextStep;
            } else {
                this.direction = Game.Direction.NONE;
            }
        }
    }

    // Turn coordinates into movement direction
    protected Game.Direction getDirectionFromPositions(Position from, Position to) {
        int rDiff = to.getRow() - from.getRow();
        int cDiff = to.getCol() - from.getCol();
        if (rDiff == -1 && cDiff == 0) return Game.Direction.UP;
        if (rDiff == 1 && cDiff == 0) return Game.Direction.DOWN;
        if (rDiff == 0 && cDiff == -1) return Game.Direction.LEFT;
        if (rDiff == 0 && cDiff == 1) return Game.Direction.RIGHT;
        return Game.Direction.NONE;
    }
}