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

    private final IBehaviorDispenseItem field_149947_P = new BehaviorDefaultDispenseItem();

    public BlockDropper() {}

    protected IBehaviorDispenseItem func_149940_a(ItemStack itemstack) {
        return this.field_149947_P;
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityDropper();
    }

    public void func_176439_d(World world, BlockPos blockposition) {
        BlockSourceImpl sourceblock = new BlockSourceImpl(world, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.func_150835_j();

        if (tileentitydispenser != null) {
            int i = tileentitydispenser.func_146017_i();

            if (i < 0) {
                world.func_175718_b(1001, blockposition, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.func_70301_a(i);

                if (!itemstack.func_190926_b()) {
                    EnumFacing enumdirection = (EnumFacing) world.func_180495_p(blockposition).func_177229_b(BlockDropper.field_176441_a);
                    BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
                    IInventory iinventory = TileEntityHopper.func_145893_b(world, (double) blockposition1.func_177958_n(), (double) blockposition1.func_177956_o(), (double) blockposition1.func_177952_p());
                    ItemStack itemstack1;

                    if (iinventory == null) {
                        itemstack1 = this.field_149947_P.func_82482_a(sourceblock, itemstack);
                    } else {
                        // CraftBukkit start - Fire event when pushing items into other inventories
                        CraftItemStack oitemstack = CraftItemStack.asCraftMirror(itemstack.func_77946_l().func_77979_a(1));

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
                        itemstack1 = TileEntityHopper.func_174918_a(tileentitydispenser, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection.func_176734_d());
                        if (event.getItem().equals(oitemstack) && itemstack1.func_190926_b()) {
                            // CraftBukkit end
                            itemstack1 = itemstack.func_77946_l();
                            itemstack1.func_190918_g(1);
                        } else {
                            itemstack1 = itemstack.func_77946_l();
                        }
                    }

                    tileentitydispenser.func_70299_a(i, itemstack1);
                }
            }
        }
    }
}
