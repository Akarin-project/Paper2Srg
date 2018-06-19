package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;

public class BiomeForest extends Biome {

    protected static final WorldGenBirchTree field_150629_aC = new WorldGenBirchTree(false, true);
    protected static final WorldGenBirchTree field_150630_aD = new WorldGenBirchTree(false, false);
    protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(false);
    private final BiomeForest.Type field_150632_aF;

    public BiomeForest(BiomeForest.Type biomeforest_type, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_150632_aF = biomeforest_type;
        this.field_76760_I.field_76832_z = 10;
        this.field_76760_I.field_76803_B = 2;
        if (this.field_150632_aF == BiomeForest.Type.FLOWER) {
            this.field_76760_I.field_76832_z = 6;
            this.field_76760_I.field_76802_A = 100;
            this.field_76760_I.field_76803_B = 1;
            this.field_76762_K.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        }

        if (this.field_150632_aF == BiomeForest.Type.NORMAL) {
            this.field_76762_K.add(new Biome.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (this.field_150632_aF == BiomeForest.Type.ROOFED) {
            this.field_76760_I.field_76832_z = -999;
        }

    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) (this.field_150632_aF == BiomeForest.Type.ROOFED && random.nextInt(3) > 0 ? BiomeForest.field_150631_aE : (this.field_150632_aF != BiomeForest.Type.BIRCH && random.nextInt(5) != 0 ? (random.nextInt(10) == 0 ? BiomeForest.field_76758_O : BiomeForest.field_76757_N) : BiomeForest.field_150630_aD));
    }

    public BlockFlower.EnumFlowerType func_180623_a(Random random, BlockPos blockposition) {
        if (this.field_150632_aF == BiomeForest.Type.FLOWER) {
            double d0 = MathHelper.func_151237_a((1.0D + BiomeForest.field_180281_af.func_151601_a((double) blockposition.func_177958_n() / 48.0D, (double) blockposition.func_177952_p() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
            BlockFlower.EnumFlowerType blockflowers_enumflowervarient = BlockFlower.EnumFlowerType.values()[(int) (d0 * (double) BlockFlower.EnumFlowerType.values().length)];

            return blockflowers_enumflowervarient == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : blockflowers_enumflowervarient;
        } else {
            return super.func_180623_a(random, blockposition);
        }
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        if (this.field_150632_aF == BiomeForest.Type.ROOFED) {
            this.func_185379_b(world, random, blockposition);
        }

        int i = random.nextInt(5) - 3;

        if (this.field_150632_aF == BiomeForest.Type.FLOWER) {
            i += 2;
        }

        this.func_185378_a(world, random, blockposition, i);
        super.func_180624_a(world, random, blockposition);
    }

    protected void func_185379_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                int k = i * 4 + 1 + 8 + random.nextInt(3);
                int l = j * 4 + 1 + 8 + random.nextInt(3);
                BlockPos blockposition1 = world.func_175645_m(blockposition.func_177982_a(k, 0, l));

                if (random.nextInt(20) == 0) {
                    WorldGenBigMushroom worldgenhugemushroom = new WorldGenBigMushroom();

                    worldgenhugemushroom.func_180709_b(world, random, blockposition1);
                } else {
                    WorldGenAbstractTree worldgentreeabstract = this.func_150567_a(random);

                    worldgentreeabstract.func_175904_e();
                    if (worldgentreeabstract.func_180709_b(world, random, blockposition1)) {
                        worldgentreeabstract.func_180711_a(world, random, blockposition1);
                    }
                }
            }
        }

    }

    protected void func_185378_a(World world, Random random, BlockPos blockposition, int i) {
        int j = 0;

        while (j < i) {
            int k = random.nextInt(3);

            if (k == 0) {
                BiomeForest.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.SYRINGA);
            } else if (k == 1) {
                BiomeForest.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.ROSE);
            } else if (k == 2) {
                BiomeForest.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.PAEONIA);
            }

            int l = 0;

            while (true) {
                if (l < 5) {
                    int i1 = random.nextInt(16) + 8;
                    int j1 = random.nextInt(16) + 8;
                    int k1 = random.nextInt(world.func_175645_m(blockposition.func_177982_a(i1, 0, j1)).func_177956_o() + 32);

                    if (!BiomeForest.field_180280_ag.func_180709_b(world, random, new BlockPos(blockposition.func_177958_n() + i1, k1, blockposition.func_177952_p() + j1))) {
                        ++l;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }

    }

    public Class<? extends Biome> func_150562_l() {
        return BiomeForest.class;
    }

    public static enum Type {

        NORMAL, FLOWER, BIRCH, ROOFED;

        private Type() {}
    }
}
