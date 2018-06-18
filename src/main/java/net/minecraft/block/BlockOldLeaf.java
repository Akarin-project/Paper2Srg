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

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.getMetadata() < 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockOldLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(true)).withProperty(BlockOldLeaf.DECAYABLE, Boolean.valueOf(true)));
    }

    protected void dropApple(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (iblockdata.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.OAK && world.rand.nextInt(i) == 0) {
            spawnAsEntity(world, blockposition, new ItemStack(Items.APPLE));
        }

    }

    protected int getSaplingDropChance(IBlockState iblockdata) {
        return iblockdata.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.JUNGLE ? 40 : super.getSaplingDropChance(iblockdata);
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLeaf.VARIANT)).getMetadata());
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockOldLeaf.VARIANT, this.getWoodType(i)).withProperty(BlockOldLeaf.DECAYABLE, Boolean.valueOf((i & 4) == 0)).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLeaf.VARIANT)).getMetadata();

        if (!((Boolean) iblockdata.getValue(BlockOldLeaf.DECAYABLE)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.getValue(BlockOldLeaf.CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType getWoodType(int i) {
        return BlockPlanks.EnumType.byMetadata((i & 3) % 4);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockOldLeaf.VARIANT, BlockOldLeaf.CHECK_DECAY, BlockOldLeaf.DECAYABLE});
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLeaf.VARIANT)).getMetadata();
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockOldLeaf.VARIANT)).getMetadata()));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }
}
