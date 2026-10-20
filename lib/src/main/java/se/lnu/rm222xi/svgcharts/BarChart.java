package se.lnu.rm222xi.svgcharts;

/**
 * A chart with one vertical bar per value. Positive values grow upwards from the zero line and
 * negative values downwards, so the y-axis always includes zero.
 *
 * <pre>{@code
 * BarChart chart = new BarChart();
 * chart.setTitle("Temperatur i Lund");
 * chart.setLabels(List.of("Mån", "Tis", "Ons"));
 * chart.setValues(List.of(14.0, -2.0, 18.0));
 * String svg = chart.toSvg();
 * }</pre>
 */
public class BarChart extends Chart {

  private static final double BAR_WIDTH_RATIO = 0.8;

  /**
   * Creates a bar chart with default options: 600 × 400 pixels and blue bars.
   */
  public BarChart() {
    this(new ChartOptions());
  }

  /**
   * Creates a bar chart with the given size and color.
   *
   * @param options the size and color settings
   * @throws ChartDataException if options is null
   */
  public BarChart(ChartOptions options) {
    super(options);
  }

  @Override
  double axisMin() {
    return Math.min(0, minValue());
  }

  @Override
  double axisMax() {
    return Math.max(0, maxValue());
  }

  @Override
  double horizontalPositionOf(int index) {
    return getPlotArea().left() + categoryWidth() * (index + 0.5);
  }

  @Override
  SvgElement drawData(LinearScale verticalScale) {
    SvgElement bars = new SvgElement("g").setAttribute("fill", getOptions().getColor());
    for (int i = 0; i < getValues().size(); i++) {
      bars.addChild(createBar(i, verticalScale));
    }

    return bars;
  }

  private SvgElement createBar(int index, LinearScale verticalScale) {
    double centerX = horizontalPositionOf(index);
    double valueY = verticalScale.toPixel(getValues().get(index));
    double zeroLineY = verticalScale.toPixel(0);
    double barWidth = categoryWidth() * BAR_WIDTH_RATIO;

    return new SvgElement("rect")
        .setAttribute("x", centerX - barWidth / 2)
        .setAttribute("y", Math.min(valueY, zeroLineY))
        .setAttribute("width", barWidth)
        .setAttribute("height", Math.abs(zeroLineY - valueY));
  }

  private double categoryWidth() {
    return getPlotArea().width() / getValues().size();
  }
}
