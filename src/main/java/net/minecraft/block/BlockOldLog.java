package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOldLog extends BlockLog {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176301_b = PropertyEnum.func_177708_a("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.func_176839_a() < 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockOldLog() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.OAK).func_177226_a(BlockOldLog.field_176299_a, BlockLog.EnumAxis.Y));
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockPlanks.EnumType blockwood_enumlogvariant = (BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLog.field_176301_b);

        switch ((BlockLog.EnumAxis) iblockdata.func_177229_b(BlockOldLog.field_176299_a)) {
        case X:
        case Z:
        case NONE:
        default:
            switch (blockwood_enumlogvariant) {
            case OAK:
            default:
                return BlockPlanks.EnumType.SPRUCE.func_181070_c();

            case SPRUCE:
                return BlockPlanks.EnumType.DARK_OAK.func_181070_c();

            case BIRCH:
                return MapColor.field_151677_p;

            case JUNGLE:
                return BlockPlanks.EnumType.SPRUCE.func_181070_c();
            }

        case Y:
            return blockwood_enumlogvariant.func_181070_c();
        }
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()));
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.func_176837_a((i & 3) % 4));

        switch (i & 12) {
        case 0:
            iblockdata = iblockdata.func_177226_a(BlockOldLog.field_176299_a, BlockLog.EnumAxis.Y);
            break;

        case 4:
            iblockdata = iblockdata.func_177226_a(BlockOldLog.field_176299_a, BlockLog.EnumAxis.X);
            break;

        case 8:
            iblockdata = iblockdata.func_177226_a(BlockOldLog.field_176299_a, BlockLog.EnumAxis.Z);
            break;

        default:
            iblockdata = iblockdata.func_177226_a(BlockOldLog.field_176299_a, BlockLog.EnumAxis.NONE);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLog.field_176301_b)).func_176839_a();

        switch ((BlockLog.EnumAxis) iblockdata.func_177229_b(BlockOldLog.field_176299_a)) {
        case X:
            i |= 4;
            break;

        case Z:
            i |= 8;
            break;

        case NONE:
            i |= 12;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockOldLog.field_176301_b, BlockOldLog.field_176299_a});
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(this), 1, ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLog.field_176301_b)).func_176839_a());
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockOldLog.field_176301_b)).func_176839_a();
    }
}
