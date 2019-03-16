package milktea.cim.framework.util.random;

import java.util.Arrays;
import java.util.Random;

public class XORShift extends Random {

  private static final long serialVersionUID = -9202076145657714508L;

  long[] seed;

  private double nextGaussian;
  private boolean hasNextGaussian;

  public XORShift() {
    setSeed(System.currentTimeMillis());
    for (long i = System.nanoTime() % 1000; i != 0; --i) {
      next();
    }
  }

  public XORShift(final long... seed) {
    if (seed != null && seed.length > 3) {
      assert seed != null;
      System.arraycopy(seed, 0, this.seed, 0, this.seed.length);
    } else {
      if (seed == null) {
        assert seed == null;
        setSeed(System.currentTimeMillis());
      } else {
        assert seed != null;
        setSeed(seed);
      }
    }
  }

  long[] seed() {
    if (seed == null) {
      seed = new long[4];
    }
    return seed;
  }

  void seed(int index, long value) {
    seed()[index] = value;
  }

  long seed(int index) {
    return seed()[index];
  }

  @Override
  public void setSeed(long seed) {
    Arrays.fill(seed(), seed);
  }

  public XORShift setSeed(long... seed) {
    if (seed.length < 4) {
      setSeed(seed[0]);
      return this;
    }
    System.arraycopy(seed, 0, seed(), 0, seed().length);
    return this;
  }

  public long next() {
    long t = seed(0) ^ (seed(0) << 11);
    seed(0, seed(1));
    seed(1, seed(2));
    seed(2, seed(3));
    seed(3, (seed(3) ^ (seed(3) >> 19)) ^ (t ^ (t >> 8)));
    return seed(3);
  }

  @Override
  public long nextLong() {
    long l = next();
    if (nextBoolean()) {
      l = ~l + 1L;
    }
    return l;
  }

  @Override
  public int nextInt() {
    return (int) next();
  }

  @Override
  public int nextInt(int i) {
    double r = nextDouble();
    r *= i;
    r = Math.floor(r);
    return (int) r;
  }

  @Override
  public float nextFloat() {
    return (float) this.next() / Long.MAX_VALUE;
  }

  @Override
  public double nextDouble() {
    return (double) this.next() / Long.MAX_VALUE;
  }

  private int boolSetOffset = 0;
  private long currentBoolSet;

  @Override
  public boolean nextBoolean() {
    if (boolSetOffset == 63) {
      currentBoolSet = next();
      boolSetOffset = 0;
    }
    boolean b = ((currentBoolSet >> boolSetOffset) & 1L) == 0;
    ++boolSetOffset;
    return b;
  }

  @Override
  public void nextBytes(byte[] b) {
    long tmp = nextLong();
    int count = 0;
    for (int i = 0; i < b.length; i++) {
      if (count == 8) {
        count = 0;
        tmp = nextLong();
      }
      b[i] = (byte) (tmp >>> (count << 3));
      count += 1;
    }
  }

  @Override
  public double nextGaussian() {
    if (hasNextGaussian) {
      hasNextGaussian = false;
      return nextGaussian;
    }
    var x = Math.sqrt(-2D * Math.log(nextDouble()));
    var y = 2D * Math.PI * nextDouble();
    var z = x * Math.sin(y);
    nextGaussian = x * Math.cos(y);
    hasNextGaussian = true;
    return z;
  }
}
