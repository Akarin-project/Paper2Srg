package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeSavannaMutated extends BiomeSavanna {

    public BiomeSavannaMutated(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_76760_I.field_76832_z = 2;
        this.field_76760_I.field_76802_A = 2;
        this.field_76760_I.field_76803_B = 5;
    }

    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.field_76752_A = Blocks.field_150349_c.func_176223_P();
        this.field_76753_B = Blocks.field_150346_d.func_176223_P();
        if (d0 > 1.75D) {
            this.field_76752_A = Blocks.field_150348_b.func_176223_P();
            this.field_76753_B = Blocks.field_150348_b.func_176223_P();
        } else if (d0 > -0.5D) {
            this.field_76752_A = Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.COARSE_DIRT);
        }

        this.func_180628_b(world, random, chunksnapshot, i, j, d0);
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        this.field_76760_I.func_180292_a(world, random, this, blockposition);
    }
}
