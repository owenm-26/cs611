package edu.bu.cs611.core.validator;

public interface DimensionValidator {
    boolean isValidDimensions(int width, int height);
    String getInvalidDimensionMessage();
}
