import javax.lang.model.type.NullType;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Game {

    public enum GameType {
        SLIDER
    }
    public Board gameboard;
    public int board_width;
    public int board_height;

    public Game(String gameType, int width, int height){
        GameType type = validateGameType(gameType);
        board_width = width;
        board_height = height;

        // convert string input into gametype
        switch (type) {
            case SLIDER:
                this.gameboard = new SliderBoard(width, height);
                break;
            default:
                throw new IllegalArgumentException(getInvalidGameTypeMessage());
        }

        gameboard.printCurrentBoard();
    }

    public GameType validateGameType(String gameType){
        try {
            return GameType.valueOf(gameType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(getInvalidGameTypeMessage());
        }
    }

    public String getInvalidGameTypeMessage(){
        String options = Arrays.stream(GameType.values())
                .map(Enum::name) // get the string name of each enum
                .collect(Collectors.joining(", "));

        return String.format("That game does not exist, try one of the existing ones: %s", options);
    }


    public static void main(String []args) {
        System.out.println("Starting a new game...");
        Game game = new Game("slider", 3,3);
    }

}

