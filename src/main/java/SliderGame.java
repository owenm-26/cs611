public class SliderGame extends Game{
    public String player_name;
    public SliderBoard gameboard;


    public static void main(String[] args){
        String name = getPlayerInfo();
        System.out.println("Now we'll figure out the dimensions of the board.");
        int[] dimensions = getDesiredBoardDimensions();
        SliderGame game = new SliderGame(dimensions[0],dimensions[1], name);

    }

    public SliderGame(int width, int height, int missing_x, int missing_y, String playerName){
        super(width, height);
        player_name=playerName;
        gameboard = new SliderBoard(width, height, missing_x, missing_y);
        initializeGame();
    }

    public SliderGame(int width, int height, String playerName){
        this(width, height, width-2, height-2, playerName);
    }

    protected void initializeGame(){
        welcome();
        playGame();
    }

    protected void welcome(){
        System.out.println("--- Welcome to Owen Mariani's Slider Puzzle! ---");
        System.out.println(getHowToQuitMessage() + " (except the next one)\n");
    }

    protected static int[] getDesiredBoardDimensions(){
        System.out.println("First give me your desired width: ");
        String userInputtedWidth = scanner.nextLine();
        System.out.println("Great now give me the desired height: ");
        String userInputtedHeight = scanner.nextLine();
        int width; int height;
        try{
            width = Integer.parseInt(userInputtedWidth);
            height = Integer.parseInt(userInputtedHeight);
            if (!Board.valid_dimension(width, height)) {
                throw new IllegalArgumentException();
            }
            int[] dimensions = {width, height};
            return dimensions;
        }catch (NumberFormatException e){
            System.out.println("Oops, looks like you entered non-integers. Please try again.");
            return getDesiredBoardDimensions();
        }catch (IllegalArgumentException e){
            System.out.println(Board.getInvalidDimensionMessage() + " Please try again.");
            return getDesiredBoardDimensions();
        }

    }

    protected static String getPlayerInfo(){
        System.out.println("What is your name young buck?");
        return scanner.nextLine();
    }

    protected void playGame(){
        boolean has_won = false;
        gameboard.printCurrentBoard();

        while (!has_won){
            executeNextMove();
            gameboard.printCurrentBoard();
            has_won = checkWin();
        }
        endGame();
    }

    protected void executeNextMove(){
        String keyToSwapWith = null;
        String message = String.format("%s, which tile do you want to slide to the empty space? ", player_name);

        // Validate the requested move
        while (true){
            System.out.print(message);
            keyToSwapWith = scanner.next();

            quitIfRequested(keyToSwapWith);

            if (!gameboard.getEligibleSwapCharacters().containsKey(keyToSwapWith)){
                System.out.println(keyToSwapWith + " is an invalid tiles to swap with. Valid tiles must be directly touching the empty tile and must have the key must be in the puzzle.");
            }
            else{
                System.out.println();
                turn_count++;
                break;
            }
        }

        // Execute the swap
        gameboard.slide_tile(keyToSwapWith);

    }

    protected boolean checkWin(){
        int correctCurrentNum = 1;

        // make sure the empty space is in the bottom right corner, else its wrong
        if(gameboard.missing_tile[0] != gameboard.height-1 || gameboard.missing_tile[1] != gameboard.width-1){
            return false;
        }

        for(int row=0; row < board_height; row++){

            for(int col=0; col < board_width; col++){
                if (correctCurrentNum!=gameboard.height * gameboard.width && Integer.parseInt(gameboard.board_arr[row][col].getContent()) != correctCurrentNum){
                    return false;
                }
                correctCurrentNum++;
            }
        }

        return true;
    }

    protected void endGame(){
        String turn = turn_count == 1 ? "turn" : "turns";
        String goodGameMessage = String.format("Good game %s. You finished the puzzle in %d %s!\nYou make a decent slider 🍔", player_name, turn_count, turn);

        // Ask if they want to play again or quit
        System.out.println(goodGameMessage);
        System.out.println("\nEnter the number corresponding to your choice:\n[1] Play again\n[2] Quit");
        String selection;
        int number;
        while(true){
            selection = scanner.next();
            try{
                number = Integer.parseInt(selection);
                if (number < 1 || number > 2){
                    System.out.println("Invalid Option selected. Please try again.");
                }
                else{
                    break;
                }
            }catch (NumberFormatException e){
                System.out.println("Entry must be a number. Please try again.");
            }

        }

        switch(number){
            case 1: {
                playGame();
            }
            case 2:{
                System.out.println("Hope to see you again soon!");
                System.exit(0);
            }
            default:{
                throw new IllegalArgumentException("Something went wrong with your input.");
            }
        }


    }
}
