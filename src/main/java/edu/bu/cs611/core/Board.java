package edu.bu.cs611.core;/* Board.java — generic abstract grid board with validation hooks. */

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;


public abstract class Board<T extends Piece> {
    public int height;
    public int width;

    public Tile<T>[][] board_arr;
    public HashMap<String, int[]> positions_map;

    public static final int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
    public static final String[] KEYS = { "U", "D", "L", "R" };
    public static final Map<String, int[]> KEYS_TO_DIR = new HashMap<>();

    static {
        KEYS_TO_DIR.put("U", new int[]{-1, 0});
        KEYS_TO_DIR.put("D", new int[]{1, 0});
        KEYS_TO_DIR.put("L", new int[]{0, -1});
        KEYS_TO_DIR.put("R", new int[]{0, 1});
    }

    public Board(int w, int h){
        if (!(isValidDimension(w,h))){
            throw new InvalidParameterException(invalidDimensionMessage());
        }
        height = h;
        width = w;
        positions_map = new HashMap<String, int[]>();
    }

    public Board(int d){
        this(d,d);
    }
    public Board(){
        this(2,2);
    }

    public static boolean valid_dimension(int w, int h){
       return w > 1 &&  h > 1 && w < 10 && h < 10;
    }

    protected boolean isValidDimension(int w, int h){
        return valid_dimension(w,h);
    }

    protected String invalidDimensionMessage() {
        return "Dimensions must be greater than 2 and less than 10.";
    }

    protected boolean valid_position(int x, int y){
        boolean width_valid = x >= 0 && x < this.width;
        boolean height_valid = y >= 0 && y < this.height;

        return width_valid && height_valid;
    }

//    public static String getInvalidDimensionMessage() {
//        return "Dimensions must be greater than 2 and less than 10.";
//    }

    public String getInvalidPositionMessage(int x, int y) {
        return String.format("Tile must be inside the dimensions of the board, zero-indexed. Dimensions are %d x %d. You gave positions (%d, %d)", this.width, this.height,x,y);
    }

    public abstract void printCurrentBoard();
}
