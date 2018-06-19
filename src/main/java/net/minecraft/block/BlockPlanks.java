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


public class BlockPlanks extends Block {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176383_a = PropertyEnum.func_177709_a("variant", BlockPlanks.EnumType.class);

    public BlockPlanks() {
        super(Material.field_151575_d);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.OAK));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockPlanks.field_176383_a)).func_176839_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockPlanks.EnumType[] ablockwood_enumlogvariant = BlockPlanks.EnumType.values();
        int i = ablockwood_enumlogvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockwood_enumlogvariant.func_176839_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.func_176837_a(i));
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockPlanks.field_176383_a)).func_181070_c();
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockPlanks.field_176383_a)).func_176839_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPlanks.field_176383_a});
    }

    public static enum EnumType implements IStringSerializable {

        OAK(0, "oak", MapColor.field_151663_o), SPRUCE(1, "spruce", MapColor.field_151654_J), BIRCH(2, "birch", MapColor.field_151658_d), JUNGLE(3, "jungle", MapColor.field_151664_l), ACACIA(4, "acacia", MapColor.field_151676_q), DARK_OAK(5, "dark_oak", "big_oak", MapColor.field_151650_B);

        private static final BlockPlanks.EnumType[] field_176842_g = new BlockPlanks.EnumType[values().length];
        private final int field_176850_h;
        private final String field_176851_i;
        private final String field_176848_j;
        private final MapColor field_181071_k;

        private EnumType(int i, String s, MapColor materialmapcolor) {
            this(i, s, s, materialmapcolor);
        }

        private EnumType(int i, String s, String s1, MapColor materialmapcolor) {
            this.field_176850_h = i;
            this.field_176851_i = s;
            this.field_176848_j = s1;
            this.field_181071_k = materialmapcolor;
        }

        public int func_176839_a() {
            return this.field_176850_h;
        }

        public MapColor func_181070_c() {
            return this.field_181071_k;
        }

        public String toString() {
            return this.field_176851_i;
        }

        public static BlockPlanks.EnumType func_176837_a(int i) {
            if (i < 0 || i >= BlockPlanks.EnumType.field_176842_g.length) {
                i = 0;
            }

            return BlockPlanks.EnumType.field_176842_g[i];
        }

        public String func_176610_l() {
            return this.field_176851_i;
        }

        public String func_176840_c() {
            return this.field_176848_j;
        }

        static {
            BlockPlanks.EnumType[] ablockwood_enumlogvariant = values();
            int i = ablockwood_enumlogvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

                BlockPlanks.EnumType.field_176842_g[blockwood_enumlogvariant.func_176839_a()] = blockwood_enumlogvariant;
            }

        }
    }
}
