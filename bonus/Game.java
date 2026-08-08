public class Game {

    public enum GameState {
        START_SCREEN,
        READY,
        PLAYING,
        PAUSED,
        WON,
        LOST,
        BACKTRACKING
    }

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1), NONE(0, 0);

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() { return dRow; }
        public int getDCol() { return dCol; }
    }

    private final Player player;
    private final Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;

    private final double PACMAN_SPEED = 0.10;
    private final double ENEMY_SPEED = 0.067;

    // stack for rewind and tracking eaten dots
    private Stack<Snapshot> history = new Stack<>();
    private Position eatenPelletThisTick = null;

    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
    }

    public Player getPlayer() { return player; }
    public Enemy[] getEnemies() { return enemies; }
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }

    // main loop logic
    public void update() {
        // handle rewind
        if (gameState == GameState.BACKTRACKING) {
            if (history.isEmpty()) {
                restartGame();
            } else {
                Snapshot snap = history.pop();

                // put pacman back
                player.setPos(snap.playerPos);
                player.setVisualRow(snap.playerPos.getRow());
                player.setVisualCol(snap.playerPos.getCol());
                player.setScore(snap.score);
                player.setCurrentDirection(snap.playerDir);

                // keep mouth moving
                player.setMoving(true);

                // put ghosts back
                for (int i = 0; i < enemies.length; i++) {
                    enemies[i].setPos(snap.enemyPos[i]);
                    enemies[i].setVisualRow(snap.enemyPos[i].getRow());
                    enemies[i].setVisualCol(snap.enemyPos[i].getCol());
                    enemies[i].setDirection(snap.enemyDir[i]);
                }

                // restore eaten dot
                if (snap.eatenPellet != null) {
                    mapData.restorePellet(snap.eatenPellet.getRow(), snap.eatenPellet.getCol());
                }
            }
            return;
        }

        if (gameState != GameState.PLAYING) return;

        // save positions before moving
        Position prevPlayerPos = player.getPos();
        Position[] prevEnemyPos = new Position[enemies.length];
        for (int i = 0; i < enemies.length; i++) prevEnemyPos[i] = enemies[i].getPos();
        eatenPelletThisTick = null;

        updatePlayer();
        updateEnemies();
        checkCollisions();
        checkPellet();

        // see if anyone actually moved
        boolean moved = !player.getPos().equals(prevPlayerPos);
        for (int i = 0; i < enemies.length; i++) {
            if (!enemies[i].getPos().equals(prevEnemyPos[i])) moved = true;
        }

        // save history if something changed
        if (moved || eatenPelletThisTick != null) {
            Position[] ePos = new Position[enemies.length];
            Game.Direction[] eDir = new Game.Direction[enemies.length];
            for (int i = 0; i < enemies.length; i++) {
                ePos[i] = enemies[i].getPos();
                eDir[i] = enemies[i].getDirection();
            }

            history.push(new Snapshot(player.getPos(), player.getCurrentDirection(), player.getScore(), ePos, eDir, eatenPelletThisTick));
        }

        checkWinCondition();
    }

    // pacman movement
    private void updatePlayer() {
        if (isGridAligned(player.getVisualRow(), player.getVisualCol())) {
            Direction reqDir = player.getRequestedDirection();
            int nextR = player.getPos().getRow() + reqDir.getDRow();
            int nextC = player.getPos().getCol() + reqDir.getDCol();

            if (reqDir != Direction.NONE && mapData.isValidMove(nextR, nextC)) {
                player.setCurrentDirection(reqDir);
            }
        }

        Direction currDir = player.getCurrentDirection();
        if (currDir != Direction.NONE) {
            int targetR = player.getPos().getRow() + currDir.getDRow();
            int targetC = player.getPos().getCol() + currDir.getDCol();

            if (mapData.isValidMove(targetR, targetC)) {
                player.setMoving(true);

                // slide to target
                double newVisRow = moveTowards(player.getVisualRow(), targetR, PACMAN_SPEED);
                double newVisCol = moveTowards(player.getVisualCol(), targetC, PACMAN_SPEED);

                player.setVisualRow(newVisRow);
                player.setVisualCol(newVisCol);

                // snap to grid if close enough
                if (Math.abs(newVisRow - targetR) < 0.01 && Math.abs(newVisCol - targetC) < 0.01) {
                    player.setPos(new Position(targetR, targetC));
                    player.setVisualRow(targetR);
                    player.setVisualCol(targetC);
                }
            } else {
                player.setMoving(false);
            }
        }
    }

    // ghosts movement
    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            if (isGridAligned(enemy.getVisualRow(), enemy.getVisualCol())) {
                enemy.move(player, mapData);
            }

            Direction eDir = enemy.getDirection();
            if (eDir != Direction.NONE) {
                int targetR = enemy.getPos().getRow();
                int targetC = enemy.getPos().getCol();

                double newVisRow = moveTowards(enemy.getVisualRow(), targetR, ENEMY_SPEED);
                double newVisCol = moveTowards(enemy.getVisualCol(), targetC, ENEMY_SPEED);

                enemy.setVisualRow(newVisRow);
                enemy.setVisualCol(newVisCol);
            }
        }
    }

    // check if centered on grid
    private boolean isGridAligned(double visR, double visC) {
        return Math.abs(visR - Math.round(visR)) < 0.05 && Math.abs(visC - Math.round(visC)) < 0.05;
    }

    // smooth sliding math
    private double moveTowards(double current, double target, double speed) {
        if (current < target) return Math.min(current + speed, target);
        if (current > target) return Math.max(current - speed, target);
        return target;
    }

    // check hitboxes
    private void checkCollisions() {
        for (Enemy enemy : enemies) {
            double dist = Math.sqrt(Math.pow(player.getVisualRow() - enemy.getVisualRow(), 2) +
                    Math.pow(player.getVisualCol() - enemy.getVisualCol(), 2));
            if (dist < 0.6) {
                setGameState(GameState.LOST);
            }
        }
    }

    // eat dots
    private void checkPellet() {
        int r = player.getPos().getRow();
        int c = player.getPos().getCol();
        if (mapData.hasPellet(r, c)) {
            mapData.removePellet(r, c);
            player.addScore(10);
            eatenPelletThisTick = new Position(r, c);
        }
    }

    // win if no dots left
    private void checkWinCondition() {
        boolean noPelletsLeft = true;
        for (int r = 0; r < mapData.getRows(); r++) {
            for (int c = 0; c < mapData.getCols(); c++) {
                if (mapData.hasPellet(r, c)) {
                    noPelletsLeft = false;
                    break;
                }
            }
        }
        if (noPelletsLeft) setGameState(GameState.WON);
    }

    // reset everything
    public void restartGame() {
        mapData.resetMap();
        // clear history
        history = new Stack<>();

        player.setPos(mapData.getPlayerStart());
        player.setVisualRow(mapData.getPlayerStart().getRow());
        player.setVisualCol(mapData.getPlayerStart().getCol());
        player.setCurrentDirection(Direction.NONE);
        player.setRequestedDirection(Direction.NONE);
        player.setMoving(false);
        player.setScore(0);

        for (Enemy enemy : enemies) {
            if (enemy instanceof Pinky) enemy.setPos(mapData.getPinkyStart());
            else if (enemy instanceof Inky) enemy.setPos(mapData.getInkyStart());
            else if (enemy instanceof Blinky) enemy.setPos(mapData.getBlinkyStart());

            enemy.setVisualRow(enemy.getPos().getRow());
            enemy.setVisualCol(enemy.getPos().getCol());
            enemy.setDirection(Direction.NONE);
        }

        this.gameState = GameState.START_SCREEN;
    }
}