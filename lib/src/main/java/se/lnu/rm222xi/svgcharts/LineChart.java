package se.lnu.rm222xi.svgcharts;

import java.util.List;

/**
 * A chart with a line through all values and a dot on each value. The y-axis follows the smallest
 * and largest value, so it does not have to start at zero.
 *
 * <pre>{@code
 * LineChart chart = new LineChart();
 * chart.setTitle("Aktiekurs");
 * chart.setLabels(List.of("Jan", "Feb", "Mar"));
 * chart.setValues(List.of(102.5, 98.0, 110.25));
 * String svg = chart.toSvg();
 * }</pre>
 */
public class LineChart extends Chart {

  private static final int LINE_WIDTH = 2;
  private static final int POINT_RADIUS = 4;

  private final NumberFormatter numberFormatter = new NumberFormatter();

  /**
   * Creates a line chart with default options: 600 × 400 pixels and a blue line.
   */
  public LineChart() {
    this(new ChartOptions());
  }

  /**
   * Creates a line chart with the given size and color.
   *
   * @param options the size and color settings
   * @throws ChartDataException if options is null
   */
  public LineChart(ChartOptions options) {
    super(options);
  }

  @Override
  double axisMin() {
    return minValue();
  }

  @Override
  double axisMax() {
    return maxValue();
  }

  @Override
  double horizontalPositionOf(int index) {
    int pointCount = getValues().size();
    if (pointCount == 1) {
      return (plotLeft() + plotRight()) / 2;
    }
    double distanceBetweenPoints = (plotRight() - plotLeft()) / (pointCount - 1);
    return plotLeft() + index * distanceBetweenPoints;
  }

  @Override
  SvgElement drawData(LinearScale verticalScale) {
    SvgElement lineWithPoints = new SvgElement("g").addChild(createLine(verticalScale));
    for (int i = 0; i < getValues().size(); i++) {
      lineWithPoints.addChild(createPoint(i, verticalScale));
    }
    return lineWithPoints;
  }

  private SvgElement createLine(LinearScale verticalScale) {
    StringBuilder pathData = new StringBuilder();
    List<Double> values = getValues();
    for (int i = 0; i < values.size(); i++) {
      pathData.append(i == 0 ? "M " : " L ")
          .append(numberFormatter.format(horizontalPositionOf(i)))
          .append(' ')
          .append(numberFormatter.format(verticalScale.toPixel(values.get(i))));
    }
    return new SvgElement("path")
        .setAttribute("d", pathData.toString())
        .setAttribute("fill", "none")
        .setAttribute("stroke", getOptions().getColor())
        .setAttribute("stroke-width", LINE_WIDTH);
  }

  private SvgElement createPoint(int index, LinearScale verticalScale) {
    return new SvgElement("circle")
        .setAttribute("cx", horizontalPositionOf(index))
        .setAttribute("cy", verticalScale.toPixel(getValues().get(index)))
        .setAttribute("r", POINT_RADIUS)
        .setAttribute("fill", getOptions().getColor());
  }
}
