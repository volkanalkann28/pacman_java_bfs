import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        try {
            // Load map and initialize game objects
            MapData mapData = loadMapData("data/map.txt");
            Player player = new Player(mapData.getPlayerStart());
            Enemy[] enemies = new Enemy[]{
                    new Pinky(mapData.getPinkyStart()),
                    new Inky(mapData.getInkyStart()),
                    new Blinky(mapData.getBlinkyStart())
            };

            Game game = new Game(player, enemies, mapData);
            GameRenderer renderer = new GameRenderer(mapData, game);
            renderer.setupDraw();

            // Main Game Loop
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    // Quit only if game is over or at start screen
                    if (key == 'q' || key == 'Q') System.exit(0);

                    // Game state controls
                    if (game.getGameState() == Game.GameState.START_SCREEN && key == ' ') {
                        game.setGameState(Game.GameState.READY);
                        renderer.drawGame();
                        StdDraw.pause(1000); // Wait for ready text
                        game.setGameState(Game.GameState.PLAYING);
                    }
                    else if (key == 'p' || key == 'P') {
                        if (game.getGameState() == Game.GameState.PLAYING) game.setGameState(Game.GameState.PAUSED);
                        else if (game.getGameState() == Game.GameState.PAUSED) game.setGameState(Game.GameState.PLAYING);
                    }
                    else if (key == 'r' || key == 'R') {
                        if (game.getGameState() == Game.GameState.LOST || game.getGameState() == Game.GameState.WON) {
                            game.restartGame();
                        }
                    }
                }

                // Handling arrow keys for movement
                if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) game.getPlayer().setRequestedDirection(Game.Direction.UP);
                else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) game.getPlayer().setRequestedDirection(Game.Direction.DOWN);
                else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) game.getPlayer().setRequestedDirection(Game.Direction.LEFT);
                else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) game.getPlayer().setRequestedDirection(Game.Direction.RIGHT);

                // Update logic and draw
                game.update();
                renderer.tickAnimation();
                renderer.drawGame();
                StdDraw.pause(20); // Maintain around 50 FPS
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Map file could not be loaded.");
            e.printStackTrace();
        }
    }

    // Reads the map structure from a text file
    private static MapData loadMapData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        scanner.nextLine();

        // Read player and ghost start positions
        String[] playerLine = scanner.nextLine().split(" ");
        Position playerStart = new Position(Integer.parseInt(playerLine[1]), Integer.parseInt(playerLine[2]));

        String[] directEnemyLine = scanner.nextLine().split(" ");
        Position directEnemyStart = new Position(Integer.parseInt(directEnemyLine[1]), Integer.parseInt(directEnemyLine[2]));

        String[] randomChaseEnemyLine = scanner.nextLine().split(" ");
        Position randomChaseEnemyStart = new Position(Integer.parseInt(randomChaseEnemyLine[1]), Integer.parseInt(randomChaseEnemyLine[2]));

        String[] closestCornerEnemyLine = scanner.nextLine().split(" ");
        Position closestCornerEnemyStart = new Position(Integer.parseInt(closestCornerEnemyLine[1]), Integer.parseInt(closestCornerEnemyLine[2]));

        // Load corners for specific ghost AI
        String[] cornerHeader = scanner.nextLine().split(" ");
        int cornerCount = Integer.parseInt(cornerHeader[1]);

        Position[] corners = new Position[cornerCount];
        for (int i = 0; i < cornerCount; i++) {
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            scanner.nextLine();
            corners[i] = new Position(r, c);
        }

        // Fill the 2D map array
        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < cols; j++) {
                map[i][j] = line.charAt(j);
            }
        }
        scanner.close();

        return new MapData(map, playerStart, directEnemyStart, randomChaseEnemyStart, closestCornerEnemyStart, corners);
    }
}