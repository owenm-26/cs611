public class Tile {

    public String content;

    public Tile(String content){
        this.content=content;
    }

    public Tile(){
        this("-");
    }
    public String getContent(){
        return this.content;
    }

    public void setContent(String s){
        content = s;
    }

}
