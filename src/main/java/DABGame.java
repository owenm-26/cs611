public class DABGame extends Game{

    public DABBoard gameboard;

    public DABGame(int width, int height){
        super(width, height);
        gameboard = new DABBoard(width, height);


    }

    public void playGame(){}

    public void executeNextMove(){}

    public boolean checkWin(){return false;}

    public void endGame(){}

}
