package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWeb extends Block {

    public BlockWeb() {
        super(Material.WEB);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        entity.setInWeb();
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockWeb.NULL_AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.STRING;
    }

    protected boolean canSilkHarvest() {
        return true;
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Item.getItemFromBlock(this), 1));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
