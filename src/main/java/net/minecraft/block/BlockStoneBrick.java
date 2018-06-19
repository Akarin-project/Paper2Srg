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


public class BlockStoneBrick extends Block {

    public static final PropertyEnum<BlockStoneBrick.EnumType> field_176249_a = PropertyEnum.func_177709_a("variant", BlockStoneBrick.EnumType.class);
    public static final int field_176248_b = BlockStoneBrick.EnumType.DEFAULT.func_176612_a();
    public static final int field_176250_M = BlockStoneBrick.EnumType.MOSSY.func_176612_a();
    public static final int field_176251_N = BlockStoneBrick.EnumType.CRACKED.func_176612_a();
    public static final int field_176252_O = BlockStoneBrick.EnumType.CHISELED.func_176612_a();

    public BlockStoneBrick() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.DEFAULT));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockStoneBrick.EnumType) iblockdata.func_177229_b(BlockStoneBrick.field_176249_a)).func_176612_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneBrick.EnumType[] ablocksmoothbrick_enumstonebricktype = BlockStoneBrick.EnumType.values();
        int i = ablocksmoothbrick_enumstonebricktype.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneBrick.EnumType blocksmoothbrick_enumstonebricktype = ablocksmoothbrick_enumstonebricktype[j];

            nonnulllist.add(new ItemStack(this, 1, blocksmoothbrick_enumstonebricktype.func_176612_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.func_176613_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockStoneBrick.EnumType) iblockdata.func_177229_b(BlockStoneBrick.field_176249_a)).func_176612_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStoneBrick.field_176249_a});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "stonebrick", "default"), MOSSY(1, "mossy_stonebrick", "mossy"), CRACKED(2, "cracked_stonebrick", "cracked"), CHISELED(3, "chiseled_stonebrick", "chiseled");

        private static final BlockStoneBrick.EnumType[] field_176618_e = new BlockStoneBrick.EnumType[values().length];
        private final int field_176615_f;
        private final String field_176616_g;
        private final String field_176622_h;

        private EnumType(int i, String s, String s1) {
            this.field_176615_f = i;
            this.field_176616_g = s;
            this.field_176622_h = s1;
        }

        public int func_176612_a() {
            return this.field_176615_f;
        }

        public String toString() {
            return this.field_176616_g;
        }

        public static BlockStoneBrick.EnumType func_176613_a(int i) {
            if (i < 0 || i >= BlockStoneBrick.EnumType.field_176618_e.length) {
                i = 0;
            }

            return BlockStoneBrick.EnumType.field_176618_e[i];
        }

        public String func_176610_l() {
            return this.field_176616_g;
        }

        public String func_176614_c() {
            return this.field_176622_h;
        }

        static {
            BlockStoneBrick.EnumType[] ablocksmoothbrick_enumstonebricktype = values();
            int i = ablocksmoothbrick_enumstonebricktype.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneBrick.EnumType blocksmoothbrick_enumstonebricktype = ablocksmoothbrick_enumstonebricktype[j];

                BlockStoneBrick.EnumType.field_176618_e[blocksmoothbrick_enumstonebricktype.func_176612_a()] = blocksmoothbrick_enumstonebricktype;
            }

        }
    }
}
