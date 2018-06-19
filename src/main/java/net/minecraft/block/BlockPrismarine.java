package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;


public class BlockPrismarine extends Block {

    public static final PropertyEnum<BlockPrismarine.EnumType> field_176332_a = PropertyEnum.func_177709_a("variant", BlockPrismarine.EnumType.class);
    public static final int field_176331_b = BlockPrismarine.EnumType.ROUGH.func_176807_a();
    public static final int field_176333_M = BlockPrismarine.EnumType.BRICKS.func_176807_a();
    public static final int field_176334_N = BlockPrismarine.EnumType.DARK.func_176807_a();

    public BlockPrismarine() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPrismarine.field_176332_a, BlockPrismarine.EnumType.ROUGH));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + "." + BlockPrismarine.EnumType.ROUGH.func_176809_c() + ".name");
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177229_b(BlockPrismarine.field_176332_a) == BlockPrismarine.EnumType.ROUGH ? MapColor.field_151679_y : MapColor.field_151648_G;
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPrismarine.EnumType) iblockdata.func_177229_b(BlockPrismarine.field_176332_a)).func_176807_a();
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockPrismarine.EnumType) iblockdata.func_177229_b(BlockPrismarine.field_176332_a)).func_176807_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPrismarine.field_176332_a});
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, BlockPrismarine.EnumType.func_176810_a(i));
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.field_176331_b));
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.field_176333_M));
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.field_176334_N));
    }

    public static enum EnumType implements IStringSerializable {

        ROUGH(0, "prismarine", "rough"), BRICKS(1, "prismarine_bricks", "bricks"), DARK(2, "dark_prismarine", "dark");

        private static final BlockPrismarine.EnumType[] field_176813_d = new BlockPrismarine.EnumType[values().length];
        private final int field_176814_e;
        private final String field_176811_f;
        private final String field_176812_g;

        private EnumType(int i, String s, String s1) {
            this.field_176814_e = i;
            this.field_176811_f = s;
            this.field_176812_g = s1;
        }

        public int func_176807_a() {
            return this.field_176814_e;
        }

        public String toString() {
            return this.field_176811_f;
        }

        public static BlockPrismarine.EnumType func_176810_a(int i) {
            if (i < 0 || i >= BlockPrismarine.EnumType.field_176813_d.length) {
                i = 0;
            }

            return BlockPrismarine.EnumType.field_176813_d[i];
        }

        public String func_176610_l() {
            return this.field_176811_f;
        }

        public String func_176809_c() {
            return this.field_176812_g;
        }

        static {
            BlockPrismarine.EnumType[] ablockprismarine_enumprismarinevariant = values();
            int i = ablockprismarine_enumprismarinevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockPrismarine.EnumType blockprismarine_enumprismarinevariant = ablockprismarine_enumprismarinevariant[j];

                BlockPrismarine.EnumType.field_176813_d[blockprismarine_enumprismarinevariant.func_176807_a()] = blockprismarine_enumprismarinevariant;
            }

        }
    }
}
