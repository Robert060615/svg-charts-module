package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PlotArea")
class PlotAreaTest {

  private static final double PRECISION = 1e-9;

  private final ChartOptions options = new ChartOptions();
  private final PlotArea plotArea = new PlotArea(options);

  @Test
  @DisplayName("should leave room for the y-axis numbers to the left")
  void leavesRoomToTheLeft() {
    assertEquals(50, plotArea.left(), PRECISION);
  }

  @Test
  @DisplayName("should end a margin before the right edge of the chart")
  void endsBeforeRightEdge() {
    assertEquals(580, plotArea.right(), PRECISION);
  }

  @Test
  @DisplayName("should leave room for the title above")
  void leavesRoomForTitle() {
    assertEquals(40, plotArea.top(), PRECISION);
  }

  @Test
  @DisplayName("should leave room for the labels below")
  void leavesRoomForLabels() {
    assertEquals(360, plotArea.bottom(), PRECISION);
  }

  @Test
  @DisplayName("should give the distance between left and right edge as width")
  void givesWidth() {
    assertEquals(530, plotArea.width(), PRECISION);
  }

  @Test
  @DisplayName("should use options that are changed after it was created")
  void followsChangedOptions() {
    options.setWidth(800);
    options.setHeight(300);

    assertEquals(780, plotArea.right(), PRECISION);
    assertEquals(260, plotArea.bottom(), PRECISION);
  }
}
