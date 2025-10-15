package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Piece;

public class EdgePiece extends Piece {
    private boolean isBlocked;

    public EdgePiece(){
        isBlocked = false;
    }

    public boolean getIsBlocked(){
        return this.isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
