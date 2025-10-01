public class ConnectionsTile extends Tile{

    private Player playerThatOwns;
    private ConnectionsTile[] connectedTiles;
    private int connectionsCount = 0; // acts as the offset for the connectedTiles array

    public ConnectionsTile(){
        this.connectedTiles = new ConnectionsTile[4];
    }

    public void setPlayerThatOwns(Player p){
        this.playerThatOwns = p;
    }

    public Player getPlayerThatOwns(){
        return this.playerThatOwns;
    }

    public int getConnectionsCount(){
        return this.connectionsCount;
    }

    public ConnectionsTile[] getConnectedTiles(){
        return this.connectedTiles;
    }

    public void addConnection(ConnectionsTile t){
        this.connectedTiles[connectionsCount] = t;
        this.connectionsCount++;
    }
    public String toString(){
        return "";
    }
}
