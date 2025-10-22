package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.Player;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;

public class QuoridorPlayer extends Player {
    private int wallsLeft;
    private int[] startingCoordinates;

    public QuoridorPlayer(String name, int wallBudget, int[] startingCoordinates){
        super(name);
        validateStartingCoordinates(startingCoordinates);
        validateWallBudget(wallBudget);

        wallsLeft = wallBudget;
        this.startingCoordinates = startingCoordinates;
    }

    private void validateStartingCoordinates(int[] startingCoordinates){
        if(startingCoordinates.length != 2){
            throw new ValueException("Received coordinates not of length 2. Coordinates must be (x,y).");
        }
        int x = startingCoordinates[0];
        int y = startingCoordinates[1];

        QuoridorValidator v = new QuoridorValidator();
        if(!v.isValidPosition(x,y)){
            String m = String.format("Proposed position (%d, %d) is invalid for a quoridor position", x, y);
            throw new ValueException(m);
        }
    }
    private void validateWallBudget(int budget){
        if(budget > 10 || budget < 5){
            throw new ValueException("Wall budget must be between 5 and 10 inclusively.");
        }
    }

    public int getWallsLeft() {
        return wallsLeft;
    }

    public int[] getStartingCoordinates() {
        return startingCoordinates;
    }

    public void decrementWallsLeft() {
        /*
        Use everytime a wall is placed by a player
         */
        this.wallsLeft--;
    }

    public boolean hasWallToPlace(){
        /*
        Quick shortcut to check if a player has a wall to place
         */
        return wallsLeft != 0;
    }

    public boolean isWinningArea(int[] coordinates){
        /*
          Returns whether the current coordinates meet the objective of the player
         */
        ArrayList<Integer> extremes = new ArrayList<>(); // the edges of the board
        extremes.add(0);
        extremes.add(8);

        // top or bottom is starting point
        if (extremes.contains(startingCoordinates[0])){
            return coordinates[0] != startingCoordinates[0] && extremes.contains(coordinates[0]);
        }

        // right or left is starting point
        else{
            return coordinates[1] != startingCoordinates[1] && extremes.contains(coordinates[1]);
        }


    }
}
