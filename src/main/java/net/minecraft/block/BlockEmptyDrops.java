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

    public int func_149745_a(Random random) {
        return 0;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }
}
