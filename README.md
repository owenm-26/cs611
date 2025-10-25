# Console-Based Object-Oriented Game Project
By Owen Mariani (U74333523) - Alim Kura (U86485144)

10/24/2025

## File Tree
Below are the components that make up the solution.
```
/src/main/java
├── Main.java
├── GameLauncher.java
├── Game.java
├── Board.java
├── Tile.java
├── Piece.java
├── Player.java
│
├── SliderValidator.java
├── SliderBoard.java
├── PuzzlePiece.java
├── SliderGame.java
│
├── DABValidator.java
├── DABBoard.java
├── ConnectionsPiece.java
├── DABGame.java
│
├── QuoridorValidator.java
├── QuoridorBoard.java
├── QuoridorPiece.java
├── QuoridorPlayer.java
├── QuoridorGame.java
├── EdgePiece.java
├── Position.java
│
└── DimensionValidator.java
```

## Notes 
The Design documents, UML Diagrams, and I/O Examples can be found in [/documentation](/documentation) and its subfolders

### Features  
- **GameLauncher** — centralized menu to start Sliding Puzzle, Dots & Boxes, or Quoridor.  
- **Three Complete Games** — Sliding Puzzle, Dots & Boxes, and Quoridor all fully implemented.  
- **Quoridor Support** — 9x9 board with wall placement, jump mechanics (straight & diagonal), pathfinding validation, and 2-4 player support.
- **QUIT Handling** — user can type `"QUIT"` anytime to gracefully exit.  
- **Player Validation** — prevents duplicate names and assigns unique initials.  
- **Movement Validation** — handles orthogonal moves, straight jumps over opponents, and diagonal jumps when blocked.
- **Wall Placement** — validates wall placement with overlap detection and pathfinding to ensure no player gets trapped.


## Compiling & Running Directions
This is a Maven project so compilation is made easy for us. Follow the commands below:
```bash
// Compile
mvn clean compile

// Run Launcher (choose game from menu)
mvn exec:java
```

## Quoridor Gameplay

### Objective
Get to the opposite side of the board from where you start before your opponent(s) does by moving towards the goal area and placing walls to block others.

### Controls
- **Move:** Enter `M` to move your pawn, then select one of your valid moves (which will be shown to you!)
  - Orthogonal: U/D/L/R 
  - Diagonal jumps: UL/UR/DL/DR (when jumping over opponents)
- **Place Wall:** Enter `W` to place a wall
  - Enter coordinates as row + column letter (e.g., "5 G")
  - Select orientation: U (up), D (down), L (left), or R (right)

### Wall Placement Instructions
When placing walls, you select the tile where your wall originates and then choose the orientation from that tile:
- **U (Up) or D (Down):** Creates a **horizontal wall** (blocks vertical movement)
  - The wall extends horizontally from the selected edge
- **L (Left) or R (Right):** Creates a **vertical wall** (blocks horizontal movement)
  - The wall extends vertically from the selected edge

Each wall is of length 2 and will always extend further in the positive direction (grid-wise) from where you start it. 

**Important:** You cannot block an opponent from their objective entirely, but you can make it more difficult to reach it! The game validates that all players still have a path to their goal before allowing wall placement.

## Sources
Used W3 Schools and Google's AI generated search responses to 
brush up on Java basics.

Used ChatGPT to learn best practices
such as `protected` vs `public` and `abstract class`.