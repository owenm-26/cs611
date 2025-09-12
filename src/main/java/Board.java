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

    protected String getInvalidPositionMessage(int x, int y) {
        return String.format("Position must be inside the dimensions of the board, zero-indexed. Dimensions are %d x %d. You gave positions (%d, %d)", this.width, this.height,x,y);
    }

    public void printCurrentBoard(){
        // Build the horizontal delimiting string
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for(int k = 0; k<board_arr.length; k++){
            builder.append("--+");
        }
        String horizontal = builder.toString();

        for(int i=0; i<board_arr.length; i++){
            System.out.println(horizontal); // print a horizontal above
            System.out.print("|"); // The leftmost vertical bar
            for(int j=0; j < board_arr[0].length; j++){
                String current_square = String.format("%s |", board_arr[i][j].getContent());
                System.out.print(current_square);
            }
            System.out.println(); // start new line
        }
        System.out.println(horizontal); // the bottom horizontal

    }

    public String getSquareContent(int x, int y) {
        if (!valid_position(x, y)) {
            throw new IndexOutOfBoundsException(getInvalidPositionMessage(x,y));
        }
        return this.board_arr[x][y].getContent();
    }
}
