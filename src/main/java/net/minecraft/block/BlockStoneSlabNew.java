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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlabNew extends BlockSlab {

    public static final PropertyBool field_176558_b = PropertyBool.func_177716_a("seamless");
    public static final PropertyEnum<BlockStoneSlabNew.EnumType> field_176559_M = PropertyEnum.func_177709_a("variant", BlockStoneSlabNew.EnumType.class);

    public BlockStoneSlabNew() {
        super(Material.field_151576_e);
        IBlockState iblockdata = this.field_176227_L.func_177621_b();

        if (this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlabNew.field_176558_b, Boolean.valueOf(false));
        } else {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlabNew.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.func_180632_j(iblockdata.func_177226_a(BlockStoneSlabNew.field_176559_M, BlockStoneSlabNew.EnumType.RED_SANDSTONE));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + ".red_sandstone.name");
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_180389_cP);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_180389_cP, 1, ((BlockStoneSlabNew.EnumType) iblockdata.func_177229_b(BlockStoneSlabNew.field_176559_M)).func_176915_a());
    }

    public String func_150002_b(int i) {
        return super.func_149739_a() + "." + BlockStoneSlabNew.EnumType.func_176916_a(i).func_176918_c();
    }

    public IProperty<?> func_176551_l() {
        return BlockStoneSlabNew.field_176559_M;
    }

    public Comparable<?> func_185674_a(ItemStack itemstack) {
        return BlockStoneSlabNew.EnumType.func_176916_a(itemstack.func_77960_j() & 7);
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneSlabNew.EnumType[] ablockdoublestonestepabstract_enumstoneslab2variant = BlockStoneSlabNew.EnumType.values();
        int i = ablockdoublestonestepabstract_enumstoneslab2variant.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneSlabNew.EnumType blockdoublestonestepabstract_enumstoneslab2variant = ablockdoublestonestepabstract_enumstoneslab2variant[j];

            nonnulllist.add(new ItemStack(this, 1, blockdoublestonestepabstract_enumstoneslab2variant.func_176915_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockStoneSlabNew.field_176559_M, BlockStoneSlabNew.EnumType.func_176916_a(i & 7));

        if (this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlabNew.field_176558_b, Boolean.valueOf((i & 8) != 0));
        } else {
            iblockdata = iblockdata.func_177226_a(BlockStoneSlabNew.field_176554_a, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockStoneSlabNew.EnumType) iblockdata.func_177229_b(BlockStoneSlabNew.field_176559_M)).func_176915_a();

        if (this.func_176552_j()) {
            if (((Boolean) iblockdata.func_177229_b(BlockStoneSlabNew.field_176558_b)).booleanValue()) {
                i |= 8;
            }
        } else if (iblockdata.func_177229_b(BlockStoneSlabNew.field_176554_a) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[] { BlockStoneSlabNew.field_176558_b, BlockStoneSlabNew.field_176559_M}) : new BlockStateContainer(this, new IProperty[] { BlockStoneSlabNew.field_176554_a, BlockStoneSlabNew.field_176559_M});
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStoneSlabNew.EnumType) iblockdata.func_177229_b(BlockStoneSlabNew.field_176559_M)).func_181068_c();
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockStoneSlabNew.EnumType) iblockdata.func_177229_b(BlockStoneSlabNew.field_176559_M)).func_176915_a();
    }

    public static enum EnumType implements IStringSerializable {

        RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.func_176687_c());

        private static final BlockStoneSlabNew.EnumType[] field_176921_b = new BlockStoneSlabNew.EnumType[values().length];
        private final int field_176922_c;
        private final String field_176919_d;
        private final MapColor field_181069_e;

        private EnumType(int i, String s, MapColor materialmapcolor) {
            this.field_176922_c = i;
            this.field_176919_d = s;
            this.field_181069_e = materialmapcolor;
        }

        public int func_176915_a() {
            return this.field_176922_c;
        }

        public MapColor func_181068_c() {
            return this.field_181069_e;
        }

        public String toString() {
            return this.field_176919_d;
        }

        public static BlockStoneSlabNew.EnumType func_176916_a(int i) {
            if (i < 0 || i >= BlockStoneSlabNew.EnumType.field_176921_b.length) {
                i = 0;
            }

            return BlockStoneSlabNew.EnumType.field_176921_b[i];
        }

        public String func_176610_l() {
            return this.field_176919_d;
        }

        public String func_176918_c() {
            return this.field_176919_d;
        }

        static {
            BlockStoneSlabNew.EnumType[] ablockdoublestonestepabstract_enumstoneslab2variant = values();
            int i = ablockdoublestonestepabstract_enumstoneslab2variant.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneSlabNew.EnumType blockdoublestonestepabstract_enumstoneslab2variant = ablockdoublestonestepabstract_enumstoneslab2variant[j];

                BlockStoneSlabNew.EnumType.field_176921_b[blockdoublestonestepabstract_enumstoneslab2variant.func_176915_a()] = blockdoublestonestepabstract_enumstoneslab2variant;
            }

        }
    }
}
