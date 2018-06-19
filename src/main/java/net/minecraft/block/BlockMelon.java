package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockMelon extends Block {

    protected BlockMelon() {
        super(Material.field_151572_C, MapColor.field_151672_u);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151127_ba;
    }

    public int func_149745_a(Random random) {
        return 3 + random.nextInt(5);
    }

    public int func_149679_a(int i, Random random) {
        return Math.min(9, this.func_149745_a(random) + random.nextInt(1 + i));
    }
}
