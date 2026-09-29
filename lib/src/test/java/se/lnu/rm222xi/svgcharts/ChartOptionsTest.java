package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ChartOptions")
class ChartOptionsTest {

  private final ChartOptions options = new ChartOptions();

  @Test
  @DisplayName("should have width 600, height 400 and color #1f77b4 by default")
  void hasDefaults() {
    assertEquals(600, options.getWidth());
    assertEquals(400, options.getHeight());
    assertEquals("#1f77b4", options.getColor());
  }

  @Nested
  @DisplayName("setWidth() and setHeight()")
  class SizeTest {

    @Test
    @DisplayName("should accept the minimum size 100")
    void acceptsMinimumSize() {
      options.setWidth(100);
      options.setHeight(100);

      assertEquals(100, options.getWidth());
      assertEquals(100, options.getHeight());
    }

    @Test
    @DisplayName("should throw with a clear message when the width is too small")
    void throwsForTooSmallWidth() {
      ChartDataException exception =
          assertThrows(ChartDataException.class, () -> options.setWidth(99));

      assertEquals("Width must be at least 100 pixels, got 99", exception.getMessage());
    }

    @Test
    @DisplayName("should throw with a clear message when the height is negative")
    void throwsForNegativeHeight() {
      ChartDataException exception =
          assertThrows(ChartDataException.class, () -> options.setHeight(-5));

      assertEquals("Height must be at least 100 pixels, got -5", exception.getMessage());
    }

    @Test
    @DisplayName("should keep the old value when a new value is rejected")
    void keepsOldValueAfterRejection() {
      options.setWidth(800);

      assertThrows(ChartDataException.class, () -> options.setWidth(0));
      assertEquals(800, options.getWidth());
    }
  }

  @Nested
  @DisplayName("setColor()")
  class ColorTest {

    @Test
    @DisplayName("should accept a 6-digit hex color")
    void acceptsSixDigits() {
      options.setColor("#ff8800");

      assertEquals("#ff8800", options.getColor());
    }

    @Test
    @DisplayName("should accept a 3-digit hex color with capital letters")
    void acceptsThreeDigitsUpperCase() {
      options.setColor("#F80");

      assertEquals("#F80", options.getColor());
    }

    @Test
    @DisplayName("should throw for a color without #")
    void throwsWithoutHash() {
      assertThrows(ChartDataException.class, () -> options.setColor("ff8800"));
    }

    @Test
    @DisplayName("should throw for a color with the wrong number of digits")
    void throwsForWrongLength() {
      assertThrows(ChartDataException.class, () -> options.setColor("#ff88"));
    }

    @Test
    @DisplayName("should throw for a color with characters that are not hex digits")
    void throwsForNonHexCharacters() {
      assertThrows(ChartDataException.class, () -> options.setColor("#gg0000"));
    }

    @Test
    @DisplayName("should throw for a color name")
    void throwsForColorName() {
      ChartDataException exception =
          assertThrows(ChartDataException.class, () -> options.setColor("red"));

      assertEquals("Color must be a hex color like \"#f00\" or \"#1f77b4\", got \"red\"",
          exception.getMessage());
    }

    @Test
    @DisplayName("should throw for null")
    void throwsForNull() {
      assertThrows(ChartDataException.class, () -> options.setColor(null));
    }
  }
}
