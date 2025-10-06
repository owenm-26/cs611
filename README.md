# Console-Based Object-Oriented Game Project
By Owen Mariani (U74333523) - Alim Kura (U86485144)

10/06/2025

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
└── DimensionValidator.java
```

## Notes
The Design document and related documentation can be found in [/documentation](/documentation)

## I/O Example
See [Sliding Game I/O Example File](documentation/sliding_game_io_example.txt)

### New Features  
- **GameLauncher** — centralized menu to start either Sliding Puzzle or Dots & Boxes.  
- **Dots & Boxes Support** — full second game implemented with board rendering and turn-based logic.  
- **QUIT Handling** — user can type `"QUIT"` anytime to gracefully exit.  
- **Player Validation** — prevents duplicate names and assigns unique initials.  


## Compiling & Running Directions
This a Maven project so the compilation is made easy for us. Follow the commands below:
```agsl
// Compile
mvn clean compile

// Run Launcher (choose game from menu)
mvn exec:java
```

## Sources
Used W3 Schools and Google's AI generated search responses to 
brush up on Java basics.

Used ChatGPT to learn best practices
such as `protected` vs `public` and `abstract class`.