package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {

    private static final int[][] field_151611_e = new int[][] { { 1, 1, 0}, { -1, 1, 0}, { 1, -1, 0}, { -1, -1, 0}, { 1, 0, 1}, { -1, 0, 1}, { 1, 0, -1}, { -1, 0, -1}, { 0, 1, 1}, { 0, -1, 1}, { 0, 1, -1}, { 0, -1, -1}};
    public static final double field_151614_a = Math.sqrt(3.0D);
    private final int[] field_151608_f;
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g = 0.5D * (NoiseGeneratorSimplex.field_151614_a - 1.0D);
    private static final double field_151615_h = (3.0D - NoiseGeneratorSimplex.field_151614_a) / 6.0D;

    public NoiseGeneratorSimplex() {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random random) {
        this.field_151608_f = new int[512];
        this.field_151612_b = random.nextDouble() * 256.0D;
        this.field_151613_c = random.nextDouble() * 256.0D;
        this.field_151610_d = random.nextDouble() * 256.0D;

        int i;

        for (i = 0; i < 256; this.field_151608_f[i] = i++) {
            ;
        }

        for (i = 0; i < 256; ++i) {
            int j = random.nextInt(256 - i) + i;
            int k = this.field_151608_f[i];

            this.field_151608_f[i] = this.field_151608_f[j];
            this.field_151608_f[j] = k;
            this.field_151608_f[i + 256] = this.field_151608_f[i];
        }

    }

    private static int func_151607_a(double d0) {
        return d0 > 0.0D ? (int) d0 : (int) d0 - 1;
    }

    private static double func_151604_a(int[] aint, double d0, double d1) {
        return (double) aint[0] * d0 + (double) aint[1] * d1;
    }

    public double func_151605_a(double d0, double d1) {
        double d2 = 0.5D * (NoiseGeneratorSimplex.field_151614_a - 1.0D);
        double d3 = (d0 + d1) * d2;
        int i = func_151607_a(d0 + d3);
        int j = func_151607_a(d1 + d3);
        double d4 = (3.0D - NoiseGeneratorSimplex.field_151614_a) / 6.0D;
        double d5 = (double) (i + j) * d4;
        double d6 = (double) i - d5;
        double d7 = (double) j - d5;
        double d8 = d0 - d6;
        double d9 = d1 - d7;
        byte b0;
        byte b1;

        if (d8 > d9) {
            b0 = 1;
            b1 = 0;
        } else {
            b0 = 0;
            b1 = 1;
        }

        double d10 = d8 - (double) b0 + d4;
        double d11 = d9 - (double) b1 + d4;
        double d12 = d8 - 1.0D + 2.0D * d4;
        double d13 = d9 - 1.0D + 2.0D * d4;
        int k = i & 255;
        int l = j & 255;
        int i1 = this.field_151608_f[k + this.field_151608_f[l]] % 12;
        int j1 = this.field_151608_f[k + b0 + this.field_151608_f[l + b1]] % 12;
        int k1 = this.field_151608_f[k + 1 + this.field_151608_f[l + 1]] % 12;
        double d14 = 0.5D - d8 * d8 - d9 * d9;
        double d15;

        if (d14 < 0.0D) {
            d15 = 0.0D;
        } else {
            d14 *= d14;
            d15 = d14 * d14 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[i1], d8, d9);
        }

        double d16 = 0.5D - d10 * d10 - d11 * d11;
        double d17;

        if (d16 < 0.0D) {
            d17 = 0.0D;
        } else {
            d16 *= d16;
            d17 = d16 * d16 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[j1], d10, d11);
        }

        double d18 = 0.5D - d12 * d12 - d13 * d13;
        double d19;

        if (d18 < 0.0D) {
            d19 = 0.0D;
        } else {
            d18 *= d18;
            d19 = d18 * d18 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[k1], d12, d13);
        }

        return 70.0D * (d15 + d17 + d19);
    }

    public void func_151606_a(double[] adouble, double d0, double d1, int i, int j, double d2, double d3, double d4) {
        int k = 0;

        for (int l = 0; l < j; ++l) {
            double d5 = (d1 + (double) l) * d3 + this.field_151613_c;

            for (int i1 = 0; i1 < i; ++i1) {
                double d6 = (d0 + (double) i1) * d2 + this.field_151612_b;
                double d7 = (d6 + d5) * NoiseGeneratorSimplex.field_151609_g;
                int j1 = func_151607_a(d6 + d7);
                int k1 = func_151607_a(d5 + d7);
                double d8 = (double) (j1 + k1) * NoiseGeneratorSimplex.field_151615_h;
                double d9 = (double) j1 - d8;
                double d10 = (double) k1 - d8;
                double d11 = d6 - d9;
                double d12 = d5 - d10;
                byte b0;
                byte b1;

                if (d11 > d12) {
                    b0 = 1;
                    b1 = 0;
                } else {
                    b0 = 0;
                    b1 = 1;
                }

                double d13 = d11 - (double) b0 + NoiseGeneratorSimplex.field_151615_h;
                double d14 = d12 - (double) b1 + NoiseGeneratorSimplex.field_151615_h;
                double d15 = d11 - 1.0D + 2.0D * NoiseGeneratorSimplex.field_151615_h;
                double d16 = d12 - 1.0D + 2.0D * NoiseGeneratorSimplex.field_151615_h;
                int l1 = j1 & 255;
                int i2 = k1 & 255;
                int j2 = this.field_151608_f[l1 + this.field_151608_f[i2]] % 12;
                int k2 = this.field_151608_f[l1 + b0 + this.field_151608_f[i2 + b1]] % 12;
                int l2 = this.field_151608_f[l1 + 1 + this.field_151608_f[i2 + 1]] % 12;
                double d17 = 0.5D - d11 * d11 - d12 * d12;
                double d18;

                if (d17 < 0.0D) {
                    d18 = 0.0D;
                } else {
                    d17 *= d17;
                    d18 = d17 * d17 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[j2], d11, d12);
                }

                double d19 = 0.5D - d13 * d13 - d14 * d14;
                double d20;

                if (d19 < 0.0D) {
                    d20 = 0.0D;
                } else {
                    d19 *= d19;
                    d20 = d19 * d19 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[k2], d13, d14);
                }

                double d21 = 0.5D - d15 * d15 - d16 * d16;
                double d22;

                if (d21 < 0.0D) {
                    d22 = 0.0D;
                } else {
                    d21 *= d21;
                    d22 = d21 * d21 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[l2], d15, d16);
                }

                int i3 = k++;

                adouble[i3] += 70.0D * (d18 + d20 + d22) * d4;
            }
        }

    }
}
