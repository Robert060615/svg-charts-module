package se.lnu.rm222xi.svgcharts;

/**
 * Calculates evenly spaced, human-friendly axis ticks such as 0, 20, 40 instead of 0, 21.75, 43.5.
 */
class TickGenerator {

  private final int targetTickCount;

  /**
   * Creates a generator that aims for roughly the given number of ticks on an axis.
   *
   * @param targetTickCount the preferred number of ticks, at least 2
   */
  TickGenerator(int targetTickCount) {
    this.targetTickCount = targetTickCount;
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
}
