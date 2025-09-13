import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Random;

// INVARIANT: NO CHARACTERS IN THE SLIDER ARE THE SAME
public class SliderBoard extends Board{
    public int[] missing_tile;

    public static Random random = new Random();

    public SliderBoard(int w, int h, int x, int y){
        super(w,h);

        if (!valid_position(x,y)){
            throw new IllegalArgumentException(getInvalidPositionMessage(x,y));
        }

        missing_tile = new int[2];
        missing_tile[0] = x; missing_tile[1] = y;

        populateBoard();
    }
    public SliderBoard(int w, int h){
        this(w,h,w-1,h-1);
    }
    public SliderBoard(){
        this(2,2);
    }

    @Override
    protected void populateBoard() {
        List<Integer> range = IntStream.rangeClosed(1, height*width-1)
                .boxed().collect(Collectors.toList());
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                String content;
                if (col == missing_tile[0] && row == missing_tile[1]){
                    content = " ";
                }
                else{
                    int randomIndex = random.nextInt(range.size());
                    int tileContent = range.get(randomIndex);
                    range.remove(randomIndex);
                    content = Integer.toString(tileContent);
                }
                int[] coordinates = {col, row};
                board_arr[row][col] = new Position(content);
                positions_map.put(content, coordinates);
            }
        }
    }

    public void setMissingTile(int x, int y){
        if(!valid_position(x,y)){
            throw new IllegalArgumentException(getInvalidPositionMessage(x,y));
        }

        missing_tile[0] = x; missing_tile[1] = y;
    }

    public HashMap<String, int[]> getEligibleSwapCharacters(){
        HashMap<String, int[]> neighbors_keys = new HashMap<String, int[]>();

        int[][] DIRECTIONS = {
                { 1, 0 },  // Right
                { -1, 0 }, // Left
                { 0, 1 },  // Down
                { 0, -1 }  // Up
        };

        for (int i = 0; i < DIRECTIONS.length; i++) {
            int nx = missing_tile[0] + DIRECTIONS[i][0];
            int ny = missing_tile[1] + DIRECTIONS[i][1];

            // Check boundaries
            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                int[] coordinates = new int[2];
                coordinates[0] = nx; coordinates[1] = ny;
                neighbors_keys.put(board_arr[nx][ny].getContent(), coordinates);
            }
        }

        return neighbors_keys;
    }

    public void swap_tiles(String key_to_swap_with){
        HashMap<String, int[]> valid_swap_tiles = getEligibleSwapCharacters();

        if (!valid_swap_tiles.containsKey(key_to_swap_with)){
            throw new IllegalArgumentException("Not an eligible key to swap with. Must be up, left, right, or down of the empty space in order to swap.");
        }
        int[] to_be_swapped_coordinates = valid_swap_tiles.get(key_to_swap_with);

        // Swap the contents of the tiles
        String temp_content = board_arr[to_be_swapped_coordinates[0]][to_be_swapped_coordinates[1]].getContent();
        board_arr[missing_tile[0]][missing_tile[1]].setContent(temp_content);
        board_arr[to_be_swapped_coordinates[0]][to_be_swapped_coordinates[1]].setContent(" ");

        // Update missing tile to be the coordinates of the new empty tile
        setMissingTile(to_be_swapped_coordinates[0], to_be_swapped_coordinates[1]);
    }
}
