package se.lnu.rm222xi.svgcharts;

/**
 * A stretch of numbers from a start to an end, for example the data values 0 to 100 or the pixel
 * positions 360 to 40. The end may be smaller than the start, which is how a y-axis in SVG is
 * described, since SVG counts y downwards.
 */
class Interval {

  private final double start;
  private final double end;

  /**
   * Creates an interval between two numbers.
   *
   * @param start where the interval begins
   * @param end where the interval ends, may be smaller than start
   */
  Interval(double start, double end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Returns where the interval begins.
   *
   * @return the start
   */
  double start() {
    return start;
  }

  /**
   * Returns where the interval ends.
   *
   * @return the end
   */
  double end() {
    return end;
  }

  /**
   * Returns the distance from start to end. It is negative if end is smaller than start.
   *
   * @return end minus start
   */
  double length() {
    return end - start;
  }

  /**
   * Returns the number halfway between start and end.
   *
   * @return the middle of the interval
   */
  double middle() {
    return (start + end) / 2;
  }

  /**
   * Returns how far into the interval a number lies, where 0 is the start and 1 is the end.
   *
   * @param value the number to locate, should not be used if the length is 0
   * @return the fraction, below 0 or above 1 if the value lies outside the interval
   */
  double fractionOf(double value) {
    return (value - start) / length();
  }

  /**
   * Returns the number that lies the given fraction into the interval, where 0 is the start and 1
   * is the end.
   *
   * @param fraction how far into the interval
   * @return the number at that fraction
   */
  double valueAt(double fraction) {
    return start + fraction * length();
  }
}
