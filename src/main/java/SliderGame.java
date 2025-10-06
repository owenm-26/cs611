public class SliderGame extends Game{
    public SliderBoard gameboard;

    public SliderGame(int width, int height, int missing_x, int missing_y, String playerName){
        super(width, height);
        setPlayers(new Player[]{ new Player(playerName) });
        players[0].setTurn(true);
        gameboard = new SliderBoard(board_width, board_height, missing_x, missing_y);
        initializeGame(GameType.SLIDER, gameboard);
    }

    public SliderGame(int width, int height, String playerName){
        this(width, height, width-2, height-2, playerName);
    }

    protected void executeNextMove(){
        String keyToSwapWith = null;
        String displayName = (players != null && players.length > 0) ? players[0].getName() : "Player";
        String message = String.format("%s, which tile do you want to slide to the empty space? ", displayName);

        // Validate the requested move
        while (true){
            System.out.print(message);
            keyToSwapWith = scanner.next();
            scanner.nextLine();

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
                if (correctCurrentNum!=gameboard.height * gameboard.width && Integer.parseInt(gameboard.board_arr[row][col].getPiecesOnTile().get(0).getContent()) != correctCurrentNum){
                    return false;
                }
                correctCurrentNum++;
            }
        }

        return true;
    }

    protected void endGame(){
        String turn = turn_count == 1 ? "turn" : "turns";
        String displayName = (players != null && players.length > 0) ? players[0].getName() : "Player";
        String goodGameMessage = String.format("Good game %s. You finished the puzzle in %d %s!\nYou make a decent slider 🍔", displayName, turn_count, turn);

        // Ask if they want to play again or quit
        System.out.println(goodGameMessage);
////        Main.main(new String[]{""});
//        System.out.println("\nEnter the number corresponding to your choice:\n[1] Play again\n[2] Quit");
//        String selection;
//        int number;
//        while(true){
//            selection = scanner.next();
//            try{
//                number = Integer.parseInt(selection);
//                if (number < 1 || number > 2){
//                    System.out.println("Invalid Option selected. Please try again.");
//                }
//                else{
//                    break;
//                }
//            }catch (NumberFormatException e){
//                System.out.println("Entry must be a number. Please try again.");
//            }
//
//        }
//
//        switch(number){
//            case 1: {
//                gameboard.populateBoard();
//                playGame(gameboard);
//            }
//            case 2:{
//                System.out.println("Hope to see you again soon!");
//                System.exit(0);
//            }
//            default:{
//                throw new IllegalArgumentException("Something went wrong with your input.");
//            }
//        }

    }

    public static void runSlider() {
        System.out.println("\n--- Sliding Puzzle ---");
        // Create a Player and get their name
        Player p = new Player("Player");
        p.promptForName("What is your name young buck?");

        System.out.println("Now we'll figure out the dimensions of the board.");
        int[] dims = SliderGame.getDesiredBoardDimensions(new SliderValidator());
        new SliderGame(dims[0], dims[1], p.getName()); // SliderGame runs its own lifecycle
        System.out.println();
    }
}
