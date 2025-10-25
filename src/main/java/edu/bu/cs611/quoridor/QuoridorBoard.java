package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        return player.getName();
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

    public boolean isEdgeBlocked(int r1, int c1, int r2, int c2) {
        if (r1 == r2) return isVerticalEdgeBlocked(r1, Math.min(c1, c2));
        return isHorizontalEdgeBlocked(Math.min(r1, r2), c1);
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
    
    public HashMap<String, int[]> getValidMoves(QuoridorPlayer currentPlayer) {
        HashMap<String, int[]> validMoves = new HashMap<>();
        int[] pos = getPlayerPosition(currentPlayer);
        if (pos == null) return validMoves;
        
        for (String dir : KEYS) {
            int[] offset = KEYS_TO_DIR.get(dir);
            int newR = pos[0] + offset[0];
            int newC = pos[1] + offset[1];
            
            if (!valid_position(newR, newC) || isEdgeBlocked(pos[0], pos[1], newR, newC)) continue;
            
            ArrayList<QuoridorPiece> pieces = board_arr[newR][newC].getPiecesOnTile();
            
            // case 1: Simple orthogonal move
            if (pieces.isEmpty()) {
                validMoves.put(dir, new int[]{newR, newC});
            } else {
            // case 2: jump over opponent
            // case 3: diagonal jump
                QuoridorPlayer opp = pieces.get(0).getPlayer();
                if (opp != null && !opp.equals(currentPlayer)) {
                    addJumpMoves(validMoves, newR, newC, dir);
                }
            }
        }
        return validMoves;
    }

    private void addJumpMoves(HashMap<String, int[]> moves, int oppR, int oppC, String dir) {
        int[] offset = KEYS_TO_DIR.get(dir);
        int jR = oppR + offset[0];
        int jC = oppC + offset[1];
        
        // Case 2: Straight jump over opponent
        if (valid_position(jR, jC) && !isEdgeBlocked(oppR, oppC, jR, jC) && 
            board_arr[jR][jC].getPiecesOnTile().isEmpty()) {
            moves.put(dir, new int[]{jR, jC});
            return;
        }
        
        // Case 3: Diagonal jump (blocked behind opponent)
        String[] perps = dir.equals("U") || dir.equals("D") ? new String[]{"L", "R"} : new String[]{"U", "D"};
        for (String p : perps) {
            int[] pOff = KEYS_TO_DIR.get(p);
            int dR = oppR + pOff[0];
            int dC = oppC + pOff[1];
            if (valid_position(dR, dC) && !isEdgeBlocked(oppR, oppC, dR, dC) && 
                board_arr[dR][dC].getPiecesOnTile().isEmpty()) {
                
                // Create diagonal key: combine original direction with perpendicular
                String diagonalKey = dir + p;  // e.g., "UL", "UR", "DL", "DR"
                moves.put(diagonalKey, new int[]{dR, dC});
            }
        }
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
        // Column headers (A, B, C, ...)
        System.out.print("   ");
        for (int c = 0; c < width; c++) {
            System.out.print("  " + (char) ('A' + c) + " ");
        }
        System.out.println();

        // Top % border
        System.out.print("   ");
        for (int i = 0; i < width * 4 + 1; i++) {
            System.out.print("%");
        }
        System.out.println();

        for (int r = 0; r < height; r++) {
            // Row number and left border
            System.out.printf("%2d |", r);

            // Print each tile and vertical walls
            for (int c = 0; c < width; c++) {
                ArrayList<QuoridorPiece> pieces = board_arr[r][c].getPiecesOnTile();
                char pieceSymbol = ' ';
                if (!pieces.isEmpty()) {
                    QuoridorPiece p = pieces.get(0);
                    if (p != null && p.getPlayer() != null) {
                        pieceSymbol = p.getPlayer().getInitial();
                    } else {
                        pieceSymbol = 'O';
                    }
                }

                // Tile contents
                System.out.print(" " + pieceSymbol + " ");

                // Vertical edge or right border
                if (c < width - 1) {
                    if (isVerticalEdgeBlocked(r, c)) System.out.print("|");
                    else System.out.print(" ");
                } else {
                    System.out.print("|");
                }
            }
            System.out.println();

            // Horizontal walls or bottom structure
            if (r < height - 1) {
                System.out.print("   +");
                for (int c = 0; c < width - 1; c++) {
                    if (isHorizontalEdgeBlocked(r, c)) System.out.print("---+");
                    else System.out.print("   +");
                }
                System.out.println("   +");
            }
        }

        // Bottom % border
        System.out.print("   ");
        for (int i = 0; i < width * 4 + 1; i++) {
            System.out.print("%");
        }
        System.out.println();

        // Footer coordinate labels
        System.out.print("   ");
        for (int c = 0; c < width; c++) {
            System.out.print("  " + (char) ('A' + c) + " ");
        }
        System.out.println("\n");
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