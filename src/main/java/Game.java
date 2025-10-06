import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Scanner;


public abstract class Game {
    //TODO: Add to this enum everytime you add a new "Board" child
    public enum GameType {
        SLIDER,
        DOTS_AND_BOXES
    }

    public int board_width;
    public int board_height;
    public int turn_count=0;

    // Shared players array (1+ players depending on the game)
    protected Player[] players;

    public static String RESERVED_QUIT_KEYWORD = "QUIT";
    public static Scanner scanner = new Scanner(System.in);


    public Game(int width, int height){
        board_width = width;
        board_height = height;
    }

    protected void setPlayers(Player[] players) { this.players = players; }
    protected Player[] getPlayers() { return this.players; }

    protected String getHowToQuitMessage(){
        return "You can quit anytime by entering 'QUIT' as your input for any question";
    }
    protected void quitIfRequested(String input){

        if (input.equals(RESERVED_QUIT_KEYWORD)){
            System.out.println("User triggered a quit. Closing game.");
            System.exit(0);
        }
    }

    protected static int[] getDesiredBoardDimensions(DimensionValidator validator){
        System.out.println("First give me your desired width: ");
        String userInputtedWidth = scanner.nextLine();
        System.out.println("Great now give me the desired height: ");
        String userInputtedHeight = scanner.nextLine();
        int width; int height;
        try{
            width = Integer.parseInt(userInputtedWidth);
            height = Integer.parseInt(userInputtedHeight);
            if (!validator.isValidDimensions(width, height)) {
                throw new IllegalArgumentException();
            }
            int[] dimensions = {width, height};
            return dimensions;
        }catch (NumberFormatException e){
            System.out.println("Oops, looks like you entered non-integers. Please try again.");
            return getDesiredBoardDimensions(validator);
        }catch (IllegalArgumentException e){
            System.out.println(validator.getInvalidDimensionMessage() + " Please try again.");
            return getDesiredBoardDimensions(validator);
        }

    }

    protected void initializeGame(GameType gameType, Board gameboard){
        String gameName = "";
        switch (gameType){
            case SLIDER:
                gameName = "Sliding Puzzle";
                break;
            case DOTS_AND_BOXES:
                gameName = "Dots and Boxes";
                break;
        }
        welcome(gameName);
        playGame(gameboard);
    }

    protected void welcome(String gameName){
        System.out.printf("--- Welcome to the %s! ---", gameName);
        System.out.println(getHowToQuitMessage() + " (except the next one)\n");
    }

    protected void playGame(Board gameboard){
        turn_count = 0; // TODO: Move this to be an aspect of each player
        boolean has_won = false;
        gameboard.printCurrentBoard();

        while (!has_won){
            executeNextMove();
            gameboard.printCurrentBoard();
            has_won = checkWin();
        }
        endGame();
    }

    protected abstract void executeNextMove();

    protected abstract boolean checkWin();

    protected abstract void endGame();


}

