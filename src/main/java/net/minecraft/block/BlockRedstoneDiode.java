package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public abstract class BlockRedstoneDiode extends BlockHorizontal {

    protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected final boolean isRepeaterPowered;

    protected BlockRedstoneDiode(boolean flag) {
        super(Material.CIRCUITS);
        this.isRepeaterPowered = flag;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRedstoneDiode.REDSTONE_DIODE_AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.down()).isTopSolid() ? super.canPlaceBlockAt(world, blockposition) : false;
    }

    public boolean canBlockStay(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.down()).isTopSolid();
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.isLocked((IBlockAccess) world, blockposition, iblockdata)) {
            boolean flag = this.shouldBePowered(world, blockposition, iblockdata);

            if (this.isRepeaterPowered && !flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, this.getUnpoweredState(iblockdata), 2);
            } else if (!this.isRepeaterPowered) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, this.getPoweredState(iblockdata), 2);
                if (!flag) {
                    world.updateBlockTick(blockposition, this.getPoweredState(iblockdata).getBlock(), this.getTickDelay(iblockdata), -1);
                }
            }

        }
    }

    protected boolean isPowered(IBlockState iblockdata) {
        return this.isRepeaterPowered;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return iblockdata.getWeakPower(iblockaccess, blockposition, enumdirection);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !this.isPowered(iblockdata) ? 0 : (iblockdata.getValue(BlockRedstoneDiode.FACING) == enumdirection ? this.getActiveSignal(iblockaccess, blockposition, iblockdata) : 0);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.canBlockStay(world, blockposition)) {
            this.updateState(world, blockposition, iblockdata);
        } else {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection), this, false);
            }

        }
    }

    protected void updateState(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.isLocked((IBlockAccess) world, blockposition, iblockdata)) {
            boolean flag = this.shouldBePowered(world, blockposition, iblockdata);

            if (this.isRepeaterPowered != flag && !world.isBlockTickPending(blockposition, (Block) this)) {
                byte b0 = -1;

                if (this.isFacingTowardsRepeater(world, blockposition, iblockdata)) {
                    b0 = -3;
                } else if (this.isRepeaterPowered) {
                    b0 = -2;
                }

                world.updateBlockTick(blockposition, this, this.getDelay(iblockdata), b0);
            }

        }
    }

    public boolean isLocked(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return false;
    }

    protected boolean shouldBePowered(World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.calculateInputStrength(world, blockposition, iblockdata) > 0;
    }

    protected int calculateInputStrength(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneDiode.FACING);
        BlockPos blockposition1 = blockposition.offset(enumdirection);
        int i = world.getRedstonePower(blockposition1, enumdirection);

        if (i >= 15) {
            return i;
        } else {
            IBlockState iblockdata1 = world.getBlockState(blockposition1);

            return Math.max(i, iblockdata1.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer) iblockdata1.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }

    protected int getPowerOnSides(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneDiode.FACING);
        EnumFacing enumdirection1 = enumdirection.rotateY();
        EnumFacing enumdirection2 = enumdirection.rotateYCCW();

        return Math.max(this.getPowerOnSide(iblockaccess, blockposition.offset(enumdirection1), enumdirection1), this.getPowerOnSide(iblockaccess, blockposition.offset(enumdirection2), enumdirection2));
    }

    protected int getPowerOnSide(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        return this.isAlternateInput(iblockdata) ? (block == Blocks.REDSTONE_BLOCK ? 15 : (block == Blocks.REDSTONE_WIRE ? ((Integer) iblockdata.getValue(BlockRedstoneWire.POWER)).intValue() : iblockaccess.getStrongPower(blockposition, enumdirection))) : 0;
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockRedstoneDiode.FACING, entityliving.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (this.shouldBePowered(world, blockposition, iblockdata)) {
            world.scheduleUpdate(blockposition, (Block) this, 1);
        }

    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.notifyNeighbors(world, blockposition, iblockdata);
    }

    protected void notifyNeighbors(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneDiode.FACING);
        BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());

        world.neighborChanged(blockposition1, (Block) this, blockposition);
        world.notifyNeighborsOfStateExcept(blockposition1, (Block) this, enumdirection);
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.isRepeaterPowered) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection), this, false);
            }
        }

        super.onBlockDestroyedByPlayer(world, blockposition, iblockdata);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    protected boolean isAlternateInput(IBlockState iblockdata) {
        return iblockdata.canProvidePower();
    }

    protected int getActiveSignal(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return 15;
    }

    public static boolean isDiode(IBlockState iblockdata) {
        return Blocks.UNPOWERED_REPEATER.isSameDiode(iblockdata) || Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockdata);
    }

    public boolean isSameDiode(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    public boolean isFacingTowardsRepeater(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = ((EnumFacing) iblockdata.getValue(BlockRedstoneDiode.FACING)).getOpposite();
        BlockPos blockposition1 = blockposition.offset(enumdirection);

        return isDiode(world.getBlockState(blockposition1)) ? world.getBlockState(blockposition1).getValue(BlockRedstoneDiode.FACING) != enumdirection : false;
    }

    protected int getTickDelay(IBlockState iblockdata) {
        return this.getDelay(iblockdata);
    }

    protected abstract int getDelay(IBlockState iblockdata);

    protected abstract IBlockState getPoweredState(IBlockState iblockdata);

    protected abstract IBlockState getUnpoweredState(IBlockState iblockdata);

    public boolean isAssociatedBlock(Block block) {
        return this.isSameDiode(block.getDefaultState());
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
