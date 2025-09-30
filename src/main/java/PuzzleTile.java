public class PuzzleTile extends Tile{

    public String content;

    public String toString(){
        return this.content;
    }

    public PuzzleTile(String content){
        this.content=content;
    }

    public PuzzleTile(){
        this("-");
    }
    public String getContent(){
        return this.content;
    }

    public void setContent(String s){
        content = s;
    }

}
