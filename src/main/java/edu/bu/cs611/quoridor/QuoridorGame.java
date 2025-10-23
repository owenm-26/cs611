package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Game;
import edu.bu.cs611.core.Player;

import java.util.*;

import static edu.bu.cs611.quoridor.QuoridorBoard.orientationIsHorizontal;

public class QuoridorGame extends Game<QuoridorPlayer> {
    public QuoridorBoard gameboard;

    public QuoridorGame(){
        super(QuoridorBoard.DEFAULT_SIZE, QuoridorBoard.DEFAULT_SIZE);
        gameboard = new QuoridorBoard();

    }
    @Override
    protected void executeNextMove() {
    //TODO:
        QuoridorPlayer p = getPlayerWhoseTurnItIs();
        if(true){
            if (!p.hasWallToPlace()){
                System.out.println("Out of walls");

            }
            else
                placeWall(0,0,"L");
        }

    }

    @Override
    protected void endGame() {
        //TODO:
    }

    @Override
    protected boolean checkWin() {
        //TODO:
        return false;
    }

    @Override
    protected void changeTurns() {
        //TODO: Round robin style (in case of 4 players)
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
        gameboard.positions_map.put(player.toString(), new int[]{toRow, toCol});
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
        int[][] coordinates = new int[1][2];
        switch (orientation){
            case "U":
                coordinates[0] = new int[]{x,y};
                coordinates[1] = new int[]{x,y+1};
                break;
            case "D":
                coordinates[0] = new int[]{x+1,y};
                coordinates[1] = new int[]{x+1,y+1};
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

}
