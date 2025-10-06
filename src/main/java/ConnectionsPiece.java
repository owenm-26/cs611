/* ConnectionsPiece.java — node for Dots & Boxes graph.
 * Tracks up to 4 connections and checks adjacency between pieces. */

 import java.util.HashSet;
public class ConnectionsPiece extends Piece{
    private HashSet<ConnectionsPiece> connectedPieces;

    public ConnectionsPiece(){
        connectedPieces = new HashSet<ConnectionsPiece>();
    }

    public void addConnection(ConnectionsPiece piece){
        if(connectedPieces.size() > 4){
            throw new IllegalStateException("More than 4 connections for the requested piece");
        }
        connectedPieces.add(piece);
    }

    public HashSet<ConnectionsPiece> getConnectedPieces(){
        return connectedPieces;
    }

    public static boolean piecesAreConnected(ConnectionsPiece a, ConnectionsPiece b){
        HashSet<ConnectionsPiece> aConnected = a.getConnectedPieces();
        HashSet<ConnectionsPiece> bConnected = b.getConnectedPieces();
        return aConnected.contains(b) || bConnected.contains(a);
    }
}
