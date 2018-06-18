package net.minecraft.block;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// CraftBukkit end

public class BlockDropper extends BlockDispenser {

    private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

    public BlockDropper() {}

    protected IBehaviorDispenseItem getBehavior(ItemStack itemstack) {
        return this.dropBehavior;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDropper();
    }

    public void dispense(World world, BlockPos blockposition) {
        BlockSourceImpl sourceblock = new BlockSourceImpl(world, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getBlockTileEntity();

        if (tileentitydispenser != null) {
            int i = tileentitydispenser.getDispenseSlot();

            if (i < 0) {
                world.playEvent(1001, blockposition, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    EnumFacing enumdirection = (EnumFacing) world.getBlockState(blockposition).getValue(BlockDropper.FACING);
                    BlockPos blockposition1 = blockposition.offset(enumdirection);
                    IInventory iinventory = TileEntityHopper.getInventoryAtPosition(world, (double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
                    ItemStack itemstack1;

                    if (iinventory == null) {
                        itemstack1 = this.dropBehavior.dispense(sourceblock, itemstack);
                    } else {
                        // CraftBukkit start - Fire event when pushing items into other inventories
                        CraftItemStack oitemstack = CraftItemStack.asCraftMirror(itemstack.copy().splitStack(1));

                        org.bukkit.inventory.Inventory destinationInventory;
                        // Have to special case large chests as they work oddly
                        if (iinventory instanceof InventoryLargeChest) {
                            destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
                        } else {
                            destinationInventory = iinventory.getOwner().getInventory();
                        }

                        InventoryMoveItemEvent event = new InventoryMoveItemEvent(tileentitydispenser.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
                        world.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }
                        itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(tileentitydispenser, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection.getOpposite());
                        if (event.getItem().equals(oitemstack) && itemstack1.isEmpty()) {
                            // CraftBukkit end
                            itemstack1 = itemstack.copy();
                            itemstack1.shrink(1);
                        } else {
                            itemstack1 = itemstack.copy();
                        }
                    }

                    tileentitydispenser.setInventorySlotContents(i, itemstack1);
                }
            }
        }
    }
}
