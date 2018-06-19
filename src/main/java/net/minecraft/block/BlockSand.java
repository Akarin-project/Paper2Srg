package net.minecraft.block;
import net.minecraft.block.material.MapColor;
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


public class BlockSand extends BlockFalling {

    public static final PropertyEnum<BlockSand.EnumType> field_176504_a = PropertyEnum.func_177709_a("variant", BlockSand.EnumType.class);

    public BlockSand() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSand.field_176504_a, BlockSand.EnumType.SAND));
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockSand.EnumType) iblockdata.func_177229_b(BlockSand.field_176504_a)).func_176688_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSand.EnumType[] ablocksand_enumsandvariant = BlockSand.EnumType.values();
        int i = ablocksand_enumsandvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockSand.EnumType blocksand_enumsandvariant = ablocksand_enumsandvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blocksand_enumsandvariant.func_176688_a()));
        }

    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockSand.EnumType) iblockdata.func_177229_b(BlockSand.field_176504_a)).func_176687_c();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSand.field_176504_a, BlockSand.EnumType.func_176686_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockSand.EnumType) iblockdata.func_177229_b(BlockSand.field_176504_a)).func_176688_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSand.field_176504_a});
    }

    public static enum EnumType implements IStringSerializable {

        SAND(0, "sand", "default", MapColor.field_151658_d, -2370656), RED_SAND(1, "red_sand", "red", MapColor.field_151676_q, -5679071);

        private static final BlockSand.EnumType[] field_176695_c = new BlockSand.EnumType[values().length];
        private final int field_176692_d;
        private final String field_176693_e;
        private final MapColor field_176690_f;
        private final String field_176691_g;
        private final int field_189866_h;

        private EnumType(int i, String s, String s1, MapColor materialmapcolor, int j) {
            this.field_176692_d = i;
            this.field_176693_e = s;
            this.field_176690_f = materialmapcolor;
            this.field_176691_g = s1;
            this.field_189866_h = j;
        }

        public int func_176688_a() {
            return this.field_176692_d;
        }

        public String toString() {
            return this.field_176693_e;
        }

        public MapColor func_176687_c() {
            return this.field_176690_f;
        }

        public static BlockSand.EnumType func_176686_a(int i) {
            if (i < 0 || i >= BlockSand.EnumType.field_176695_c.length) {
                i = 0;
            }

            return BlockSand.EnumType.field_176695_c[i];
        }

        public String func_176610_l() {
            return this.field_176693_e;
        }

        public String func_176685_d() {
            return this.field_176691_g;
        }

        static {
            BlockSand.EnumType[] ablocksand_enumsandvariant = values();
            int i = ablocksand_enumsandvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockSand.EnumType blocksand_enumsandvariant = ablocksand_enumsandvariant[j];

                BlockSand.EnumType.field_176695_c[blocksand_enumsandvariant.func_176688_a()] = blocksand_enumsandvariant;
            }

        }
    }
}
