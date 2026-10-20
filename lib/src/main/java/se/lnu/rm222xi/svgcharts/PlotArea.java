package se.lnu.rm222xi.svgcharts;

/**
 * The part of the chart where the data is drawn: everything inside the margins. The margins leave
 * room for the title above, the y-axis numbers to the left and the labels below.
 *
 * <p>The edges are calculated from the options each time they are asked for, so a change to the
 * options after the chart is created is still used when the chart is drawn.
 */
class PlotArea {

  private static final int MARGIN_TOP = 40;
  private static final int MARGIN_RIGHT = 20;
  private static final int MARGIN_BOTTOM = 40;
  private static final int MARGIN_LEFT = 50;

  private final ChartOptions options;

  /**
   * Creates the plot area for a chart with the given size.
   *
   * @param options the size settings of the whole chart
   */
  PlotArea(ChartOptions options) {
    this.options = options;
  }

  /**
   * Returns the x-coordinate of the left edge, where the y-axis is drawn.
   *
   * @return the left edge in pixels
   */
  double left() {
    return MARGIN_LEFT;
  }

  /**
   * Returns the x-coordinate of the right edge.
   *
   * @return the right edge in pixels
   */
  double right() {
    return options.getWidth() - MARGIN_RIGHT;
  }

  /**
   * Returns the y-coordinate of the top edge, just below the title.
   *
   * @return the top edge in pixels
   */
  double top() {
    return MARGIN_TOP;
  }

  /**
   * Returns the y-coordinate of the bottom edge, just above the labels.
   *
   * @return the bottom edge in pixels
   */
  double bottom() {
    return options.getHeight() - MARGIN_BOTTOM;
  }

  /**
   * Returns the distance from the left edge to the right edge.
   *
   * @return the width in pixels
   */
  double width() {
    return right() - left();
  }
}
