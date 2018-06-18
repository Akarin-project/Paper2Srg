package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public class BiomeProviderSingle extends BiomeProvider {

    private final Biome biome;

    public BiomeProviderSingle(Biome biomebase) {
        this.biome = biomebase;
    }

    public Biome getBiome(BlockPos blockposition) {
        return this.biome;
    }

    public Biome[] getBiomesForGeneration(Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.biome);
        return abiomebase;
    }

    public Biome[] getBiomes(@Nullable Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.biome);
        return abiomebase;
    }

    public Biome[] getBiomes(@Nullable Biome[] abiomebase, int i, int j, int k, int l, boolean flag) {
        return this.getBiomes(abiomebase, i, j, k, l);
    }

    @Nullable
    public BlockPos findBiomePosition(int i, int j, int k, List<Biome> list, Random random) {
        return list.contains(this.biome) ? new BlockPos(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1)) : null;
    }

    public boolean areBiomesViable(int i, int j, int k, List<Biome> list) {
        return list.contains(this.biome);
    }

    public boolean isFixedBiome() {
        return true;
    }

    public Biome getFixedBiome() {
        return this.biome;
    }
}
