package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSign extends BlockContainer {

    protected static final AxisAlignedBB SIGN_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockSign() {
        super(Material.WOOD);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSign.SIGN_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSign.NULL_AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canSpawnInBlock() {
        return true;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySign();
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.SIGN;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.SIGN);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            return tileentity instanceof TileEntitySign ? ((TileEntitySign) tileentity).executeCommand(entityhuman) : false;
        }
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return !this.hasInvalidNeighbor(world, blockposition) && super.canPlaceBlockAt(world, blockposition);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
