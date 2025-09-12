public class Position {

    public static String content;

    public Position(String content){
        this.content=content;
    }

    public Position(){
        this(" ");
    }
    public String getContent(){
        return this.content;
    }

    public void setContent(String s){
        content = s;
    }

}
