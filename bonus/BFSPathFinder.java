import java.util.ArrayList;
import java.util.Collections;

// bfs algorithm for ghost pathfinding
public class BFSPathFinder {
    public ArrayList<Position> getFullShortestPath(Position start, Position goal, MapData mapData) {
        if (start.equals(goal)) {
            ArrayList<Position> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        Queue<Position> queue = new Queue<>();
        int rows = mapData.getRows();
        int cols = mapData.getCols();

        boolean[][] visited = new boolean[rows][cols];
        Position[][] parent = new Position[rows][cols];

        queue.enqueue(start);
        visited[start.getRow()][start.getCol()] = true;

        // check order up down left right
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
        boolean found = false;

        while (!queue.isEmpty()) {
            Position current = queue.dequeue();
            if (current.equals(goal)) {
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + dRow[i];
                int newCol = current.getCol() + dCol[i];

                if (mapData.isValidMove(newRow, newCol) && !visited[newRow][newCol]) {
                    Position nextPos = new Position(newRow, newCol);
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                    queue.enqueue(nextPos);
                }
            }
        }

        if (!found) return null;

        // trace back from goal to start
        ArrayList<Position> path = new ArrayList<>();
        Position curr = goal;
        while (curr != null) {
            path.add(curr);
            if (curr.equals(start)) break;
            curr = parent[curr.getRow()][curr.getCol()];
        }
        Collections.reverse(path);
        return path;
    }
}