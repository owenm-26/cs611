package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Piece;

public class QuoridorPiece extends Piece {
    private QuoridorPlayer player;

    public QuoridorPiece(){
        player = null;
    }

    public void setPlayer(QuoridorPlayer player) {
        this.player = player;
    }

    public QuoridorPlayer getPlayer() {
        return player;
    }
}
