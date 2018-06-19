package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends Biome {

    private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
    private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree MEGA_PINE_GENERATOR = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree MEGA_SPRUCE_GENERATOR = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
    private final BiomeTaiga.Type type;

    public BiomeTaiga(BiomeTaiga.Type biometaiga_type, Biome.a biomebase_a) {
        super(biomebase_a);
        this.type = biometaiga_type;
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.decorator.treesPerChunk = 10;
        if (biometaiga_type != BiomeTaiga.Type.MEGA && biometaiga_type != BiomeTaiga.Type.MEGA_SPRUCE) {
            this.decorator.grassPerChunk = 1;
            this.decorator.mushroomsPerChunk = 1;
        } else {
            this.decorator.grassPerChunk = 7;
            this.decorator.deadBushPerChunk = 1;
            this.decorator.mushroomsPerChunk = 3;
        }

    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) && random.nextInt(3) == 0 ? (this.type != BiomeTaiga.Type.MEGA_SPRUCE && random.nextInt(13) != 0 ? BiomeTaiga.MEGA_PINE_GENERATOR : BiomeTaiga.MEGA_SPRUCE_GENERATOR) : (random.nextInt(3) == 0 ? BiomeTaiga.PINE_GENERATOR : BiomeTaiga.SPRUCE_GENERATOR);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockposition) {
        int i;
        int j;
        int k;
        int l;

        if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
            i = random.nextInt(3);

            for (j = 0; j < i; ++j) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                BlockPos blockposition1 = world.getHeight(blockposition.add(k, 0, l));

                BiomeTaiga.FOREST_ROCK_GENERATOR.generate(world, random, blockposition1);
            }
        }

        BiomeTaiga.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

        for (i = 0; i < 7; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            l = random.nextInt(world.getHeight(blockposition.add(j, 0, k)).getY() + 32);
            BiomeTaiga.DOUBLE_PLANT_GENERATOR.generate(world, random, blockposition.add(j, l, k));
        }

        super.decorate(world, random, blockposition);
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
            this.topBlock = Blocks.GRASS.getDefaultState();
            this.fillerBlock = Blocks.DIRT.getDefaultState();
            if (d0 > 1.75D) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            } else if (d0 > -0.95D) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }

        this.generateBiomeTerrain(world, random, chunksnapshot, i, j, d0);
    }

    public static enum Type {

        NORMAL, MEGA, MEGA_SPRUCE;

        private Type() {}
    }
}
