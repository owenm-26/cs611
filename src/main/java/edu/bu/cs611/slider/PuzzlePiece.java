package edu.bu.cs611.slider;

import edu.bu.cs611.core.Piece;

public class PuzzlePiece extends Piece {

    public String content;

    public PuzzlePiece(String content){
        this.content=content;
    }

    public PuzzlePiece(){
        this("-");
    }
    public String getContent(){
        return this.content;
    }

    public void setContent(String s){
        content = s;
    }

}
