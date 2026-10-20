package se.lnu.rm222xi.svgcharts;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates evenly spaced, human-friendly axis ticks such as 0, 20, 40 instead of 0, 21.75, 43.5.
 */
class TickGenerator {

  /**
   * Allowed rounding error when deciding whether a value already lies on a tick, so that for
   * example 0.30000000000000004 counts as 0.3.
   */
  private static final double TOLERANCE = 1e-9;

  private final int targetTickCount;

  /**
   * Creates a generator that aims for roughly the given number of ticks on an axis.
   *
   * @param targetTickCount the preferred number of ticks, at least 2
   * @throws IllegalArgumentException if targetTickCount is less than 2
   */
  TickGenerator(int targetTickCount) {
    if (targetTickCount < 2) {
      throw new IllegalArgumentException(
          "An axis needs at least 2 ticks, got " + targetTickCount);
    }

    this.targetTickCount = targetTickCount;
  }

  /**
   * Returns the ticks for an axis that covers all values from min to max. The first tick is at or
   * below min and the last tick is at or above max. If min equals max, the axis is stretched to
   * zero so that it still has a range to divide.
   *
   * @param min the smallest value on the axis
   * @param max the largest value on the axis, not less than min
   * @return the tick values in increasing order
   * @throws IllegalArgumentException if min is greater than max
   */
  List<Double> generateTicks(double min, double max) {
    requireMinNotGreaterThanMax(min, max);

    if (min == max) {
      return generateTicksForSingleValue(min);
    }
    return generateTicksForRange(min, max);
  }

  /**
   * Returns the distance between two ticks: 1, 2 or 5 times a power of ten, chosen so that the
   * axis gets close to the target number of ticks.
   *
   * @param min the smallest value on the axis
   * @param max the largest value on the axis
   * @return the step between ticks
   */
  double calculateStep(double min, double max) {
    double roughStep = (max - min) / (targetTickCount - 1);
    double magnitude = Math.pow(10, Math.floor(Math.log10(roughStep)));
    double normalizedStep = roughStep / magnitude;
    return roundToNiceNumber(normalizedStep) * magnitude;
  }

  private void requireMinNotGreaterThanMax(double min, double max) {
    if (min > max) {
      throw new IllegalArgumentException(
          "min (" + min + ") must not be greater than max (" + max + ")");
    }
  }

  private List<Double> generateTicksForRange(double min, double max) {
    double step = calculateStep(min, max);
    double axisStart = roundDownToStep(min, step);
    double axisEnd = roundUpToStep(max, step);
    long intervalCount = Math.round((axisEnd - axisStart) / step);

    List<Double> ticks = new ArrayList<>();
    for (int i = 0; i <= intervalCount; i++) {
      ticks.add(roundToStepPrecision(axisStart + i * step, step));
    }

    return ticks;
  }

  private double roundDownToStep(double value, double step) {
    return Math.floor(value / step + TOLERANCE) * step;
  }

  private double roundUpToStep(double value, double step) {
    return Math.ceil(value / step - TOLERANCE) * step;
  }

  /**
   * Returns ticks from zero to the value, since a single value gives no range to divide. If the
   * value is zero, the axis goes from 0 to 1.
   */
  private List<Double> generateTicksForSingleValue(double value) {
    if (value > 0) {
      return generateTicks(0, value);
    }
    if (value < 0) {
      return generateTicks(value, 0);
    }
    return generateTicks(0, 1);
  }

  /**
   * Rounds a step between 1 and 10 to the closest of 1, 2, 5 or 10, the numbers people find easy
   * to count in.
   */
  private double roundToNiceNumber(double normalizedStep) {
    if (normalizedStep < 1.5) {
      return 1;
    }
    if (normalizedStep < 3.5) {
      return 2;
    }
    if (normalizedStep < 7.5) {
      return 5;
    }
    return 10;
  }

  /**
   * Rounds a value to as many decimals as the step has, which removes floating point errors such
   * as 0.30000000000000004 when the step is 0.1.
   */
  private double roundToStepPrecision(double value, double step) {
    double decimalCount = Math.max(0, -Math.floor(Math.log10(step)));
    double scale = Math.pow(10, decimalCount);
    return Math.round(value * scale) / scale;
  }
}
