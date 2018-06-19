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

    public static final PropertyEnum<BlockSlab.EnumBlockHalf> field_176554_a = PropertyEnum.func_177709_a("half", BlockSlab.EnumBlockHalf.class);
    protected static final AxisAlignedBB field_185676_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185677_c = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockSlab(Material material) {
        this(material, material.func_151565_r());
    }

    public BlockSlab(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.field_149787_q = this.func_176552_j();
        this.func_149713_g(255);
    }

    protected boolean func_149700_E() {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.func_176552_j() ? BlockSlab.field_185505_j : (iblockdata.func_177229_b(BlockSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP ? BlockSlab.field_185677_c : BlockSlab.field_185676_b);
    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return ((BlockSlab) iblockdata.func_177230_c()).func_176552_j() || iblockdata.func_177229_b(BlockSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return ((BlockSlab) iblockdata.func_177230_c()).func_176552_j() ? BlockFaceShape.SOLID : (enumdirection == EnumFacing.UP && iblockdata.func_177229_b(BlockSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP ? BlockFaceShape.SOLID : (enumdirection == EnumFacing.DOWN && iblockdata.func_177229_b(BlockSlab.field_176554_a) == BlockSlab.EnumBlockHalf.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return this.func_176552_j();
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = super.func_180642_a(world, blockposition, enumdirection, f, f1, f2, i, entityliving).func_177226_a(BlockSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);

        return this.func_176552_j() ? iblockdata : (enumdirection != EnumFacing.DOWN && (enumdirection == EnumFacing.UP || (double) f1 <= 0.5D) ? iblockdata : iblockdata.func_177226_a(BlockSlab.field_176554_a, BlockSlab.EnumBlockHalf.TOP));
    }

    public int func_149745_a(Random random) {
        return this.func_176552_j() ? 2 : 1;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return this.func_176552_j();
    }

    public abstract String func_150002_b(int i);

    public abstract boolean func_176552_j();

    public abstract IProperty<?> func_176551_l();

    public abstract Comparable<?> func_185674_a(ItemStack itemstack);

    public static enum EnumBlockHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String field_176988_c;

        private EnumBlockHalf(String s) {
            this.field_176988_c = s;
        }

        public String toString() {
            return this.field_176988_c;
        }

        public String func_176610_l() {
            return this.field_176988_c;
        }
    }
}
