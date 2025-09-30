import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Random;

// INVARIANT: NO CHARACTERS IN THE SLIDER ARE THE SAME
public class SliderBoard extends Board<PuzzleTile>{
    public int[] missing_tile;
    public static String MISSING_TILE_CONTENT=" ";

    public static Random random = new Random();

    public SliderBoard(int w, int h, int x, int y){
        super(w,h);
        board_arr = new PuzzleTile[h][w];

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
        this(3,3);
    }

    @Override
    protected void populateBoard() {
        List<Integer> range = IntStream.rangeClosed(1, height*width-1)
                .boxed().collect(Collectors.toList());
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                String content;
                if (col == missing_tile[0] && row == missing_tile[1]){
                    content = MISSING_TILE_CONTENT;
                }
                else{
                    int randomIndex = random.nextInt(range.size());
                    int tileContent = range.get(randomIndex);
                    range.remove(randomIndex);
                    content = Integer.toString(tileContent);
                }
                int[] coordinates = {row, col};
                board_arr[row][col] = new PuzzleTile(content);
                positions_map.put(content, coordinates);
            }
        }
        // Add a swap to make it solvable
        if(!isSolvable()) {
            makeSolvable();
        }
    }

    private void makeSolvable(){
        System.out.println("Making solvable..");
        int[] randomCoordinates1;
        int[] randomCoordinates2;
        int index1;
        int index2;
        String valueAtIndex1;
        String valueAtIndex2;
        while(true){
            index1 =random.nextInt(height*width-1)+1;
            index2 =random.nextInt(height*width-1)+1;
            randomCoordinates1 = new int[]{Math.floorDiv(index1, width), index1 % width};
            randomCoordinates2 = new int[]{Math.floorDiv(index2, width), index2 % width};
            valueAtIndex1 = board_arr[randomCoordinates1[0]][randomCoordinates1[1]].getContent();
            valueAtIndex2 = board_arr[randomCoordinates2[0]][randomCoordinates2[1]].getContent();

            // Case #1: they're the same value or one of them is the blank tile
            if(index1 == index2 || valueAtIndex1.equals(MISSING_TILE_CONTENT) || valueAtIndex2.equals(MISSING_TILE_CONTENT)){
                continue;
            }

            // Case #2: index1 < index2 and comes before it
            if(index1 < index2 &&  Integer.parseInt(valueAtIndex1) < Integer.parseInt(valueAtIndex2)){
                System.out.println("Case 2. Swapping");
                swap_tiles(randomCoordinates1, randomCoordinates2);
                break;
            }
            // Case #3: index2 < index1 and comes before it
            if(index2 < index1 &&  Integer.parseInt(valueAtIndex2) < Integer.parseInt(valueAtIndex1)){
                System.out.println("Case 3. Swapping");
                swap_tiles(randomCoordinates2, randomCoordinates1);
                break;
            }
        }
    }
    // Checks that the puzzle is solvable
    private boolean isSolvable(){
        String[] flattenedBoardArr = new String[width*height];
        int index = 0;
        for(int row=0; row < height; row++){
            for(int col=0; col< width; col++){
                if(board_arr[row][col].getContent().equals(MISSING_TILE_CONTENT)){
                    continue;
                }
                flattenedBoardArr[index] = board_arr[row][col].getContent();
                index++;
            }
        }

        // Count inversions
        int inversions = 0;
        for (int i = 0; i < height*width; i++) {
            if (flattenedBoardArr[i] == null){
                break;
            }
            for (int j = i + 1; j < height*width; j++) {
                if (flattenedBoardArr[j] == null){
                    break;
                }
                if (Integer.parseInt(flattenedBoardArr[i]) > Integer.parseInt(flattenedBoardArr[j])) {
                    inversions++;
                }
            }
        }

        int blankRowDistanceFromBottom = height-missing_tile[1];

        // Apply solvability rules
        if (width % 2 != 0) {
//            System.out.println("Odd. inversions:" + inversions + " " + (inversions % 2 == 0));
            return inversions % 2 == 0;
        } else {
//            System.out.println("Even. inversions:" + inversions + " distance from bottom: " + blankRowDistanceFromBottom + " " + ((inversions + blankRowDistanceFromBottom) % 2 == 1));
            return (inversions + blankRowDistanceFromBottom) % 2 == 1;
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

    protected void swap_tiles(int[] tile1_coordinates, int[] tile2_coordinates){
        if (tile1_coordinates.length != tile2_coordinates.length && tile1_coordinates.length != 2){
            throw new IllegalArgumentException("Coordinate lists must be 2 elements long");
        }


        String tile1_content = board_arr[tile1_coordinates[0]][tile1_coordinates[1]].getContent();
        String tile2_content = board_arr[tile2_coordinates[0]][tile2_coordinates[1]].getContent();

        System.out.println("Swapping values. " + tile1_content + "<-->" + tile2_content);

        // Make sure you're not swapping the missing_tile because it has special rules
        if (tile1_content.equals(MISSING_TILE_CONTENT)){
            slide_tile(tile2_content);
        }
        else if (tile2_content.equals(MISSING_TILE_CONTENT)){
            slide_tile(tile1_content);
        }
        else{
            board_arr[tile2_coordinates[0]][tile2_coordinates[1]].setContent(tile1_content);
            board_arr[tile1_coordinates[0]][tile1_coordinates[1]].setContent(tile2_content);
        }

    }

    public void slide_tile(String key_to_swap_with){
        HashMap<String, int[]> valid_swap_tiles = getEligibleSwapCharacters();

        if (!valid_swap_tiles.containsKey(key_to_swap_with)){
            throw new IllegalArgumentException("Not an eligible key to swap with. Must be up, left, right, or down of the empty space in order to swap.");
        }
        int[] to_be_swapped_coordinates = valid_swap_tiles.get(key_to_swap_with);

        // Swap the contents of the tiles
        String temp_content = board_arr[to_be_swapped_coordinates[0]][to_be_swapped_coordinates[1]].getContent();
        board_arr[missing_tile[0]][missing_tile[1]].setContent(temp_content);
        board_arr[to_be_swapped_coordinates[0]][to_be_swapped_coordinates[1]].setContent(MISSING_TILE_CONTENT);

        // Update missing tile to be the coordinates of the new empty tile
        setMissingTile(to_be_swapped_coordinates[0], to_be_swapped_coordinates[1]);
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
