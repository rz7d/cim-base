package milktea.cim.framework.util.random;

public class XORShift {

  private long[] seed;

  private double nextGaussian;
  private boolean hasNextGaussian;

  public XORShift() {
    long l = System.currentTimeMillis();
    seed = new long[] { l, l, l, l };
    for (long i = System.nanoTime() % 1000; i != 0; i--) {
      next();
    }
  }

  public XORShift(long... seed) {
    setSeed(seed);
  }

  public XORShift setSeed(long seed) {
    this.seed = new long[] { seed, seed, seed, seed };
    return this;
  }

  public XORShift setSeed(long... seed) {
    if (seed.length < 4) {
      return setSeed(seed[0]);
    }
    this.seed = seed;
    return this;
  }

  public long next() {
    long t = seed[0] ^ (seed[0] << 11);
    seed[0] = seed[1];
    seed[1] = seed[2];
    seed[2] = seed[3];
    seed[3] = (seed[3] ^ (seed[3] >> 19)) ^ (t ^ (t >> 8));
    return seed[3];
  }

  public long nextLong() {
    long l = this.next();
    if (this.nextBoolean()) {
      l = ~l + 1L;
    }
    return l;
  }

  public int nextInt() {
    return (int) this.next();
  }

  public int nextInt(int i) {
    double r = this.nextDouble();
    r *= i;
    r = Math.floor(r);
    return (int) r;
  }

  public float nextFloat() {
    return (float) this.next() / Long.MAX_VALUE;
  }

  public double nextDouble() {
    return (double) this.next() / Long.MAX_VALUE;
  }

  private long temp;
  private int cnt = 0;

  public boolean nextBoolean() {
    if (cnt == 63) {
      temp = this.next();
      cnt = 0;
    }
    boolean b = ((temp >> cnt) & 1L) == 0;
    cnt += 1;
    return b;
  }

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

  public double nextGaussian() {
    if (hasNextGaussian) {
      hasNextGaussian = false;
      return nextGaussian;
    }
    double x = Math.sqrt(-2D * Math.log(this.nextDouble()));
    double y = 2D * Math.PI * this.nextDouble();
    double z = x * Math.sin(y);
    nextGaussian = x * Math.cos(y);
    hasNextGaussian = true;
    return z;
  }
}
