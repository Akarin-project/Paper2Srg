package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator {

    public WorldGenAbstractTree(boolean flag) {
        super(flag);
    }

    protected boolean func_150523_a(Block block) {
        Material material = block.func_176223_P().func_185904_a();

        return material == Material.field_151579_a || material == Material.field_151584_j || block == Blocks.field_150349_c || block == Blocks.field_150346_d || block == Blocks.field_150364_r || block == Blocks.field_150363_s || block == Blocks.field_150345_g || block == Blocks.field_150395_bd;
    }

    public void func_180711_a(World world, Random random, BlockPos blockposition) {}

    protected void func_175921_a(World world, BlockPos blockposition) {
        if (world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150346_d) {
            this.func_175903_a(world, blockposition, Blocks.field_150346_d.func_176223_P());
        }

    }
}
