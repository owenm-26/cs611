# Design Document
As of 10/06/2025

## Class Design
We used **predominantly Abstract Superclasses such as `Game`,  `Board`, `Tile`, and `Piece` to shape how 
different games operate in a consistent manner**, having each new game share as much of the previous
code as possible and make any necessary modifications in the shape of a new child class.

We also used an **interface to help handle the Dimension Validation of the user as each game has a 
different valid dimension range**, so we can use validator classes such as `DABValidator` and `SliderValidator`
to handle that logic and be passed around as a bundle easily.

Finally, we have a **`Main` class that handles the highest level orchestration of the game,
which is mostly allowing the user to choose what game** they want to play and when they want to 
play again.

### Inter Class Relationships
```agsl
Game > Board > Tile(s) > Piece(s)
SliderGame > SlideBoard > Tile(s) > PuzzlePiece(s)
DABGame > DABBoard > Tile(s) > ConnectionsPiece(s)
```

We used inheritance for **extendibility** so that we can quickly make new games / boards and modify them
with ease. In particular, we chose to make Tile's contents of type `Piece` and declare concrete classes to add
unique functionality per-game.

We encourage **scalability** by cleverly checking the game state by using preexisting variables from the `Player` class
instances and `DABBoard` class. This saves us on space complexity, which is bound at most n=10.

## Changes

### From Last Assignment
- Create `Player` class
- Create `Piece` Class
- Renamed `Position` to `Tile` and made it hold `Piece`s instead of str
- Handle play-again & game selection in `Main`
- Dimension Validation done via game specific validators
- Used Generics to promote reusability and flexibility
- Removed default method definitions in abstract classes that weren't applicable

### For Future Assignments
- Create method to handle all inputs, first scanning for `QUIT` Keyword then returning the user inputted result