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

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

    public BlockWoodSlab() {
        super(Material.WOOD);
        IBlockState iblockdata = this.blockState.getBaseState();

        if (!this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockdata.withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockWoodSlab.VARIANT)).getMapColor();
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.WOODEN_SLAB);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.WOODEN_SLAB, 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockWoodSlab.VARIANT)).getMetadata());
    }

    public String getUnlocalizedName(int i) {
        return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.byMetadata(i).getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return BlockWoodSlab.VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack itemstack) {
        return BlockPlanks.EnumType.byMetadata(itemstack.getMetadata() & 7);
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockPlanks.EnumType[] ablockwood_enumlogvariant = BlockPlanks.EnumType.values();
        int i = ablockwood_enumlogvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockwood_enumlogvariant.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.byMetadata(i & 7));

        if (!this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockWoodSlab.HALF, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockWoodSlab.VARIANT)).getMetadata();

        if (!this.isDouble() && iblockdata.getValue(BlockWoodSlab.HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { BlockWoodSlab.VARIANT}) : new BlockStateContainer(this, new IProperty[] { BlockWoodSlab.HALF, BlockWoodSlab.VARIANT});
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockWoodSlab.VARIANT)).getMetadata();
    }
}
