package net.minecraft.util.math;

import java.util.Random;
import java.util.UUID;

public class MathHelper {

    public static final float field_180189_a = func_76129_c(2.0F);
    private static final float[] field_76144_a = new float[65536];
    private static final Random field_188211_c = new Random();
    private static final int[] field_151242_b;
    private static final double field_181163_d;
    private static final double[] field_181164_e;
    private static final double[] field_181165_f;

    public static float func_76126_a(float f) {
        return MathHelper.field_76144_a[(int) (f * 10430.378F) & '\uffff'];
    }

    public static float func_76134_b(float f) {
        return MathHelper.field_76144_a[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float func_76129_c(float f) {
        return (float) Math.sqrt((double) f);
    }

    public static float func_76133_a(double d0) {
        return (float) Math.sqrt(d0);
    }

    public static int func_76141_d(float f) {
        int i = (int) f;

        return f < (float) i ? i - 1 : i;
    }

    public static int func_76128_c(double d0) {
        int i = (int) d0;

        return d0 < (double) i ? i - 1 : i;
    }

    public static long func_76124_d(double d0) {
        long i = (long) d0;

        return d0 < (double) i ? i - 1L : i;
    }

    public static float func_76135_e(float f) {
        return f >= 0.0F ? f : -f;
    }

    public static int func_76130_a(int i) {
        return i >= 0 ? i : -i;
    }

    public static int func_76123_f(float f) {
        int i = (int) f;

        return f > (float) i ? i + 1 : i;
    }

    public static int func_76143_f(double d0) {
        int i = (int) d0;

        return d0 > (double) i ? i + 1 : i;
    }

    public static int func_76125_a(int i, int j, int k) {
        return i < j ? j : (i > k ? k : i);
    }

    public static float func_76131_a(float f, float f1, float f2) {
        return f < f1 ? f1 : (f > f2 ? f2 : f);
    }

    public static double func_151237_a(double d0, double d1, double d2) {
        return d0 < d1 ? d1 : (d0 > d2 ? d2 : d0);
    }

    public static double func_151238_b(double d0, double d1, double d2) {
        return d2 < 0.0D ? d0 : (d2 > 1.0D ? d1 : d0 + (d1 - d0) * d2);
    }

    public static double func_76132_a(double d0, double d1) {
        if (d0 < 0.0D) {
            d0 = -d0;
        }

        if (d1 < 0.0D) {
            d1 = -d1;
        }

        return d0 > d1 ? d0 : d1;
    }

    public static int func_76136_a(Random random, int i, int j) {
        return i >= j ? i : random.nextInt(j - i + 1) + i;
    }

    public static float func_151240_a(Random random, float f, float f1) {
        return f >= f1 ? f : random.nextFloat() * (f1 - f) + f;
    }

    public static double func_82716_a(Random random, double d0, double d1) {
        return d0 >= d1 ? d0 : random.nextDouble() * (d1 - d0) + d0;
    }

    public static double func_76127_a(long[] along) {
        long i = 0L;
        long[] along1 = along;
        int j = along.length;

        for (int k = 0; k < j; ++k) {
            long l = along1[k];

            i += l;
        }

        return (double) i / (double) along.length;
    }

    public static float func_76142_g(float f) {
        f %= 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static double func_76138_g(double d0) {
        d0 %= 360.0D;
        if (d0 >= 180.0D) {
            d0 -= 360.0D;
        }

        if (d0 < -180.0D) {
            d0 += 360.0D;
        }

        return d0;
    }

    public static int func_188209_b(int i) {
        i %= 360;
        if (i >= 180) {
            i -= 360;
        }

        if (i < -180) {
            i += 360;
        }

        return i;
    }

    public static int func_82715_a(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (Throwable throwable) {
            return i;
        }
    }

    public static int func_82714_a(String s, int i, int j) {
        return Math.max(j, func_82715_a(s, i));
    }

    public static double func_82712_a(String s, double d0) {
        try {
            return Double.parseDouble(s);
        } catch (Throwable throwable) {
            return d0;
        }
    }

    public static double func_82713_a(String s, double d0, double d1) {
        return Math.max(d1, func_82712_a(s, d0));
    }

    public static int func_151236_b(int i) {
        int j = i - 1;

        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    private static boolean func_151235_d(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    public static int func_151241_e(int i) {
        i = func_151235_d(i) ? i : func_151236_b(i);
        return MathHelper.field_151242_b[(int) ((long) i * 125613361L >> 27) & 31];
    }

    public static int func_151239_c(int i) {
        return func_151241_e(i) - (func_151235_d(i) ? 0 : 1);
    }

    public static int func_154354_b(int i, int j) {
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

    public static long func_180187_c(int i, int j, int k) {
        long l = (long) (i * 3129871) ^ (long) k * 116129781L ^ (long) j;

        l = l * l * 42317861L + l * 11L;
        return l;
    }

    public static UUID func_180182_a(Random random) {
        long i = random.nextLong() & -61441L | 16384L;
        long j = random.nextLong() & 4611686018427387903L | Long.MIN_VALUE;

        return new UUID(i, j);
    }

    public static UUID func_188210_a() {
        return func_180182_a(MathHelper.field_188211_c);
    }

    public static double func_181160_c(double d0, double d1, double d2) {
        return (d0 - d1) / (d2 - d1);
    }

    public static double func_181159_b(double d0, double d1) {
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

            d3 = func_181161_i(d2);
            d1 *= d3;
            d0 *= d3;
            double d4 = MathHelper.field_181163_d + d0;
            int i = (int) Double.doubleToRawLongBits(d4);
            double d5 = MathHelper.field_181164_e[i];
            double d6 = MathHelper.field_181165_f[i];
            double d7 = d4 - MathHelper.field_181163_d;
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

    public static double func_181161_i(double d0) {
        double d1 = 0.5D * d0;
        long i = Double.doubleToRawLongBits(d0);

        i = 6910469410427058090L - (i >> 1);
        d0 = Double.longBitsToDouble(i);
        d0 *= 1.5D - d1 * d0 * d0;
        return d0;
    }

    public static int func_188208_f(int i) {
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
            MathHelper.field_76144_a[i] = (float) Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }

        field_151242_b = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];

        for (i = 0; i < 257; ++i) {
            double d0 = (double) i / 256.0D;
            double d1 = Math.asin(d0);

            MathHelper.field_181165_f[i] = Math.cos(d1);
            MathHelper.field_181164_e[i] = d1;
        }

    }
}
