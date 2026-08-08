public class Player {
    private Position pos;
    private int score;
    private Game.Direction currentDirection;
    private Game.Direction requestedDirection;
    private double visualRow;
    private double visualCol;
    private boolean moving;

    public Player(Position pos) {
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.pos = pos;
        this.score = 0;
        // visual coordinates for smooth movement
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.moving = false;
    }

    // Standard getters and setters for player state
    public boolean isMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }
    public Position getPos() { return pos; }
    public void setPos(Position pos) { this.pos = pos; }
    public Game.Direction getCurrentDirection() { return currentDirection; }
    public void setCurrentDirection(Game.Direction currentDirection) { this.currentDirection = currentDirection; }
    public Game.Direction getRequestedDirection() { return requestedDirection; }
    public void setRequestedDirection(Game.Direction requestedDirection) { this.requestedDirection = requestedDirection; }
    public double getVisualRow() { return visualRow; }
    public void setVisualRow(double visualRow) { this.visualRow = visualRow; }
    public double getVisualCol() { return visualCol; }
    public void setVisualCol(double visualCol) { this.visualCol = visualCol; }
    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }
    public void resetScore() { this.score = 0; }
}