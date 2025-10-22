package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Tile;
import sun.misc.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class QuoridorBoard extends Board<QuoridorPiece> {
    private EdgePiece[][] horizontalEdges;
    private EdgePiece[][] verticalEdges;
    
    public static final int DEFAULT_SIZE = 9;
    
    final static int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
    final static String[] KEYS = { "U", "D", "L", "R" };
    private enum HorizontalOrVertical{
        HORIZONTAL,
        VERTICAL
    }
    
    final static Map<String, int[]> KEYS_TO_DIR = new HashMap<>();
    
    static {
        KEYS_TO_DIR.put("U", new int[]{-1, 0});
        KEYS_TO_DIR.put("D", new int[]{1, 0});
        KEYS_TO_DIR.put("L", new int[]{0, -1});
        KEYS_TO_DIR.put("R", new int[]{0, 1});
    }
    
    public QuoridorBoard() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    public QuoridorBoard(int width, int height) {
        super(width, height);
        initializeBoard();
        initializeEdges();
    }
    
    public QuoridorBoard(int d) {
        this(d, d);
    }
    
    private void initializeBoard() {
        board_arr = (Tile<QuoridorPiece>[][]) new Tile[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                board_arr[r][c] = new Tile<QuoridorPiece>();
            }
        }
    }
    
    private void initializeEdges() {
        horizontalEdges = new EdgePiece[height - 1][width];
        for (int r = 0; r < height - 1; r++) {
            for (int c = 0; c < width; c++) {
                horizontalEdges[r][c] = new EdgePiece();
            }
        }
        
        verticalEdges = new EdgePiece[height][width - 1];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width - 1; c++) {
                verticalEdges[r][c] = new EdgePiece();
            }
        }
    }
    
    public int[] getPlayerPosition(QuoridorPlayer player) {
        return positions_map.get(getPlayerKey(player));
    }
    
    private String getPlayerKey(QuoridorPlayer player) {
        return player.toString();
    }
    
    public boolean isHorizontalEdgeBlocked(int row, int col) {
        if (QuoridorValidator.isValidHorizontalEdge(row, col)) {
            return horizontalEdges[row][col].getIsBlocked();
        }
        return false;
    }

    public boolean isVerticalEdgeBlocked(int row, int col) {
        if (QuoridorValidator.isValidVerticalEdge(row, col)) {
            return verticalEdges[row][col].getIsBlocked();
        }
        return false;
    }

    private void blockHorizontalEdge(int row, int col) {
        if (QuoridorValidator.isValidHorizontalEdge(row, col)) {
            horizontalEdges[row][col].setBlocked(true);
        }
    }

    private void blockVerticalEdge(int row, int col) {
        if (QuoridorValidator.isValidVerticalEdge(row, col)) {
            verticalEdges[row][col].setBlocked(true);
        }
    }
    
    public HashMap<String, int[]> getValidMoves(int row, int col) {
        // TODO: Implement movement validation
        // - Check orthogonal neighbors
        // - Check if edge is blocked by wall
        // - Handle jumps over opponents
        // - Handle diagonal jumps when blocked
        return new HashMap<>();
    }
    
    public void movePlayer(QuoridorPlayer player, int toRow, int toCol) {
        // TODO: Implement player movement
        // - Get current position
        // - Remove player from old tile
        // - Add player to new tile
        // - Update positions_map
    }
    
    public boolean hasPathToGoal(QuoridorPlayer p, int[] currentPos) throws InterruptedException {
        Queue<int[]> q = new Queue<>();
        q.enqueue(currentPos);
        HashSet<int[]> seen = new HashSet<>();
        seen.add(currentPos);

        int[] popped = new int[2];
        while(!q.isEmpty()){
            popped = q.dequeue();
            for(String d: QuoridorBoard.KEYS_TO_DIR.keySet()){
                int[] newSpace = {popped[0] + QuoridorBoard.KEYS_TO_DIR.get(d)[0], popped[1] + QuoridorBoard.KEYS_TO_DIR.get(d)[1]};
                // skip if position out of bounds
                if (!QuoridorValidator.isValidPosition(newSpace[0], newSpace[1])) continue;
                // skip if already explored
                if (seen.contains(newSpace)) continue;

                // skip if blocked
                //horizontal
                if(!orientationIsHorizontal(d) && isVerticalEdgeBlocked(newSpace[0], newSpace[1])) continue;
                //vertical
                else if (orientationIsHorizontal(d) && isHorizontalEdgeBlocked(newSpace[0], newSpace[1])) continue;

                // add to queue and seen
                if(p.isWinningArea(newSpace)) return true;
                q.enqueue(newSpace);
                seen.add(newSpace);

            }
        }
        return false;
    }

    public boolean orientationIsHorizontal(String orientation){
        String[] horizontal ={"U", "D"};
        return Arrays.asList(horizontal).contains(orientation);
    }

    private HashMap<HorizontalOrVertical,int[][]> getEdgeCoordinatesFromUserInput(int x, int y, String orientation){
        /*
        Helper method that consistently returns what the vertical or horizontal edges that would be blocked
        would be after a user gives x,y, and orientation
         */
        // validate inputs
        if(!QuoridorValidator.isValidOrientation(orientation))
            System.out.println(QuoridorValidator.getInvalidOrientationMessage());

        HashMap<HorizontalOrVertical, int[][]> res = new HashMap<>();
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
            res.put(HorizontalOrVertical.HORIZONTAL, coordinates);
        }
        else{
            res.put(HorizontalOrVertical.VERTICAL, coordinates);
        }
        return res;
    }

    public boolean wallIsInBoundsAndNonOverlapping(int x, int y, String orientation) {
        HashMap<HorizontalOrVertical,int[][]> coordinates = getEdgeCoordinatesFromUserInput(x,y,orientation);

        // - Check overlap with existing walls (Check 3-length wall merge)
        for (int[][] cPair: coordinates.values()){
            for (int[] c: cPair){
                if(coordinates.containsKey(HorizontalOrVertical.HORIZONTAL) && isHorizontalEdgeBlocked(c[0],c[1]))
                    return false;
                if(coordinates.containsKey(HorizontalOrVertical.VERTICAL) && isVerticalEdgeBlocked(c[0],c[1]))
                    return false;
            }
        }
        return true;

//        // - Check path exists for all players (hasPathToGoal)
//        // -place the walls first
//        boolean traversable = true;
//        for (int[][] cPair: coordinates.values()){
//            for (int[] c: cPair){
//                if(coordinates.containsKey(HorizontalOrVertical.HORIZONTAL))
//                    blockHorizontalEdge(c[0], c[1]);
//                if(coordinates.containsKey(HorizontalOrVertical.VERTICAL))
//                    blockVerticalEdge(c[0], c[1]);
//            }
//        }
//        // run from all players starting position
//        for (QuoridorPlayer p: )

        //remove walls

//        return traversable;
    }
    
    public void placeWall(int x, int y, String orientation) {
        // TODO: Implement wall placement
        // - Place 2-unit wall in specified orientation
        // - Call blockHorizontalEdge or blockVerticalEdge
    }
    
    @Override
    public void printCurrentBoard() {
        // TODO: Implement board visualization
        System.out.println("Board visualization not yet implemented");
    }
    
    @Override
    protected boolean isValidDimension(int w, int h) {
        QuoridorValidator validator = new QuoridorValidator();
        return validator.isValidDimensions(w, h);
    }
    
    @Override
    protected String invalidDimensionMessage() {
        return "Quoridor boards must be 9x9.";
    }
}