package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeSwamp extends Biome {

    protected static final IBlockState field_185387_y = Blocks.field_150392_bi.func_176223_P();

    protected BiomeSwamp(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_76760_I.field_76832_z = 2;
        this.field_76760_I.field_76802_A = 1;
        this.field_76760_I.field_76804_C = 1;
        this.field_76760_I.field_76798_D = 8;
        this.field_76760_I.field_76799_E = 10;
        this.field_76760_I.field_76806_I = 1;
        this.field_76760_I.field_76833_y = 4;
        this.field_76760_I.field_76805_H = 0;
        this.field_76760_I.field_76801_G = 0;
        this.field_76760_I.field_76803_B = 5;
        this.field_76761_J.add(new Biome.SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return BiomeSwamp.field_76763_Q;
    }

    public BlockFlower.EnumFlowerType func_180623_a(Random random, BlockPos blockposition) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }

    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        double d1 = BiomeSwamp.field_180281_af.func_151601_a((double) i * 0.25D, (double) j * 0.25D);

        if (d1 > 0.0D) {
            int k = i & 15;
            int l = j & 15;

            for (int i1 = 255; i1 >= 0; --i1) {
                if (chunksnapshot.func_177856_a(l, i1, k).func_185904_a() != Material.field_151579_a) {
                    if (i1 == 62 && chunksnapshot.func_177856_a(l, i1, k).func_177230_c() != Blocks.field_150355_j) {
                        chunksnapshot.func_177855_a(l, i1, k, BiomeSwamp.field_185372_h);
                        if (d1 < 0.12D) {
                            chunksnapshot.func_177855_a(l, i1 + 1, k, BiomeSwamp.field_185387_y);
                        }
                    }
                    break;
                }
            }
        }

        this.func_180628_b(world, random, chunksnapshot, i, j, d0);
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        super.func_180624_a(world, random, blockposition);
        if (random.nextInt(64) == 0) {
            (new WorldGenFossils()).func_180709_b(world, random, blockposition);
        }

    }
}
