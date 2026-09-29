package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BarChart")
class BarChartTest {

  private final BarChart chart = new BarChart();

  private static int countOccurrences(String text, String part) {
    return text.split(part, -1).length - 1;
  }

  @Test
  @DisplayName("should draw one bar per value, besides the background")
  void drawsOneBarPerValue() {
    chart.setValues(List.of(14.0, 16.0, 12.0, 18.0, 15.0));

    int backgroundCount = 1;
    assertEquals(5 + backgroundCount, countOccurrences(chart.toSvg(), "<rect"));
  }

  @Test
  @DisplayName("should draw a positive bar upwards from the zero line")
  void drawsPositiveBarUpwards() {
    chart.setValues(List.of(10.0, -10.0));

    // Axis -10..10 over y 360..40, so the zero line is at y 200.
    assertTrue(chart.toSvg().contains("<rect x=\"76.5\" y=\"40\" width=\"212\" height=\"160\"/>"));
  }

  @Test
  @DisplayName("should draw a negative bar downwards from the zero line")
  void drawsNegativeBarDownwards() {
    chart.setValues(List.of(10.0, -10.0));

    assertTrue(
        chart.toSvg().contains("<rect x=\"341.5\" y=\"200\" width=\"212\" height=\"160\"/>"));
  }

  @Test
  @DisplayName("should draw a zero value as a bar without height")
  void drawsZeroAsFlatBar() {
    chart.setValues(List.of(0.0, 10.0));

    assertTrue(chart.toSvg().contains("y=\"360\" width=\"212\" height=\"0\"/>"));
  }

  @Test
  @DisplayName("should start the y-axis at zero even when all values are far above it")
  void includesZeroForPositiveValues() {
    chart.setValues(List.of(50.0, 60.0));

    assertTrue(chart.toSvg().contains(">0</text>"));
  }

  @Test
  @DisplayName("should end the y-axis at zero when all values are negative")
  void includesZeroForNegativeValues() {
    chart.setValues(List.of(-50.0, -60.0));

    assertTrue(chart.toSvg().contains("y=\"40\" dominant-baseline=\"middle\">0</text>"));
  }

  @Test
  @DisplayName("should place each label under the middle of its bar")
  void placesLabelUnderBar() {
    chart.setLabels(List.of("Mån", "Tis"));
    chart.setValues(List.of(1.0, 2.0));

    assertTrue(chart.toSvg().contains("x=\"182.5\" y=\"380\">Mån</text>"));
  }

  @Test
  @DisplayName("should fill the bars with the color from the options")
  void usesColorFromOptions() {
    ChartOptions options = new ChartOptions();
    options.setColor("#ff0000");
    BarChart redChart = new BarChart(options);
    redChart.setValues(List.of(1.0));

    assertTrue(redChart.toSvg().contains("<g fill=\"#ff0000\">"));
  }
}
