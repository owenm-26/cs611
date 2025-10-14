package edu.bu.cs611.dotsandboxes;/* DABBoard.java — board for Dots & Boxes: tracks edges/owners, validates moves, renders state. */

import edu.bu.cs611.core.Board;
import edu.bu.cs611.core.Player;
import edu.bu.cs611.core.Tile;

import java.util.HashMap;
import java.util.Map;

public class DABBoard extends Board<ConnectionsPiece> {

    // Horizontal edge between (r,c) and (r,c+1)  -> hOwner[r][c]
    // Vertical edge between (r,c) and (r+1,c)    -> vOwner[r][c]
    private Player[][] hOwner; // [height][width-1]
    private Player[][] vOwner; // [height-1][width]

    final static int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
    final static String[] KEYS = { "U", "D", "L", "R" };

    final static Map<String, int[]> KEYS_TO_DIR = new HashMap<>();

    static {
        KEYS_TO_DIR.put("U", new int[]{-1, 0});
        KEYS_TO_DIR.put("D", new int[]{1, 0});
        KEYS_TO_DIR.put("L", new int[]{0, -1});
        KEYS_TO_DIR.put("R", new int[]{0, 1});
    };

    public DABBoard(int w, int h){
        super(w,h);
        board_arr = (Tile<ConnectionsPiece>[][]) new Tile[h][w];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Tile<ConnectionsPiece> tile = new Tile<>();
                tile.addPiece(new ConnectionsPiece());
                board_arr[r][c] = tile;
            }
        }
        hOwner = new Player[height][width - 1];
        vOwner = new Player[height - 1][width];
    }
    public DABBoard(int d){
        this(d,d);
    }
    public DABBoard(){
        this(4);
    }

    @Override
    protected boolean isValidDimension(int w, int h) {
        return w >= 3 && h >= 3 && w < 10 && h < 10; // 3..9 dots
    }

    @Override
    protected boolean valid_position(int r, int c) {
        return r >= 0 && r < height && c >= 0 && c < width;
    }

    @Override
    protected String invalidDimensionMessage() {
        return "Dots & Boxes requires at least 3×3 dots and at most 9×9.";
    }

    public void printCurrentBoard() {
        final String H_NONE = "   ";         
        StringBuilder sb = new StringBuilder();

        // Horizontal edge
        for (int r = 0; r < height; r++) {
            sb.setLength(0);
            for (int c = 0; c < width; c++) {
                sb.append('*');
                if (c < width - 1) {
                    Player owner = hOwner[r][c];
                    if (owner == null) {
                        sb.append(H_NONE); // no horizontal edge
                    } else {
                        sb.append('-').append(owner.getInitial()).append('-'); 
                    }
                }
            }
            System.out.println(sb);

            // Vertical edges 
            if (r < height - 1) {
                sb.setLength(0);
                for (int c = 0; c < width; c++) {
                    Player owner = vOwner[r][c];
                    sb.append(owner == null ? ' ' : owner.getInitial());

                    if (c < width - 1) sb.append(H_NONE);
                }
                System.out.println(sb);
            }
        }
        System.out.println();
    }

    public HashMap<String, int[]> getValidConnections(int row, int col) {
        HashMap<String, int[]> map = new HashMap<>();

        if (!valid_position(row, col)) return map;
        ConnectionsPiece a = board_arr[row][col].getPiecesOnTile().get(0);
        if (a == null) return map;

        for (int i = 0; i < 4; i++) {
            int nr = row + DIRS[i][0];
            int nc = col + DIRS[i][1];
            if (!valid_position(nr, nc)) continue;

            ConnectionsPiece b = board_arr[nr][nc].getPiecesOnTile().get(0);
            if (b == null) continue;

            // not connected, thus a valid option
            if (!ConnectionsPiece.piecesAreConnected(a,b)) {
                map.put(KEYS[i], new int[]{nr, nc});
            }
        }
        return map;
    }

    public int makeConnection(int r1, int c1, int r2, int c2, Player owner) {
        if (!valid_position(r1, c1) || !valid_position(r2, c2))
            throw new IllegalArgumentException("Out of bounds.");
        if (Math.abs(r1 - r2) + Math.abs(c1 - c2) != 1) {
            throw new IllegalArgumentException("Dots must be orthogonal neighbors.");
        }

        ConnectionsPiece a = board_arr[r1][c1].getPiecesOnTile().get(0);
        ConnectionsPiece b = board_arr[r2][c2].getPiecesOnTile().get(0);
        if (a == null || b == null) throw new IllegalStateException("Uninitialized dot(s).");
        if (ConnectionsPiece.piecesAreConnected(a,b)) throw new IllegalArgumentException("Edge already exists.");

        // Add undirected connection
        a.addConnection(b);
        b.addConnection(a);

        int completed = 0;

        if (r1 == r2) {
            // Horizontal edge across columns c1<->c2 on row r1
            int cLeft = Math.min(c1, c2);
            hOwner[r1][cLeft] = owner;

            // Boxes above and below this edge
            if (r1 - 1 >= 0        && boxComplete(r1 - 1, cLeft)) completed++;
            if (r1 < height - 1    && boxComplete(r1,     cLeft)) completed++;

        } else {
            // Vertical edge across rows r1<->r2 on column c1
            int rTop = Math.min(r1, r2);
            vOwner[rTop][c1] = owner;

            // Boxes left and right of this edge
            if (c1 - 1 >= 0        && boxComplete(rTop, c1 - 1)) completed++;
            if (c1 < width - 1     && boxComplete(rTop, c1))     completed++;
        }

        return completed;
    }

    /** Is the unit box with top-left dot (br,bc) fully enclosed? */
    private boolean boxComplete(int br, int bc) {
        return hasHEdge(br, bc)           // top
            && hasHEdge(br + 1, bc)       // bottom
            && hasVEdge(br, bc)           // left
            && hasVEdge(br, bc + 1);      // right
    }

    private boolean hasHEdge(int r, int c) {
        // edge between (r,c) and (r,c+1)
        ConnectionsPiece left = board_arr[r][c].getPiecesOnTile().get(0);
        ConnectionsPiece right = board_arr[r][c + 1].getPiecesOnTile().get(0);
        return left != null && right != null && ConnectionsPiece.piecesAreConnected(left,right);
    }

    private boolean hasVEdge(int r, int c) {
        // edge between (r,c) and (r+1,c)
        ConnectionsPiece top = board_arr[r][c].getPiecesOnTile().get(0);
        ConnectionsPiece bottom = board_arr[r + 1][c].getPiecesOnTile().get(0);
        return top != null && bottom != null && ConnectionsPiece.piecesAreConnected(top, bottom);
    }

}
