package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab {

    public static final PropertyBool field_176555_b = PropertyBool.func_177716_a("seamless");
    public static final PropertyEnum<BlockStoneSlab.EnumType> field_176556_M = PropertyEnum.func_177709_a("variant", BlockStoneSlab.EnumType.class);

    public BlockStoneSlab() {
        super(Material.field_151576_e);
        IBlockState iblockdata = this.field_176227_L.func_177621_b();

        if (this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlab.field_176555_b, Boolean.valueOf(false));
        } else {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.func_180632_j(iblockdata.func_177226_a(BlockStoneSlab.field_176556_M, BlockStoneSlab.EnumType.STONE));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150333_U);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150333_U, 1, ((BlockStoneSlab.EnumType) iblockdata.func_177229_b(BlockStoneSlab.field_176556_M)).func_176624_a());
    }

    public String func_150002_b(int i) {
        return super.func_149739_a() + "." + BlockStoneSlab.EnumType.func_176625_a(i).func_176627_c();
    }

    public IProperty<?> func_176551_l() {
        return BlockStoneSlab.field_176556_M;
    }

    public Comparable<?> func_185674_a(ItemStack itemstack) {
        return BlockStoneSlab.EnumType.func_176625_a(itemstack.func_77960_j() & 7);
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneSlab.EnumType[] ablockdoublestepabstract_enumstoneslabvariant = BlockStoneSlab.EnumType.values();
        int i = ablockdoublestepabstract_enumstoneslabvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneSlab.EnumType blockdoublestepabstract_enumstoneslabvariant = ablockdoublestepabstract_enumstoneslabvariant[j];

            if (blockdoublestepabstract_enumstoneslabvariant != BlockStoneSlab.EnumType.WOOD) {
                nonnulllist.add(new ItemStack(this, 1, blockdoublestepabstract_enumstoneslabvariant.func_176624_a()));
            }
        }

    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockStoneSlab.field_176556_M, BlockStoneSlab.EnumType.func_176625_a(i & 7));

        if (this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlab.field_176555_b, Boolean.valueOf((i & 8) != 0));
        } else {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlab.field_176554_a, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockStoneSlab.EnumType) iblockdata.func_177229_b(BlockStoneSlab.field_176556_M)).func_176624_a();

        if (this.func_176552_j()) {
            if (((Boolean) iblockdata.func_177229_b(BlockStoneSlab.field_176555_b)).booleanValue()) {
                i |= 8;
            }
        } else if (iblockdata.func_177229_b(BlockStoneSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[] { BlockStoneSlab.field_176555_b, BlockStoneSlab.field_176556_M}) : new BlockStateContainer(this, new IProperty[] { BlockStoneSlab.field_176554_a, BlockStoneSlab.field_176556_M});
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockStoneSlab.EnumType) iblockdata.func_177229_b(BlockStoneSlab.field_176556_M)).func_176624_a();
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStoneSlab.EnumType) iblockdata.func_177229_b(BlockStoneSlab.field_176556_M)).func_181074_c();
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, MapColor.field_151665_m, "stone"), SAND(1, MapColor.field_151658_d, "sandstone", "sand"), WOOD(2, MapColor.field_151663_o, "wood_old", "wood"), COBBLESTONE(3, MapColor.field_151665_m, "cobblestone", "cobble"), BRICK(4, MapColor.field_151645_D, "brick"), SMOOTHBRICK(5, MapColor.field_151665_m, "stone_brick", "smoothStoneBrick"), NETHERBRICK(6, MapColor.field_151655_K, "nether_brick", "netherBrick"), QUARTZ(7, MapColor.field_151677_p, "quartz");

        private static final BlockStoneSlab.EnumType[] field_176640_i = new BlockStoneSlab.EnumType[values().length];
        private final int field_176637_j;
        private final MapColor field_181075_k;
        private final String field_176638_k;
        private final String field_176635_l;

        private EnumType(int i, MapColor materialmapcolor, String s) {
            this(i, materialmapcolor, s, s);
        }

        private EnumType(int i, MapColor materialmapcolor, String s, String s1) {
            this.field_176637_j = i;
            this.field_181075_k = materialmapcolor;
            this.field_176638_k = s;
            this.field_176635_l = s1;
        }

        public int func_176624_a() {
            return this.field_176637_j;
        }

        public MapColor func_181074_c() {
            return this.field_181075_k;
        }

        public String toString() {
            return this.field_176638_k;
        }

        public static BlockStoneSlab.EnumType func_176625_a(int i) {
            if (i < 0 || i >= BlockStoneSlab.EnumType.field_176640_i.length) {
                i = 0;
            }

            return BlockStoneSlab.EnumType.field_176640_i[i];
        }

        public String func_176610_l() {
            return this.field_176638_k;
        }

        public String func_176627_c() {
            return this.field_176635_l;
        }

        static {
            BlockStoneSlab.EnumType[] ablockdoublestepabstract_enumstoneslabvariant = values();
            int i = ablockdoublestepabstract_enumstoneslabvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneSlab.EnumType blockdoublestepabstract_enumstoneslabvariant = ablockdoublestepabstract_enumstoneslabvariant[j];

                BlockStoneSlab.EnumType.field_176640_i[blockdoublestepabstract_enumstoneslabvariant.func_176624_a()] = blockdoublestepabstract_enumstoneslabvariant;
            }

        }
    }
}
