package se.lnu.rm222xi.svgcharts;

import java.util.Collections;
import java.util.List;

/**
 * Common base for all chart types. Holds the title, labels and values, and checks them as soon as
 * they are set. Use {@link BarChart} or {@link LineChart} to create a chart.
 */
public abstract class Chart {

  /** The font used for all text in the chart. */
  static final String FONT_FAMILY = "sans-serif";

  private static final int TITLE_BASELINE = 24;
  private static final int TITLE_FONT_SIZE = 16;
  private static final int CATEGORY_LABEL_OFFSET = 20;

  private final ChartOptions options;
  private final PlotArea plotArea;
  private final LabelStyle labelStyle = new LabelStyle();
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
    this.plotArea = new PlotArea(options);
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
    requireFiniteValues(checkedValues);
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
    ValueAxis valueAxis = createValueAxis();

    return createRoot()
        .addChild(new SvgElement("title").setText(title))
        .addChild(createBackground())
        .addChild(createHeading())
        .addChild(valueAxis.toSvgElement())
        .addChild(createCategoryLabels())
        .addChild(drawData(valueAxis.scale()))
        .toString();
  }

  /**
   * Returns the lowest value the y-axis must show. Each chart type decides, for example whether
   * zero must be included.
   *
   * @return the lowest value on the y-axis
   */
  abstract double axisMin();

  /**
   * Returns the highest value the y-axis must show.
   *
   * @return the highest value on the y-axis
   */
  abstract double axisMax();

  /**
   * Returns the x-coordinate where the value and label at the given index are placed.
   *
   * @param index the position in the list of values, starting at 0
   * @return the x-coordinate in pixels
   */
  abstract double horizontalPositionOf(int index);

  /**
   * Draws the data itself (bars, lines) inside the plot area. Each chart type decides how.
   *
   * @param verticalScale converts a value to its y-coordinate
   * @return an element containing everything the chart type draws
   */
  abstract SvgElement drawData(LinearScale verticalScale);

  /**
   * Returns the smallest of the values. Only call it when there is at least one value.
   *
   * @return the smallest value
   */
  double minValue() {
    return Collections.min(values);
  }

  /**
   * Returns the largest of the values. Only call it when there is at least one value.
   *
   * @return the largest value
   */
  double maxValue() {
    return Collections.max(values);
  }

  /**
   * Returns the y-coordinate of the value at the given index.
   *
   * @param index the position in the list of values, starting at 0
   * @param verticalScale converts a value to its y-coordinate
   * @return the y-coordinate in pixels
   */
  double verticalPositionOf(int index, LinearScale verticalScale) {
    return verticalScale.toPixel(values.get(index));
  }

  /**
   * Returns the size and color settings the chart was created with.
   *
   * @return the options
   */
  ChartOptions getOptions() {
    return options;
  }

  /**
   * Returns the part of the chart inside the margins, where the data is drawn.
   *
   * @return the plot area
   */
  PlotArea getPlotArea() {
    return plotArea;
  }

  /**
   * Returns the values to draw.
   *
   * @return an unmodifiable list, empty if no values are set
   */
  List<Double> getValues() {
    return values;
  }

  /**
   * Checks that neither the list nor anything in it is null, and returns an unmodifiable copy so
   * that later changes to the caller's list do not affect the chart.
   *
   * @param items the list to check
   * @param itemName what one item is called in the error message, for example "Label"
   * @return an unmodifiable copy of the list
   */
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

  private void requireFiniteValues(List<Double> values) {
    for (int i = 0; i < values.size(); i++) {
      if (!Double.isFinite(values.get(i))) {
        throw new ChartDataException(
            "Value at index " + i + " must be a finite number, got " + values.get(i));
      }
    }
  }

  private void requireRenderableData() {
    requireValues();
    requireOneLabelPerValue();
  }

  private void requireValues() {
    if (values.isEmpty()) {
      throw new ChartDataException("Cannot render chart without values. Call setValues() first.");
    }
  }

  private void requireOneLabelPerValue() {
    if (hasLabels() && !hasOneLabelPerValue()) {
      throw new ChartDataException(
          "Expected " + labels.size() + " values to match labels, got " + values.size());
    }
  }

  private boolean hasLabels() {
    return !labels.isEmpty();
  }

  private boolean hasOneLabelPerValue() {
    return labels.size() == values.size();
  }

  private ValueAxis createValueAxis() {
    return new ValueAxis(new Interval(axisMin(), axisMax()), plotArea);
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

  private SvgElement createCategoryLabels() {
    SvgElement labelGroup = labelStyle.createGroup("middle");
    for (int i = 0; i < labels.size(); i++) {
      labelGroup.addChild(createCategoryLabel(i));
    }

    return labelGroup;
  }

  private SvgElement createCategoryLabel(int index) {
    return new SvgElement("text")
        .setAttribute("x", horizontalPositionOf(index))
        .setAttribute("y", plotArea.bottom() + CATEGORY_LABEL_OFFSET)
        .setText(labels.get(index));
  }
}
