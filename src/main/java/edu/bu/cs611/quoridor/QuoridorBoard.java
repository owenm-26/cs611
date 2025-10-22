package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QuoridorBoard extends Board<QuoridorPiece> {
    private EdgePiece[][] horizontalEdges;
    private EdgePiece[][] verticalEdges;
    
    public static final int DEFAULT_SIZE = 9;
    
    public enum HorizontalOrVertical{
        HORIZONTAL,
        VERTICAL
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

    public void setBlockHorizontalEdge(int row, int col, boolean block) {
        if (QuoridorValidator.isValidHorizontalEdge(row, col)) {
            horizontalEdges[row][col].setBlocked(block);
        }
    }

    public void setBlockVerticalEdge(int row, int col, boolean block) {
        if (QuoridorValidator.isValidVerticalEdge(row, col)) {
            verticalEdges[row][col].setBlocked(block);
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

    public static boolean orientationIsHorizontal(String orientation){
        String[] horizontal ={"U", "D"};
        return Arrays.asList(horizontal).contains(orientation);
    }

    public boolean wallIsInBoundsAndNonOverlapping( HashMap<HorizontalOrVertical,int[][]> coordinates) {
//        HashMap<HorizontalOrVertical,int[][]> coordinates = getEdgeCoordinatesFromUserInput(x,y,orientation);

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