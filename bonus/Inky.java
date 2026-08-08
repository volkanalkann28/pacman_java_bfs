import java.util.ArrayList;
import java.util.Random;

// cyan ghost with random movements
public class Inky extends Enemy {
    private Random rand;

    public Inky(Position pos) {
        super(pos);
        this.rand = new Random();
    }

    @Override
    public Position selectTarget(Player player, MapData mapData) {
        // mostly target pacman directly
        if (rand.nextDouble() < 0.6) {
            return player.getPos();
        } else {
            // sometimes pick a random neighbor cell
            ArrayList<Position> validNeighbors = new ArrayList<>();
            int[] dRow = {-1, 1, 0, 0};
            int[] dCol = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int newRow = this.pos.getRow() + dRow[i];
                int newCol = this.pos.getCol() + dCol[i];

                if (mapData.isValidMove(newRow, newCol)) {
                    validNeighbors.add(new Position(newRow, newCol));
                }
            }

            if (!validNeighbors.isEmpty()) {
                int randomIndex = rand.nextInt(validNeighbors.size());
                return validNeighbors.get(randomIndex);
            }
            return this.pos;
        }
    }
}