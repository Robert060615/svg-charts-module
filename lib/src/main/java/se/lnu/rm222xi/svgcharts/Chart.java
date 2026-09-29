package se.lnu.rm222xi.svgcharts;

import java.util.Collections;
import java.util.List;

/**
 * Common base for all chart types. Holds the title, labels and values, and checks them as soon as
 * they are set. Use {@link BarChart} or {@link LineChart} to create a chart.
 */
public abstract class Chart {

  static final String FONT_FAMILY = "sans-serif";

  private static final int TARGET_TICK_COUNT = 5;
  private static final int MARGIN_TOP = 40;
  private static final int MARGIN_RIGHT = 20;
  private static final int MARGIN_BOTTOM = 40;
  private static final int MARGIN_LEFT = 50;
  private static final int TITLE_BASELINE = 24;
  private static final int TITLE_FONT_SIZE = 16;

  private final ChartOptions options;
  private String title = "";
  private List<String> labels = List.of();
  private List<Double> values = List.of();

  /**
   * Creates a chart with the given options. Only subclasses in this package can call it.
   *
   * @param options the size and color settings
   * @throws ChartDataException if options is null
   */
  Chart(ChartOptions options) {
    if (options == null) {
      throw new ChartDataException("Options must not be null");
    }
    this.options = options;
  }

  /**
   * Sets the heading shown at the top of the chart. It is also used as the accessible name of the
   * SVG, which screen readers read aloud.
   *
   * @param title the heading, may be empty but not null
   * @throws ChartDataException if title is null
   */
  public void setTitle(String title) {
    if (title == null) {
      throw new ChartDataException("Title must not be null");
    }
    this.title = title;
  }

  /**
   * Sets the labels shown under each value on the x-axis. Labels are optional, but if set there
   * must be exactly one per value when the chart is drawn.
   *
   * @param labels one label per value
   * @throws ChartDataException if the list or any label in it is null
   */
  public void setLabels(List<String> labels) {
    this.labels = copyWithoutNulls(labels, "Label");
  }

  /**
   * Sets the values to draw.
   *
   * @param values the data values, in the order they are drawn from left to right
   * @throws ChartDataException if the list is null, or a value in it is null, infinite or NaN
   */
  public void setValues(List<Double> values) {
    List<Double> checkedValues = copyWithoutNulls(values, "Value");
    for (int i = 0; i < checkedValues.size(); i++) {
      if (!Double.isFinite(checkedValues.get(i))) {
        throw new ChartDataException(
            "Value at index " + i + " must be a finite number, got " + checkedValues.get(i));
      }
    }
    this.values = checkedValues;
  }

  /**
   * Draws the chart as a complete SVG document. Calling it does not change the chart, so calling it
   * twice gives the same result.
   *
   * @return the SVG markup, ready to be saved as a .svg file or put inside an HTML page
   * @throws ChartDataException if no values are set, or labels are set but their number differs
   *     from the number of values
   */
  public String toSvg() {
    requireRenderableData();
    List<Double> ticks = new TickGenerator(TARGET_TICK_COUNT).generateTicks(axisMin(), axisMax());
    LinearScale verticalScale =
        new LinearScale(ticks.getFirst(), ticks.getLast(), plotBottom(), plotTop());

    return createRoot()
        .addChild(new SvgElement("title").setText(title))
        .addChild(createBackground())
        .addChild(createHeading())
        .addChild(drawData(verticalScale))
        .toString();
  }

  /**
   * Returns the lowest value the y-axis must show. Each chart type decides, for example whether
   * zero must be included.
   */
  abstract double axisMin();

  /**
   * Returns the highest value the y-axis must show.
   */
  abstract double axisMax();

  /**
   * Draws the data itself (bars, lines) inside the plot area. Each chart type decides how.
   *
   * @param verticalScale converts a value to its y-coordinate
   * @return an element containing everything the chart type draws
   */
  abstract SvgElement drawData(LinearScale verticalScale);

  double minValue() {
    return Collections.min(values);
  }

  double maxValue() {
    return Collections.max(values);
  }

  double plotLeft() {
    return MARGIN_LEFT;
  }

  double plotRight() {
    return options.getWidth() - MARGIN_RIGHT;
  }

  double plotTop() {
    return MARGIN_TOP;
  }

  double plotBottom() {
    return options.getHeight() - MARGIN_BOTTOM;
  }

  ChartOptions getOptions() {
    return options;
  }

  String getTitle() {
    return title;
  }

  List<String> getLabels() {
    return labels;
  }

  List<Double> getValues() {
    return values;
  }

  private <T> List<T> copyWithoutNulls(List<T> items, String itemName) {
    if (items == null) {
      throw new ChartDataException(itemName + "s must not be null");
    }
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) == null) {
        throw new ChartDataException(itemName + " at index " + i + " is null");
      }
    }
    return List.copyOf(items);
  }

  private void requireRenderableData() {
    if (values.isEmpty()) {
      throw new ChartDataException("Cannot render chart without values. Call setValues() first.");
    }
    if (!labels.isEmpty() && labels.size() != values.size()) {
      throw new ChartDataException(
          "Expected " + labels.size() + " values to match labels, got " + values.size());
    }
  }

  private SvgElement createRoot() {
    int width = options.getWidth();
    int height = options.getHeight();
    return new SvgElement("svg")
        .setAttribute("xmlns", "http://www.w3.org/2000/svg")
        .setAttribute("width", width)
        .setAttribute("height", height)
        .setAttribute("viewBox", "0 0 " + width + " " + height)
        .setAttribute("role", "img");
  }

  private SvgElement createBackground() {
    return new SvgElement("rect")
        .setAttribute("width", options.getWidth())
        .setAttribute("height", options.getHeight())
        .setAttribute("fill", "#ffffff");
  }

  private SvgElement createHeading() {
    return new SvgElement("text")
        .setAttribute("x", options.getWidth() / 2.0)
        .setAttribute("y", TITLE_BASELINE)
        .setAttribute("text-anchor", "middle")
        .setAttribute("font-family", FONT_FAMILY)
        .setAttribute("font-size", TITLE_FONT_SIZE)
        .setText(title);
  }
}
