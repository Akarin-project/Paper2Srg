package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockRedstoneLight extends Block {

    private final boolean field_150171_a;

    public BlockRedstoneLight(boolean flag) {
        super(Material.field_151591_t);
        this.field_150171_a = flag;
        if (flag) {
            this.func_149715_a(1.0F);
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            if (this.field_150171_a && !world.func_175640_z(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, Blocks.field_150379_bu.func_176223_P(), 2);
            } else if (!this.field_150171_a && world.func_175640_z(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, Blocks.field_150374_bv.func_176223_P(), 2);
            }

        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            if (this.field_150171_a && !world.func_175640_z(blockposition)) {
                world.func_175684_a(blockposition, (Block) this, 4);
            } else if (!this.field_150171_a && world.func_175640_z(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, Blocks.field_150374_bv.func_176223_P(), 2);
            }

        }
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (this.field_150171_a && !world.func_175640_z(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, Blocks.field_150379_bu.func_176223_P(), 2);
            }

        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150379_bu);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150379_bu);
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150379_bu);
    }
}
