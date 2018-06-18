package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeJungle extends Biome {

    private final boolean isEdge;
    private static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public BiomeJungle(boolean flag, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.isEdge = flag;
        if (flag) {
            this.decorator.treesPerChunk = 2;
        } else {
            this.decorator.treesPerChunk = 50;
        }

        this.decorator.grassPerChunk = 25;
        this.decorator.flowersPerChunk = 4;
        if (!flag) {
            this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 40, 1, 2));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(10) == 0 ? BiomeJungle.BIG_TREE_FEATURE : (random.nextInt(2) == 0 ? new WorldGenShrub(BiomeJungle.JUNGLE_LOG, BiomeJungle.OAK_LEAF) : (!this.isEdge && random.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, BiomeJungle.JUNGLE_LOG, BiomeJungle.JUNGLE_LEAF) : new WorldGenTrees(false, 4 + random.nextInt(7), BiomeJungle.JUNGLE_LOG, BiomeJungle.JUNGLE_LEAF, true))));
    }

    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        return random.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        super.decorate(world, random, blockposition);
        int i = random.nextInt(16) + 8;
        int j = random.nextInt(16) + 8;
        // Paper start - Don't allow a 0 height
        int height = world.getHeight(blockposition.add(i, 0, j)).getY() * 2;
        if (height < 1) height = 1;
        int k = random.nextInt(height);
        // Paper end

        (new WorldGenMelon()).generate(world, random, blockposition.add(i, k, j));
        WorldGenVines worldgenvines = new WorldGenVines();

        for (j = 0; j < 50; ++j) {
            k = random.nextInt(16) + 8;
            boolean flag = true;
            int l = random.nextInt(16) + 8;

            worldgenvines.generate(world, random, blockposition.add(k, 128, l));
        }

    }
}
