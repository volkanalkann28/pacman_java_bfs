public class Snapshot {
    public Position playerPos;
    public Game.Direction playerDir; // pacman facing direction
    public int score;
    public Position[] enemyPos;
    public Game.Direction[] enemyDir;
    public Position eatenPellet;

    public Snapshot(Position playerPos, Game.Direction playerDir, int score, Position[] enemyPos, Game.Direction[] enemyDir, Position eatenPellet) {
        this.playerPos = playerPos;
        this.playerDir = playerDir;
        this.score = score;
        this.enemyPos = enemyPos.clone();
        this.enemyDir = enemyDir.clone();
        this.eatenPellet = eatenPellet;
    }
}