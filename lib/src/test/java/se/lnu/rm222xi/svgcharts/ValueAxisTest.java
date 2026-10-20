package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ValueAxis")
class ValueAxisTest {

  private static final double PRECISION = 1e-9;

  private final PlotArea plotArea = new PlotArea(new ChartOptions());
  private final ValueAxis axis = new ValueAxis(new Interval(0, 100), plotArea);

  private static int countOccurrences(String text, String part) {
    return text.split(part, -1).length - 1;
  }

  @Test
  @DisplayName("should place the lowest tick at the bottom of the plot area")
  void placesLowestTickAtBottom() {
    assertEquals(plotArea.bottom(), axis.scale().toPixel(0), PRECISION);
  }

  @Test
  @DisplayName("should place the highest tick at the top of the plot area")
  void placesHighestTickAtTop() {
    assertEquals(plotArea.top(), axis.scale().toPixel(100), PRECISION);
  }

  @Test
  @DisplayName("should stretch the scale to readable ticks around the data")
  void stretchesScaleToReadableTicks() {
    ValueAxis unevenAxis = new ValueAxis(new Interval(3, 97), plotArea);

    assertEquals(plotArea.top(), unevenAxis.scale().toPixel(100), PRECISION);
  }

  @Test
  @DisplayName("should draw one grid line and one number per tick")
  void drawsOneGridLineAndNumberPerTick() {
    String svg = axis.toSvgElement().toString();

    assertEquals(6, countOccurrences(svg, "<line "));
    assertEquals(6, countOccurrences(svg, "<text "));
  }

  @Test
  @DisplayName("should write tick numbers without trailing zeros")
  void writesTickNumbersWithoutTrailingZeros() {
    String svg = new ValueAxis(new Interval(0, 1), plotArea).toSvgElement().toString();

    assertTrue(svg.contains(">0.2</text>"));
  }
}
