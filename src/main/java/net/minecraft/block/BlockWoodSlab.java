package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockWoodSlab extends BlockSlab {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176557_b = PropertyEnum.func_177709_a("variant", BlockPlanks.EnumType.class);

    public BlockWoodSlab() {
        super(Material.field_151575_d);
        IBlockState iblockdata = this.field_176227_L.func_177621_b();

        if (!this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockWoodSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.func_180632_j(iblockdata.func_177226_a(BlockWoodSlab.field_176557_b, BlockPlanks.EnumType.OAK));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockWoodSlab.field_176557_b)).func_181070_c();
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150376_bx);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150376_bx, 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockWoodSlab.field_176557_b)).func_176839_a());
    }

    public String func_150002_b(int i) {
        return super.func_149739_a() + "." + BlockPlanks.EnumType.func_176837_a(i).func_176840_c();
    }

    public IProperty<?> func_176551_l() {
        return BlockWoodSlab.field_176557_b;
    }

    public Comparable<?> func_185674_a(ItemStack itemstack) {
        return BlockPlanks.EnumType.func_176837_a(itemstack.func_77960_j() & 7);
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
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockWoodSlab.field_176557_b, BlockPlanks.EnumType.func_176837_a(i & 7));

        if (!this.func_176552_j()) {
            iblockdata = iblockdata.func_177226_a(BlockWoodSlab.field_176554_a, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockWoodSlab.field_176557_b)).func_176839_a();

        if (!this.func_176552_j() && iblockdata.func_177229_b(BlockWoodSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[] { BlockWoodSlab.field_176557_b}) : new BlockStateContainer(this, new IProperty[] { BlockWoodSlab.field_176554_a, BlockWoodSlab.field_176557_b});
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockWoodSlab.field_176557_b)).func_176839_a();
    }
}
