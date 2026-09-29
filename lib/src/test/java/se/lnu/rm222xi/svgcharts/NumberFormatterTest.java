package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NumberFormatter")
class NumberFormatterTest {

  private final NumberFormatter formatter = new NumberFormatter();
  private final Locale originalLocale = Locale.getDefault();

  @AfterEach
  void restoreLocale() {
    Locale.setDefault(originalLocale);
  }

  @Test
  @DisplayName("should write whole numbers without decimals")
  void writesWholeNumbersWithoutDecimals() {
    assertEquals("100", formatter.format(100.0));
  }

  @Test
  @DisplayName("should round to two decimals")
  void roundsToTwoDecimals() {
    assertEquals("12.35", formatter.format(12.3456));
  }

  @Test
  @DisplayName("should remove trailing zeros")
  void removesTrailingZeros() {
    assertEquals("0.5", formatter.format(0.5));
  }

  @Test
  @DisplayName("should write zero and negative zero as 0")
  void writesZeroAsZero() {
    assertEquals("0", formatter.format(0.0));
    assertEquals("0", formatter.format(-0.001));
  }

  @Test
  @DisplayName("should keep the minus sign on negative numbers")
  void keepsMinusSign() {
    assertEquals("-2.5", formatter.format(-2.5));
  }

  @Test
  @DisplayName("should use a dot as decimal separator even with Swedish settings")
  void usesDotWithSwedishLocale() {
    Locale.setDefault(Locale.of("sv", "SE"));

    assertEquals("1.5", formatter.format(1.5));
  }
}
