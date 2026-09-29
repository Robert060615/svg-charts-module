package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Chart")
class ChartTest {

  private final Chart chart = new MinimalChart(new ChartOptions());

  /** The smallest possible chart type, so the shared logic in Chart can be tested on its own. */
  private static final class MinimalChart extends Chart {

    MinimalChart(ChartOptions options) {
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
      return plotLeft() + index * 10;
    }

    @Override
    SvgElement drawData(LinearScale verticalScale) {
      return new SvgElement("g").setAttribute("id", "minimal-data");
    }
  }

  private static int countOccurrences(String text, String part) {
    return text.split(part, -1).length - 1;
  }

  @Test
  @DisplayName("should throw when created with null options")
  void throwsForNullOptions() {
    assertThrows(ChartDataException.class, () -> new MinimalChart(null));
  }

  @Nested
  @DisplayName("setTitle()")
  class SetTitleTest {

    @Test
    @DisplayName("should store the title")
    void storesTitle() {
      chart.setTitle("Temperatur i Lund");

      assertEquals("Temperatur i Lund", chart.getTitle());
    }

    @Test
    @DisplayName("should throw for null")
    void throwsForNull() {
      assertThrows(ChartDataException.class, () -> chart.setTitle(null));
    }
  }

  @Nested
  @DisplayName("setLabels()")
  class SetLabelsTest {

    @Test
    @DisplayName("should store the labels")
    void storesLabels() {
      chart.setLabels(List.of("Mån", "Tis"));

      assertEquals(List.of("Mån", "Tis"), chart.getLabels());
    }

    @Test
    @DisplayName("should throw with the index of a null label")
    void throwsForNullLabel() {
      ChartDataException exception = assertThrows(ChartDataException.class,
          () -> chart.setLabels(Arrays.asList("Mån", null)));

      assertEquals("Label at index 1 is null", exception.getMessage());
    }

    @Test
    @DisplayName("should throw for a null list")
    void throwsForNullList() {
      ChartDataException exception =
          assertThrows(ChartDataException.class, () -> chart.setLabels(null));

      assertEquals("Labels must not be null", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("setValues()")
  class SetValuesTest {

    @Test
    @DisplayName("should store the values")
    void storesValues() {
      chart.setValues(List.of(14.0, -2.0));

      assertEquals(List.of(14.0, -2.0), chart.getValues());
    }

    @Test
    @DisplayName("should throw with the index of a null value")
    void throwsForNullValue() {
      ChartDataException exception = assertThrows(ChartDataException.class,
          () -> chart.setValues(Arrays.asList(14.0, 16.0, null)));

      assertEquals("Value at index 2 is null", exception.getMessage());
    }

    @Test
    @DisplayName("should throw for infinity and NaN")
    void throwsForNonFiniteValues() {
      assertThrows(ChartDataException.class,
          () -> chart.setValues(List.of(1.0, Double.POSITIVE_INFINITY)));
      assertThrows(ChartDataException.class, () -> chart.setValues(List.of(Double.NaN)));
    }

    @Test
    @DisplayName("should not be affected when the caller changes the original list afterwards")
    void keepsOwnCopy() {
      List<Double> original = new ArrayList<>(List.of(1.0, 2.0));
      chart.setValues(original);

      original.add(3.0);

      assertEquals(List.of(1.0, 2.0), chart.getValues());
    }

    @Test
    @DisplayName("should keep the old values when new values are rejected")
    void keepsOldValuesAfterRejection() {
      chart.setValues(List.of(1.0, 2.0));

      assertThrows(ChartDataException.class, () -> chart.setValues(List.of(Double.NaN)));
      assertEquals(List.of(1.0, 2.0), chart.getValues());
    }
  }

  @Nested
  @DisplayName("toSvg()")
  class ToSvgTest {

    @Test
    @DisplayName("should start with an svg element of the chosen size")
    void startsWithSizedSvgElement() {
      ChartOptions options = new ChartOptions();
      options.setWidth(300);
      options.setHeight(200);
      Chart sizedChart = new MinimalChart(options);
      sizedChart.setValues(List.of(1.0));

      String svg = sizedChart.toSvg();

      assertTrue(svg.startsWith("<svg xmlns=\"http://www.w3.org/2000/svg\""));
      assertTrue(svg.contains("width=\"300\" height=\"200\" viewBox=\"0 0 300 200\""));
    }

    @Test
    @DisplayName("should contain the title both as <title> and as visible text")
    void containsTitleTwice() {
      chart.setTitle("Temperatur i Lund");
      chart.setValues(List.of(1.0));

      String svg = chart.toSvg();

      assertTrue(svg.contains("<title>Temperatur i Lund</title>"));
      assertTrue(svg.contains(">Temperatur i Lund</text>"));
    }

    @Test
    @DisplayName("should escape special characters in the title")
    void escapesTitle() {
      chart.setTitle("Salt & <peppar>");
      chart.setValues(List.of(1.0));

      assertTrue(chart.toSvg().contains("<title>Salt &amp; &lt;peppar&gt;</title>"));
    }

    @Test
    @DisplayName("should include what the chart type draws")
    void includesDrawnData() {
      chart.setValues(List.of(1.0));

      assertTrue(chart.toSvg().contains("<g id=\"minimal-data\"/>"));
    }

    @Test
    @DisplayName("should give the same result when called twice")
    void isRepeatable() {
      chart.setValues(List.of(3.0, 7.0));

      assertEquals(chart.toSvg(), chart.toSvg());
    }

    @Test
    @DisplayName("should work without labels")
    void worksWithoutLabels() {
      chart.setValues(List.of(3.0, 7.0));

      assertTrue(chart.toSvg().startsWith("<svg"));
    }

    @Test
    @DisplayName("should throw when no values are set")
    void throwsWithoutValues() {
      ChartDataException exception = assertThrows(ChartDataException.class, chart::toSvg);

      assertEquals("Cannot render chart without values. Call setValues() first.",
          exception.getMessage());
    }

    @Test
    @DisplayName("should throw when the number of labels and values differ")
    void throwsForMismatchedLabels() {
      chart.setLabels(List.of("Mån", "Tis", "Ons", "Tor", "Fre"));
      chart.setValues(List.of(1.0, 2.0, 3.0, 4.0));

      ChartDataException exception = assertThrows(ChartDataException.class, chart::toSvg);

      assertEquals("Expected 5 values to match labels, got 4", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("axes")
  class AxesTest {

    @Test
    @DisplayName("should draw one grid line and one label per tick")
    void drawsGridLineAndLabelPerTick() {
      chart.setValues(List.of(0.0, 87.0));

      String svg = chart.toSvg();

      assertEquals(6, countOccurrences(svg, "<line"));
      assertTrue(svg.contains(">0</text>"));
      assertTrue(svg.contains(">100</text>"));
    }

    @Test
    @DisplayName("should write decimal ticks without floating point noise")
    void writesDecimalTicksCleanly() {
      chart.setValues(List.of(0.0, 1.0));

      String svg = chart.toSvg();

      assertTrue(svg.contains(">0.6</text>"));
      assertFalse(svg.contains("0.6000"));
    }

    @Test
    @DisplayName("should write large ticks without scientific notation")
    void writesLargeTicksInFull() {
      chart.setValues(List.of(0.0, 1_000_000.0));

      assertTrue(chart.toSvg().contains(">1000000</text>"));
    }

    @Test
    @DisplayName("should place the lowest tick at the bottom of the plot area")
    void placesLowestTickAtBottom() {
      chart.setValues(List.of(0.0, 87.0));

      assertTrue(chart.toSvg().contains("y=\"360\" dominant-baseline=\"middle\">0</text>"));
    }

    @Test
    @DisplayName("should write one escaped label per value under the plot area")
    void writesCategoryLabels() {
      chart.setLabels(List.of("Mån", "<&>"));
      chart.setValues(List.of(1.0, 2.0));

      String svg = chart.toSvg();

      assertTrue(svg.contains(">Mån</text>"));
      assertTrue(svg.contains(">&lt;&amp;&gt;</text>"));
    }
  }
}
