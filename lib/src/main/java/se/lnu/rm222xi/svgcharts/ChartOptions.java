package se.lnu.rm222xi.svgcharts;

/**
 * Size and color settings for a chart. Every setter checks its value right away and throws a
 * {@link ChartDataException} if it is invalid, so a chart can never be drawn with broken options.
 *
 * <p>Defaults: width 600, height 400, color {@code "#1f77b4"}.
 */
public class ChartOptions {

  private static final int MINIMUM_SIZE = 100;

  private int width = 600;
  private int height = 400;
  private String color = "#1f77b4";

  /**
   * Sets the width of the whole chart.
   *
   * @param width the width in pixels, at least 100
   * @throws ChartDataException if the width is less than 100
   */
  public void setWidth(int width) {
    this.width = requireMinimumSize("Width", width);
  }

  /**
   * Sets the height of the whole chart.
   *
   * @param height the height in pixels, at least 100
   * @throws ChartDataException if the height is less than 100
   */
  public void setHeight(int height) {
    this.height = requireMinimumSize("Height", height);
  }

  /**
   * Sets the color of the bars or the line.
   *
   * @param color a hex color with 3 or 6 digits, for example {@code "#f00"} or {@code "#1f77b4"}
   * @throws ChartDataException if the color is not a valid hex color
   */
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
    if (size < MINIMUM_SIZE) {
      throw new ChartDataException(
          dimension + " must be at least " + MINIMUM_SIZE + " pixels, got " + size);
    }
    return size;
  }

  private boolean isHexColor(String color) {
    if (color == null || !color.startsWith("#")) {
      return false;
    }
    if (color.length() != 4 && color.length() != 7) {
      return false;
    }
    for (int i = 1; i < color.length(); i++) {
      if (Character.digit(color.charAt(i), 16) == -1) {
        return false;
      }
    }
    return true;
  }
}
