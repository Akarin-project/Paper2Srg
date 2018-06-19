package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPurpurSlab extends BlockSlab {

    public static final PropertyEnum<BlockPurpurSlab.Variant> field_185678_d = PropertyEnum.func_177709_a("variant", BlockPurpurSlab.Variant.class);

    public BlockPurpurSlab() {
        super(Material.field_151576_e, MapColor.field_151675_r);
        IBlockState iblockdata = this.field_176227_L.func_177621_b();

        if (!this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockPurpurSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.func_180632_j(iblockdata.func_177226_a(BlockPurpurSlab.field_185678_d, BlockPurpurSlab.Variant.DEFAULT));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_185771_cX);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_185771_cX);
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockPurpurSlab.field_185678_d, BlockPurpurSlab.Variant.DEFAULT);

        if (!this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockPurpurSlab.field_176554_a, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;

        if (!this.func_176552_j() && iblockdata.func_177229_b(BlockPurpurSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[] { BlockPurpurSlab.field_185678_d}) : new BlockStateContainer(this, new IProperty[] { BlockPurpurSlab.field_176554_a, BlockPurpurSlab.field_185678_d});
    }

    public String func_150002_b(int i) {
        return super.func_149739_a();
    }

    public IProperty<?> func_176551_l() {
        return BlockPurpurSlab.field_185678_d;
    }

    public Comparable<?> func_185674_a(ItemStack itemstack) {
        return BlockPurpurSlab.Variant.DEFAULT;
    }

    public static enum Variant implements IStringSerializable {

        DEFAULT;

        private Variant() {}

        public String func_176610_l() {
            return "default";
        }
    }

    public static class Double extends BlockPurpurSlab {

        public Double() {}

        public boolean func_176552_j() {
            return true;
        }
    }

    public static class Half extends BlockPurpurSlab {

        public Half() {}

        public boolean func_176552_j() {
            return false;
        }
    }
}
