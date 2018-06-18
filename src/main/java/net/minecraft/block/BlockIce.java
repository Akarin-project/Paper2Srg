package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable {

    public BlockIce() {
        super(Material.ICE, false);
        this.slipperiness = 0.98F;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        entityhuman.addStat(StatList.getBlockStats((Block) this));
        entityhuman.addExhaustion(0.005F);
        if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0) {
            spawnAsEntity(world, blockposition, this.getSilkTouchDrop(iblockdata));
        } else {
            if (world.provider.doesWaterVaporize()) {
                world.setBlockToAir(blockposition);
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack);

            this.dropBlockAsItem(world, blockposition, iblockdata, i);
            Material material = world.getBlockState(blockposition.down()).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlockState(blockposition, Blocks.FLOWING_WATER.getDefaultState());
            }
        }

    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.getLightFor(EnumSkyBlock.BLOCK, blockposition) > 11 - this.getDefaultState().getLightOpacity()) {
            this.turnIntoWater(world, blockposition);
        }

    }

    protected void turnIntoWater(World world, BlockPos blockposition) {
        // CraftBukkit start
        if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), world.provider.doesWaterVaporize() ? Blocks.AIR : Blocks.WATER).isCancelled()) {
            return;
        }
        // CraftBukkit end
        if (world.provider.doesWaterVaporize()) {
            world.setBlockToAir(blockposition);
        } else {
            this.dropBlockAsItem(world, blockposition, world.getBlockState(blockposition), 0);
            world.setBlockState(blockposition, Blocks.WATER.getDefaultState());
            world.neighborChanged(blockposition, (Block) Blocks.WATER, blockposition);
        }
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }
}
