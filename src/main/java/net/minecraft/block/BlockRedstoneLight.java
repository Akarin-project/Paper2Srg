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

    private final boolean isOn;

    public BlockRedstoneLight(boolean flag) {
        super(Material.REDSTONE_LIGHT);
        this.isOn = flag;
        if (flag) {
            this.setLightLevel(1.0F);
        }

    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            if (this.isOn && !world.isBlockPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
            } else if (!this.isOn && world.isBlockPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
            }

        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            if (this.isOn && !world.isBlockPowered(blockposition)) {
                world.scheduleUpdate(blockposition, (Block) this, 4);
            } else if (!this.isOn && world.isBlockPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
            }

        }
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (this.isOn && !world.isBlockPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setBlockState(blockposition, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
            }

        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.REDSTONE_LAMP);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.REDSTONE_LAMP);
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Blocks.REDSTONE_LAMP);
    }
}
