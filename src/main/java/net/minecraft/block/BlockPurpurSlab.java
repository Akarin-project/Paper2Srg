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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPurpurSlab extends BlockSlab {

    public static final PropertyEnum<BlockPurpurSlab.Variant> VARIANT = PropertyEnum.create("variant", BlockPurpurSlab.Variant.class);

    public BlockPurpurSlab() {
        super(Material.ROCK, MapColor.MAGENTA);
        IBlockState iblockdata = this.blockState.getBaseState();

        if (!this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockPurpurSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockdata.withProperty(BlockPurpurSlab.VARIANT, BlockPurpurSlab.Variant.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.PURPUR_SLAB);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.PURPUR_SLAB);
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockPurpurSlab.VARIANT, BlockPurpurSlab.Variant.DEFAULT);

        if (!this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockPurpurSlab.HALF, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;

        if (!this.isDouble() && iblockdata.getValue(BlockPurpurSlab.HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { BlockPurpurSlab.VARIANT}) : new BlockStateContainer(this, new IProperty[] { BlockPurpurSlab.HALF, BlockPurpurSlab.VARIANT});
    }

    public String getUnlocalizedName(int i) {
        return super.getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return BlockPurpurSlab.VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack itemstack) {
        return BlockPurpurSlab.Variant.DEFAULT;
    }

    public static enum Variant implements IStringSerializable {

        DEFAULT;

        private Variant() {}

        public String getName() {
            return "default";
        }
    }

    public static class Double extends BlockPurpurSlab {

        public Double() {}

        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockPurpurSlab {

        public Half() {}

        public boolean isDouble() {
            return false;
        }
    }
}
