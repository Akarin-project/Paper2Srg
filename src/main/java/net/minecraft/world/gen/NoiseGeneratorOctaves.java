package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class NoiseGeneratorOctaves extends NoiseGenerator {

    private final NoiseGeneratorImproved[] generatorCollection;
    private final int octaves;

    public NoiseGeneratorOctaves(Random random, int i) {
        this.octaves = i;
        this.generatorCollection = new NoiseGeneratorImproved[i];

        for (int j = 0; j < i; ++j) {
            this.generatorCollection[j] = new NoiseGeneratorImproved(random);
        }

    }

    public double[] generateNoiseOctaves(double[] adouble, int i, int j, int k, int l, int i1, int j1, double d0, double d1, double d2) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        } else {
            for (int k1 = 0; k1 < adouble.length; ++k1) {
                adouble[k1] = 0.0D;
            }
        }

        double d3 = 1.0D;

        for (int l1 = 0; l1 < this.octaves; ++l1) {
            double d4 = (double) i * d3 * d0;
            double d5 = (double) j * d3 * d1;
            double d6 = (double) k * d3 * d2;
            long i2 = MathHelper.lfloor(d4);
            long j2 = MathHelper.lfloor(d6);

            d4 -= (double) i2;
            d6 -= (double) j2;
            i2 %= 16777216L;
            j2 %= 16777216L;
            d4 += (double) i2;
            d6 += (double) j2;
            this.generatorCollection[l1].populateNoiseArray(adouble, d4, d5, d6, l, i1, j1, d0 * d3, d1 * d3, d2 * d3, d3);
            d3 /= 2.0D;
        }

        return adouble;
    }

    public double[] generateNoiseOctaves(double[] adouble, int i, int j, int k, int l, double d0, double d1, double d2) {
        return this.generateNoiseOctaves(adouble, i, 10, j, k, 1, l, d0, 1.0D, d1);
    }
}
