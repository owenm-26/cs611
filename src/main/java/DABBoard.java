import java.util.HashMap;

public class DABBoard extends Board<ConnectionsTile>{

    // Horizontal edge between (r,c) and (r,c+1)  -> hOwner[r][c]
    // Vertical edge between (r,c) and (r+1,c)    -> vOwner[r][c]
    private Player[][] hOwner; // [height][width-1]
    private Player[][] vOwner; // [height-1][width]

    public DABBoard(int w, int h){
        super(w,h); 
        
        // DAB-specific minimum: at least 3×3 dots (=> 2×2 boxes)
        if (w < 3 || h < 3) {
            throw new IllegalArgumentException(
                "Dots & Boxes requires at least 3×3 dots. Max is 9×9 dots."
            );
        }
        
        board_arr = new ConnectionsTile[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                board_arr[r][c] = new ConnectionsTile();
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
    // TODO: Implement Print Board
    public void printCurrentBoard() {
        final String H_NONE = "   ";         
        StringBuilder sb = new StringBuilder();

        // Horizontal edge
        for (int r = 0; r < height; r++) {
            sb.setLength(0);
            for (int c = 0; c < width; c++) {
                sb.append('o');
                if (c < width - 1) {
                    Player owner = hOwner[r][c];
                    if (owner == null) {
                        sb.append(H_NONE); // no horizontal edge
                    } else {
                        sb.append('-').append(initial(owner)).append('-'); 
                    }
                }
            }
            System.out.println(sb);

            // Vertical edges 
            if (r < height - 1) {
                sb.setLength(0);
                for (int c = 0; c < width; c++) {
                    Player owner = vOwner[r][c];
                    sb.append(owner == null ? ' ' : initial(owner));

                    if (c < width - 1) sb.append(H_NONE);
                }
                System.out.println(sb);
            }
        }
        System.out.println();
    }
    private char initial(Player p) {
        String name = (p == null || p.getName() == null || p.getName().isEmpty())
            ? "?" : p.getName().trim();
        return Character.toUpperCase(name.charAt(0));
    }


    private boolean inBounds(int r, int c) {
        return r >= 0 && r < height && c >= 0 && c < width;
    }


    // TODO: Implement "getValidConnections()" to check if a line can be drawn validly
    //  - inspired by getEligibleSwapCharacters() in SlideBoard
    public HashMap<String, int[]> getValidConnections(int row, int col) {
        HashMap<String, int[]> map = new HashMap<>();

        if (!inBounds(row, col)) return map;
        ConnectionsTile a = board_arr[row][col];
        if (a == null) return map;

        final int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
        final String[] KEYS = { "U", "D", "L", "R" };

        for (int i = 0; i < 4; i++) {
            int nr = row + DIRS[i][0];
            int nc = col + DIRS[i][1];
            if (!inBounds(nr, nc)) continue;

            ConnectionsTile b = board_arr[nr][nc];
            if (b == null) continue;

            if (!a.areConnected(b)) {
                map.put(KEYS[i], new int[]{nr, nc});
            }
        }
        return map;
    }

    // TODO: Implement "makeConnection()" which should use getValidConnections()
    //  and should make a two-way connection between the tiles

    public int makeConnection(int r1, int c1, int r2, int c2, Player owner) {
        if (!inBounds(r1, c1) || !inBounds(r2, c2))
            throw new IllegalArgumentException("Out of bounds.");
        if (Math.abs(r1 - r2) + Math.abs(c1 - c2) != 1)
            throw new IllegalArgumentException("Dots must be orthogonal neighbors.");

        ConnectionsTile a = board_arr[r1][c1];
        ConnectionsTile b = board_arr[r2][c2];
        if (a == null || b == null) throw new IllegalStateException("Uninitialized dot(s).");
        if (a.areConnected(b)) throw new IllegalArgumentException("Edge already exists.");

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

    private boolean hasHEdge(int r, int c) {
        // edge between (r,c) and (r,c+1)
        ConnectionsTile left = board_arr[r][c];
        ConnectionsTile right = board_arr[r][c + 1];
        return left != null && right != null && left.areConnected(right);
    }

    private boolean hasVEdge(int r, int c) {
        // edge between (r,c) and (r+1,c)
        ConnectionsTile top = board_arr[r][c];
        ConnectionsTile bottom = board_arr[r + 1][c];
        return top != null && bottom != null && top.areConnected(bottom);
    }

    /** Is the unit box with top-left dot (br,bc) fully enclosed? */
    private boolean boxComplete(int br, int bc) {
        return hasHEdge(br, bc)           // top
            && hasHEdge(br + 1, bc)       // bottom
            && hasVEdge(br, bc)           // left
            && hasVEdge(br, bc + 1);      // right
    }


    
}
