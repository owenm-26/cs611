public class PuzzlePiece extends Piece{

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
