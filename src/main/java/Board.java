import java.security.InvalidParameterException;
import java.util.HashMap;


public class Board {
    public int height;
    public int width;

    public Position[][] board_arr;
    public HashMap<String, int[]> positions_map;

    public Board(int h, int w){
        if (!(valid_dimension(w,h))){
            throw new InvalidParameterException(getInvalidDimensionMessage());
        }
        height = h;
        width = w;
        board_arr = new Position[w][h];
        positions_map = new HashMap<String, int[]>();
    }

    public Board(int d){
        this(d,d);
    }
    public Board(){
        this(2,2);
    }

    protected boolean valid_dimension(int w, int h){
       return w > 1 &&  h > 1;
    }

    protected boolean valid_position(int x, int y){
        boolean width_valid = x > 0 && x < this.width;
        boolean height_valid = y > 0 && y < this.height;

        return width_valid && height_valid;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    protected String getInvalidDimensionMessage() {
        return "Dimensions must be greater than 1 and within the size of the board";
    }

    protected String getInvalidPositionMessage() {
        return String.format("Position must be inside the dimensions of the board, zero-indexed. Dimensions are %d x %d", this.width, this.height);
    }

    public String getSquareContent(int x, int y) {
        if (!valid_position(x, y)) {
            throw new IndexOutOfBoundsException(getInvalidPositionMessage());
        }
        return this.board_arr[x][y].getContent();
    }
}
