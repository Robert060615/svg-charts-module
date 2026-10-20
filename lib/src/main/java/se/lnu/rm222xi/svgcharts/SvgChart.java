package se.lnu.rm222xi.svgcharts;

import java.util.List;

/**
 * A chart that is drawn as SVG. This is everything you can do with a chart: give it data and get
 * the SVG back. Create one with {@link BarChart} or {@link LineChart}.
 *
 * <p>Every setter checks its argument right away and throws a {@link ChartDataException} if it is
 * invalid, so a broken chart is found where the mistake is made, not when it is drawn.
 *
 * <pre>{@code
 * SvgChart chart = new BarChart();
 * chart.setTitle("Temperatur i Lund");
 * chart.setLabels(List.of("Mån", "Tis", "Ons"));
 * chart.setValues(List.of(14.0, -2.0, 18.0));
 * String svg = chart.toSvg();
 * }</pre>
 *
 * @since 1.1.0
 */
public interface SvgChart {

  /**
   * Sets the heading shown at the top of the chart. It is also used as the accessible name of the
   * SVG, which screen readers read aloud.
   *
   * @param title the heading, may be empty but not null
   * @throws ChartDataException if title is null
   */
  void setTitle(String title);

  /**
   * Sets the labels shown under each value on the x-axis. Labels are optional, but if set there
   * must be exactly one per value when the chart is drawn.
   *
   * @param labels one label per value
   * @throws ChartDataException if the list or any label in it is null
   */
  void setLabels(List<String> labels);

  /**
   * Sets the values to draw.
   *
   * @param values the data values, in the order they are drawn from left to right
   * @throws ChartDataException if the list is null, or a value in it is null, infinite or NaN
   */
  void setValues(List<Double> values);

  /**
   * Draws the chart as a complete SVG document. Calling it does not change the chart, so calling it
   * twice gives the same result.
   *
   * @return the SVG markup, ready to be saved as a .svg file or put inside an HTML page
   * @throws ChartDataException if no values are set, or labels are set but their number differs
   *     from the number of values
   */
  String toSvg();
}
