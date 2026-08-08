public class Pinky extends Enemy {
    public Pinky(Position pos) {
        super(pos);
    }

    @Override
    public Position selectTarget(Player player, MapData mapData) {
        // Pinky just targets the player's current spot
        return player.getPos();
    }
}