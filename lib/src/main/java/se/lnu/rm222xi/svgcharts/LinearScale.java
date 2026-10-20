package se.lnu.rm222xi.svgcharts;

/**
 * Converts data values to pixel positions by linear interpolation. A value at the start of the
 * domain ends up at the start of the pixel interval, a value at the end of the domain at the end of
 * the pixel interval, and everything in between proportionally between them. For a y-axis, pass a
 * pixel interval whose start is greater than its end, since SVG counts y downwards.
 */
class LinearScale {

  private final Interval domain;
  private final Interval pixels;

  /**
   * Creates a scale from the data interval to the pixel interval.
   *
   * @param domain the data values, from smallest to largest
   * @param pixels the pixel positions for the start and end of the domain
   */
  LinearScale(Interval domain, Interval pixels) {
    this.domain = domain;
    this.pixels = pixels;
  }

  /**
   * Returns the pixel position for a data value. If the domain has no length, every value is placed
   * in the middle of the pixel interval.
   *
   * @param value the data value
   * @return the pixel position
   */
  double toPixel(double value) {
    if (domain.length() == 0) {
      return pixels.middle();
    }

    return pixels.valueAt(domain.fractionOf(value));
  }
}
