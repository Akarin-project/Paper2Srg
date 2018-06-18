package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockTripWireHook extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    protected static final AxisAlignedBB HOOK_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.625D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.625D, 0.375D);
    protected static final AxisAlignedBB HOOK_WEST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB HOOK_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 0.375D, 0.625D, 0.6875D);

    public BlockTripWireHook() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTripWireHook.FACING, EnumFacing.NORTH).withProperty(BlockTripWireHook.POWERED, Boolean.valueOf(false)).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING)) {
        case EAST:
        default:
            return BlockTripWireHook.HOOK_EAST_AABB;

        case WEST:
            return BlockTripWireHook.HOOK_WEST_AABB;

        case SOUTH:
            return BlockTripWireHook.HOOK_SOUTH_AABB;

        case NORTH:
            return BlockTripWireHook.HOOK_NORTH_AABB;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTripWireHook.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        EnumFacing enumdirection1 = enumdirection.getOpposite();
        BlockPos blockposition1 = blockposition.offset(enumdirection1);
        IBlockState iblockdata = world.getBlockState(blockposition1);
        boolean flag = isExceptBlockForAttachWithPiston(iblockdata.getBlock());

        return !flag && enumdirection.getAxis().isHorizontal() && iblockdata.getBlockFaceShape(world, blockposition1, enumdirection) == BlockFaceShape.SOLID && !iblockdata.canProvidePower();
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        EnumFacing enumdirection;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            enumdirection = (EnumFacing) iterator.next();
        } while (!this.canPlaceBlockOnSide(world, blockposition, enumdirection));

        return true;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockTripWireHook.POWERED, Boolean.valueOf(false)).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(false));

        if (enumdirection.getAxis().isHorizontal()) {
            iblockdata = iblockdata.withProperty(BlockTripWireHook.FACING, enumdirection);
        }

        return iblockdata;
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        this.calculateState(world, blockposition, iblockdata, false, false, -1, (IBlockState) null);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (block != this) {
            if (this.checkForDrop(world, blockposition, iblockdata)) {
                EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING);

                if (!this.canPlaceBlockOnSide(world, blockposition, enumdirection)) {
                    this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                    world.setBlockToAir(blockposition);
                }
            }

        }
    }

    public void calculateState(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag, boolean flag1, int i, @Nullable IBlockState iblockdata1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING);
        boolean flag2 = ((Boolean) iblockdata.getValue(BlockTripWireHook.ATTACHED)).booleanValue();
        boolean flag3 = ((Boolean) iblockdata.getValue(BlockTripWireHook.POWERED)).booleanValue();
        boolean flag4 = !flag;
        boolean flag5 = false;
        int j = 0;
        IBlockState[] aiblockdata = new IBlockState[42];

        BlockPos blockposition1;

        for (int k = 1; k < 42; ++k) {
            blockposition1 = blockposition.offset(enumdirection, k);
            IBlockState iblockdata2 = world.getBlockState(blockposition1);

            if (iblockdata2.getBlock() == Blocks.TRIPWIRE_HOOK) {
                if (iblockdata2.getValue(BlockTripWireHook.FACING) == enumdirection.getOpposite()) {
                    j = k;
                }
                break;
            }

            if (iblockdata2.getBlock() != Blocks.TRIPWIRE && k != i) {
                aiblockdata[k] = null;
                flag4 = false;
            } else {
                if (k == i) {
                    iblockdata2 = (IBlockState) MoreObjects.firstNonNull(iblockdata1, iblockdata2);
                }

                boolean flag6 = !((Boolean) iblockdata2.getValue(BlockTripWire.DISARMED)).booleanValue();
                boolean flag7 = ((Boolean) iblockdata2.getValue(BlockTripWire.POWERED)).booleanValue();

                flag5 |= flag6 && flag7;
                aiblockdata[k] = iblockdata2;
                if (k == i) {
                    world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
                    flag4 &= flag6;
                }
            }
        }

        flag4 &= j > 1;
        flag5 &= flag4;
        IBlockState iblockdata3 = this.getDefaultState().withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(flag4)).withProperty(BlockTripWireHook.POWERED, Boolean.valueOf(flag5));

        if (j > 0) {
            blockposition1 = blockposition.offset(enumdirection, j);
            EnumFacing enumdirection1 = enumdirection.getOpposite();

            world.setBlockState(blockposition1, iblockdata3.withProperty(BlockTripWireHook.FACING, enumdirection1), 3);
            this.notifyNeighbors(world, blockposition1, enumdirection1);
            this.playSound(world, blockposition1, flag4, flag5, flag2, flag3);
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
        world.getServer().getPluginManager().callEvent(eventRedstone);

        if (eventRedstone.getNewCurrent() > 0) {
            return;
        }
        // CraftBukkit end

        this.playSound(world, blockposition, flag4, flag5, flag2, flag3);
        if (!flag) {
            world.setBlockState(blockposition, iblockdata3.withProperty(BlockTripWireHook.FACING, enumdirection), 3);
            if (flag1) {
                this.notifyNeighbors(world, blockposition, enumdirection);
            }
        }

        if (flag2 != flag4) {
            for (int l = 1; l < j; ++l) {
                BlockPos blockposition2 = blockposition.offset(enumdirection, l);
                IBlockState iblockdata4 = aiblockdata[l];

                if (iblockdata4 != null && world.getBlockState(blockposition2).getMaterial() != Material.AIR) {
                    world.setBlockState(blockposition2, iblockdata4.withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(flag4)), 3);
                }
            }
        }

    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.calculateState(world, blockposition, iblockdata, false, true, -1, (IBlockState) null);
    }

    private void playSound(World world, BlockPos blockposition, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        if (flag1 && !flag3) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4F, 0.6F);
        } else if (!flag1 && flag3) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4F, 0.5F);
        } else if (flag && !flag2) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4F, 0.7F);
        } else if (!flag && flag2) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4F, 1.2F / (world.rand.nextFloat() * 0.2F + 0.9F));
        }

    }

    private void notifyNeighbors(World world, BlockPos blockposition, EnumFacing enumdirection) {
        world.notifyNeighborsOfStateChange(blockposition, this, false);
        world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection.getOpposite()), this, false);
    }

    private boolean checkForDrop(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.canPlaceBlockAt(world, blockposition)) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        } else {
            return true;
        }
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.getValue(BlockTripWireHook.ATTACHED)).booleanValue();
        boolean flag1 = ((Boolean) iblockdata.getValue(BlockTripWireHook.POWERED)).booleanValue();

        if (flag || flag1) {
            this.calculateState(world, blockposition, iblockdata, true, false, -1, (IBlockState) null);
        }

        if (flag1) {
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            world.notifyNeighborsOfStateChange(blockposition.offset(((EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING)).getOpposite()), this, false);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.getValue(BlockTripWireHook.POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.getValue(BlockTripWireHook.POWERED)).booleanValue() ? 0 : (iblockdata.getValue(BlockTripWireHook.FACING) == enumdirection ? 15 : 0);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.getHorizontal(i & 3)).withProperty(BlockTripWireHook.POWERED, Boolean.valueOf((i & 8) > 0)).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf((i & 4) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING)).getHorizontalIndex();

        if (((Boolean) iblockdata.getValue(BlockTripWireHook.POWERED)).booleanValue()) {
            i |= 8;
        }

        if (((Boolean) iblockdata.getValue(BlockTripWireHook.ATTACHED)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockTripWireHook.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockTripWireHook.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTripWireHook.FACING, BlockTripWireHook.POWERED, BlockTripWireHook.ATTACHED});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
