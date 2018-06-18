package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider {

    protected BlockContainer(Material material) {
        this(material, material.getMaterialMapColor());
    }

    protected BlockContainer(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.hasTileEntity = true;
    }

    protected boolean isInvalidNeighbor(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return world.getBlockState(blockposition.offset(enumdirection)).getMaterial() == Material.CACTUS;
    }

    protected boolean hasInvalidNeighbor(World world, BlockPos blockposition) {
        return this.isInvalidNeighbor(world, blockposition, EnumFacing.NORTH) || this.isInvalidNeighbor(world, blockposition, EnumFacing.SOUTH) || this.isInvalidNeighbor(world, blockposition, EnumFacing.WEST) || this.isInvalidNeighbor(world, blockposition, EnumFacing.EAST);
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        world.removeTileEntity(blockposition);
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (tileentity instanceof IWorldNameable && ((IWorldNameable) tileentity).hasCustomName()) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            entityhuman.addExhaustion(0.005F);
            if (world.isRemote) {
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack);
            Item item = this.getItemDropped(iblockdata, world.rand, i);

            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack1 = new ItemStack(item, this.quantityDropped(world.rand));

            itemstack1.setStackDisplayName(((IWorldNameable) tileentity).getName());
            spawnAsEntity(world, blockposition, itemstack1);
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public boolean eventReceived(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        super.eventReceived(iblockdata, world, blockposition, i, j);
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity == null ? false : tileentity.receiveClientEvent(i, j);
    }
}
