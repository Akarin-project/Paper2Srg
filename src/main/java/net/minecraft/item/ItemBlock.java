package net.minecraft.item;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBlock extends Item {

    protected final Block block;

    public ItemBlock(Block block) {
        this.block = block;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        if (!block.isReplaceable((IBlockAccess) world, blockposition)) {
            blockposition = blockposition.offset(enumdirection);
        }

        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && world.mayPlace(this.block, blockposition, false, enumdirection, entityhuman)) { // Paper - Pass entityhuman instead of null
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockdata1 = this.block.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, i, entityhuman);

            if (world.setBlockState(blockposition, iblockdata1, 11)) {
                iblockdata1 = world.getBlockState(blockposition);
                if (iblockdata1.getBlock() == this.block) {
                    setTileEntityNBT(world, entityhuman, blockposition, itemstack);
                    this.block.onBlockPlacedBy(world, blockposition, iblockdata1, entityhuman, itemstack);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }
                }

                SoundType soundeffecttype = this.block.getSoundType();

                // world.a(entityhuman, blockposition, soundeffecttype.e(), SoundCategory.BLOCKS, (soundeffecttype.a() + 1.0F) / 2.0F, soundeffecttype.b() * 0.8F); // CraftBukkit - SPIGOT-1288
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public static boolean setTileEntityNBT(World world, @Nullable EntityPlayer entityhuman, BlockPos blockposition, ItemStack itemstack) {
        MinecraftServer minecraftserver = world.getMinecraftServer();

        if (minecraftserver == null) {
            return false;
        } else {
            NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

            if (nbttagcompound != null) {
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity != null) {
                    if (!world.isRemote && tileentity.onlyOpsCanSetNbt() && (entityhuman == null || !entityhuman.canUseCommandBlock())) {
                        return false;
                    }

                    NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
                    NBTTagCompound nbttagcompound2 = nbttagcompound1.copy();

                    nbttagcompound1.merge(nbttagcompound);
                    nbttagcompound1.setInteger("x", blockposition.getX());
                    nbttagcompound1.setInteger("y", blockposition.getY());
                    nbttagcompound1.setInteger("z", blockposition.getZ());
                    if (!nbttagcompound1.equals(nbttagcompound2)) {
                        tileentity.readFromNBT(nbttagcompound1);
                        tileentity.markDirty();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return this.block.getUnlocalizedName();
    }

    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }

    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTabToDisplayOn();
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            this.block.getSubBlocks(creativemodetab, nonnulllist);
        }

    }

    public Block getBlock() {
        return this.block;
    }
}
