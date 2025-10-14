package edu.bu.cs611.slider;

import edu.bu.cs611.core.validator.DimensionValidator;

public class SliderValidator implements DimensionValidator {
    @Override
    public boolean isValidDimensions(int width, int height) {
        return width >= 2 && width <= 10 && height >= 2 && height <= 10;
    }

    @Override
    public String getInvalidDimensionMessage() {
        return "Slider Puzzle boards must be between 2x2 and 10x10.";
    }
}
