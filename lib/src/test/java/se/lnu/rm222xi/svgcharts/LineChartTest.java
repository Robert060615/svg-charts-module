package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LineChart")
class LineChartTest {

  private final LineChart chart = new LineChart();

  private static int countOccurrences(String text, String part) {
    return text.split(part, -1).length - 1;
  }

  @Test
  @DisplayName("should draw one path that starts with M and continues with L")
  void drawsPathWithMoveThenLines() {
    chart.setValues(List.of(0.0, 5.0, 10.0));

    // Axis 0..10 over y 360..40, points spread from x 50 to x 580.
    String svg = chart.toSvg();

    assertEquals(1, countOccurrences(svg, "<path"));
    assertTrue(svg.contains("d=\"M 50 360 L 315 200 L 580 40\""));
  }

  @Test
  @DisplayName("should draw one circle per value")
  void drawsOneCirclePerValue() {
    chart.setValues(List.of(14.0, 16.0, 12.0, 18.0, 15.0));

    assertEquals(5, countOccurrences(chart.toSvg(), "<circle"));
  }

  @Test
  @DisplayName("should place a single value in the middle")
  void placesSingleValueInMiddle() {
    chart.setValues(List.of(7.0));

    assertTrue(chart.toSvg().contains("<circle cx=\"315\""));
  }

  @Test
  @DisplayName("should let the y-axis start above zero when all values are far above it")
  void doesNotForceZero() {
    chart.setValues(List.of(52.0, 58.0));

    String svg = chart.toSvg();

    assertTrue(svg.contains(">52</text>"));
    assertFalse(svg.contains(">0</text>"));
  }

  @Test
  @DisplayName("should draw a line when all values are equal")
  void drawsLineForEqualValues() {
    chart.setValues(List.of(40.0, 40.0, 40.0));

    assertTrue(chart.toSvg().contains("d=\"M 50 40 L 315 40 L 580 40\""));
  }

  @Test
  @DisplayName("should place labels under the points")
  void placesLabelsUnderPoints() {
    chart.setLabels(List.of("Jan", "Feb"));
    chart.setValues(List.of(1.0, 2.0));

    String svg = chart.toSvg();

    assertTrue(svg.contains("x=\"50\" y=\"380\">Jan</text>"));
    assertTrue(svg.contains("x=\"580\" y=\"380\">Feb</text>"));
  }

  @Test
  @DisplayName("should use the color from the options for both line and points")
  void usesColorFromOptions() {
    ChartOptions options = new ChartOptions();
    options.setColor("#00aa00");
    LineChart greenChart = new LineChart(options);
    greenChart.setValues(List.of(1.0, 2.0));

    String svg = greenChart.toSvg();

    assertTrue(svg.contains("stroke=\"#00aa00\""));
    assertEquals(2, countOccurrences(svg, "fill=\"#00aa00\""));
  }
}
