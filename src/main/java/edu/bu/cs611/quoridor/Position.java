/* Position.java - Immutable coordinate pair (row, col) with proper equals/hashCode.
 * Used for position tracking and Set/Map operations in pathfinding. */
package edu.bu.cs611.quoridor;

import java.util.Objects;

public class Position {
    int r, c;
    public Position(int r, int c) { this.r = r; this.c = c; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return r == p.r && c == p.c;
    }
    @Override public int hashCode() { return Objects.hash(r, c); }
}
