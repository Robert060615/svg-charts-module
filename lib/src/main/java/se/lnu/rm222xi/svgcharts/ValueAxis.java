package se.lnu.rm222xi.svgcharts;

import java.math.BigDecimal;
import java.util.List;

/**
 * The y-axis of a chart: picks readable ticks for the data, converts values to y-coordinates, and
 * draws a grid line and a number for each tick.
 */
class ValueAxis {

  private static final int TARGET_TICK_COUNT = 5;
  private static final int TICK_LABEL_GAP = 8;
  private static final String GRID_COLOR = "#e0e0e0";

  private final PlotArea plotArea;
  private final List<Double> ticks;
  private final LinearScale scale;
  private final LabelStyle labelStyle = new LabelStyle();

  /**
   * Creates a y-axis that covers the given data and fills the height of the plot area.
   *
   * @param dataRange the lowest and highest value the axis must show, lowest first
   * @param plotArea where the axis is drawn
   */
  ValueAxis(Interval dataRange, PlotArea plotArea) {
    this.plotArea = plotArea;
    this.ticks = new TickGenerator(TARGET_TICK_COUNT)
        .generateTicks(dataRange.start(), dataRange.end());
    this.scale = createScale();
  }

  /**
   * Returns the scale that converts a value to its y-coordinate. The lowest tick is at the bottom
   * of the plot area and the highest tick at the top.
   *
   * @return the vertical scale
   */
  LinearScale scale() {
    return scale;
  }

  /**
   * Draws the axis: one horizontal grid line across the plot area and one number to the left of
   * it for each tick.
   *
   * @return a group containing the grid lines and numbers
   */
  SvgElement toSvgElement() {
    SvgElement axis = labelStyle.createGroup("end");
    for (double tick : ticks) {
      double y = scale.toPixel(tick);
      axis.addChild(createGridLine(y));
      axis.addChild(createTickLabel(tick, y));
    }

    return axis;
  }

  private LinearScale createScale() {
    Interval tickInterval = new Interval(ticks.getFirst(), ticks.getLast());
    Interval pixelInterval = new Interval(plotArea.bottom(), plotArea.top());

    return new LinearScale(tickInterval, pixelInterval);
  }

  private SvgElement createGridLine(double y) {
    return new SvgElement("line")
        .setAttribute("x1", plotArea.left())
        .setAttribute("y1", y)
        .setAttribute("x2", plotArea.right())
        .setAttribute("y2", y)
        .setAttribute("stroke", GRID_COLOR);
  }

  private SvgElement createTickLabel(double tick, double y) {
    return new SvgElement("text")
        .setAttribute("x", plotArea.left() - TICK_LABEL_GAP)
        .setAttribute("y", y)
        .setAttribute("dominant-baseline", "middle")
        .setText(formatTick(tick));
  }

  private String formatTick(double tick) {
    return BigDecimal.valueOf(tick).stripTrailingZeros().toPlainString();
  }
}
