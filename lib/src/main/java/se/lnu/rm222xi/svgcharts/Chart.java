package se.lnu.rm222xi.svgcharts;

import java.util.List;

/**
 * Common base for all chart types. Holds the title, labels and values, and checks them as soon as
 * they are set. Use {@link BarChart} or {@link LineChart} to create a chart.
 */
public abstract class Chart {

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
   * Draws the data itself (bars, lines) inside the plot area. Each chart type decides how.
   *
   * @param verticalScale converts a value to its y-coordinate
   * @return an element containing everything the chart type draws
   */
  abstract SvgElement drawData(LinearScale verticalScale);

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
}
