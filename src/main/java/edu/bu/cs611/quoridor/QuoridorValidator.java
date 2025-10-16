package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.validator.DimensionValidator;

public class QuoridorValidator implements DimensionValidator {
    @Override
    public boolean isValidDimensions(int width, int height) {
        return width == 9 && height == 9;
    }

    public boolean isValidPosition(int x, int y){
        return x > 0 && y > 0 && x <= 9 && y <= 9;
    }

    @Override
    public String getInvalidDimensionMessage() {
        return "Dots and Boxes boards must be 9x9.";
    }

    public static boolean isValidHorizontalEdge(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 9;
    }

    public static boolean isValidVerticalEdge(int row, int col) {
        return row >= 0 && row < 9 && col >= 0 && col < 8;
    }
}
