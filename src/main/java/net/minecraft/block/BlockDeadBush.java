package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDeadBush extends BlockBush {

    protected static final AxisAlignedBB DEAD_BUSH_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockDeadBush() {
        super(Material.VINE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDeadBush.DEAD_BUSH_AABB;
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.WOOD;
    }

    protected boolean canSustainBush(IBlockState iblockdata) {
        return iblockdata.getBlock() == Blocks.SAND || iblockdata.getBlock() == Blocks.HARDENED_CLAY || iblockdata.getBlock() == Blocks.STAINED_HARDENED_CLAY || iblockdata.getBlock() == Blocks.DIRT;
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public int quantityDropped(Random random) {
        return random.nextInt(3);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.STICK;
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Blocks.DEADBUSH, 1, 0));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }
}
