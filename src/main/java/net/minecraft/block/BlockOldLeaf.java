package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOldLeaf extends BlockLeaves {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176239_P = PropertyEnum.func_177708_a("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.func_176839_a() < 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockOldLeaf() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.OAK).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(true)).func_177226_a(BlockOldLeaf.field_176237_a, Boolean.valueOf(true)));
    }

    protected void func_176234_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (iblockdata.func_177229_b(BlockOldLeaf.field_176239_P) == BlockPlanks.EnumType.OAK && world.field_73012_v.nextInt(i) == 0) {
            func_180635_a(world, blockposition, new ItemStack(Items.field_151034_e));
        }

    }

    protected int func_176232_d(IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockOldLeaf.field_176239_P) == BlockPlanks.EnumType.JUNGLE ? 40 : super.func_176232_d(iblockdata);
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()));
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(this), 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLeaf.field_176239_P)).func_176839_a());
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, this.func_176233_b(i)).func_177226_a(BlockOldLeaf.field_176237_a, Boolean.valueOf((i & 4) == 0)).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLeaf.field_176239_P)).func_176839_a();

        if (!((Boolean) iblockdata.func_177229_b(BlockOldLeaf.field_176237_a)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockOldLeaf.field_176236_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType func_176233_b(int i) {
        return BlockPlanks.EnumType.func_176837_a((i & 3) % 4);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockOldLeaf.field_176239_P, BlockOldLeaf.field_176236_b, BlockOldLeaf.field_176237_a});
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLeaf.field_176239_P)).func_176839_a();
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Item.func_150898_a(this), 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLeaf.field_176239_P)).func_176839_a()));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }
}
