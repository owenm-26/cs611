public class DABGame extends Game{
    public DABBoard gameboard;

    public DABGame(int width, int height, Player[] players){
        super(width, height);
        gameboard = new DABBoard(width, height);
        initializeGame(GameType.DOTS_AND_BOXES, gameboard);
        // automatically handles all printing & runs executeNextMove
    }

    public void executeNextMove(){}

    public boolean checkWin(){return false;}

    public void endGame(){}

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
        int[] dims = DABGame.getDesiredBoardDimensions();
        new DABGame(dims[0], dims[1], players); // SliderGame runs its own lifecycle
        System.out.println();
    }

}
