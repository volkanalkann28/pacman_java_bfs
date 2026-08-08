public class MapData {

    private final char[][] originalMap;
    private char[][] map;
    private final int rows;
    private final int cols;
    private final Position playerStart;
    private final Position pinkyStart;
    private final Position inkyStart;
    private final Position blinkyStart;
    private final Position[] corners;

    public MapData(char[][] map,
                   Position playerStart,
                   Position pinkyStart,
                   Position inkyStart,
                   Position blinkyStart,
                   Position[] corners) {
        this.rows = map.length;
        this.cols = map[0].length;
        this.originalMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            this.originalMap[i] = map[i].clone();
        }
        this.map = deepCopy(originalMap);
        this.playerStart = playerStart;
        this.pinkyStart = pinkyStart;
        this.inkyStart = inkyStart;
        this.blinkyStart = blinkyStart;
        this.corners = corners;
    }

    // safe array copy
    private char[][] deepCopy(char[][] src) {
        char[][] copy = new char[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }
        return copy;
    }


    public void resetMap() {
        this.map = deepCopy(originalMap);
    }

    public void restorePellet(int row, int col) {
        if (isInside(row, col) && map[row][col] == '_') {
            map[row][col] = '.';
        }
    }

    // boundary check
    public boolean isInside(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // prevent wall hits
    public boolean isValidMove(int row, int col) {
        return isInside(row, col) && !(map[row][col] == '#');
    }

    public boolean hasPellet(int row, int col) {
        return isInside(row, col) && map[row][col] == '.';
    }

    // clear eaten dot
    public void removePellet(int row, int col) {
        if (hasPellet(row, col)) {
            map[row][col] = '_';
        }
    }

    // get map tile type
    public char getTile(int row, int col) {
        return isInside(row, col) ? map[row][col] : '#';
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Position[] getCorners() { return corners; }
    public Position getPlayerStart() { return playerStart; }
    public Position getPinkyStart() { return pinkyStart; }
    public Position getInkyStart() { return inkyStart; }
    public Position getBlinkyStart() { return blinkyStart; }
}