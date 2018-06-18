package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends BlockDirectional {

    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.create("type", BlockPistonExtension.EnumPistonType.class);
    public static final PropertyBool SHORT = PropertyBool.create("short");
    protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
    protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
    protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_UP_ARM_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB SHORT_DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB SHORT_SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB SHORT_NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB SHORT_EAST_ARM_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    public BlockPistonExtension() {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPistonExtension.FACING, EnumFacing.NORTH).withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(false)));
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5F);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)) {
        case DOWN:
        default:
            return BlockPistonExtension.PISTON_EXTENSION_DOWN_AABB;

        case UP:
            return BlockPistonExtension.PISTON_EXTENSION_UP_AABB;

        case NORTH:
            return BlockPistonExtension.PISTON_EXTENSION_NORTH_AABB;

        case SOUTH:
            return BlockPistonExtension.PISTON_EXTENSION_SOUTH_AABB;

        case WEST:
            return BlockPistonExtension.PISTON_EXTENSION_WEST_AABB;

        case EAST:
            return BlockPistonExtension.PISTON_EXTENSION_EAST_AABB;
        }
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, iblockdata.getBoundingBox(world, blockposition));
        addCollisionBoxToList(blockposition, axisalignedbb, list, this.getArmShape(iblockdata));
    }

    private AxisAlignedBB getArmShape(IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.getValue(BlockPistonExtension.SHORT)).booleanValue();

        switch ((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)) {
        case DOWN:
        default:
            return flag ? BlockPistonExtension.SHORT_DOWN_ARM_AABB : BlockPistonExtension.DOWN_ARM_AABB;

        case UP:
            return flag ? BlockPistonExtension.SHORT_UP_ARM_AABB : BlockPistonExtension.UP_ARM_AABB;

        case NORTH:
            return flag ? BlockPistonExtension.SHORT_NORTH_ARM_AABB : BlockPistonExtension.NORTH_ARM_AABB;

        case SOUTH:
            return flag ? BlockPistonExtension.SHORT_SOUTH_ARM_AABB : BlockPistonExtension.SOUTH_ARM_AABB;

        case WEST:
            return flag ? BlockPistonExtension.SHORT_WEST_ARM_AABB : BlockPistonExtension.WEST_ARM_AABB;

        case EAST:
            return flag ? BlockPistonExtension.SHORT_EAST_ARM_AABB : BlockPistonExtension.EAST_ARM_AABB;
        }
    }

    public boolean isTopSolid(IBlockState iblockdata) {
        return iblockdata.getValue(BlockPistonExtension.FACING) == EnumFacing.UP;
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (entityhuman.capabilities.isCreativeMode) {
            BlockPos blockposition1 = blockposition.offset(((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)).getOpposite());
            Block block = world.getBlockState(blockposition1).getBlock();

            if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON) {
                world.setBlockToAir(blockposition1);
            }
        }

        super.onBlockHarvested(world, blockposition, iblockdata, entityhuman);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        EnumFacing enumdirection = ((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)).getOpposite();

        blockposition = blockposition.offset(enumdirection);
        IBlockState iblockdata1 = world.getBlockState(blockposition);

        if ((iblockdata1.getBlock() == Blocks.PISTON || iblockdata1.getBlock() == Blocks.STICKY_PISTON) && ((Boolean) iblockdata1.getValue(BlockPistonBase.EXTENDED)).booleanValue()) {
            iblockdata1.getBlock().dropBlockAsItem(world, blockposition, iblockdata1, 0);
            world.setBlockToAir(blockposition);
        }

    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return false;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING);
        BlockPos blockposition2 = blockposition.offset(enumdirection.getOpposite());
        IBlockState iblockdata1 = world.getBlockState(blockposition2);

        if (iblockdata1.getBlock() != Blocks.PISTON && iblockdata1.getBlock() != Blocks.STICKY_PISTON) {
            world.setBlockToAir(blockposition);
        } else {
            iblockdata1.neighborChanged(world, blockposition2, block, blockposition1);
        }

    }

    @Nullable
    public static EnumFacing getFacing(int i) {
        int j = i & 7;

        return j > 5 ? null : EnumFacing.getFront(j);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(iblockdata.getValue(BlockPistonExtension.TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPistonExtension.FACING, getFacing(i)).withProperty(BlockPistonExtension.TYPE, (i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)).getIndex();

        if (iblockdata.getValue(BlockPistonExtension.TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockPistonExtension.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPistonExtension.FACING, BlockPistonExtension.TYPE, BlockPistonExtension.SHORT});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == iblockdata.getValue(BlockPistonExtension.FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum EnumPistonType implements IStringSerializable {

        DEFAULT("normal"), STICKY("sticky");

        private final String VARIANT;

        private EnumPistonType(String s) {
            this.VARIANT = s;
        }

        public String toString() {
            return this.VARIANT;
        }

        public String getName() {
            return this.VARIANT;
        }
    }
}
