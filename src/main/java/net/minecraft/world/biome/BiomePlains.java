package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomePlains extends Biome {

    protected boolean sunflowers;

    protected BiomePlains(boolean flag, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.sunflowers = flag;
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 3));
        this.decorator.treesPerChunk = 0;
        this.decorator.extraTreeChance = 0.05F;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 10;
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockposition) {
        double d0 = BiomePlains.GRASS_COLOR_NOISE.getValue((double) blockposition.getX() / 200.0D, (double) blockposition.getZ() / 200.0D);
        int i;

        if (d0 < -0.8D) {
            i = random.nextInt(4);
            switch (i) {
            case 0:
                return BlockFlower.EnumFlowerType.ORANGE_TULIP;

            case 1:
                return BlockFlower.EnumFlowerType.RED_TULIP;

            case 2:
                return BlockFlower.EnumFlowerType.PINK_TULIP;

            case 3:
            default:
                return BlockFlower.EnumFlowerType.WHITE_TULIP;
            }
        } else if (random.nextInt(3) > 0) {
            i = random.nextInt(3);
            return i == 0 ? BlockFlower.EnumFlowerType.POPPY : (i == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
        } else {
            return BlockFlower.EnumFlowerType.DANDELION;
        }
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        double d0 = BiomePlains.GRASS_COLOR_NOISE.getValue((double) (blockposition.getX() + 8) / 200.0D, (double) (blockposition.getZ() + 8) / 200.0D);
        int i;
        int j;
        int k;
        int l;

        if (d0 < -0.8D) {
            this.decorator.flowersPerChunk = 15;
            this.decorator.grassPerChunk = 5;
        } else {
            this.decorator.flowersPerChunk = 4;
            this.decorator.grassPerChunk = 10;
            BiomePlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

            for (i = 0; i < 7; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.getHeight(blockposition.add(j, 0, k)).getY() + 32);
                BiomePlains.DOUBLE_PLANT_GENERATOR.generate(world, random, blockposition.add(j, l, k));
            }
        }

        if (this.sunflowers) {
            BiomePlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);

            for (i = 0; i < 10; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.getHeight(blockposition.add(j, 0, k)).getY() + 32);
                BiomePlains.DOUBLE_PLANT_GENERATOR.generate(world, random, blockposition.add(j, l, k));
            }
        }

        super.decorate(world, random, blockposition);
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(3) == 0 ? BiomePlains.BIG_TREE_FEATURE : BiomePlains.TREE_FEATURE);
    }
}
