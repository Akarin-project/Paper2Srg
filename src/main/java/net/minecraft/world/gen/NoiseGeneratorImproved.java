package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved extends NoiseGenerator {

    private final int[] permutations;
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final double[] GRAD_X = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
    private static final double[] GRAD_Y = new double[] { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D};
    private static final double[] GRAD_Z = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};
    private static final double[] GRAD_2X = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
    private static final double[] GRAD_2Z = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};

    public NoiseGeneratorImproved() {
        this(new Random());
    }

    public NoiseGeneratorImproved(Random random) {
        this.permutations = new int[512];
        this.xCoord = random.nextDouble() * 256.0D;
        this.yCoord = random.nextDouble() * 256.0D;
        this.zCoord = random.nextDouble() * 256.0D;

        int i;

        for (i = 0; i < 256; this.permutations[i] = i++) {
            ;
        }

        for (i = 0; i < 256; ++i) {
            int j = random.nextInt(256 - i) + i;
            int k = this.permutations[i];

            this.permutations[i] = this.permutations[j];
            this.permutations[j] = k;
            this.permutations[i + 256] = this.permutations[i];
        }

    }

    public final double lerp(double d0, double d1, double d2) {
        return d1 + d0 * (d2 - d1);
    }

    public final double grad2(int i, double d0, double d1) {
        int j = i & 15;

        return NoiseGeneratorImproved.GRAD_2X[j] * d0 + NoiseGeneratorImproved.GRAD_2Z[j] * d1;
    }

    public final double grad(int i, double d0, double d1, double d2) {
        int j = i & 15;

        return NoiseGeneratorImproved.GRAD_X[j] * d0 + NoiseGeneratorImproved.GRAD_Y[j] * d1 + NoiseGeneratorImproved.GRAD_Z[j] * d2;
    }

    public void populateNoiseArray(double[] adouble, double d0, double d1, double d2, int i, int j, int k, double d3, double d4, double d5, double d6) {
        int l;
        int i1;
        double d7;
        double d8;
        int j1;
        double d9;
        int k1;
        int l1;
        double d10;
        int i2;
        int j2;

        if (j == 1) {
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            double d11 = 0.0D;
            double d12 = 0.0D;

            i2 = 0;
            double d13 = 1.0D / d6;

            for (int k2 = 0; k2 < i; ++k2) {
                d7 = d0 + (double) k2 * d3 + this.xCoord;
                int l2 = (int) d7;

                if (d7 < (double) l2) {
                    --l2;
                }

                int i3 = l2 & 255;

                d7 -= (double) l2;
                d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);

                for (j1 = 0; j1 < k; ++j1) {
                    d9 = d2 + (double) j1 * d5 + this.zCoord;
                    k1 = (int) d9;
                    if (d9 < (double) k1) {
                        --k1;
                    }

                    l1 = k1 & 255;
                    d9 -= (double) k1;
                    d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
                    l = this.permutations[i3] + 0;
                    int j3 = this.permutations[l] + l1;
                    int k3 = this.permutations[i3 + 1] + 0;

                    i1 = this.permutations[k3] + l1;
                    d11 = this.lerp(d8, this.grad2(this.permutations[j3], d7, d9), this.grad(this.permutations[i1], d7 - 1.0D, 0.0D, d9));
                    d12 = this.lerp(d8, this.grad(this.permutations[j3 + 1], d7, 0.0D, d9 - 1.0D), this.grad(this.permutations[i1 + 1], d7 - 1.0D, 0.0D, d9 - 1.0D));
                    double d14 = this.lerp(d10, d11, d12);

                    j2 = i2++;
                    adouble[j2] += d14 * d13;
                }
            }

        } else {
            l = 0;
            double d15 = 1.0D / d6;

            i1 = -1;
            boolean flag4 = false;
            boolean flag5 = false;
            boolean flag6 = false;
            boolean flag7 = false;
            boolean flag8 = false;
            boolean flag9 = false;
            double d16 = 0.0D;

            d7 = 0.0D;
            double d17 = 0.0D;

            d8 = 0.0D;

            for (j1 = 0; j1 < i; ++j1) {
                d9 = d0 + (double) j1 * d3 + this.xCoord;
                k1 = (int) d9;
                if (d9 < (double) k1) {
                    --k1;
                }

                l1 = k1 & 255;
                d9 -= (double) k1;
                d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);

                for (int l3 = 0; l3 < k; ++l3) {
                    double d18 = d2 + (double) l3 * d5 + this.zCoord;
                    int i4 = (int) d18;

                    if (d18 < (double) i4) {
                        --i4;
                    }

                    int j4 = i4 & 255;

                    d18 -= (double) i4;
                    double d19 = d18 * d18 * d18 * (d18 * (d18 * 6.0D - 15.0D) + 10.0D);

                    for (int k4 = 0; k4 < j; ++k4) {
                        double d20 = d1 + (double) k4 * d4 + this.yCoord;
                        int l4 = (int) d20;

                        if (d20 < (double) l4) {
                            --l4;
                        }

                        int i5 = l4 & 255;

                        d20 -= (double) l4;
                        double d21 = d20 * d20 * d20 * (d20 * (d20 * 6.0D - 15.0D) + 10.0D);

                        if (k4 == 0 || i5 != i1) {
                            i1 = i5;
                            int j5 = this.permutations[l1] + i5;
                            int k5 = this.permutations[j5] + j4;
                            int l5 = this.permutations[j5 + 1] + j4;
                            int i6 = this.permutations[l1 + 1] + i5;

                            i2 = this.permutations[i6] + j4;
                            int j6 = this.permutations[i6 + 1] + j4;

                            d16 = this.lerp(d10, this.grad(this.permutations[k5], d9, d20, d18), this.grad(this.permutations[i2], d9 - 1.0D, d20, d18));
                            d7 = this.lerp(d10, this.grad(this.permutations[l5], d9, d20 - 1.0D, d18), this.grad(this.permutations[j6], d9 - 1.0D, d20 - 1.0D, d18));
                            d17 = this.lerp(d10, this.grad(this.permutations[k5 + 1], d9, d20, d18 - 1.0D), this.grad(this.permutations[i2 + 1], d9 - 1.0D, d20, d18 - 1.0D));
                            d8 = this.lerp(d10, this.grad(this.permutations[l5 + 1], d9, d20 - 1.0D, d18 - 1.0D), this.grad(this.permutations[j6 + 1], d9 - 1.0D, d20 - 1.0D, d18 - 1.0D));
                        }

                        double d22 = this.lerp(d21, d16, d7);
                        double d23 = this.lerp(d21, d17, d8);
                        double d24 = this.lerp(d19, d22, d23);

                        j2 = l++;
                        adouble[j2] += d24 * d15;
                    }
                }
            }

        }
    }
}
