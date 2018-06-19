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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;

public class BlockStone extends Block {

    public static final PropertyEnum<BlockStone.EnumType> field_176247_a = PropertyEnum.func_177709_a("variant", BlockStone.EnumType.class);

    public BlockStone() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStone.field_176247_a, BlockStone.EnumType.STONE));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + "." + BlockStone.EnumType.STONE.func_176644_c() + ".name");
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStone.EnumType) iblockdata.func_177229_b(BlockStone.field_176247_a)).func_181072_c();
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return iblockdata.func_177229_b(BlockStone.field_176247_a) == BlockStone.EnumType.STONE ? Item.func_150898_a(Blocks.field_150347_e) : Item.func_150898_a(Blocks.field_150348_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockStone.EnumType) iblockdata.func_177229_b(BlockStone.field_176247_a)).func_176642_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStone.EnumType[] ablockstone_enumstonevariant = BlockStone.EnumType.values();
        int i = ablockstone_enumstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockStone.EnumType blockstone_enumstonevariant = ablockstone_enumstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockstone_enumstonevariant.func_176642_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStone.field_176247_a, BlockStone.EnumType.func_176643_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockStone.EnumType) iblockdata.func_177229_b(BlockStone.field_176247_a)).func_176642_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStone.field_176247_a});
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, MapColor.field_151665_m, "stone", true), GRANITE(1, MapColor.field_151664_l, "granite", true), GRANITE_SMOOTH(2, MapColor.field_151664_l, "smooth_granite", "graniteSmooth", false), DIORITE(3, MapColor.field_151677_p, "diorite", true), DIORITE_SMOOTH(4, MapColor.field_151677_p, "smooth_diorite", "dioriteSmooth", false), ANDESITE(5, MapColor.field_151665_m, "andesite", true), ANDESITE_SMOOTH(6, MapColor.field_151665_m, "smooth_andesite", "andesiteSmooth", false);

        private static final BlockStone.EnumType[] field_176655_h = new BlockStone.EnumType[values().length];
        private final int field_176656_i;
        private final String field_176653_j;
        private final String field_176654_k;
        private final MapColor field_181073_l;
        private final boolean field_190913_m;

        private EnumType(int i, MapColor materialmapcolor, String s, boolean flag) {
            this(i, materialmapcolor, s, s, flag);
        }

        private EnumType(int i, MapColor materialmapcolor, String s, String s1, boolean flag) {
            this.field_176656_i = i;
            this.field_176653_j = s;
            this.field_176654_k = s1;
            this.field_181073_l = materialmapcolor;
            this.field_190913_m = flag;
        }

        public int func_176642_a() {
            return this.field_176656_i;
        }

        public MapColor func_181072_c() {
            return this.field_181073_l;
        }

        public String toString() {
            return this.field_176653_j;
        }

        public static BlockStone.EnumType func_176643_a(int i) {
            if (i < 0 || i >= BlockStone.EnumType.field_176655_h.length) {
                i = 0;
            }

            return BlockStone.EnumType.field_176655_h[i];
        }

        public String func_176610_l() {
            return this.field_176653_j;
        }

        public String func_176644_c() {
            return this.field_176654_k;
        }

        public boolean func_190912_e() {
            return this.field_190913_m;
        }

        static {
            BlockStone.EnumType[] ablockstone_enumstonevariant = values();
            int i = ablockstone_enumstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockStone.EnumType blockstone_enumstonevariant = ablockstone_enumstonevariant[j];

                BlockStone.EnumType.field_176655_h[blockstone_enumstonevariant.func_176642_a()] = blockstone_enumstonevariant;
            }

        }
    }
}
