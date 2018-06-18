package net.minecraft.util.math;

import java.util.Random;
import java.util.UUID;

public class MathHelper {

    public static final float SQRT_2 = sqrt(2.0F);
    private static final float[] SIN_TABLE = new float[65536];
    private static final Random RANDOM = new Random();
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASINE_TAB;
    private static final double[] COS_TAB;

    public static float sin(float f) {
        return MathHelper.SIN_TABLE[(int) (f * 10430.378F) & '\uffff'];
    }

    public static float cos(float f) {
        return MathHelper.SIN_TABLE[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt((double) f);
    }

    public static float sqrt(double d0) {
        return (float) Math.sqrt(d0);
    }

    public static int floor(float f) {
        int i = (int) f;

        return f < (float) i ? i - 1 : i;
    }

    public static int floor(double d0) {
        int i = (int) d0;

        return d0 < (double) i ? i - 1 : i;
    }

    public static long lfloor(double d0) {
        long i = (long) d0;

        return d0 < (double) i ? i - 1L : i;
    }

    public static float abs(float f) {
        return f >= 0.0F ? f : -f;
    }

    public static int abs(int i) {
        return i >= 0 ? i : -i;
    }

    public static int ceil(float f) {
        int i = (int) f;

        return f > (float) i ? i + 1 : i;
    }

    public static int ceil(double d0) {
        int i = (int) d0;

        return d0 > (double) i ? i + 1 : i;
    }

    public static int clamp(int i, int j, int k) {
        return i < j ? j : (i > k ? k : i);
    }

    public static float clamp(float f, float f1, float f2) {
        return f < f1 ? f1 : (f > f2 ? f2 : f);
    }

    public static double clamp(double d0, double d1, double d2) {
        return d0 < d1 ? d1 : (d0 > d2 ? d2 : d0);
    }

    public static double clampedLerp(double d0, double d1, double d2) {
        return d2 < 0.0D ? d0 : (d2 > 1.0D ? d1 : d0 + (d1 - d0) * d2);
    }

    public static double absMax(double d0, double d1) {
        if (d0 < 0.0D) {
            d0 = -d0;
        }

        if (d1 < 0.0D) {
            d1 = -d1;
        }

        return d0 > d1 ? d0 : d1;
    }

    public static int getInt(Random random, int i, int j) {
        return i >= j ? i : random.nextInt(j - i + 1) + i;
    }

    public static float nextFloat(Random random, float f, float f1) {
        return f >= f1 ? f : random.nextFloat() * (f1 - f) + f;
    }

    public static double nextDouble(Random random, double d0, double d1) {
        return d0 >= d1 ? d0 : random.nextDouble() * (d1 - d0) + d0;
    }

    public static double average(long[] along) {
        long i = 0L;
        long[] along1 = along;
        int j = along.length;

        for (int k = 0; k < j; ++k) {
            long l = along1[k];

            i += l;
        }

        return (double) i / (double) along.length;
    }

    public static float wrapDegrees(float f) {
        f %= 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static double wrapDegrees(double d0) {
        d0 %= 360.0D;
        if (d0 >= 180.0D) {
            d0 -= 360.0D;
        }

        if (d0 < -180.0D) {
            d0 += 360.0D;
        }

        return d0;
    }

    public static int wrapDegrees(int i) {
        i %= 360;
        if (i >= 180) {
            i -= 360;
        }

        if (i < -180) {
            i += 360;
        }

        return i;
    }

    public static int getInt(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (Throwable throwable) {
            return i;
        }
    }

    public static int getInt(String s, int i, int j) {
        return Math.max(j, getInt(s, i));
    }

    public static double getDouble(String s, double d0) {
        try {
            return Double.parseDouble(s);
        } catch (Throwable throwable) {
            return d0;
        }
    }

    public static double getDouble(String s, double d0, double d1) {
        return Math.max(d1, getDouble(s, d0));
    }

    public static int smallestEncompassingPowerOfTwo(int i) {
        int j = i - 1;

        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    private static boolean isPowerOfTwo(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    public static int log2DeBruijn(int i) {
        i = isPowerOfTwo(i) ? i : smallestEncompassingPowerOfTwo(i);
        return MathHelper.MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) i * 125613361L >> 27) & 31];
    }

    public static int log2(int i) {
        return log2DeBruijn(i) - (isPowerOfTwo(i) ? 0 : 1);
    }

    public static int roundUp(int i, int j) {
        if (j == 0) {
            return 0;
        } else if (i == 0) {
            return j;
        } else {
            if (i < 0) {
                j *= -1;
            }

            int k = i % j;

            return k == 0 ? i : i + j - k;
        }
    }

    public static long getCoordinateRandom(int i, int j, int k) {
        long l = (long) (i * 3129871) ^ (long) k * 116129781L ^ (long) j;

        l = l * l * 42317861L + l * 11L;
        return l;
    }

    public static UUID getRandomUUID(Random random) {
        long i = random.nextLong() & -61441L | 16384L;
        long j = random.nextLong() & 4611686018427387903L | Long.MIN_VALUE;

        return new UUID(i, j);
    }

    public static UUID getRandomUUID() {
        return getRandomUUID(MathHelper.RANDOM);
    }

    public static double pct(double d0, double d1, double d2) {
        return (d0 - d1) / (d2 - d1);
    }

    public static double atan2(double d0, double d1) {
        double d2 = d1 * d1 + d0 * d0;

        if (Double.isNaN(d2)) {
            return Double.NaN;
        } else {
            boolean flag = d0 < 0.0D;

            if (flag) {
                d0 = -d0;
            }

            boolean flag1 = d1 < 0.0D;

            if (flag1) {
                d1 = -d1;
            }

            boolean flag2 = d0 > d1;
            double d3;

            if (flag2) {
                d3 = d1;
                d1 = d0;
                d0 = d3;
            }

            d3 = fastInvSqrt(d2);
            d1 *= d3;
            d0 *= d3;
            double d4 = MathHelper.FRAC_BIAS + d0;
            int i = (int) Double.doubleToRawLongBits(d4);
            double d5 = MathHelper.ASINE_TAB[i];
            double d6 = MathHelper.COS_TAB[i];
            double d7 = d4 - MathHelper.FRAC_BIAS;
            double d8 = d0 * d6 - d1 * d7;
            double d9 = (6.0D + d8 * d8) * d8 * 0.16666666666666666D;
            double d10 = d5 + d9;

            if (flag2) {
                d10 = 1.5707963267948966D - d10;
            }

            if (flag1) {
                d10 = 3.141592653589793D - d10;
            }

            if (flag) {
                d10 = -d10;
            }

            return d10;
        }
    }

    public static double fastInvSqrt(double d0) {
        double d1 = 0.5D * d0;
        long i = Double.doubleToRawLongBits(d0);

        i = 6910469410427058090L - (i >> 1);
        d0 = Double.longBitsToDouble(i);
        d0 *= 1.5D - d1 * d0 * d0;
        return d0;
    }

    public static int hash(int i) {
        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;
        return i;
    }

    static {
        int i;

        for (i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float) Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }

        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
        ASINE_TAB = new double[257];
        COS_TAB = new double[257];

        for (i = 0; i < 257; ++i) {
            double d0 = (double) i / 256.0D;
            double d1 = Math.asin(d0);

            MathHelper.COS_TAB[i] = Math.cos(d1);
            MathHelper.ASINE_TAB[i] = d1;
        }

    }
}
