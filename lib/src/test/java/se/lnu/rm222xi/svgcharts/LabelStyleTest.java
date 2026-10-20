package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LabelStyle")
class LabelStyleTest {

  @Test
  @DisplayName("should create a group with the label font and the given alignment")
  void createsGroupWithFontAndAlignment() {
    SvgElement group = new LabelStyle().createGroup("end");

    assertEquals(
        "<g font-family=\"sans-serif\" font-size=\"12\" text-anchor=\"end\"/>",
        group.toString());
  }
}
