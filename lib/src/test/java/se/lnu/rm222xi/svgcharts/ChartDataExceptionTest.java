package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ChartDataException")
class ChartDataExceptionTest {

  @Test
  @DisplayName("should keep the message it was created with")
  void keepsMessage() {
    ChartDataException exception = new ChartDataException("Value at index 2 is null");

    assertEquals("Value at index 2 is null", exception.getMessage());
  }

  @Test
  @DisplayName("should be an IllegalArgumentException, so callers can catch either")
  void isIllegalArgumentException() {
    assertInstanceOf(IllegalArgumentException.class, new ChartDataException("any"));
  }
}
