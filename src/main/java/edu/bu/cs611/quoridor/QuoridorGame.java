package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Game;
import edu.bu.cs611.core.Player;

import java.util.*;

import static edu.bu.cs611.quoridor.QuoridorBoard.orientationIsHorizontal;

public class QuoridorGame extends Game<QuoridorPlayer> {
    public QuoridorBoard gameboard;

    public QuoridorGame(List<QuoridorPlayer> players) {
        super(QuoridorBoard.DEFAULT_SIZE, QuoridorBoard.DEFAULT_SIZE);
        gameboard = new QuoridorBoard();
        this.players = players;
        
        // Place pieces on board at starting positions
        for (QuoridorPlayer player : players) {
            QuoridorPiece piece = new QuoridorPiece();
            piece.setPlayer(player);
            int[] pos = player.getStartingCoordinates();
            gameboard.board_arr[pos[0]][pos[1]].addPiece(piece);
            gameboard.positions_map.put(player.getName(), pos);
        }
        
        initializeGame(GameType.QUORIDOR, gameboard);
    }

    @Override
    protected void executeNextMove() {
        QuoridorPlayer currentPlayer = getPlayerWhoseTurnItIs();
        if (currentPlayer == null) return;
        
        System.out.println("\n" + currentPlayer.getName() + "'s turn");
        
        boolean actionSuccessful = false;
        while (!actionSuccessful) {
            System.out.print("Move (M) or Place Wall (W)? ");
            String action = scanner.nextLine().trim().toUpperCase();
            quitIfRequested(action);
            
            if (action.equals("M")) {
                actionSuccessful = handleMove(currentPlayer);
            } else if (action.equals("W")) {
                actionSuccessful = handleWall(currentPlayer);
            } else {
                System.out.println("Invalid input. Please enter 'M' for Move or 'W' for Wall.");
            }
        }

    }

    @Override
    protected void playGame(Board gameboard) {
        turn_count = 0;
        boolean has_won = false;
        gameboard.printCurrentBoard();
        
        while (!has_won) {
            executeNextMove();
            gameboard.printCurrentBoard();
            has_won = checkWin();
            
            if (!has_won) {
                changeTurns();  // Only change turns if nobody won
            }
        }
        
        endGame();
    }

    private boolean handleMove(QuoridorPlayer player) {
        HashMap<String, int[]> validMoves = gameboard.getValidMoves(player);
        
        if (validMoves.isEmpty()) {
            System.out.println("No valid moves available. Game over.");
            return true;
        }
        
        System.out.print("Valid moves: ");
        for (String dir : validMoves.keySet()) {
            System.out.print(dir + " ");
        }
        System.out.println();
        
        while (true) {
            System.out.print("Enter direction (U/D/L/R): ");
            String direction = scanner.nextLine().trim().toUpperCase();
            quitIfRequested(direction);
            
            if (!validMoves.containsKey(direction)) {
                System.out.println("Invalid direction '" + direction + "'. Valid moves are: " + validMoves.keySet());
                continue;
            }
            
            try {
                int[] targetPos = validMoves.get(direction);
                movePlayer(player, targetPos[0], targetPos[1]);
                System.out.println("Move successful!");
                return true;
            } catch (Exception e) {
                System.out.println("Move failed: " + e.getMessage());
            }
        }
    }

    private boolean handleWall(QuoridorPlayer player) {
        if (!player.hasWallToPlace()) {
            System.out.println("You have no walls remaining. Please choose to move instead.");
            return false;
        }
        
        System.out.println("You have " + player.getWallsLeft() + " walls remaining.");
        
        while (true) {
            System.out.print("Enter wall coordinates (x y): ");
            String coordInput = scanner.nextLine().trim();
            quitIfRequested(coordInput);
            
            String[] parts = coordInput.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid format. Please enter two numbers separated by space (e.g., '3 4').");
                continue;
            }
            
            int x, y;
            try {
                x = Integer.parseInt(parts[0]);
                y = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid coordinates. Please enter numbers between 0-8.");
                continue;
            }
            
            System.out.print("Enter wall orientation (U/D/L/R): ");
            String orientation = scanner.nextLine().trim().toUpperCase();
            quitIfRequested(orientation);
            
            try {
                boolean success = placeWall(x, y, orientation);
                if (success) {
                    player.decrementWallsLeft();
                    System.out.println("Wall placed successfully!");
                    return true;
                } else {
                    System.out.println("Wall placement failed: would block a player's path to goal or overlaps with existing wall.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid wall placement: " + e.getMessage());
            }
        }
    }

    @Override
    protected void endGame() {
        QuoridorPlayer winner = getPlayerWhoseTurnItIs();
        
        System.out.println("\n------- Game Over! -------");
        System.out.println("🏆 " + winner.getName() + " wins!!!");
        System.out.println("\nFinal Statistics:");
        System.out.println(winner.getName() + " - Walls remaining: " + winner.getWallsLeft());
        
        System.out.println("\nAll Players:");
        for (QuoridorPlayer p : players) {
            System.out.println(p.getName() + " - Walls remaining: " + p.getWallsLeft());
        }
        
        System.out.println("\nThank you for playing Quoridor!");
    }

    @Override
    protected boolean checkWin() {
        QuoridorPlayer currentPlayer = getPlayerWhoseTurnItIs();
        if (currentPlayer == null) return false;
        
        int[] currentPos = gameboard.getPlayerPosition(currentPlayer);
        if (currentPos == null) return false;
        
        return currentPlayer.isWinningArea(currentPos);
    }

    @Override
    protected void changeTurns() {
        QuoridorPlayer currentPlayer = getPlayerWhoseTurnItIs();
        
        if (currentPlayer != null) {
            currentPlayer.setTurn(false);
        }
        
        // find current player's index
        int currentIndex = players.indexOf(currentPlayer);
        
        // Get next player (round-robin with wraparound)
        int nextIndex = (currentIndex + 1) % players.size();
        players.get(nextIndex).setTurn(true);
    }

    public boolean placeWall(int x, int y, String orientation) {
        HashMap<QuoridorBoard.HorizontalOrVertical, int[][]> coordinates = new HashMap<>();
        try {
           coordinates = getEdgeCoordinatesFromUserInput(x,y, orientation);
            if (!gameboard.wallIsInBoundsAndNonOverlapping(coordinates)) return false;
        }catch(IllegalArgumentException e) {
            System.out.println(e);
        }
        // -place the walls first
        for (int[][] cPair: coordinates.values()){
            for (int[] c: cPair){
                if(coordinates.containsKey(QuoridorBoard.HorizontalOrVertical.HORIZONTAL))
                    gameboard.setBlockHorizontalEdge(c[0], c[1], true);
                if(coordinates.containsKey(QuoridorBoard.HorizontalOrVertical.VERTICAL))
                    gameboard.setBlockVerticalEdge(c[0], c[1], true);
            }
        }

        boolean traversable = allUsersCanReachGoal();

        // remove all added edges
        if(!traversable){
            for (int[][] cPair: coordinates.values()){
                for (int[] c: cPair){
                    if(coordinates.containsKey(QuoridorBoard.HorizontalOrVertical.HORIZONTAL))
                        gameboard.setBlockHorizontalEdge(c[0], c[1], false);
                    if(coordinates.containsKey(QuoridorBoard.HorizontalOrVertical.VERTICAL))
                        gameboard.setBlockVerticalEdge(c[0], c[1], false);
                }
            }
        }
        return traversable;
    }

    public void movePlayer(QuoridorPlayer player, int toRow, int toCol) {
        HashMap<String, int[]> validMoves = gameboard.getValidMoves(player);
        
        boolean isValidMove = false;
        for (int[] targetPos : validMoves.values()) {
            if (targetPos[0] == toRow && targetPos[1] == toCol) {
                isValidMove = true;
                break;
            }
        }
        
        if (!isValidMove) {
            throw new IllegalArgumentException(
                String.format("Invalid move: cannot move to (%d, %d)", toRow, toCol)
            );
        }
        
        int[] currentPos = gameboard.getPlayerPosition(player);
        if (currentPos == null) {
            throw new IllegalStateException("Player not found on board");
        }
        
        QuoridorPiece playerPiece = null;
        for (QuoridorPiece piece : gameboard.board_arr[currentPos[0]][currentPos[1]].getPiecesOnTile()) {
            if (piece.getPlayer() != null && piece.getPlayer().equals(player)) {
                playerPiece = piece;
                break;
            }
        }
        
        if (playerPiece == null) {
            throw new IllegalStateException("Player piece not found at current position");
        }
        
        gameboard.board_arr[currentPos[0]][currentPos[1]].removePiece(playerPiece);
        gameboard.board_arr[toRow][toCol].addPiece(playerPiece);
        gameboard.positions_map.put(player.getName(), new int[]{toRow, toCol});
    }

    
    public HashMap<QuoridorBoard.HorizontalOrVertical,int[][]> getEdgeCoordinatesFromUserInput(int x, int y, String orientation){
        /*
        Helper method that consistently returns what the vertical or horizontal edges that would be blocked
        would be after a user gives x,y, and orientation
         */
        // validate inputs
        if(!QuoridorValidator.isValidOrientation(orientation))
            System.out.println(QuoridorValidator.getInvalidOrientationMessage());

        HashMap<QuoridorBoard.HorizontalOrVertical, int[][]> res = new HashMap<>();
        int[][] coordinates = new int[2][2];
        switch (orientation){
            case "U":
                coordinates[0] = new int[]{x-1,y};
                coordinates[1] = new int[]{x-1,y+1};
                break;
            case "D":
                coordinates[0] = new int[]{x,y};
                coordinates[1] = new int[]{x,y+1};
                break;
            case "L":
                coordinates[0] = new int[]{x,y-1};
                coordinates[1] = new int[]{x-1,y-1};
                break;
            case "R":
                coordinates[0] = new int[]{x,y};
                coordinates[1] = new int[]{x-1,y};
                break;
        }

        // validate edges chosen from user
        for(int[] edge: coordinates){
            if (orientationIsHorizontal(orientation) && !QuoridorValidator.isValidHorizontalEdge(edge[0],edge[1]))
                throw new IllegalArgumentException(QuoridorValidator.getInvalidEdgeDimensionMessage());

            else if (!orientationIsHorizontal(orientation) && !QuoridorValidator.isValidHorizontalEdge(edge[0],edge[1]))
                throw new IllegalArgumentException(QuoridorValidator.getInvalidEdgeDimensionMessage());
        }

        if(orientationIsHorizontal(orientation)){
            res.put(QuoridorBoard.HorizontalOrVertical.HORIZONTAL, coordinates);
        }
        else{
            res.put(QuoridorBoard.HorizontalOrVertical.VERTICAL, coordinates);
        }
        return res;
    }

    public boolean allUsersCanReachGoal(){
        /*
        Helper Method to use when considering placing new walls
         */
        // run from all players starting position
        try{
            for (QuoridorPlayer p: players){
                if (!playerHasPathToGoal(p, gameboard.getPlayerPosition(p)))
                    return false;
            }
        }catch (InterruptedException e){
            System.out.println(e);
        }
        return true;
    }

    public boolean playerHasPathToGoal(QuoridorPlayer p, int[] currentPos) throws InterruptedException {
        Queue<int[]> q = new LinkedList<>();
        q.add(currentPos);
        HashSet<int[]> seen = new HashSet<>();
        seen.add(currentPos);

        int[] popped = new int[2];
        while(!q.isEmpty()){
            popped = q.remove();
            for(String d: QuoridorBoard.KEYS_TO_DIR.keySet()){
                int[] newSpace = {popped[0] + QuoridorBoard.KEYS_TO_DIR.get(d)[0], popped[1] + QuoridorBoard.KEYS_TO_DIR.get(d)[1]};
                // skip if position out of bounds
                if (!QuoridorValidator.isValidPosition(newSpace[0], newSpace[1])) continue;
                // skip if already explored
                if (seen.contains(newSpace)) continue;

                // skip if blocked
                //horizontal
                if(!orientationIsHorizontal(d) && gameboard.isVerticalEdgeBlocked(newSpace[0], newSpace[1])) continue;
                    //vertical
                else if (orientationIsHorizontal(d) && gameboard.isHorizontalEdgeBlocked(newSpace[0], newSpace[1])) continue;

                // add to queue and seen
                if(p.isWinningArea(newSpace)) return true;
                q.add(newSpace);
                seen.add(newSpace);

            }
        }
        return false;
    }

    public static void runQuoridor() {
        System.out.println("\n--- Quoridor ---");
        
        // Get number of players
        int numPlayers = getNumberOfPlayers();
        
        // Calculate wall budget
        int wallBudget = 20 / numPlayers;
        
        // Define starting positions
        int[][] startingPositions = getStartingPositions(numPlayers);
        
        // Create players
        List<QuoridorPlayer> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            QuoridorPlayer p = new QuoridorPlayer("Player" + (i+1), wallBudget, startingPositions[i]);
            if (i == 0) p.setTurn(true);
            players.add(p);
        }
        
        // Get player names - need to convert to List<Player>
        List<Player> playerList = new ArrayList<>(players);
        Player.promptMultiplePlayersForNames(playerList);
        
        // Start game
        new QuoridorGame(players);
        System.out.println();
    }

    private static int getNumberOfPlayers() {
        while (true) {
            System.out.print("How many players? (2 or 4): ");
            String input = Game.scanner.nextLine().trim();
            
            if (input.equals(Game.RESERVED_QUIT_KEYWORD)) {
                System.out.println("User triggered a quit. Closing game.");
                System.exit(0);
            }
            
            try {
                int num = Integer.parseInt(input);
                if (num == 2 || num == 4) {
                    return num;
                } else {
                    System.out.println("Invalid number. Please enter 2 or 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (2 or 4).");
            }
        }
    }

    private static int[][] getStartingPositions(int numPlayers) {
        if (numPlayers == 2) {
            return new int[][]{
                {0, 4},  // Top center
                {8, 4}   // Bottom center
            };
        } else {  // 4 players
            return new int[][]{
                {0, 4},  // Top center
                {8, 4},  // Bottom center
                {4, 0},  // Left center
                {4, 8}   // Right center
            };
        }
    }

}
