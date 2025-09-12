public class SliderGame extends Game{
    public String player_name;
    public SliderBoard gameboard;


    public static void main(String[] args){
        SliderGame game = new SliderGame(3,3);

    }

    public SliderGame(int width, int height, int missing_x, int missing_y){
        super(width, height);
        gameboard = new SliderBoard(width, height, missing_x, missing_y);
        initializeGame();
    }

    public SliderGame(int width, int height){
        this(width, height, width-1, height-1);
    }

    protected void initializeGame(){
        welcome();
        getPlayerInfo();
        playGame();
    }

    protected void welcome(){
        System.out.println("--- Welcome to Owen Mariani's Slider Puzzle! ---");
        System.out.println(getHowToQuitMessage() + " (except the next one)\n");
    }

    protected void getPlayerInfo(){
        System.out.println("What is your name young buck?");
        player_name = scanner.nextLine();
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
        gameboard.swap_tiles(keyToSwapWith);

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
        System.out.println(goodGameMessage);
        System.exit(0);
    }
}
