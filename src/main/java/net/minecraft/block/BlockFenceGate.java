package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS_INWALL = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS_INWALL = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_COLLISION_BOX_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB AABB_COLLISION_BOX_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    public BlockFenceGate(BlockPlanks.EnumType blockwood_enumlogvariant) {
        super(Material.WOOD, blockwood_enumlogvariant.getMapColor());
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        return ((Boolean) iblockdata.getValue(BlockFenceGate.IN_WALL)).booleanValue() ? (((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getAxis() == EnumFacing.Axis.X ? BlockFenceGate.AABB_HITBOX_XAXIS_INWALL : BlockFenceGate.AABB_HITBOX_ZAXIS_INWALL) : (((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getAxis() == EnumFacing.Axis.X ? BlockFenceGate.AABB_HITBOX_XAXIS : BlockFenceGate.AABB_HITBOX_ZAXIS);
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing.Axis enumdirection_enumaxis = ((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getAxis();

        if (enumdirection_enumaxis == EnumFacing.Axis.Z && (iblockaccess.getBlockState(blockposition.west()).getBlock() == Blocks.COBBLESTONE_WALL || iblockaccess.getBlockState(blockposition.east()).getBlock() == Blocks.COBBLESTONE_WALL) || enumdirection_enumaxis == EnumFacing.Axis.X && (iblockaccess.getBlockState(blockposition.north()).getBlock() == Blocks.COBBLESTONE_WALL || iblockaccess.getBlockState(blockposition.south()).getBlock() == Blocks.COBBLESTONE_WALL)) {
            iblockdata = iblockdata.withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(true));
        }

        return iblockdata;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockFenceGate.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)));
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.down()).getMaterial().isSolid() ? super.canPlaceBlockAt(world, blockposition) : false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue() ? BlockFenceGate.NULL_AABB : (((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getAxis() == EnumFacing.Axis.Z ? BlockFenceGate.AABB_COLLISION_BOX_ZAXIS : BlockFenceGate.AABB_COLLISION_BOX_XAXIS);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Boolean) iblockaccess.getBlockState(blockposition).getValue(BlockFenceGate.OPEN)).booleanValue();
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        boolean flag = world.isBlockPowered(blockposition);

        return this.getDefaultState().withProperty(BlockFenceGate.FACING, entityliving.getHorizontalFacing()).withProperty(BlockFenceGate.OPEN, Boolean.valueOf(flag)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(flag)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue()) {
            iblockdata = iblockdata.withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false));
            world.setBlockState(blockposition, iblockdata, 10);
        } else {
            EnumFacing enumdirection1 = EnumFacing.fromAngle((double) entityhuman.rotationYaw);

            if (iblockdata.getValue(BlockFenceGate.FACING) == enumdirection1.getOpposite()) {
                iblockdata = iblockdata.withProperty(BlockFenceGate.FACING, enumdirection1);
            }

            iblockdata = iblockdata.withProperty(BlockFenceGate.OPEN, Boolean.valueOf(true));
            world.setBlockState(blockposition, iblockdata, 10);
        }

        world.playEvent(entityhuman, ((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue() ? 1008 : 1014, blockposition, 0);
        return true;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            boolean flag = world.isBlockPowered(blockposition);

            if (((Boolean) iblockdata.getValue(BlockFenceGate.POWERED)).booleanValue() != flag) {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockFenceGate.POWERED, Boolean.valueOf(flag)).withProperty(BlockFenceGate.OPEN, Boolean.valueOf(flag)), 2);
                if (((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue() != flag) {
                    world.playEvent((EntityPlayer) null, flag ? 1008 : 1014, blockposition, 0);
                }
            }

        }
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.getHorizontal(i)).withProperty(BlockFenceGate.OPEN, Boolean.valueOf((i & 4) != 0)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf((i & 8) != 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getHorizontalIndex();

        if (((Boolean) iblockdata.getValue(BlockFenceGate.POWERED)).booleanValue()) {
            i |= 8;
        }

        if (((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? (((EnumFacing) iblockdata.getValue(BlockFenceGate.FACING)).getAxis() == enumdirection.rotateY().getAxis() ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED) : BlockFaceShape.UNDEFINED;
    }
}
