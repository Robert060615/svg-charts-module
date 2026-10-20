package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Interval")
class IntervalTest {

  private static final double PRECISION = 1e-9;

  private final Interval interval = new Interval(10, 50);

  @Test
  @DisplayName("should give back its start and end")
  void givesStartAndEnd() {
    assertEquals(10, interval.start(), PRECISION);
    assertEquals(50, interval.end(), PRECISION);
  }

  @Test
  @DisplayName("should give end minus start as length")
  void givesLength() {
    assertEquals(40, interval.length(), PRECISION);
  }

  @Test
  @DisplayName("should give a negative length when end is smaller than start")
  void givesNegativeLengthWhenReversed() {
    assertEquals(-40, new Interval(50, 10).length(), PRECISION);
  }

  @Test
  @DisplayName("should give the number halfway between start and end as middle")
  void givesMiddle() {
    assertEquals(30, interval.middle(), PRECISION);
  }

  @Test
  @DisplayName("should give how far into the interval a value lies")
  void givesFractionOfValue() {
    assertEquals(0.25, interval.fractionOf(20), PRECISION);
  }

  @Test
  @DisplayName("should give the value that lies a fraction into the interval")
  void givesValueAtFraction() {
    assertEquals(20, interval.valueAt(0.25), PRECISION);
  }
}
