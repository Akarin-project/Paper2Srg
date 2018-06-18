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

public class BlockNewLog extends BlockLog {

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.getMetadata() >= 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockNewLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockPlanks.EnumType blockwood_enumlogvariant = (BlockPlanks.EnumType) iblockdata.getValue(BlockNewLog.VARIANT);

        switch ((BlockLog.EnumAxis) iblockdata.getValue(BlockNewLog.LOG_AXIS)) {
        case X:
        case Z:
        case NONE:
        default:
            switch (blockwood_enumlogvariant) {
            case ACACIA:
            default:
                return MapColor.STONE;

            case DARK_OAK:
                return BlockPlanks.EnumType.DARK_OAK.getMapColor();
            }

        case Y:
            return blockwood_enumlogvariant.getMapColor();
        }
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.byMetadata((i & 3) + 4));

        switch (i & 12) {
        case 0:
            iblockdata = iblockdata.withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y);
            break;

        case 4:
            iblockdata = iblockdata.withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X);
            break;

        case 8:
            iblockdata = iblockdata.withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z);
            break;

        default:
            iblockdata = iblockdata.withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLog.VARIANT)).getMetadata() - 4;

        switch ((BlockLog.EnumAxis) iblockdata.getValue(BlockNewLog.LOG_AXIS)) {
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

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockNewLog.VARIANT, BlockNewLog.LOG_AXIS});
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLog.VARIANT)).getMetadata() - 4);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLog.VARIANT)).getMetadata() - 4;
    }
}
