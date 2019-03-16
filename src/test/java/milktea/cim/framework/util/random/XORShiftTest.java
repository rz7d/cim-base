package milktea.cim.framework.util.random;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class XORShiftTest {

  @Test
  public void testConstructorNoArgs() {
    var target = new XORShift();
    assertEquals(4, target.seed.length);
  }

  @Test
  public void testConsturctorWithSeeds() {
    // シードあり
    var target = new XORShift(new long[] { 4, 2, 1, 2 });
    assertArrayEquals(new long[] { 4, 2, 1, 2 }, target.seed);
    assertEquals(4, target.seed.length);
  }

  @Test
  public void testConsturctorUnderCapacity() {
    // シードあり (サイズ以下)
    var target = new XORShift(new long[] { 2000, 89, 31 });
    assertArrayEquals(new long[] { 2000, 2000, 2000, 2000 }, target.seed);
    assertEquals(4, target.seed.length);
  }

  @Test
  public void testConstructorOverCapacity() {
    // シードあり (サイズ超過)
    var target = new XORShift(new long[] { 3, 3, 4, 3, 3, 4 });
    assertArrayEquals(new long[] { 3, 3, 4, 3 }, target.seed);
    assertEquals(4, target.seed.length);
  }

}
