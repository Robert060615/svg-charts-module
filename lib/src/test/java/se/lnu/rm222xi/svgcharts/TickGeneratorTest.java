package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TickGenerator")
class TickGeneratorTest {

  private static final double PRECISION = 1e-9;

  private final TickGenerator generator = new TickGenerator(5);

  @Nested
  @DisplayName("calculateStep()")
  class CalculateStepTest {

    @Test
    @DisplayName("should choose 20 for values from 0 to 87")
    void choosesTwentyForZeroToEightySeven() {
      assertEquals(20, generator.calculateStep(0, 87), PRECISION);
    }

    @Test
    @DisplayName("should choose 10 when the rough step is exactly 10")
    void keepsStepThatIsAlreadyNice() {
      assertEquals(10, generator.calculateStep(0, 40), PRECISION);
    }

    @Test
    @DisplayName("should choose 5 when the rough step is between 3.5 and 7.5")
    void choosesFive() {
      assertEquals(5, generator.calculateStep(0, 20), PRECISION);
    }

    @Test
    @DisplayName("should round up to the next power of ten when the rough step is close to it")
    void roundsUpToNextPowerOfTen() {
      assertEquals(100, generator.calculateStep(0, 320), PRECISION);
    }

    @Test
    @DisplayName("should work for axes that cross zero")
    void worksAcrossZero() {
      assertEquals(20, generator.calculateStep(-50, 50), PRECISION);
    }

    @Test
    @DisplayName("should choose decimal steps for small ranges")
    void choosesDecimalStepForSmallRange() {
      assertEquals(0.2, generator.calculateStep(0, 1), PRECISION);
    }

    @Test
    @DisplayName("should choose large steps for large ranges")
    void choosesLargeStepForLargeRange() {
      assertEquals(200_000, generator.calculateStep(0, 1_000_000), PRECISION);
    }
  }

  @Nested
  @DisplayName("generateTicks()")
  class GenerateTicksTest {

    @Test
    @DisplayName("should give ticks 0 to 100 in steps of 20 for values from 0 to 87")
    void givesZeroToHundredForZeroToEightySeven() {
      assertEquals(List.of(0.0, 20.0, 40.0, 60.0, 80.0, 100.0), generator.generateTicks(0, 87));
    }

    @Test
    @DisplayName("should not add extra ticks when min and max are already on a tick")
    void keepsBoundsThatAreOnATick() {
      assertEquals(List.of(0.0, 10.0, 20.0, 30.0, 40.0), generator.generateTicks(0, 40));
    }

    @Test
    @DisplayName("should extend the axis below min when min is not on a tick")
    void extendsAxisBelowMin() {
      assertEquals(List.of(0.0, 5.0, 10.0, 15.0, 20.0), generator.generateTicks(3, 18));
    }

    @Test
    @DisplayName("should include zero on an axis that crosses zero")
    void includesZeroAcrossZero() {
      assertEquals(List.of(-60.0, -40.0, -20.0, 0.0, 20.0, 40.0, 60.0),
          generator.generateTicks(-50, 50));
    }

    @Test
    @DisplayName("should give exact decimal ticks without floating point errors")
    void givesExactDecimalTicks() {
      assertEquals(List.of(0.0, 0.2, 0.4, 0.6, 0.8, 1.0), generator.generateTicks(0, 1));
    }

    @Test
    @DisplayName("should not add an extra tick when min/step is slightly off a whole number")
    void handlesFloatingPointErrorAtBounds() {
      assertEquals(List.of(0.6, 0.8, 1.0, 1.2, 1.4), generator.generateTicks(0.6, 1.4));
    }
  }

  @Nested
  @DisplayName("edge cases")
  class EdgeCaseTest {

    @Test
    @DisplayName("should handle an axis with only negative values")
    void handlesOnlyNegativeValues() {
      assertEquals(List.of(-100.0, -80.0, -60.0, -40.0, -20.0, 0.0),
          generator.generateTicks(-87, -3));
    }

    @Test
    @DisplayName("should stretch the axis down to zero when min equals a positive max")
    void stretchesPositiveSingleValueToZero() {
      assertEquals(List.of(0.0, 10.0, 20.0, 30.0, 40.0), generator.generateTicks(40, 40));
    }

    @Test
    @DisplayName("should stretch the axis up to zero when min equals a negative max")
    void stretchesNegativeSingleValueToZero() {
      assertEquals(List.of(-40.0, -30.0, -20.0, -10.0, 0.0), generator.generateTicks(-40, -40));
    }

    @Test
    @DisplayName("should give an axis from 0 to 1 when all values are zero")
    void givesZeroToOneForOnlyZeros() {
      assertEquals(List.of(0.0, 0.2, 0.4, 0.6, 0.8, 1.0), generator.generateTicks(0, 0));
    }

    @Test
    @DisplayName("should throw when min is greater than max")
    void throwsWhenMinIsGreaterThanMax() {
      assertThrows(IllegalArgumentException.class, () -> generator.generateTicks(10, 5));
    }

    @Test
    @DisplayName("should throw when fewer than 2 ticks are requested")
    void throwsForTooFewTicks() {
      assertThrows(IllegalArgumentException.class, () -> new TickGenerator(1));
    }
  }
}
