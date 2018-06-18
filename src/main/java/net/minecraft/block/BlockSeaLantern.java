package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockSeaLantern extends Block {

    public BlockSeaLantern(Material material) {
        super(material);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int quantityDropped(Random random) {
        return 2 + random.nextInt(2);
    }

    public int quantityDroppedWithBonus(int i, Random random) {
        return MathHelper.clamp(this.quantityDropped(random) + random.nextInt(i + 1), 1, 5);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.PRISMARINE_CRYSTALS;
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.QUARTZ;
    }

    protected boolean canSilkHarvest() {
        return true;
    }
}
