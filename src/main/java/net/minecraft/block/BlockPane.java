package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] { new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private final boolean canDrop;

    protected BlockPane(Material material, boolean flag) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)));
        this.canDrop = flag;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.getActualState(iblockdata, world, blockposition);
        }

        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockPane.AABB_BY_INDEX[0]);
        if (((Boolean) iblockdata.getValue(BlockPane.NORTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockPane.AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.NORTH)]);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.SOUTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockPane.AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.SOUTH)]);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.EAST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockPane.AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.EAST)]);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.WEST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockPane.AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.WEST)]);
        }

    }

    private static int getBoundingBoxIndex(EnumFacing enumdirection) {
        return 1 << enumdirection.getHorizontalIndex();
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        return BlockPane.AABB_BY_INDEX[getBoundingBoxIndex(iblockdata)];
    }

    private static int getBoundingBoxIndex(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.getValue(BlockPane.NORTH)).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.NORTH);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.EAST)).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.EAST);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.SOUTH)).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.SOUTH);
        }

        if (((Boolean) iblockdata.getValue(BlockPane.WEST)).booleanValue()) {
            i |= getBoundingBoxIndex(EnumFacing.WEST);
        }

        return i;
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.withProperty(BlockPane.NORTH, Boolean.valueOf(this.attachesTo(iblockaccess, iblockaccess.getBlockState(blockposition.north()), blockposition.north(), EnumFacing.SOUTH))).withProperty(BlockPane.SOUTH, Boolean.valueOf(this.attachesTo(iblockaccess, iblockaccess.getBlockState(blockposition.south()), blockposition.south(), EnumFacing.NORTH))).withProperty(BlockPane.WEST, Boolean.valueOf(this.attachesTo(iblockaccess, iblockaccess.getBlockState(blockposition.west()), blockposition.west(), EnumFacing.EAST))).withProperty(BlockPane.EAST, Boolean.valueOf(this.attachesTo(iblockaccess, iblockaccess.getBlockState(blockposition.east()), blockposition.east(), EnumFacing.WEST)));
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return !this.canDrop ? Items.AIR : super.getItemDropped(iblockdata, random, i);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public final boolean attachesTo(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        Block block = iblockdata.getBlock();
        BlockFaceShape enumblockfaceshape = iblockdata.getBlockFaceShape(iblockaccess, blockposition, enumdirection);

        return !isExcepBlockForAttachWithPiston(block) && enumblockfaceshape == BlockFaceShape.SOLID || enumblockfaceshape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    protected static boolean isExcepBlockForAttachWithPiston(Block block) {
        return block instanceof BlockShulkerBox || block instanceof BlockLeaves || block == Blocks.BEACON || block == Blocks.CAULDRON || block == Blocks.GLOWSTONE || block == Blocks.ICE || block == Blocks.SEA_LANTERN || block == Blocks.PISTON || block == Blocks.STICKY_PISTON || block == Blocks.PISTON_HEAD || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN || block == Blocks.BARRIER;
    }

    protected boolean canSilkHarvest() {
        return true;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.withProperty(BlockPane.NORTH, iblockdata.getValue(BlockPane.SOUTH)).withProperty(BlockPane.EAST, iblockdata.getValue(BlockPane.WEST)).withProperty(BlockPane.SOUTH, iblockdata.getValue(BlockPane.NORTH)).withProperty(BlockPane.WEST, iblockdata.getValue(BlockPane.EAST));

        case COUNTERCLOCKWISE_90:
            return iblockdata.withProperty(BlockPane.NORTH, iblockdata.getValue(BlockPane.EAST)).withProperty(BlockPane.EAST, iblockdata.getValue(BlockPane.SOUTH)).withProperty(BlockPane.SOUTH, iblockdata.getValue(BlockPane.WEST)).withProperty(BlockPane.WEST, iblockdata.getValue(BlockPane.NORTH));

        case CLOCKWISE_90:
            return iblockdata.withProperty(BlockPane.NORTH, iblockdata.getValue(BlockPane.WEST)).withProperty(BlockPane.EAST, iblockdata.getValue(BlockPane.NORTH)).withProperty(BlockPane.SOUTH, iblockdata.getValue(BlockPane.EAST)).withProperty(BlockPane.WEST, iblockdata.getValue(BlockPane.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.withProperty(BlockPane.NORTH, iblockdata.getValue(BlockPane.SOUTH)).withProperty(BlockPane.SOUTH, iblockdata.getValue(BlockPane.NORTH));

        case FRONT_BACK:
            return iblockdata.withProperty(BlockPane.EAST, iblockdata.getValue(BlockPane.WEST)).withProperty(BlockPane.WEST, iblockdata.getValue(BlockPane.EAST));

        default:
            return super.withMirror(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPane.NORTH, BlockPane.EAST, BlockPane.WEST, BlockPane.SOUTH});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
    }
}
