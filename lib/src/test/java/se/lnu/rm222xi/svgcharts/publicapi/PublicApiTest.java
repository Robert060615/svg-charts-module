package se.lnu.rm222xi.svgcharts.publicapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.lnu.rm222xi.svgcharts.BarChart;
import se.lnu.rm222xi.svgcharts.Chart;
import se.lnu.rm222xi.svgcharts.ChartDataException;
import se.lnu.rm222xi.svgcharts.ChartOptions;
import se.lnu.rm222xi.svgcharts.LineChart;

/**
 * Tests that apply to every chart type. The class lies in its own package, so it can only use the
 * public interface, exactly like a programmer who has installed the library.
 */
@DisplayName("Public API, for every chart type")
class PublicApiTest {

  private static final Pattern DECIMAL_COMMA = Pattern.compile("\\d,\\d");

  private final Locale originalLocale = Locale.getDefault();

  @AfterEach
  void restoreLocale() {
    Locale.setDefault(originalLocale);
  }

  private List<Chart> createChartOfEveryType(ChartOptions options) {
    return List.of(new BarChart(options), new LineChart(options));
  }

  private List<Chart> createChartOfEveryTypeWithData() {
    List<Chart> charts = createChartOfEveryType(new ChartOptions());
    for (Chart chart : charts) {
      chart.setTitle("Temperatur i Lund");
      chart.setLabels(List.of("Mån", "Tis", "Ons"));
      chart.setValues(List.of(14.0, -2.0, 18.0));
    }
    return charts;
  }

  @Test
  @DisplayName("should start with an <svg> element")
  void startsWithSvgElement() {
    for (Chart chart : createChartOfEveryTypeWithData()) {
      assertTrue(chart.toSvg().startsWith("<svg"), chart.getClass().getSimpleName());
    }
  }

  @Test
  @DisplayName("should contain a <title> for screen readers")
  void containsAccessibleTitle() {
    for (Chart chart : createChartOfEveryTypeWithData()) {
      assertTrue(chart.toSvg().contains("<title>Temperatur i Lund</title>"),
          chart.getClass().getSimpleName());
    }
  }

  @Test
  @DisplayName("should give identical results when toSvg() is called twice")
  void givesSameResultTwice() {
    for (Chart chart : createChartOfEveryTypeWithData()) {
      assertEquals(chart.toSvg(), chart.toSvg(), chart.getClass().getSimpleName());
    }
  }

  @Test
  @DisplayName("should write decimals with a dot even when the computer uses Swedish settings")
  void usesDecimalDotWithSwedishLocale() {
    Locale.setDefault(Locale.of("sv", "SE"));
    ChartOptions options = new ChartOptions();
    options.setWidth(333);

    for (Chart chart : createChartOfEveryType(options)) {
      chart.setValues(List.of(1.25, 2.5, 3.75));
      String svg = chart.toSvg();

      String chartType = chart.getClass().getSimpleName();
      assertTrue(svg.contains("."), chartType + " should contain decimals");
      assertFalse(DECIMAL_COMMA.matcher(svg).find(), chartType + " should not use decimal commas");
    }
  }

  @Test
  @DisplayName("should let the caller catch invalid input as ChartDataException")
  void throwsChartDataExceptionForInvalidInput() {
    for (Chart chart : createChartOfEveryType(new ChartOptions())) {
      assertThrows(ChartDataException.class, chart::toSvg);
      assertThrows(ChartDataException.class, () -> chart.setValues(null));
    }
  }

  @Test
  @DisplayName("should let the caller catch ChartDataException as IllegalArgumentException")
  void canBeCaughtAsIllegalArgumentException() {
    ChartOptions options = new ChartOptions();

    assertThrows(IllegalArgumentException.class, () -> options.setColor("red"));
  }
}
