package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockGlass extends BlockBreakable {

    public BlockGlass(Material material, boolean flag) {
        super(material, flag);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    protected boolean func_149700_E() {
        return true;
    }
}
