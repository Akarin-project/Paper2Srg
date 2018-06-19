package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFrostedIce extends BlockIce {

    public static final PropertyInteger field_185682_a = PropertyInteger.func_177719_a("age", 0, 3);

    public BlockFrostedIce() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFrostedIce.field_185682_a, Integer.valueOf(0)));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockFrostedIce.field_185682_a)).intValue();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockFrostedIce.field_185682_a, Integer.valueOf(MathHelper.func_76125_a(i, 0, 3)));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.paperConfig.frostedIceEnabled) return; // Paper - add ability to disable frosted ice
        if ((random.nextInt(3) == 0 || this.func_185680_c(world, blockposition) < 4) && world.func_175671_l(blockposition) > 11 - ((Integer) iblockdata.func_177229_b(BlockFrostedIce.field_185682_a)).intValue() - iblockdata.func_185891_c()) {
            this.func_185681_a(world, blockposition, iblockdata, random, true);
        } else {
            // Paper start - use configurable min/max delay
            //world.a(blockposition, (Block) this, MathHelper.nextInt(random, 20, 40));
            world.func_175684_a(blockposition, this, MathHelper.func_76136_a(random, world.paperConfig.frostedIceDelayMin, world.paperConfig.frostedIceDelayMax));
            // Paper end
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (block == this) {
            int i = this.func_185680_c(world, blockposition);

            if (i < 2) {
                this.func_185679_b(world, blockposition);
            }
        }

    }

    private int func_185680_c(World world, BlockPos blockposition) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];

            IBlockState iblockdata1 = world.getTypeIfLoaded(blockposition.func_177972_a(enumdirection)); // Paper - don't load chunks
            if (iblockdata1 == null) continue; // Paper
            if (iblockdata1.func_177230_c() == this) { // Paper
                ++i;
                if (i >= 4) {
                    return i;
                }
            }
        }

        return i;
    }

    protected void func_185681_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random, boolean flag) {
        int i = ((Integer) iblockdata.func_177229_b(BlockFrostedIce.field_185682_a)).intValue();

        if (i < 3) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockFrostedIce.field_185682_a, Integer.valueOf(i + 1)), 2);
            world.func_175684_a(blockposition, (Block) this, MathHelper.func_76136_a(random, 20, 40));
        } else {
            this.func_185679_b(world, blockposition);
            if (flag) {
                EnumFacing[] aenumdirection = EnumFacing.values();
                int j = aenumdirection.length;

                for (int k = 0; k < j; ++k) {
                    EnumFacing enumdirection = aenumdirection[k];
                    BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
                    IBlockState iblockdata1 = world.getTypeIfLoaded(blockposition1); // Paper - don't load chunks
                    if (iblockdata1 == null) continue; // Paper

                    if (iblockdata1.func_177230_c() == this) {
                        this.func_185681_a(world, blockposition1, iblockdata1, random, false);
                    }
                }
            }
        }

    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFrostedIce.field_185682_a});
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.field_190927_a;
    }
}
