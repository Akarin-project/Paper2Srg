package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockQuartz extends Block {

    public static final PropertyEnum<BlockQuartz.EnumType> field_176335_a = PropertyEnum.func_177709_a("variant", BlockQuartz.EnumType.class);

    public BlockQuartz() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.DEFAULT));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (i == BlockQuartz.EnumType.LINES_Y.func_176796_a()) {
            switch (enumdirection.func_176740_k()) {
            case Z:
                return this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.LINES_Z);

            case X:
                return this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.LINES_X);

            case Y:
                return this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.LINES_Y);
            }
        }

        return i == BlockQuartz.EnumType.CHISELED.func_176796_a() ? this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.CHISELED) : this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.DEFAULT);
    }

    public int func_180651_a(IBlockState iblockdata) {
        BlockQuartz.EnumType blockquartz_enumquartzvariant = (BlockQuartz.EnumType) iblockdata.func_177229_b(BlockQuartz.field_176335_a);

        return blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_X && blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_Z ? blockquartz_enumquartzvariant.func_176796_a() : BlockQuartz.EnumType.LINES_Y.func_176796_a();
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        BlockQuartz.EnumType blockquartz_enumquartzvariant = (BlockQuartz.EnumType) iblockdata.func_177229_b(BlockQuartz.field_176335_a);

        return blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_X && blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_Z ? super.func_180643_i(iblockdata) : new ItemStack(Item.func_150898_a(this), 1, BlockQuartz.EnumType.LINES_Y.func_176796_a());
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.DEFAULT.func_176796_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.CHISELED.func_176796_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.LINES_Y.func_176796_a()));
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151677_p;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.func_176794_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockQuartz.EnumType) iblockdata.func_177229_b(BlockQuartz.field_176335_a)).func_176796_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((BlockQuartz.EnumType) iblockdata.func_177229_b(BlockQuartz.field_176335_a)) {
            case LINES_X:
                return iblockdata.func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.LINES_Z);

            case LINES_Z:
                return iblockdata.func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.LINES_X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockQuartz.field_176335_a});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "default", "default"), CHISELED(1, "chiseled", "chiseled"), LINES_Y(2, "lines_y", "lines"), LINES_X(3, "lines_x", "lines"), LINES_Z(4, "lines_z", "lines");

        private static final BlockQuartz.EnumType[] field_176797_f = new BlockQuartz.EnumType[values().length];
        private final int field_176798_g;
        private final String field_176805_h;
        private final String field_176806_i;

        private EnumType(int i, String s, String s1) {
            this.field_176798_g = i;
            this.field_176805_h = s;
            this.field_176806_i = s1;
        }

        public int func_176796_a() {
            return this.field_176798_g;
        }

        public String toString() {
            return this.field_176806_i;
        }

        public static BlockQuartz.EnumType func_176794_a(int i) {
            if (i < 0 || i >= BlockQuartz.EnumType.field_176797_f.length) {
                i = 0;
            }

            return BlockQuartz.EnumType.field_176797_f[i];
        }

        public String func_176610_l() {
            return this.field_176805_h;
        }

        static {
            BlockQuartz.EnumType[] ablockquartz_enumquartzvariant = values();
            int i = ablockquartz_enumquartzvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockQuartz.EnumType blockquartz_enumquartzvariant = ablockquartz_enumquartzvariant[j];

                BlockQuartz.EnumType.field_176797_f[blockquartz_enumquartzvariant.func_176796_a()] = blockquartz_enumquartzvariant;
            }

        }
    }
}
