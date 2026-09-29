package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
