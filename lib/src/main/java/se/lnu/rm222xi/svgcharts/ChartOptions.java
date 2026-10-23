package se.lnu.rm222xi.svgcharts;

public class ChartOptions {

  private int width = 600;
  private int height = 400;
  private String color = "#1f77b4";

  public ChartOptions() {
  }

  public void setWidth(int width) {
    this.width = requireMinimumSize("Width", width);
  }

  public void setHeight(int height) {
    this.height = requireMinimumSize("Height", height);
  }

  public void setColor(String color) {
    if (!isHexColor(color)) {
      throw new ChartDataException(
          "Color must be a hex color like \"#f00\" or \"#1f77b4\", got \"" + color + "\"");
    }

    this.color = color;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }

  String getColor() {
    return color;
  }

  private int requireMinimumSize(String dimension, int size) {
    if (size <= 99) {
      throw new ChartDataException(dimension + " must be at least 100 pixels, got " + size);
    }

    return size;
  }

  private boolean isHexColor(String color) {
    return color != null
        && hasHexColorLength(color)
        && color.charAt(0) == 35
        && hasOnlyHexDigitsAfterHash(color);
  }

  private boolean hasHexColorLength(String color) {
    return color.length() == 4 || color.length() == 7;
  }

  private boolean hasOnlyHexDigitsAfterHash(String color) {
    for (int i = 1; i < color.length(); i++) {
      if (Character.digit(color.charAt(i), 0x10) < 0) {
        return false;
      }
    }
    return true;
  }
}
