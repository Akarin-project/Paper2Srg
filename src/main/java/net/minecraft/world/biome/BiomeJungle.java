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

    private final boolean field_150614_aC;
    private static final IBlockState field_181620_aE = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState field_181621_aF = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.JUNGLE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));
    private static final IBlockState field_181622_aG = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.OAK).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public BiomeJungle(boolean flag, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_150614_aC = flag;
        if (flag) {
            this.field_76760_I.field_76832_z = 2;
        } else {
            this.field_76760_I.field_76832_z = 50;
        }

        this.field_76760_I.field_76803_B = 25;
        this.field_76760_I.field_76802_A = 4;
        if (!flag) {
            this.field_76761_J.add(new Biome.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }

        this.field_76762_K.add(new Biome.SpawnListEntry(EntityParrot.class, 40, 1, 2));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(10) == 0 ? BiomeJungle.field_76758_O : (random.nextInt(2) == 0 ? new WorldGenShrub(BiomeJungle.field_181620_aE, BiomeJungle.field_181622_aG) : (!this.field_150614_aC && random.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, BiomeJungle.field_181620_aE, BiomeJungle.field_181621_aF) : new WorldGenTrees(false, 4 + random.nextInt(7), BiomeJungle.field_181620_aE, BiomeJungle.field_181621_aF, true))));
    }

    public WorldGenerator func_76730_b(Random random) {
        return random.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        super.func_180624_a(world, random, blockposition);
        int i = random.nextInt(16) + 8;
        int j = random.nextInt(16) + 8;
        // Paper start - Don't allow a 0 height
        int height = world.func_175645_m(blockposition.add(i, 0, j)).func_177956_o() * 2;
        if (height < 1) height = 1;
        int k = random.nextInt(height);
        // Paper end

        (new WorldGenMelon()).func_180709_b(world, random, blockposition.func_177982_a(i, k, j));
        WorldGenVines worldgenvines = new WorldGenVines();

        for (j = 0; j < 50; ++j) {
            k = random.nextInt(16) + 8;
            boolean flag = true;
            int l = random.nextInt(16) + 8;

            worldgenvines.func_180709_b(world, random, blockposition.func_177982_a(k, 128, l));
        }

    }
}
