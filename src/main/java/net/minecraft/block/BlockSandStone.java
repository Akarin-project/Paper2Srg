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
import net.minecraft.world.IBlockAccess;


public class BlockSandStone extends Block {

    public static final PropertyEnum<BlockSandStone.EnumType> field_176297_a = PropertyEnum.func_177709_a("type", BlockSandStone.EnumType.class);

    public BlockSandStone() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSandStone.field_176297_a, BlockSandStone.EnumType.DEFAULT));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockSandStone.EnumType) iblockdata.func_177229_b(BlockSandStone.field_176297_a)).func_176675_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSandStone.EnumType[] ablocksandstone_enumsandstonevariant = BlockSandStone.EnumType.values();
        int i = ablocksandstone_enumsandstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockSandStone.EnumType blocksandstone_enumsandstonevariant = ablocksandstone_enumsandstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blocksandstone_enumsandstonevariant.func_176675_a()));
        }

    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151658_d;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSandStone.field_176297_a, BlockSandStone.EnumType.func_176673_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockSandStone.EnumType) iblockdata.func_177229_b(BlockSandStone.field_176297_a)).func_176675_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSandStone.field_176297_a});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "sandstone", "default"), CHISELED(1, "chiseled_sandstone", "chiseled"), SMOOTH(2, "smooth_sandstone", "smooth");

        private static final BlockSandStone.EnumType[] field_176679_d = new BlockSandStone.EnumType[values().length];
        private final int field_176680_e;
        private final String field_176677_f;
        private final String field_176678_g;

        private EnumType(int i, String s, String s1) {
            this.field_176680_e = i;
            this.field_176677_f = s;
            this.field_176678_g = s1;
        }

        public int func_176675_a() {
            return this.field_176680_e;
        }

        public String toString() {
            return this.field_176677_f;
        }

        public static BlockSandStone.EnumType func_176673_a(int i) {
            if (i < 0 || i >= BlockSandStone.EnumType.field_176679_d.length) {
                i = 0;
            }

            return BlockSandStone.EnumType.field_176679_d[i];
        }

        public String func_176610_l() {
            return this.field_176677_f;
        }

        public String func_176676_c() {
            return this.field_176678_g;
        }

        static {
            BlockSandStone.EnumType[] ablocksandstone_enumsandstonevariant = values();
            int i = ablocksandstone_enumsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockSandStone.EnumType blocksandstone_enumsandstonevariant = ablocksandstone_enumsandstonevariant[j];

                BlockSandStone.EnumType.field_176679_d[blocksandstone_enumsandstonevariant.func_176675_a()] = blocksandstone_enumsandstonevariant;
            }

        }
    }
}
