package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Chart")
class ChartTest {

  private final Chart chart = new MinimalChart(new ChartOptions());

  /** The smallest possible chart type, so the shared logic in Chart can be tested on its own. */
  private static final class MinimalChart extends Chart {

    MinimalChart(ChartOptions options) {
      super(options);
    }

    @Override
    SvgElement drawData(LinearScale verticalScale) {
      return new SvgElement("g");
    }
  }

  @Test
  @DisplayName("should throw when created with null options")
  void throwsForNullOptions() {
    assertThrows(ChartDataException.class, () -> new MinimalChart(null));
  }

  @Nested
  @DisplayName("setTitle()")
  class SetTitleTest {

    @Test
    @DisplayName("should store the title")
    void storesTitle() {
      chart.setTitle("Temperatur i Lund");

      assertEquals("Temperatur i Lund", chart.getTitle());
    }

    @Test
    @DisplayName("should throw for null")
    void throwsForNull() {
      assertThrows(ChartDataException.class, () -> chart.setTitle(null));
    }
  }

  @Nested
  @DisplayName("setLabels()")
  class SetLabelsTest {

    @Test
    @DisplayName("should store the labels")
    void storesLabels() {
      chart.setLabels(List.of("Mån", "Tis"));

      assertEquals(List.of("Mån", "Tis"), chart.getLabels());
    }

    @Test
    @DisplayName("should throw with the index of a null label")
    void throwsForNullLabel() {
      ChartDataException exception = assertThrows(ChartDataException.class,
          () -> chart.setLabels(Arrays.asList("Mån", null)));

      assertEquals("Label at index 1 is null", exception.getMessage());
    }

    @Test
    @DisplayName("should throw for a null list")
    void throwsForNullList() {
      ChartDataException exception =
          assertThrows(ChartDataException.class, () -> chart.setLabels(null));

      assertEquals("Labels must not be null", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("setValues()")
  class SetValuesTest {

    @Test
    @DisplayName("should store the values")
    void storesValues() {
      chart.setValues(List.of(14.0, -2.0));

      assertEquals(List.of(14.0, -2.0), chart.getValues());
    }

    @Test
    @DisplayName("should throw with the index of a null value")
    void throwsForNullValue() {
      ChartDataException exception = assertThrows(ChartDataException.class,
          () -> chart.setValues(Arrays.asList(14.0, 16.0, null)));

      assertEquals("Value at index 2 is null", exception.getMessage());
    }

    @Test
    @DisplayName("should throw for infinity and NaN")
    void throwsForNonFiniteValues() {
      assertThrows(ChartDataException.class,
          () -> chart.setValues(List.of(1.0, Double.POSITIVE_INFINITY)));
      assertThrows(ChartDataException.class, () -> chart.setValues(List.of(Double.NaN)));
    }

    @Test
    @DisplayName("should not be affected when the caller changes the original list afterwards")
    void keepsOwnCopy() {
      List<Double> original = new ArrayList<>(List.of(1.0, 2.0));
      chart.setValues(original);

      original.add(3.0);

      assertEquals(List.of(1.0, 2.0), chart.getValues());
    }

    @Test
    @DisplayName("should keep the old values when new values are rejected")
    void keepsOldValuesAfterRejection() {
      chart.setValues(List.of(1.0, 2.0));

      assertThrows(ChartDataException.class, () -> chart.setValues(List.of(Double.NaN)));
      assertEquals(List.of(1.0, 2.0), chart.getValues());
    }
  }
}
