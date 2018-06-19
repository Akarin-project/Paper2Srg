package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public class BiomeProviderSingle extends BiomeProvider {

    private final Biome field_76947_d;

    public BiomeProviderSingle(Biome biomebase) {
        this.field_76947_d = biomebase;
    }

    public Biome func_180631_a(BlockPos blockposition) {
        return this.field_76947_d;
    }

    public Biome[] func_76937_a(Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.field_76947_d);
        return abiomebase;
    }

    public Biome[] func_76933_b(@Nullable Biome[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        Arrays.fill(abiomebase, 0, k * l, this.field_76947_d);
        return abiomebase;
    }

    public Biome[] func_76931_a(@Nullable Biome[] abiomebase, int i, int j, int k, int l, boolean flag) {
        return this.func_76933_b(abiomebase, i, j, k, l);
    }

    @Nullable
    public BlockPos func_180630_a(int i, int j, int k, List<Biome> list, Random random) {
        return list.contains(this.field_76947_d) ? new BlockPos(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1)) : null;
    }

    public boolean func_76940_a(int i, int j, int k, List<Biome> list) {
        return list.contains(this.field_76947_d);
    }

    public boolean func_190944_c() {
        return true;
    }

    public Biome func_190943_d() {
        return this.field_76947_d;
    }
}
