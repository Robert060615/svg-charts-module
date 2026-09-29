package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LinearScale")
class LinearScaleTest {

  private static final double PRECISION = 1e-9;

  @Nested
  @DisplayName("with a normal pixel range")
  class NormalRangeTest {

    private final LinearScale scale = new LinearScale(0, 100, 50, 350);

    @Test
    @DisplayName("should place domainMin at pixelStart")
    void placesMinAtStart() {
      assertEquals(50, scale.toPixel(0), PRECISION);
    }

    @Test
    @DisplayName("should place domainMax at pixelEnd")
    void placesMaxAtEnd() {
      assertEquals(350, scale.toPixel(100), PRECISION);
    }

    @Test
    @DisplayName("should place the middle value in the middle")
    void placesMiddleInMiddle() {
      assertEquals(200, scale.toPixel(50), PRECISION);
    }

    @Test
    @DisplayName("should place values outside the domain outside the pixel range")
    void extrapolatesOutsideDomain() {
      assertEquals(20, scale.toPixel(-10), PRECISION);
    }
  }

  @Nested
  @DisplayName("with an inverted pixel range (y-axis)")
  class InvertedRangeTest {

    private final LinearScale scale = new LinearScale(0, 100, 350, 50);

    @Test
    @DisplayName("should place domainMin at the bottom")
    void placesMinAtBottom() {
      assertEquals(350, scale.toPixel(0), PRECISION);
    }

    @Test
    @DisplayName("should place domainMax at the top")
    void placesMaxAtTop() {
      assertEquals(50, scale.toPixel(100), PRECISION);
    }

    @Test
    @DisplayName("should place a larger value higher up, that is at a smaller y")
    void placesLargerValueHigher() {
      assertEquals(275, scale.toPixel(25), PRECISION);
    }
  }

  @Test
  @DisplayName("should work with a negative domain")
  void worksWithNegativeDomain() {
    LinearScale scale = new LinearScale(-100, 0, 0, 200);

    assertEquals(100, scale.toPixel(-50), PRECISION);
  }

  @Test
  @DisplayName("should place every value in the middle when min equals max")
  void placesValueInMiddleWhenMinEqualsMax() {
    LinearScale scale = new LinearScale(40, 40, 0, 200);

    assertEquals(100, scale.toPixel(40), PRECISION);
  }
}
