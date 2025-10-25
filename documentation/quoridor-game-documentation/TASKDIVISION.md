# Task Division - Quoridor Implementation

**Team Members:**
- Owen Mariani (U74333523) - omariani@bu.edu
- Alim Kura (U86485144) - ackura@bu.edu

---

## Owen Mariani

### Game Architecture & Refactoring
- Packaged game classes into proper module structure
- Refactored QuoridorPlayerPiece → QuoridorPlayer
- Created QuoridorPiece to hold QuoridorPlayer reference
- Resolved Game class generics to support QuoridorPlayer-specific methods

### Board Display
- `printCurrentBoard()` - visual rendering with row/column labels, player initials, and walls
- Designed coordinate system using row numbers (0-8) and column letters (A-I)
- Implemented border formatting with % symbols and grid structure

### Wall Placement System
- `placeWall()` with validation and pathfinding checks
- `getEdgeCoordinatesFromUserInput()` - converts user input (row, letter, orientation) to edge coordinates
- `wallIsInBoundsAndNonOverlapping()` - overlap detection, 3-length wall merge prevention
- Wall bisection checking to prevent dividing existing walls

### Pathfinding & Graph Traversal
- `playerHasPathToGoal()` - BFS algorithm to validate player can reach goal
- `allUsersCanReachGoal()` - ensures wall placement doesn't trap any player
- Rollback mechanism for invalid wall placements
- Fixed HashSet array comparison bug with Position class

### Supporting Classes & Validation
- EdgePiece - wall segment with blocked/unblocked state
- Position - immutable coordinate class with proper equals/hashCode
- QuoridorPlayer - extends Player with wall budget, starting coordinates, win area logic
- QuoridorValidator - validates positions, edge indices, orientations

---

## Alim Kura

### QuoridorGame Implementation
- `executeNextMove()`, `checkWin()`, `changeTurns()`, `endGame()`, `runQuoridor()`
- `handleMove()` and `handleWall()` - user input handling and validation
- Overrode `playGame()` to fix win-detection timing issue

### Movement Logic
- `getValidMoves()` - validates orthogonal moves, straight jumps, diagonal jumps
- `movePlayer()` - executes piece movement and updates board state
- Fixed diagonal move key collisions using UL/UR/DL/DR notation

### Board Structure
- Initialized QuoridorBoard with 9x9 vertex grid
- Set up horizontal and vertical edge arrays (EdgePiece[][])

### Refactoring & Debugging
- Moved DIRS/KEYS constants to abstract Board class
- Integrated Quoridor into GameLauncher menu
- Fixed player position tracking bugs (getName() vs toString() keys)
- Fixed HashMap key mismatches and array indexing errors
- Created and ran comprehensive jump test cases

---

## Collaborative

- Design decisions (coordinate systems, wall orientation, diagonal key notation, Board vs Game separation)
- Debugging sessions (position tracking, wall coordinates, jump validation, turn management)
- Testing, integration, and code reviews