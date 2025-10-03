import java.util.ArrayList;

public class Tile<T extends Piece> {
    private ArrayList<T> piecesOnTile;

    public Tile(){
        this.piecesOnTile = new ArrayList<T>();
    }

    public void addPiece(T piece){
        this.piecesOnTile.add(piece);
    }

    public void removePiece(T piece){
        this.piecesOnTile.remove(piece);
    }

    public ArrayList<T> getPiecesOnTile(){
        return this.piecesOnTile;
    }

}
