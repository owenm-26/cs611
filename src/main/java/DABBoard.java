public class DABBoard extends Board<ConnectionsTile>{

    public DABBoard(int w, int h){
        super(w,h); // TODO: If Board's "valid_dimension()" needs to be overridden you'll need to replace this with a call to that logic instead of the superclass
        board_arr = new ConnectionsTile[w][h];
    }
    public DABBoard(int d){
        super(d,d);
    }
    public DABBoard(){
        super(4);
    }
    // TODO: Implement Print Board
    public void printCurrentBoard() {
        System.out.println();
    }

    // TODO: Implement "getValidConnections()" to check if a line can be drawn validly
    //  - inspired by getEligibleSwapCharacters() in SlideBoard

    // TODO: Implement "makeConnection()" which should use getValidConnections()
    //  and should make a two-way connection between the tiles

    // TODO: Override "valid_dimension()"? Are all m x n boards valid? Idk.
    //  if so would also need to override "getInvalidDimensionMessage()"
}
