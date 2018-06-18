package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockSponge extends Block {

    public static final PropertyBool WET = PropertyBool.create("wet");

    protected BlockSponge() {
        super(Material.SPONGE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSponge.WET, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".dry.name");
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockSponge.WET)).booleanValue() ? 1 : 0;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.tryAbsorb(world, blockposition, iblockdata);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.tryAbsorb(world, blockposition, iblockdata);
        super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
    }

    protected void tryAbsorb(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!((Boolean) iblockdata.getValue(BlockSponge.WET)).booleanValue() && this.absorb(world, blockposition)) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockSponge.WET, Boolean.valueOf(true)), 2);
            world.playEvent(2001, blockposition, Block.getIdFromBlock(Blocks.WATER));
        }

    }

    private boolean absorb(World world, BlockPos blockposition) {
        LinkedList linkedlist = Lists.newLinkedList();
        ArrayList arraylist = Lists.newArrayList();

        linkedlist.add(new Tuple(blockposition, Integer.valueOf(0)));
        int i = 0;

        BlockPos blockposition1;

        while (!linkedlist.isEmpty()) {
            Tuple tuple = (Tuple) linkedlist.poll();

            blockposition1 = (BlockPos) tuple.getFirst();
            int j = ((Integer) tuple.getSecond()).intValue();
            EnumFacing[] aenumdirection = EnumFacing.values();
            int k = aenumdirection.length;

            for (int l = 0; l < k; ++l) {
                EnumFacing enumdirection = aenumdirection[l];
                BlockPos blockposition2 = blockposition1.offset(enumdirection);

                if (world.getBlockState(blockposition2).getMaterial() == Material.WATER) {
                    world.setBlockState(blockposition2, Blocks.AIR.getDefaultState(), 2);
                    arraylist.add(blockposition2);
                    ++i;
                    if (j < 6) {
                        linkedlist.add(new Tuple(blockposition2, Integer.valueOf(j + 1)));
                    }
                }
            }

            if (i > 64) {
                break;
            }
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            blockposition1 = (BlockPos) iterator.next();
            world.notifyNeighborsOfStateChange(blockposition1, Blocks.AIR, false);
        }

        return i > 0;
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, 0));
        nonnulllist.add(new ItemStack(this, 1, 1));
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSponge.WET, Boolean.valueOf((i & 1) == 1));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockSponge.WET)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSponge.WET});
    }
}
