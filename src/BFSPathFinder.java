import java.util.ArrayList;
import java.util.Collections;

public class BFSPathFinder {
    public ArrayList<Position> getFullShortestPath(Position start, Position goal, MapData mapData) {
        // If we are already at the target, just return the current position
        if (start.equals(goal)) {
            ArrayList<Position> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        Queue<Position> queue = new Queue<>();
        int rows = mapData.getRows();
        int cols = mapData.getCols();

        // Arrays to keep track of visited tiles and their parents to reconstruct the path
        boolean[][] visited = new boolean[rows][cols];
        Position[][] parent = new Position[rows][cols];

        queue.enqueue(start);
        visited[start.getRow()][start.getCol()] = true;

        // Direction order: UP, DOWN, LEFT, RIGHT
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
        boolean found = false;

        // BFS loop to find the shortest path in the grid
        while (!queue.isEmpty()) {
            Position current = queue.dequeue();
            if (current.equals(goal)) {
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + dRow[i];
                int newCol = current.getCol() + dCol[i];

                // Check boundaries and if the tile is a wall or already visited
                if (mapData.isValidMove(newRow, newCol) && !visited[newRow][newCol]) {
                    Position nextPos = new Position(newRow, newCol);
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                    queue.enqueue(nextPos);
                }
            }
        }

        if (!found) return null;

        // Backtrack from goal to start using the parent array to build the path
        ArrayList<Position> path = new ArrayList<>();
        Position curr = goal;
        while (curr != null) {
            path.add(curr);
            if (curr.equals(start)) break;
            curr = parent[curr.getRow()][curr.getCol()];
        }

        // Path is built backwards, so we need to reverse it
        Collections.reverse(path);
        return path;
    }
}