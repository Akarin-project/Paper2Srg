package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockSnowBlock extends Block {

    protected BlockSnowBlock() {
        super(Material.CRAFTED_SNOW);
        // this.a(true); // Paper - snow blocks don't need to tick
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.SNOWBALL;
    }

    public int quantityDropped(Random random) {
        return 4;
    }

    // Paper start - snow blocks don't need to tick
    /*
    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        if (world.getBrightness(EnumSkyBlock.BLOCK, blockposition) > 11) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Blocks.AIR).isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.b(world, blockposition, world.getType(blockposition), 0);
            world.setAir(blockposition);
        }

    }
    */
    //Paper end
}
