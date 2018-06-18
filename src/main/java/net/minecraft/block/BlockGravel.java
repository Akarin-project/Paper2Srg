package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGravel extends BlockFalling {

    public BlockGravel() {}

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        if (i > 3) {
            i = 3;
        }

        return random.nextInt(10 - i * 3) == 0 ? Items.FLINT : super.getItemDropped(iblockdata, random, i);
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.STONE;
    }
}
