package se.lnu.rm222xi.svgcharts;

/**
 * Converts data values to pixel positions by linear interpolation. A value at domainMin ends up at
 * pixelStart, a value at domainMax at pixelEnd, and everything in between proportionally between
 * them. For a y-axis, pass a pixelStart that is greater than pixelEnd, since SVG counts y downwards.
 */
class LinearScale {

  private final double domainMin;
  private final double domainMax;
  private final double pixelStart;
  private final double pixelEnd;

  /**
   * Creates a scale from the data range to the pixel range.
   *
   * @param domainMin the smallest data value
   * @param domainMax the largest data value
   * @param pixelStart the pixel position for domainMin
   * @param pixelEnd the pixel position for domainMax
   */
  LinearScale(double domainMin, double domainMax, double pixelStart, double pixelEnd) {
    this.domainMin = domainMin;
    this.domainMax = domainMax;
    this.pixelStart = pixelStart;
    this.pixelEnd = pixelEnd;
  }

  /**
   * Returns the pixel position for a data value. If the domain has no width, every value is placed
   * in the middle of the pixel range.
   *
   * @param value the data value
   * @return the pixel position
   */
  double toPixel(double value) {
    double domainWidth = domainMax - domainMin;
    if (domainWidth == 0) {
      return (pixelStart + pixelEnd) / 2;
    }

    double fraction = (value - domainMin) / domainWidth;
    return pixelStart + fraction * (pixelEnd - pixelStart);
  }
}
