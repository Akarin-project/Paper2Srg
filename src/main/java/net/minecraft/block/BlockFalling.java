package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block {

    public static boolean field_149832_M;

    public BlockFalling() {
        super(Material.field_151595_p);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public BlockFalling(Material material) {
        super(material);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            this.func_176503_e(world, blockposition);
        }

    }

    private void func_176503_e(World world, BlockPos blockposition) {
        if (func_185759_i(world.func_180495_p(blockposition.func_177977_b())) && blockposition.func_177956_o() >= 0) {
            boolean flag = true;

            if (!BlockFalling.field_149832_M && world.func_175707_a(blockposition.func_177982_a(-32, -32, -32), blockposition.func_177982_a(32, 32, 32))) {
                if (!world.field_72995_K) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p() + 0.5D, world.func_180495_p(blockposition));

                    this.func_149829_a(entityfallingblock);
                    world.func_72838_d(entityfallingblock);
                }
            } else {
                world.func_175698_g(blockposition);

                BlockPos blockposition1;

                for (blockposition1 = blockposition.func_177977_b(); func_185759_i(world.func_180495_p(blockposition1)) && blockposition1.func_177956_o() > 0; blockposition1 = blockposition1.func_177977_b()) {
                    ;
                }

                if (blockposition1.func_177956_o() > 0) {
                    world.func_175656_a(blockposition1.func_177984_a(), this.func_176223_P());
                }
            }

        }
    }

    protected void func_149829_a(EntityFallingBlock entityfallingblock) {}

    public int func_149738_a(World world) {
        return 2;
    }

    public static boolean func_185759_i(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();
        Material material = iblockdata.func_185904_a();

        return block == Blocks.field_150480_ab || material == Material.field_151579_a || material == Material.field_151586_h || material == Material.field_151587_i;
    }

    // Paper start - OBFHELPER
    public static boolean canMoveThrough(IBlockState blockdata) {
        return BlockFalling.func_185759_i(blockdata);
    }
    // Paper end

    public void func_176502_a_(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {}

    public void func_190974_b(World world, BlockPos blockposition) {}
}
