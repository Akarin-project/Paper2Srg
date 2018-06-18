package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockTrapDoor extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockTrapDoor(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTrapDoor.FACING, EnumFacing.NORTH).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockTrapDoor.HALF, BlockTrapDoor.DoorHalf.BOTTOM));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb;

        if (((Boolean) iblockdata.getValue(BlockTrapDoor.OPEN)).booleanValue()) {
            switch ((EnumFacing) iblockdata.getValue(BlockTrapDoor.FACING)) {
            case NORTH:
            default:
                axisalignedbb = BlockTrapDoor.NORTH_OPEN_AABB;
                break;

            case SOUTH:
                axisalignedbb = BlockTrapDoor.SOUTH_OPEN_AABB;
                break;

            case WEST:
                axisalignedbb = BlockTrapDoor.WEST_OPEN_AABB;
                break;

            case EAST:
                axisalignedbb = BlockTrapDoor.EAST_OPEN_AABB;
            }
        } else if (iblockdata.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.TOP) {
            axisalignedbb = BlockTrapDoor.TOP_AABB;
        } else {
            axisalignedbb = BlockTrapDoor.BOTTOM_AABB;
        }

        return axisalignedbb;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return !((Boolean) iblockaccess.getBlockState(blockposition).getValue(BlockTrapDoor.OPEN)).booleanValue();
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (this.blockMaterial == Material.IRON) {
            return false;
        } else {
            iblockdata = iblockdata.cycleProperty((IProperty) BlockTrapDoor.OPEN);
            world.setBlockState(blockposition, iblockdata, 2);
            this.playSound(entityhuman, world, blockposition, ((Boolean) iblockdata.getValue(BlockTrapDoor.OPEN)).booleanValue());
            return true;
        }
    }

    protected void playSound(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition, boolean flag) {
        int i;

        if (flag) {
            i = this.blockMaterial == Material.IRON ? 1037 : 1007;
            world.playEvent(entityhuman, i, blockposition, 0);
        } else {
            i = this.blockMaterial == Material.IRON ? 1036 : 1013;
            world.playEvent(entityhuman, i, blockposition, 0);
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            boolean flag = world.isBlockPowered(blockposition);

            if (flag || block.getDefaultState().canProvidePower()) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                int power = bblock.getBlockPower();
                int oldPower = (Boolean) iblockdata.getValue(OPEN) ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || block.getDefaultState().hasComparatorInputOverride()) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);
                    flag = eventRedstone.getNewCurrent() > 0;
                }
                // CraftBukkit end
                boolean flag1 = ((Boolean) iblockdata.getValue(BlockTrapDoor.OPEN)).booleanValue();

                if (flag1 != flag) {
                    world.setBlockState(blockposition, iblockdata.withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(flag)), 2);
                    this.playSound((EntityPlayer) null, world, blockposition, flag);
                }
            }

        }
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.getDefaultState();

        if (enumdirection.getAxis().isHorizontal()) {
            iblockdata = iblockdata.withProperty(BlockTrapDoor.FACING, enumdirection).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(false));
            iblockdata = iblockdata.withProperty(BlockTrapDoor.HALF, f1 > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        } else {
            iblockdata = iblockdata.withProperty(BlockTrapDoor.FACING, entityliving.getHorizontalFacing().getOpposite()).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(false));
            iblockdata = iblockdata.withProperty(BlockTrapDoor.HALF, enumdirection == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
        }

        if (world.isBlockPowered(blockposition)) {
            iblockdata = iblockdata.withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(true));
        }

        return iblockdata;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return true;
    }

    protected static EnumFacing getFacing(int i) {
        switch (i & 3) {
        case 0:
            return EnumFacing.NORTH;

        case 1:
            return EnumFacing.SOUTH;

        case 2:
            return EnumFacing.WEST;

        case 3:
        default:
            return EnumFacing.EAST;
        }
    }

    protected static int getMetaForFacing(EnumFacing enumdirection) {
        switch (enumdirection) {
        case NORTH:
            return 0;

        case SOUTH:
            return 1;

        case WEST:
            return 2;

        case EAST:
        default:
            return 3;
        }
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockTrapDoor.FACING, getFacing(i)).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf((i & 4) != 0)).withProperty(BlockTrapDoor.HALF, (i & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | getMetaForFacing((EnumFacing) iblockdata.getValue(BlockTrapDoor.FACING));

        if (((Boolean) iblockdata.getValue(BlockTrapDoor.OPEN)).booleanValue()) {
            i |= 4;
        }

        if (iblockdata.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockTrapDoor.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockTrapDoor.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockTrapDoor.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTrapDoor.FACING, BlockTrapDoor.OPEN, BlockTrapDoor.HALF});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return (enumdirection == EnumFacing.UP && iblockdata.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.TOP || enumdirection == EnumFacing.DOWN && iblockdata.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !((Boolean) iblockdata.getValue(BlockTrapDoor.OPEN)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum DoorHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String name;

        private DoorHalf(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
