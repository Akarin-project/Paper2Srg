package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockGlass extends BlockBreakable {

    public BlockGlass(Material material, boolean flag) {
        super(material, flag);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    protected boolean canSilkHarvest() {
        return true;
    }
}
