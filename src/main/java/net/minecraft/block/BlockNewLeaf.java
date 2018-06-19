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

public class BlockNewLeaf extends BlockLeaves {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176240_P = PropertyEnum.func_177708_a("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.func_176839_a() >= 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockNewLeaf() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockNewLeaf.field_176240_P, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockNewLeaf.field_176236_b, Boolean.valueOf(true)).func_177226_a(BlockNewLeaf.field_176237_a, Boolean.valueOf(true)));
    }

    protected void func_176234_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (iblockdata.func_177229_b(BlockNewLeaf.field_176240_P) == BlockPlanks.EnumType.DARK_OAK && world.field_73012_v.nextInt(i) == 0) {
            func_180635_a(world, blockposition, new ItemStack(Items.field_151034_e));
        }

    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockNewLeaf.field_176240_P)).func_176839_a();
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.func_177230_c().func_176201_c(iblockdata) & 3);
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, 0));
        nonnulllist.add(new ItemStack(this, 1, 1));
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(this), 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockNewLeaf.field_176240_P)).func_176839_a() - 4);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockNewLeaf.field_176240_P, this.func_176233_b(i)).func_177226_a(BlockNewLeaf.field_176237_a, Boolean.valueOf((i & 4) == 0)).func_177226_a(BlockNewLeaf.field_176236_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockNewLeaf.field_176240_P)).func_176839_a() - 4;

        if (!((Boolean) iblockdata.func_177229_b(BlockNewLeaf.field_176237_a)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockNewLeaf.field_176236_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType func_176233_b(int i) {
        return BlockPlanks.EnumType.func_176837_a((i & 3) + 4);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockNewLeaf.field_176240_P, BlockNewLeaf.field_176236_b, BlockNewLeaf.field_176237_a});
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Item.func_150898_a(this), 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockNewLeaf.field_176240_P)).func_176839_a() - 4));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }
}
