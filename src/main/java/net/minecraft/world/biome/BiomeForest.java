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

    protected static final WorldGenBirchTree SUPER_BIRCH_TREE = new WorldGenBirchTree(false, true);
    protected static final WorldGenBirchTree BIRCH_TREE = new WorldGenBirchTree(false, false);
    protected static final WorldGenCanopyTree ROOF_TREE = new WorldGenCanopyTree(false);
    private final BiomeForest.Type type;

    public BiomeForest(BiomeForest.Type biomeforest_type, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.type = biomeforest_type;
        this.decorator.treesPerChunk = 10;
        this.decorator.grassPerChunk = 2;
        if (this.type == BiomeForest.Type.FLOWER) {
            this.decorator.treesPerChunk = 6;
            this.decorator.flowersPerChunk = 100;
            this.decorator.grassPerChunk = 1;
            this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        }

        if (this.type == BiomeForest.Type.NORMAL) {
            this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (this.type == BiomeForest.Type.ROOFED) {
            this.decorator.treesPerChunk = -999;
        }

    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (this.type == BiomeForest.Type.ROOFED && random.nextInt(3) > 0 ? BiomeForest.ROOF_TREE : (this.type != BiomeForest.Type.BIRCH && random.nextInt(5) != 0 ? (random.nextInt(10) == 0 ? BiomeForest.BIG_TREE_FEATURE : BiomeForest.TREE_FEATURE) : BiomeForest.BIRCH_TREE));
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockposition) {
        if (this.type == BiomeForest.Type.FLOWER) {
            double d0 = MathHelper.clamp((1.0D + BiomeForest.GRASS_COLOR_NOISE.getValue((double) blockposition.getX() / 48.0D, (double) blockposition.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
            BlockFlower.EnumFlowerType blockflowers_enumflowervarient = BlockFlower.EnumFlowerType.values()[(int) (d0 * (double) BlockFlower.EnumFlowerType.values().length)];

            return blockflowers_enumflowervarient == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : blockflowers_enumflowervarient;
        } else {
            return super.pickRandomFlower(random, blockposition);
        }
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        if (this.type == BiomeForest.Type.ROOFED) {
            this.addMushrooms(world, random, blockposition);
        }

        int i = random.nextInt(5) - 3;

        if (this.type == BiomeForest.Type.FLOWER) {
            i += 2;
        }

        this.addDoublePlants(world, random, blockposition, i);
        super.decorate(world, random, blockposition);
    }

    protected void addMushrooms(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                int k = i * 4 + 1 + 8 + random.nextInt(3);
                int l = j * 4 + 1 + 8 + random.nextInt(3);
                BlockPos blockposition1 = world.getHeight(blockposition.add(k, 0, l));

                if (random.nextInt(20) == 0) {
                    WorldGenBigMushroom worldgenhugemushroom = new WorldGenBigMushroom();

                    worldgenhugemushroom.generate(world, random, blockposition1);
                } else {
                    WorldGenAbstractTree worldgentreeabstract = this.getRandomTreeFeature(random);

                    worldgentreeabstract.setDecorationDefaults();
                    if (worldgentreeabstract.generate(world, random, blockposition1)) {
                        worldgentreeabstract.generateSaplings(world, random, blockposition1);
                    }
                }
            }
        }

    }

    protected void addDoublePlants(World world, Random random, BlockPos blockposition, int i) {
        int j = 0;

        while (j < i) {
            int k = random.nextInt(3);

            if (k == 0) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
            } else if (k == 1) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
            } else if (k == 2) {
                BiomeForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
            }

            int l = 0;

            while (true) {
                if (l < 5) {
                    int i1 = random.nextInt(16) + 8;
                    int j1 = random.nextInt(16) + 8;
                    int k1 = random.nextInt(world.getHeight(blockposition.add(i1, 0, j1)).getY() + 32);

                    if (!BiomeForest.DOUBLE_PLANT_GENERATOR.generate(world, random, new BlockPos(blockposition.getX() + i1, k1, blockposition.getZ() + j1))) {
                        ++l;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }

    }

    public Class<? extends Biome> getBiomeClass() {
        return BiomeForest.class;
    }

    public static enum Type {

        NORMAL, FLOWER, BIRCH, ROOFED;

        private Type() {}
    }
}
