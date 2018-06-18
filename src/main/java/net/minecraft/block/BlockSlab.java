package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab extends Block {

    public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockSlab(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public BlockSlab(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.fullBlock = this.isDouble();
        this.setLightOpacity(255);
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.isDouble() ? BlockSlab.FULL_BLOCK_AABB : (iblockdata.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? BlockSlab.AABB_TOP_HALF : BlockSlab.AABB_BOTTOM_HALF);
    }

    public boolean isTopSolid(IBlockState iblockdata) {
        return ((BlockSlab) iblockdata.getBlock()).isDouble() || iblockdata.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return ((BlockSlab) iblockdata.getBlock()).isDouble() ? BlockFaceShape.SOLID : (enumdirection == EnumFacing.UP && iblockdata.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? BlockFaceShape.SOLID : (enumdirection == EnumFacing.DOWN && iblockdata.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED));
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return this.isDouble();
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = super.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, i, entityliving).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);

        return this.isDouble() ? iblockdata : (enumdirection != EnumFacing.DOWN && (enumdirection == EnumFacing.UP || (double) f1 <= 0.5D) ? iblockdata : iblockdata.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
    }

    public int quantityDropped(Random random) {
        return this.isDouble() ? 2 : 1;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return this.isDouble();
    }

    public abstract String getUnlocalizedName(int i);

    public abstract boolean isDouble();

    public abstract IProperty<?> getVariantProperty();

    public abstract Comparable<?> getTypeForItem(ItemStack itemstack);

    public static enum EnumBlockHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String name;

        private EnumBlockHalf(String s) {
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
