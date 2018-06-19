package net.minecraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;


public class BlockRedSandstone extends Block {

    public static final PropertyEnum<BlockRedSandstone.EnumType> field_176336_a = PropertyEnum.func_177709_a("type", BlockRedSandstone.EnumType.class);

    public BlockRedSandstone() {
        super(Material.field_151576_e, BlockSand.EnumType.RED_SAND.func_176687_c());
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRedSandstone.field_176336_a, BlockRedSandstone.EnumType.DEFAULT));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockRedSandstone.EnumType) iblockdata.func_177229_b(BlockRedSandstone.field_176336_a)).func_176827_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockRedSandstone.EnumType[] ablockredsandstone_enumredsandstonevariant = BlockRedSandstone.EnumType.values();
        int i = ablockredsandstone_enumredsandstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockRedSandstone.EnumType blockredsandstone_enumredsandstonevariant = ablockredsandstone_enumredsandstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockredsandstone_enumredsandstonevariant.func_176827_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRedSandstone.field_176336_a, BlockRedSandstone.EnumType.func_176825_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockRedSandstone.EnumType) iblockdata.func_177229_b(BlockRedSandstone.field_176336_a)).func_176827_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedSandstone.field_176336_a});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "red_sandstone", "default"), CHISELED(1, "chiseled_red_sandstone", "chiseled"), SMOOTH(2, "smooth_red_sandstone", "smooth");

        private static final BlockRedSandstone.EnumType[] field_176831_d = new BlockRedSandstone.EnumType[values().length];
        private final int field_176832_e;
        private final String field_176829_f;
        private final String field_176830_g;

        private EnumType(int i, String s, String s1) {
            this.field_176832_e = i;
            this.field_176829_f = s;
            this.field_176830_g = s1;
        }

        public int func_176827_a() {
            return this.field_176832_e;
        }

        public String toString() {
            return this.field_176829_f;
        }

        public static BlockRedSandstone.EnumType func_176825_a(int i) {
            if (i < 0 || i >= BlockRedSandstone.EnumType.field_176831_d.length) {
                i = 0;
            }

            return BlockRedSandstone.EnumType.field_176831_d[i];
        }

        public String func_176610_l() {
            return this.field_176829_f;
        }

        public String func_176828_c() {
            return this.field_176830_g;
        }

        static {
            BlockRedSandstone.EnumType[] ablockredsandstone_enumredsandstonevariant = values();
            int i = ablockredsandstone_enumredsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockRedSandstone.EnumType blockredsandstone_enumredsandstonevariant = ablockredsandstone_enumredsandstonevariant[j];

                BlockRedSandstone.EnumType.field_176831_d[blockredsandstone_enumredsandstonevariant.func_176827_a()] = blockredsandstone_enumredsandstonevariant;
            }

        }
    }
}
