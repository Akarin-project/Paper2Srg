package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeHills extends Biome {

    private final WorldGenerator field_82915_S;
    private final WorldGenTaiga2 field_150634_aD;
    private final BiomeHills.Type field_150638_aH;

    protected BiomeHills(BiomeHills.Type biomebighills_type, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_82915_S = new WorldGenMinable(Blocks.field_150418_aU.func_176223_P().func_177226_a(BlockSilverfish.field_176378_a, BlockSilverfish.EnumType.STONE), 9);
        this.field_150634_aD = new WorldGenTaiga2(false);
        if (biomebighills_type == BiomeHills.Type.EXTRA_TREES) {
            this.field_76760_I.field_76832_z = 3;
        }

        this.field_76762_K.add(new Biome.SpawnListEntry(EntityLlama.class, 5, 4, 6));
        this.field_150638_aH = biomebighills_type;
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(3) > 0 ? this.field_150634_aD : super.func_150567_a(random));
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        super.func_180624_a(world, random, blockposition);
        int i = 3 + random.nextInt(6);

        int j;
        int k;
        int l;

        // Paper start - Disable extreme hills emeralds
        if (!world.paperConfig.disableExtremeHillsEmeralds) {

        for (j = 0; j < i; ++j) {
            k = random.nextInt(16);
            l = random.nextInt(28) + 4;
            int i1 = random.nextInt(16);
            BlockPos blockposition1 = blockposition.func_177982_a(k, l, i1);

            if (world.func_180495_p(blockposition1).func_177230_c() == Blocks.field_150348_b) {
                world.func_180501_a(blockposition1, Blocks.field_150412_bA.func_176223_P(), 2);
            }
        }

        }
        // Paper end block

        // Paper start - Disable extreme hills monster eggs
        if (!world.paperConfig.disableExtremeHillsMonsterEggs) {

        for (i = 0; i < 7; ++i) {
            j = random.nextInt(16);
            k = random.nextInt(64);
            l = random.nextInt(16);
            this.field_82915_S.func_180709_b(world, random, blockposition.func_177982_a(j, k, l));
        }

        }
        // Paper end block

    }

    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.field_76752_A = Blocks.field_150349_c.func_176223_P();
        this.field_76753_B = Blocks.field_150346_d.func_176223_P();
        if ((d0 < -1.0D || d0 > 2.0D) && this.field_150638_aH == BiomeHills.Type.MUTATED) {
            this.field_76752_A = Blocks.field_150351_n.func_176223_P();
            this.field_76753_B = Blocks.field_150351_n.func_176223_P();
        } else if (d0 > 1.0D && this.field_150638_aH != BiomeHills.Type.EXTRA_TREES) {
            this.field_76752_A = Blocks.field_150348_b.func_176223_P();
            this.field_76753_B = Blocks.field_150348_b.func_176223_P();
        }

        this.func_180628_b(world, random, chunksnapshot, i, j, d0);
    }

    public static enum Type {

        NORMAL, EXTRA_TREES, MUTATED;

        private Type() {}
    }
}
