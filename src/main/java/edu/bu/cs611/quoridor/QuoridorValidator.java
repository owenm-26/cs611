package edu.bu.cs611.quoridor;

import edu.bu.cs611.core.validator.DimensionValidator;

public class QuoridorValidator implements DimensionValidator {
    @Override
    public boolean isValidDimensions(int width, int height) {
        return width >= 3 && width <= 9 && height >= 3 && height <= 9;
    }

    public boolean isValidPosition(int x, int y){
        return x > 0 && y > 0 && x <= 9 && y <= 9;
    }

    @Override
    public String getInvalidDimensionMessage() {
        return "Dots and Boxes boards must be between 3x3 and 9x9.";
    }
}
