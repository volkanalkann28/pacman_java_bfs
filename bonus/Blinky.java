// red ghost targets the nearest map corner
public class Blinky extends Enemy {
    public Blinky(Position pos) {
        super(pos);
    }

    @Override
    public Position selectTarget(Player player, MapData mapData) {
        Position targetCorner = null;
        int minDistance = Integer.MAX_VALUE;
        Position playerPos = player.getPos();

        // find closest corner using manhattan distance
        for (Position corner : mapData.getCorners()) {
            int distance = Math.abs(playerPos.getRow() - corner.getRow()) +
                    Math.abs(playerPos.getCol() - corner.getCol());
            if (distance < minDistance) {
                minDistance = distance;
                targetCorner = corner;
            }
        }
        return targetCorner;
    }
}
