package net.minecraft.block;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

public class BlockEndPortalFrame extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool EYE = PropertyBool.create("eye");
    protected static final AxisAlignedBB AABB_BLOCK = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_EYE = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    private static BlockPattern portalShape;

    public BlockEndPortalFrame() {
        super(Material.ROCK, MapColor.GREEN);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.NORTH).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(false)));
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEndPortalFrame.AABB_BLOCK;
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockEndPortalFrame.AABB_BLOCK);
        if (((Boolean) world.getBlockState(blockposition).getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockEndPortalFrame.AABB_EYE);
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockEndPortalFrame.FACING, entityliving.getHorizontalFacing().getOpposite()).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(false));
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return ((Boolean) iblockdata.getValue(BlockEndPortalFrame.EYE)).booleanValue() ? 15 : 0;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf((i & 4) != 0)).withProperty(BlockEndPortalFrame.FACING, EnumFacing.getHorizontal(i & 3));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockEndPortalFrame.FACING)).getHorizontalIndex();

        if (((Boolean) iblockdata.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockEndPortalFrame.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockEndPortalFrame.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockEndPortalFrame.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockEndPortalFrame.FACING, BlockEndPortalFrame.EYE});
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public static BlockPattern getOrCreatePortalShape() {
        if (BlockEndPortalFrame.portalShape == null) {
            BlockEndPortalFrame.portalShape = FactoryBlockPattern.start().aisle(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?"}).where('?', BlockWorldState.hasState(BlockStateMatcher.ANY)).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(BlockEndPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).where(BlockEndPortalFrame.FACING, Predicates.equalTo(EnumFacing.SOUTH)))).where('>', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(BlockEndPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).where(BlockEndPortalFrame.FACING, Predicates.equalTo(EnumFacing.WEST)))).where('v', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(BlockEndPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).where(BlockEndPortalFrame.FACING, Predicates.equalTo(EnumFacing.NORTH)))).where('<', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(BlockEndPortalFrame.EYE, Predicates.equalTo(Boolean.valueOf(true))).where(BlockEndPortalFrame.FACING, Predicates.equalTo(EnumFacing.EAST)))).build();
        }

        return BlockEndPortalFrame.portalShape;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
