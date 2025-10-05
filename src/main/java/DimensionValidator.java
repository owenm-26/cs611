public interface DimensionValidator {
    boolean isValidDimensions(int width, int height);
    String getInvalidDimensionMessage();
}
