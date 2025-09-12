import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Scanner;


public abstract class Game {
    //TODO: Add to this enum everytime you add a new "Board" child
    public enum GameType {
        SLIDER
    }
    public Board gameboard;
    public int board_width;
    public int board_height;
    public static Scanner scanner = new Scanner(System.in);


    public Game(int width, int height){
        board_width = width;
        board_height = height;
    }

    protected abstract void initializeGame();

    protected abstract void welcome();

    protected abstract void getPlayerInfo();

    protected abstract void playGame();

    protected abstract void executeNextMove(int x, int y);

    protected abstract boolean checkWin();

    protected abstract void endGame();

    public String getInvalidGameTypeMessage(){
        String options = Arrays.stream(GameType.values())
                .map(Enum::name) // get the string name of each enum
                .collect(Collectors.joining(", "));

        return String.format("That game does not exist, try one of the existing ones: %s", options);
    }

}

