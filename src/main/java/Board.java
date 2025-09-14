import java.security.InvalidParameterException;
import java.util.HashMap;


public abstract class Board {
    public int height;
    public int width;

    public Position[][] board_arr;
    public HashMap<String, int[]> positions_map;

    public Board(int w, int h){
        if (!(valid_dimension(w,h))){
            throw new InvalidParameterException(getInvalidDimensionMessage());
        }
        height = h;
        width = w;
        board_arr = new Position[h][w];
        positions_map = new HashMap<String, int[]>();
    }

    public Board(int d){
        this(d,d);
    }
    public Board(){
        this(2,2);
    }

    // Abstract Method implemented by each subclass to populate board values
    protected abstract void populateBoard();
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
        return String.format("Position must be inside the dimensions of the board, zero-indexed. Dimensions are %d x %d. You gave positions (%d, %d)", this.width, this.height,x,y);
    }

    public void printCurrentBoard(){
        // find how big each square should
        int cellWidth = Integer.toString((height*width) -1).length();
        StringBuilder topBuilder = new StringBuilder();
        for(int l=0;l<cellWidth+2; l++){
            topBuilder.append("-");
        }
        StringBuilder horizontalBuilder = new StringBuilder();
        horizontalBuilder.append("+");
        for (int c = 0; c < width; c++) {
            for (int i = 0; i < cellWidth + 2; i++) {
                horizontalBuilder.append("-");
            }
            horizontalBuilder.append("+");
        }
        String horizontal = horizontalBuilder.toString();

        for(int row=0; row< height; row++){
            System.out.println(horizontal); // print a horizontal above
            System.out.print("|"); // The leftmost vertical bar
            for(int col=0; col < width; col++){
                String content = board_arr[row][col].getContent();
                String formatted = String.format(" %" + cellWidth + "s ", content);
                System.out.print(formatted + "|");
            }
            System.out.println(); // start new line
        }
        System.out.println(horizontal); // the bottom horizontal

    }
}
