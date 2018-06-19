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

    public static final PropertyBool field_176313_a = PropertyBool.func_177716_a("wet");

    protected BlockSponge() {
        super(Material.field_151583_m);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSponge.field_176313_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + ".dry.name");
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockSponge.field_176313_a)).booleanValue() ? 1 : 0;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176311_e(world, blockposition, iblockdata);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176311_e(world, blockposition, iblockdata);
        super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
    }

    protected void func_176311_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!((Boolean) iblockdata.func_177229_b(BlockSponge.field_176313_a)).booleanValue() && this.func_176312_d(world, blockposition)) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockSponge.field_176313_a, Boolean.valueOf(true)), 2);
            world.func_175718_b(2001, blockposition, Block.func_149682_b(Blocks.field_150355_j));
        }

    }

    private boolean func_176312_d(World world, BlockPos blockposition) {
        LinkedList linkedlist = Lists.newLinkedList();
        ArrayList arraylist = Lists.newArrayList();

        linkedlist.add(new Tuple(blockposition, Integer.valueOf(0)));
        int i = 0;

        BlockPos blockposition1;

        while (!linkedlist.isEmpty()) {
            Tuple tuple = (Tuple) linkedlist.poll();

            blockposition1 = (BlockPos) tuple.func_76341_a();
            int j = ((Integer) tuple.func_76340_b()).intValue();
            EnumFacing[] aenumdirection = EnumFacing.values();
            int k = aenumdirection.length;

            for (int l = 0; l < k; ++l) {
                EnumFacing enumdirection = aenumdirection[l];
                BlockPos blockposition2 = blockposition1.func_177972_a(enumdirection);

                if (world.func_180495_p(blockposition2).func_185904_a() == Material.field_151586_h) {
                    world.func_180501_a(blockposition2, Blocks.field_150350_a.func_176223_P(), 2);
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
            world.func_175685_c(blockposition1, Blocks.field_150350_a, false);
        }

        return i > 0;
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, 0));
        nonnulllist.add(new ItemStack(this, 1, 1));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSponge.field_176313_a, Boolean.valueOf((i & 1) == 1));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockSponge.field_176313_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSponge.field_176313_a});
    }
}
