import java.security.InvalidParameterException;
import java.util.HashMap;


public class Board {
    public int height;
    public int width;

    public Position[][] board_arr;
    public HashMap<String, int[]> positions_map;

    public Board(int h, int w){
        if (!(valid_dimension(h) && valid_dimension(w))){
            throw new InvalidParameterException("Dimensions must be greater that 1");
        }
        height = h;
        width = w;
        board_arr = new Position[w][h];
        positions_map = new HashMap<String, int[]>();

//        Populate board_arr and position_map
        int count = 1;
        for(int i=0; i<w; i++){
            for(int j=0; j<h; j++){
                String content;
                // TODO: Don't hardcode the last to be empty
                // Make the last square empty
                if (i==w-1 && j==h-1) {
                    content = " ";
                }
                else{
                    content = Integer.toString(count);
                }

                int[] coordinates = new int[2];
                coordinates[0] = i; coordinates[1] =j;
                board_arr[i][j] = new Position(content);
                positions_map.put(content, coordinates);
                count++;
            }
        }
    }

    public Board(int d){
        this(d,d);
    }
    public Board(){
        this(2,2);
    }

    private static boolean valid_dimension(int d){
        return d > 1;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public String getSquareContent(int x, int y){
        if (this.width >= x || this.height >= y){
            throw new IndexOutOfBoundsException("Tried to Access a position out of bounds");
        }
        return this.board_arr[x][y].getContent();
    }
}
