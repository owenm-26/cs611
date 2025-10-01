import java.security.InvalidParameterException;
import java.util.HashMap;


public abstract class Board<T extends Tile> {
    public int height;
    public int width;

    public T[][] board_arr;
    public HashMap<String, int[]> positions_map;

    public Board(int w, int h){
        if (!(valid_dimension(w,h))){
            throw new InvalidParameterException(getInvalidDimensionMessage());
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

    protected boolean valid_position(int x, int y){
        boolean width_valid = x >= 0 && x < this.width;
        boolean height_valid = y >= 0 && y < this.height;

        return width_valid && height_valid;
    }

    public static String getInvalidDimensionMessage() {
        return "Dimensions must be greater than 2 and less than 10.";
    }

    protected String getInvalidPositionMessage(int x, int y) {
        return String.format("Tile must be inside the dimensions of the board, zero-indexed. Dimensions are %d x %d. You gave positions (%d, %d)", this.width, this.height,x,y);
    }

    public abstract void printCurrentBoard();
}
