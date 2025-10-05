public class DABValidator implements DimensionValidator {
    @Override
    public boolean isValidDimensions(int width, int height) {
        return width >= 3 && width <= 9 && height >= 3 && height <= 9;
    }

    @Override
    public String getInvalidDimensionMessage() {
        return "Dots and Boxes boards must be between 3x3 and 9x9.";
    }
}
