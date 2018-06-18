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

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    public BlockFrostedIce() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFrostedIce.AGE, Integer.valueOf(0)));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockFrostedIce.AGE)).intValue();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockFrostedIce.AGE, Integer.valueOf(MathHelper.clamp(i, 0, 3)));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.paperConfig.frostedIceEnabled) return; // Paper - add ability to disable frosted ice
        if ((random.nextInt(3) == 0 || this.countNeighbors(world, blockposition) < 4) && world.getLightFromNeighbors(blockposition) > 11 - ((Integer) iblockdata.getValue(BlockFrostedIce.AGE)).intValue() - iblockdata.getLightOpacity()) {
            this.slightlyMelt(world, blockposition, iblockdata, random, true);
        } else {
            // Paper start - use configurable min/max delay
            //world.a(blockposition, (Block) this, MathHelper.nextInt(random, 20, 40));
            world.scheduleUpdate(blockposition, this, MathHelper.getInt(random, world.paperConfig.frostedIceDelayMin, world.paperConfig.frostedIceDelayMax));
            // Paper end
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (block == this) {
            int i = this.countNeighbors(world, blockposition);

            if (i < 2) {
                this.turnIntoWater(world, blockposition);
            }
        }

    }

    private int countNeighbors(World world, BlockPos blockposition) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];

            IBlockState iblockdata1 = world.getTypeIfLoaded(blockposition.offset(enumdirection)); // Paper - don't load chunks
            if (iblockdata1 == null) continue; // Paper
            if (iblockdata1.getBlock() == this) { // Paper
                ++i;
                if (i >= 4) {
                    return i;
                }
            }
        }

        return i;
    }

    protected void slightlyMelt(World world, BlockPos blockposition, IBlockState iblockdata, Random random, boolean flag) {
        int i = ((Integer) iblockdata.getValue(BlockFrostedIce.AGE)).intValue();

        if (i < 3) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockFrostedIce.AGE, Integer.valueOf(i + 1)), 2);
            world.scheduleUpdate(blockposition, (Block) this, MathHelper.getInt(random, 20, 40));
        } else {
            this.turnIntoWater(world, blockposition);
            if (flag) {
                EnumFacing[] aenumdirection = EnumFacing.values();
                int j = aenumdirection.length;

                for (int k = 0; k < j; ++k) {
                    EnumFacing enumdirection = aenumdirection[k];
                    BlockPos blockposition1 = blockposition.offset(enumdirection);
                    IBlockState iblockdata1 = world.getTypeIfLoaded(blockposition1); // Paper - don't load chunks
                    if (iblockdata1 == null) continue; // Paper

                    if (iblockdata1.getBlock() == this) {
                        this.slightlyMelt(world, blockposition1, iblockdata1, random, false);
                    }
                }
            }
        }

    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFrostedIce.AGE});
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.EMPTY;
    }
}
