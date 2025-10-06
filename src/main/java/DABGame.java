import java.util.Arrays;
import java.util.HashMap;

public class DABGame extends Game{
    public DABBoard gameboard;


    public DABGame(int width, int height, Player[] players){
        super(width, height);
        gameboard = new DABBoard(width, height);
        this.players = players;
        initializeGame(GameType.DOTS_AND_BOXES, gameboard);  // automatically handles all printing & runs executeNextMove
    }

    public void executeNextMove(){
        // ask user for input
        Player p;
        Player otherP;
        if(players[0].getTurn()){
            p = players[0];
            otherP = players[1];
        }
        else{
            p = players[1];
            otherP = players[0];
        }
        int[] coordinates = new int[2];
        HashMap<String, int[]> validConnections;
        while(true){
            try{
                System.out.printf("%s :: Enter the coordinates of the source dot [Ex: %d,%d]: ", p.getName(), gameboard.width-1, gameboard.height-1);
                String[] parts = scanner.nextLine().split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid input format");
                }
                coordinates[0] =Integer.parseInt(parts[0]); coordinates[1] = Integer.parseInt(parts[1]);

                // validate position
                if(!gameboard.valid_position(coordinates[0], coordinates[1])){
                    System.out.println(gameboard.getInvalidPositionMessage(coordinates[0], coordinates[1]));
                    continue;
                }
                // get connection direction
                validConnections = gameboard.getValidConnections(coordinates[0], coordinates[1]);
                if(validConnections.size() == 0){
                    throw new Exception("Node already has all outgoing connections made. No new connections possible.");
                }

                break;
            } catch ( IllegalArgumentException e){
                System.out.println("(-) Invalid Format. Please try again in format 'X,Y'");
            } catch (Exception e){
                System.out.println("(-) " + e.getMessage());
            }
        }

        // convert options into string
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String key: validConnections.keySet()){
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");

        String direction;
        while(true){
            try{
                System.out.printf("%s :: Enter the direction of the connection %s:", p.getName(), sb);
                direction = scanner.next().toUpperCase();
                scanner.nextLine();
                if (!validConnections.containsKey(direction)){
                    throw new IllegalArgumentException("Not a valid direction choice");
                }
                break;
            } catch (IllegalArgumentException e){
                System.out.println("(-) Invalid Input. Please pick one of the specified direction options.");
            }
        }
        // Make connection
        int[] transition = DABBoard.KEYS_TO_DIR.get(direction);
        int[] destinationCoordinates = {coordinates[0] + transition[0], coordinates[1] + transition[1]};
        int squaresCreated = gameboard.makeConnection(coordinates[0], coordinates[1], destinationCoordinates[0], destinationCoordinates[1], p);

        // update boxes & turns
        p.setScore(p.getScore() + squaresCreated);
        if(squaresCreated < 1){
            p.setTurn(false);
            otherP.setTurn(true);
        }
    }

    public boolean checkWin(){
        int completedBoxes = 0;
        for (Player p: this.players){
            completedBoxes += p.getScore();
        }
        // check if the total number of boxes == the total possible number of boxes
        return completedBoxes == (this.board_width-1) * (this.board_height-1);
    }
    public void endGame(){
        int winnerIndex =  players[0].getScore() > players[1].getScore() ? 0 : 1;
        System.out.println("------- Game Over! -------");
        System.out.format("🏆 %s wins!!!\n", players[winnerIndex].getName());
        System.out.format("Final Score:\n%s - %d\n%s - %d\n", players[winnerIndex].getName(), (int)players[winnerIndex].getScore(), players[(winnerIndex+1) % 2].getName(), (int)players[(winnerIndex+1) % 2].getScore());
    }

    // TODO: Refactor this into Game class and have it take params like gameName
    //  and numPlayers and initialize game with an array of Players
    public static void runDAB(){
        System.out.println("\n--- Dots and Boxes ---");
        // Create two Players and get their name
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player[] players = new Player[] {p1, p2};
        Player.promptMultiplePlayersForNames(players);

        System.out.println("Now we'll figure out the dimensions of the board.");
        int[] dims = DABGame.getDesiredBoardDimensions(new DABValidator());
        new DABGame(dims[0], dims[1], players); // SliderGame runs its own lifecycle
        System.out.println();
    }

}
