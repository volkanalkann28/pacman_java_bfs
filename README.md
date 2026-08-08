# Pac-Man 2D Maze Engine 🟡👻

A complete 2D Pac-Man game engine implemented in Java using the **StdDraw** graphics library. The project features grid-based navigation with sub-tile visual interpolation, input buffering, custom BFS pathfinding AI for distinct ghost personalities, and custom generic data structures[cite: 11, 12, 13, 15, 17].

## 🛠️ Key Technical Features & Architecture

* **Object-Oriented Architecture:** Modular design with strict separation of game state (`Game`), entity inheritance (`Player`, `Enemy` abstract class, `Pinky`, `Blinky`, `Inky`), board configuration (`MapData`), and rendering (`GameRenderer`)[cite: 11, 13, 15, 16, 17, 18].
* **Breadth-First Search (BFS) AI Pathfinding:** Ghosts calculate real-time deterministic shortest paths through the maze using a custom array-based generic `Queue<T>` with strict **UP-DOWN-LEFT-RIGHT** directional priority[cite: 10, 15].
* **Distinct Ghost Strategies:**
  * 💖 **Pinky (Direct Enemy):** Uses BFS to directly target Pac-Man's exact grid coordinates[cite: 15, 17].
  * ❤️ **Blinky (Closest Corner Enemy):** Evaluates all map corners to target the corner closest to Pac-Man to cut off escape routes[cite: 12, 16].
  * 💙 **Inky (Random Chase Enemy):** Probabilistic targeting (60% direct chase, 40% random valid neighbor tile) before executing BFS.
* **Input Buffering ("Next Move" Logic):** Caches direction keypresses before intersections, changing direction only when grid-aligned and unblocked to ensure smooth control[cite: 11].
* **Sub-Tile Visual Smoothing:** Decouples integer grid logic from rendering coordinates using fractional interpolation (`visualRow`, `visualCol`) for smooth sliding motion and accurate distance-based collision detection[cite: 11, 13, 18].

## 🚀 How to Run

### Prerequisites
* Java Development Kit (JDK 8 or higher)[cite: 19]
* `StdDraw` library included in the classpath[cite: 19]

### Compilation & Execution

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/pacman-java-bfs.git](https://github.com/your-username/pacman-java-bfs.git)
   cd pacman-java-bfs
   ```
2. **Compile the source code:**
   ```bash
   javac -cp .:StdDraw.jar src/*.java -d bin/
   ```
3. **Run the game:**
   ```bash
   java -cp bin:StdDraw.jar Main
   ```
### Game Controls
* **Action,Key
* **Move Up / Down / Left / Right ,Arrow Keys(↑, ↓, ←, →)
* **Start Game,Space
* **Pause / Resume,P
* **Quit Game,Q
* **Restart Game(Game Over/Won,R
   
