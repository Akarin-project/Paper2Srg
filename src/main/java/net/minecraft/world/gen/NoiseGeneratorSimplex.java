package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {

    private static final int[][] grad3 = new int[][] { { 1, 1, 0}, { -1, 1, 0}, { 1, -1, 0}, { -1, -1, 0}, { 1, 0, 1}, { -1, 0, 1}, { 1, 0, -1}, { -1, 0, -1}, { 0, 1, 1}, { 0, -1, 1}, { 0, 1, -1}, { 0, -1, -1}};
    public static final double SQRT_3 = Math.sqrt(3.0D);
    private final int[] p;
    public double xo;
    public double yo;
    public double zo;
    private static final double F2 = 0.5D * (NoiseGeneratorSimplex.SQRT_3 - 1.0D);
    private static final double G2 = (3.0D - NoiseGeneratorSimplex.SQRT_3) / 6.0D;

    public NoiseGeneratorSimplex() {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random random) {
        this.p = new int[512];
        this.xo = random.nextDouble() * 256.0D;
        this.yo = random.nextDouble() * 256.0D;
        this.zo = random.nextDouble() * 256.0D;

        int i;

        for (i = 0; i < 256; this.p[i] = i++) {
            ;
        }

        for (i = 0; i < 256; ++i) {
            int j = random.nextInt(256 - i) + i;
            int k = this.p[i];

            this.p[i] = this.p[j];
            this.p[j] = k;
            this.p[i + 256] = this.p[i];
        }

    }

    private static int fastFloor(double d0) {
        return d0 > 0.0D ? (int) d0 : (int) d0 - 1;
    }

    private static double dot(int[] aint, double d0, double d1) {
        return (double) aint[0] * d0 + (double) aint[1] * d1;
    }

    public double getValue(double d0, double d1) {
        double d2 = 0.5D * (NoiseGeneratorSimplex.SQRT_3 - 1.0D);
        double d3 = (d0 + d1) * d2;
        int i = fastFloor(d0 + d3);
        int j = fastFloor(d1 + d3);
        double d4 = (3.0D - NoiseGeneratorSimplex.SQRT_3) / 6.0D;
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
        int i1 = this.p[k + this.p[l]] % 12;
        int j1 = this.p[k + b0 + this.p[l + b1]] % 12;
        int k1 = this.p[k + 1 + this.p[l + 1]] % 12;
        double d14 = 0.5D - d8 * d8 - d9 * d9;
        double d15;

        if (d14 < 0.0D) {
            d15 = 0.0D;
        } else {
            d14 *= d14;
            d15 = d14 * d14 * dot(NoiseGeneratorSimplex.grad3[i1], d8, d9);
        }

        double d16 = 0.5D - d10 * d10 - d11 * d11;
        double d17;

        if (d16 < 0.0D) {
            d17 = 0.0D;
        } else {
            d16 *= d16;
            d17 = d16 * d16 * dot(NoiseGeneratorSimplex.grad3[j1], d10, d11);
        }

        double d18 = 0.5D - d12 * d12 - d13 * d13;
        double d19;

        if (d18 < 0.0D) {
            d19 = 0.0D;
        } else {
            d18 *= d18;
            d19 = d18 * d18 * dot(NoiseGeneratorSimplex.grad3[k1], d12, d13);
        }

        return 70.0D * (d15 + d17 + d19);
    }

    public void add(double[] adouble, double d0, double d1, int i, int j, double d2, double d3, double d4) {
        int k = 0;

        for (int l = 0; l < j; ++l) {
            double d5 = (d1 + (double) l) * d3 + this.yo;

            for (int i1 = 0; i1 < i; ++i1) {
                double d6 = (d0 + (double) i1) * d2 + this.xo;
                double d7 = (d6 + d5) * NoiseGeneratorSimplex.F2;
                int j1 = fastFloor(d6 + d7);
                int k1 = fastFloor(d5 + d7);
                double d8 = (double) (j1 + k1) * NoiseGeneratorSimplex.G2;
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

                double d13 = d11 - (double) b0 + NoiseGeneratorSimplex.G2;
                double d14 = d12 - (double) b1 + NoiseGeneratorSimplex.G2;
                double d15 = d11 - 1.0D + 2.0D * NoiseGeneratorSimplex.G2;
                double d16 = d12 - 1.0D + 2.0D * NoiseGeneratorSimplex.G2;
                int l1 = j1 & 255;
                int i2 = k1 & 255;
                int j2 = this.p[l1 + this.p[i2]] % 12;
                int k2 = this.p[l1 + b0 + this.p[i2 + b1]] % 12;
                int l2 = this.p[l1 + 1 + this.p[i2 + 1]] % 12;
                double d17 = 0.5D - d11 * d11 - d12 * d12;
                double d18;

                if (d17 < 0.0D) {
                    d18 = 0.0D;
                } else {
                    d17 *= d17;
                    d18 = d17 * d17 * dot(NoiseGeneratorSimplex.grad3[j2], d11, d12);
                }

                double d19 = 0.5D - d13 * d13 - d14 * d14;
                double d20;

                if (d19 < 0.0D) {
                    d20 = 0.0D;
                } else {
                    d19 *= d19;
                    d20 = d19 * d19 * dot(NoiseGeneratorSimplex.grad3[k2], d13, d14);
                }

                double d21 = 0.5D - d15 * d15 - d16 * d16;
                double d22;

                if (d21 < 0.0D) {
                    d22 = 0.0D;
                } else {
                    d21 *= d21;
                    d22 = d21 * d21 * dot(NoiseGeneratorSimplex.grad3[l2], d15, d16);
                }

                int i3 = k++;

                adouble[i3] += 70.0D * (d18 + d20 + d22) * d4;
            }
        }

    }
}
