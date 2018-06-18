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

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.getMetadata() < 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockOldLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockPlanks.EnumType blockwood_enumlogvariant = (BlockPlanks.EnumType) iblockdata.getValue(BlockOldLog.VARIANT);

        switch ((BlockLog.EnumAxis) iblockdata.getValue(BlockOldLog.LOG_AXIS)) {
        case X:
        case Z:
        case NONE:
        default:
            switch (blockwood_enumlogvariant) {
            case OAK:
            default:
                return BlockPlanks.EnumType.SPRUCE.getMapColor();

            case SPRUCE:
                return BlockPlanks.EnumType.DARK_OAK.getMapColor();

            case BIRCH:
                return MapColor.QUARTZ;

            case JUNGLE:
                return BlockPlanks.EnumType.SPRUCE.getMapColor();
            }

        case Y:
            return blockwood_enumlogvariant.getMapColor();
        }
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.byMetadata((i & 3) % 4));

        switch (i & 12) {
        case 0:
            iblockdata = iblockdata.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Y);
            break;

        case 4:
            iblockdata = iblockdata.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X);
            break;

        case 8:
            iblockdata = iblockdata.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z);
            break;

        default:
            iblockdata = iblockdata.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLog.VARIANT)).getMetadata();

        switch ((BlockLog.EnumAxis) iblockdata.getValue(BlockOldLog.LOG_AXIS)) {
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
        return new BlockStateContainer(this, new IProperty[] { BlockOldLog.VARIANT, BlockOldLog.LOG_AXIS});
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLog.VARIANT)).getMetadata());
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLog.VARIANT)).getMetadata();
    }
}
