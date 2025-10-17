package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Tile;

import java.util.HashMap;
import java.util.Map;

public class QuoridorBoard extends Board<QuoridorPiece> {
    private EdgePiece[][] horizontalEdges;
    private EdgePiece[][] verticalEdges;
    
    private static final int DEFAULT_SIZE = 9;
    
    final static int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
    final static String[] KEYS = { "U", "D", "L", "R" };
    
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
    
    public boolean hasPathToGoal(int[] currentPos, int[] goalArea) {
        // TODO: Implement BFS/DFS pathfinding
        // - Check if player at currentPos can reach goalArea
        // - Consider walls blocking movement
        return false;
    }
    
    public boolean canPlaceWall(int x, int y, String orientation) {
        // TODO: Implement wall placement validation
        // - Check grid bounds
        // - Check overlap with existing walls
        // - Check 3-length wall merge
        // - Check path exists for all players (hasPathToGoal)
        return false;
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