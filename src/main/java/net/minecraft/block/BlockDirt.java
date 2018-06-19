package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockDirt extends Block {

    public static final PropertyEnum<BlockDirt.DirtType> field_176386_a = PropertyEnum.func_177709_a("variant", BlockDirt.DirtType.class);
    public static final PropertyBool field_176385_b = PropertyBool.func_177716_a("snowy");

    protected BlockDirt() {
        super(Material.field_151578_c);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT).func_177226_a(BlockDirt.field_176385_b, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockDirt.DirtType) iblockdata.func_177229_b(BlockDirt.field_176386_a)).func_181066_d();
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.func_177229_b(BlockDirt.field_176386_a) == BlockDirt.DirtType.PODZOL) {
            Block block = iblockaccess.func_180495_p(blockposition.func_177984_a()).func_177230_c();

            iblockdata = iblockdata.func_177226_a(BlockDirt.field_176385_b, Boolean.valueOf(block == Blocks.field_150433_aE || block == Blocks.field_150431_aC));
        }

        return iblockdata;
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.func_176925_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.func_176925_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.func_176925_a()));
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, ((BlockDirt.DirtType) iblockdata.func_177229_b(BlockDirt.field_176386_a)).func_176925_a());
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.func_176924_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockDirt.DirtType) iblockdata.func_177229_b(BlockDirt.field_176386_a)).func_176925_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockDirt.field_176386_a, BlockDirt.field_176385_b});
    }

    public int func_180651_a(IBlockState iblockdata) {
        BlockDirt.DirtType blockdirt_enumdirtvariant = (BlockDirt.DirtType) iblockdata.func_177229_b(BlockDirt.field_176386_a);

        if (blockdirt_enumdirtvariant == BlockDirt.DirtType.PODZOL) {
            blockdirt_enumdirtvariant = BlockDirt.DirtType.DIRT;
        }

        return blockdirt_enumdirtvariant.func_176925_a();
    }

    public static enum DirtType implements IStringSerializable {

        DIRT(0, "dirt", "default", MapColor.field_151664_l), COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.field_151664_l), PODZOL(2, "podzol", MapColor.field_151654_J);

        private static final BlockDirt.DirtType[] field_176930_d = new BlockDirt.DirtType[values().length];
        private final int field_176931_e;
        private final String field_176928_f;
        private final String field_176929_g;
        private final MapColor field_181067_h;

        private DirtType(int i, String s, MapColor materialmapcolor) {
            this(i, s, s, materialmapcolor);
        }

        private DirtType(int i, String s, String s1, MapColor materialmapcolor) {
            this.field_176931_e = i;
            this.field_176928_f = s;
            this.field_176929_g = s1;
            this.field_181067_h = materialmapcolor;
        }

        public int func_176925_a() {
            return this.field_176931_e;
        }

        public String func_176927_c() {
            return this.field_176929_g;
        }

        public MapColor func_181066_d() {
            return this.field_181067_h;
        }

        public String toString() {
            return this.field_176928_f;
        }

        public static BlockDirt.DirtType func_176924_a(int i) {
            if (i < 0 || i >= BlockDirt.DirtType.field_176930_d.length) {
                i = 0;
            }

            return BlockDirt.DirtType.field_176930_d[i];
        }

        public String func_176610_l() {
            return this.field_176928_f;
        }

        static {
            BlockDirt.DirtType[] ablockdirt_enumdirtvariant = values();
            int i = ablockdirt_enumdirtvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockDirt.DirtType blockdirt_enumdirtvariant = ablockdirt_enumdirtvariant[j];

                BlockDirt.DirtType.field_176930_d[blockdirt_enumdirtvariant.func_176925_a()] = blockdirt_enumdirtvariant;
            }

        }
    }
}
