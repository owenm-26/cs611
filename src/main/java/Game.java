import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Scanner;


public abstract class Game {
    //TODO: Add to this enum everytime you add a new "Board" child
    public enum GameType {
        SLIDER
    }
    public int board_width;
    public int board_height;
    public int turn_count=0;

    public static String RESERVED_QUIT_KEYWORD = "QUIT";
    public static Scanner scanner = new Scanner(System.in);


    public Game(int width, int height){
        board_width = width;
        board_height = height;
    }

    protected String getHowToQuitMessage(){
        return "You can quit anytime by entering 'QUIT' as your input for any question";
    }
    protected void quitIfRequested(String input){

        if (input.equals(RESERVED_QUIT_KEYWORD)){
            System.out.println("User triggered a quit. Closing game.");
            System.exit(0);
        }
    }

    protected abstract void initializeGame();

    protected abstract void welcome();

    protected abstract void getPlayerInfo();

    protected abstract void playGame();

    protected abstract void executeNextMove();

    protected abstract boolean checkWin();

    protected abstract void endGame();


}

