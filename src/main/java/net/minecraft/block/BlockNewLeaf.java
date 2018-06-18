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

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        public boolean a(@Nullable BlockPlanks.EnumType blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.getMetadata() >= 4;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockPlanks.EnumType) object);
        }
    });

    public BlockNewLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLeaf.CHECK_DECAY, Boolean.valueOf(true)).withProperty(BlockNewLeaf.DECAYABLE, Boolean.valueOf(true)));
    }

    protected void dropApple(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (iblockdata.getValue(BlockNewLeaf.VARIANT) == BlockPlanks.EnumType.DARK_OAK && world.rand.nextInt(i) == 0) {
            spawnAsEntity(world, blockposition, new ItemStack(Items.APPLE));
        }

    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLeaf.VARIANT)).getMetadata();
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.getBlock().getMetaFromState(iblockdata) & 3);
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, 0));
        nonnulllist.add(new ItemStack(this, 1, 1));
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLeaf.VARIANT)).getMetadata() - 4);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockNewLeaf.VARIANT, this.getWoodType(i)).withProperty(BlockNewLeaf.DECAYABLE, Boolean.valueOf((i & 4) == 0)).withProperty(BlockNewLeaf.CHECK_DECAY, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLeaf.VARIANT)).getMetadata() - 4;

        if (!((Boolean) iblockdata.getValue(BlockNewLeaf.DECAYABLE)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.getValue(BlockNewLeaf.CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType getWoodType(int i) {
        return BlockPlanks.EnumType.byMetadata((i & 3) + 4);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockNewLeaf.VARIANT, BlockNewLeaf.CHECK_DECAY, BlockNewLeaf.DECAYABLE});
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) iblockdata.getValue(BlockNewLeaf.VARIANT)).getMetadata() - 4));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }
}
