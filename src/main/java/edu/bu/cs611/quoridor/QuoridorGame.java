package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Game;

public class QuoridorGame extends Game {

    public QuoridorGame(){
        super(QuoridorBoard.DEFAULT_SIZE, QuoridorBoard.DEFAULT_SIZE);
    }
    @Override
    protected void executeNextMove() {
    //TODO:
    }

    @Override
    protected void endGame() {
        //TODO:
    }

    @Override
    protected boolean checkWin() {
        //TODO:
        return false;
    }
}
