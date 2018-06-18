package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockEmptyDrops extends Block {

    public BlockEmptyDrops(Material material) {
        super(material);
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }
}
