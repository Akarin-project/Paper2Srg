package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public abstract class Container {

    public NonNullList<ItemStack> inventoryItemStacks = NonNullList.create();
    public List<Slot> inventorySlots = Lists.newArrayList();
    public int windowId;
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();
    protected List<IContainerListener> listeners = Lists.newArrayList();
    private final Set<EntityPlayer> playerList = Sets.newHashSet();
    private int tickCount; // Spigot

    // CraftBukkit start
    public boolean checkReachable = true;
    public abstract InventoryView getBukkitView();
    public void transferTo(Container other, org.bukkit.craftbukkit.entity.CraftHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }
    // CraftBukkit end

    public Container() {}

    protected Slot addSlotToContainer(Slot slot) {
        slot.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(slot);
        this.inventoryItemStacks.add(ItemStack.EMPTY);
        return slot;
    }

    public void addListener(IContainerListener icrafting) {
        if (this.listeners.contains(icrafting)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.listeners.add(icrafting);
            icrafting.sendAllContents(this, this.getInventory());
            this.detectAndSendChanges();
        }
    }

    public NonNullList<ItemStack> getInventory() {
        NonNullList nonnulllist = NonNullList.create();

        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            nonnulllist.add(((Slot) this.inventorySlots.get(i)).getStack());
        }

        return nonnulllist;
    }

    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack itemstack = ((Slot) this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack) this.inventoryItemStacks.get(i);

            if (!ItemStack.fastMatches(itemstack1, itemstack) || (tickCount % org.spigotmc.SpigotConfig.itemDirtyTicks == 0 && !ItemStack.areItemStacksEqual(itemstack1, itemstack))) { // Spigot
                itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                for (int j = 0; j < this.listeners.size(); ++j) {
                    ((IContainerListener) this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
                }
            }
        }
        tickCount++; // Spigot

    }

    public boolean enchantItem(EntityPlayer entityhuman, int i) {
        return false;
    }

    @Nullable
    public Slot getSlotFromInventory(IInventory iinventory, int i) {
        for (int j = 0; j < this.inventorySlots.size(); ++j) {
            Slot slot = (Slot) this.inventorySlots.get(j);

            if (slot.isHere(iinventory, i)) {
                return slot;
            }
        }

        return null;
    }

    public Slot getSlot(int i) {
        return (Slot) this.inventorySlots.get(i);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        Slot slot = (Slot) this.inventorySlots.get(i);

        return slot != null ? slot.getStack() : ItemStack.EMPTY;
    }

    public ItemStack slotClick(int i, int j, ClickType inventoryclicktype, EntityPlayer entityhuman) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer playerinventory = entityhuman.inventory;
        ItemStack itemstack1;
        int k;
        ItemStack itemstack2;
        int l;

        if (inventoryclicktype == ClickType.QUICK_CRAFT) {
            int i1 = this.dragEvent;

            this.dragEvent = getDragEvent(j);
            if ((i1 != 1 || this.dragEvent != 2) && i1 != this.dragEvent) {
                this.resetDrag();
            } else if (playerinventory.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(j);
                if (isValidDragMode(this.dragMode, entityhuman)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = i < this.inventorySlots.size() ? this.inventorySlots.get(i) : null; // Paper - Ensure drag in bounds

                itemstack1 = playerinventory.getItemStack();
                if (slot != null && canAddItemToSlot(slot, itemstack1, true) && slot.isItemValid(itemstack1) && (this.dragMode == 2 || itemstack1.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    itemstack2 = playerinventory.getItemStack().copy();
                    l = playerinventory.getItemStack().getCount();
                    Iterator iterator = this.dragSlots.iterator();

                    Map<Integer, ItemStack> draggedSlots = new HashMap<Integer, ItemStack>(); // CraftBukkit - Store slots from drag in map (raw slot id -> new stack)
                    while (iterator.hasNext()) {
                        Slot slot1 = (Slot) iterator.next();
                        ItemStack itemstack3 = playerinventory.getItemStack();

                        if (slot1 != null && canAddItemToSlot(slot1, itemstack3, true) && slot1.isItemValid(itemstack3) && (this.dragMode == 2 || itemstack3.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot1)) {
                            ItemStack itemstack4 = itemstack2.copy();
                            int j1 = slot1.getHasStack() ? slot1.getStack().getCount() : 0;

                            computeStackSize(this.dragSlots, this.dragMode, itemstack4, j1);
                            k = Math.min(itemstack4.getMaxStackSize(), slot1.getItemStackLimit(itemstack4));
                            if (itemstack4.getCount() > k) {
                                itemstack4.setCount(k);
                            }

                            l -= itemstack4.getCount() - j1;
                            // slot1.set(itemstack4);
                            draggedSlots.put(slot1.slotNumber, itemstack4); // CraftBukkit - Put in map instead of setting
                        }
                    }

                    // CraftBukkit start - InventoryDragEvent
                    InventoryView view = getBukkitView();
                    org.bukkit.inventory.ItemStack newcursor = CraftItemStack.asCraftMirror(itemstack2);
                    newcursor.setAmount(l);
                    Map<Integer, org.bukkit.inventory.ItemStack> eventmap = new HashMap<Integer, org.bukkit.inventory.ItemStack>();
                    for (Map.Entry<Integer, ItemStack> ditem : draggedSlots.entrySet()) {
                        eventmap.put(ditem.getKey(), CraftItemStack.asBukkitCopy(ditem.getValue()));
                    }

                    // It's essential that we set the cursor to the new value here to prevent item duplication if a plugin closes the inventory.
                    ItemStack oldCursor = playerinventory.getItemStack();
                    playerinventory.setItemStack(CraftItemStack.asNMSCopy(newcursor));

                    InventoryDragEvent event = new InventoryDragEvent(view, (newcursor.getType() != org.bukkit.Material.AIR ? newcursor : null), CraftItemStack.asBukkitCopy(oldCursor), this.dragMode == 1, eventmap);
                    entityhuman.world.getServer().getPluginManager().callEvent(event);

                    // Whether or not a change was made to the inventory that requires an update.
                    boolean needsUpdate = event.getResult() != Result.DEFAULT;

                    if (event.getResult() != Result.DENY) {
                        for (Map.Entry<Integer, ItemStack> dslot : draggedSlots.entrySet()) {
                            view.setItem(dslot.getKey(), CraftItemStack.asBukkitCopy(dslot.getValue()));
                        }
                        // The only time the carried item will be set to null is if the inventory is closed by the server.
                        // If the inventory is closed by the server, then the cursor items are dropped.  This is why we change the cursor early.
                        if (playerinventory.getItemStack() != null) {
                            playerinventory.setItemStack(CraftItemStack.asNMSCopy(event.getCursor()));
                            needsUpdate = true;
                        }
                    } else {
                        playerinventory.setItemStack(oldCursor);
                    }

                    if (needsUpdate && entityhuman instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) entityhuman).sendContainerToPlayer(this);
                    }
                    // CraftBukkit end
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else {
            Slot slot2;
            int k1;

            if ((inventoryclicktype == ClickType.PICKUP || inventoryclicktype == ClickType.QUICK_MOVE) && (j == 0 || j == 1)) {
                if (i == -999) {
                    if (!playerinventory.getItemStack().isEmpty()) {
                        if (j == 0) {
                            // CraftBukkit start
                            ItemStack carried = playerinventory.getItemStack();
                            playerinventory.setItemStack(ItemStack.EMPTY);
                            entityhuman.dropItem(carried, true);
                            // CraftBukkit start
                        }

                        if (j == 1) {
                            entityhuman.dropItem(playerinventory.getItemStack().splitStack(1), true);
                        }
                    }
                } else if (inventoryclicktype == ClickType.QUICK_MOVE) {
                    if (i < 0) {
                        return ItemStack.EMPTY;
                    }

                    slot2 = (Slot) this.inventorySlots.get(i);
                    if (slot2 == null || !slot2.canTakeStack(entityhuman)) {
                        return ItemStack.EMPTY;
                    }

                    for (itemstack2 = this.transferStackInSlot(entityhuman, i); !itemstack2.isEmpty() && ItemStack.areItemsEqual(slot2.getStack(), itemstack2); itemstack2 = this.transferStackInSlot(entityhuman, i)) {
                        itemstack = itemstack2.copy();
                    }
                } else {
                    if (i < 0) {
                        return ItemStack.EMPTY;
                    }

                    slot2 = (Slot) this.inventorySlots.get(i);
                    if (slot2 != null) {
                        itemstack2 = slot2.getStack();
                        itemstack1 = playerinventory.getItemStack();
                        if (!itemstack2.isEmpty()) {
                            itemstack = itemstack2.copy();
                        }

                        if (itemstack2.isEmpty()) {
                            if (!itemstack1.isEmpty() && slot2.isItemValid(itemstack1)) {
                                k1 = j == 0 ? itemstack1.getCount() : 1;
                                if (k1 > slot2.getItemStackLimit(itemstack1)) {
                                    k1 = slot2.getItemStackLimit(itemstack1);
                                }

                                slot2.putStack(itemstack1.splitStack(k1));
                            }
                        } else if (slot2.canTakeStack(entityhuman)) {
                            if (itemstack1.isEmpty()) {
                                if (itemstack2.isEmpty()) {
                                    slot2.putStack(ItemStack.EMPTY);
                                    playerinventory.setItemStack(ItemStack.EMPTY);
                                } else {
                                    k1 = j == 0 ? itemstack2.getCount() : (itemstack2.getCount() + 1) / 2;
                                    playerinventory.setItemStack(slot2.decrStackSize(k1));
                                    if (itemstack2.isEmpty()) {
                                        slot2.putStack(ItemStack.EMPTY);
                                    }

                                    slot2.onTake(entityhuman, playerinventory.getItemStack());
                                }
                            } else if (slot2.isItemValid(itemstack1)) {
                                if (itemstack2.getItem() == itemstack1.getItem() && itemstack2.getMetadata() == itemstack1.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack2, itemstack1)) {
                                    k1 = j == 0 ? itemstack1.getCount() : 1;
                                    if (k1 > slot2.getItemStackLimit(itemstack1) - itemstack2.getCount()) {
                                        k1 = slot2.getItemStackLimit(itemstack1) - itemstack2.getCount();
                                    }

                                    if (k1 > itemstack1.getMaxStackSize() - itemstack2.getCount()) {
                                        k1 = itemstack1.getMaxStackSize() - itemstack2.getCount();
                                    }

                                    itemstack1.shrink(k1);
                                    itemstack2.grow(k1);
                                } else if (itemstack1.getCount() <= slot2.getItemStackLimit(itemstack1)) {
                                    slot2.putStack(itemstack1);
                                    playerinventory.setItemStack(itemstack2);
                                }
                            } else if (itemstack2.getItem() == itemstack1.getItem() && itemstack1.getMaxStackSize() > 1 && (!itemstack2.getHasSubtypes() || itemstack2.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack2, itemstack1) && !itemstack2.isEmpty()) {
                                k1 = itemstack2.getCount();
                                if (k1 + itemstack1.getCount() <= itemstack1.getMaxStackSize()) {
                                    itemstack1.grow(k1);
                                    itemstack2 = slot2.decrStackSize(k1);
                                    if (itemstack2.isEmpty()) {
                                        slot2.putStack(ItemStack.EMPTY);
                                    }

                                    slot2.onTake(entityhuman, playerinventory.getItemStack());
                                }
                            }
                        }

                        slot2.onSlotChanged();
                        // CraftBukkit start - Make sure the client has the right slot contents
                        if (entityhuman instanceof EntityPlayerMP && slot2.getSlotStackLimit() != 64) {
                            ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketSetSlot(this.windowId, slot2.slotNumber, slot2.getStack()));
                            // Updating a crafting inventory makes the client reset the result slot, have to send it again
                            if (this.getBukkitView().getType() == InventoryType.WORKBENCH || this.getBukkitView().getType() == InventoryType.CRAFTING) {
                                ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketSetSlot(this.windowId, 0, this.getSlot(0).getStack()));
                            }
                        }
                        // CraftBukkit end
                    }
                }
            } else if (inventoryclicktype == ClickType.SWAP && j >= 0 && j < 9) {
                slot2 = (Slot) this.inventorySlots.get(i);
                itemstack2 = playerinventory.getStackInSlot(j);
                itemstack1 = slot2.getStack();
                if (!itemstack2.isEmpty() || !itemstack1.isEmpty()) {
                    if (itemstack2.isEmpty()) {
                        if (slot2.canTakeStack(entityhuman)) {
                            playerinventory.setInventorySlotContents(j, itemstack1);
                            slot2.onSwapCraft(itemstack1.getCount());
                            slot2.putStack(ItemStack.EMPTY);
                            slot2.onTake(entityhuman, itemstack1);
                        }
                    } else if (itemstack1.isEmpty()) {
                        if (slot2.isItemValid(itemstack2)) {
                            k1 = slot2.getItemStackLimit(itemstack2);
                            if (itemstack2.getCount() > k1) {
                                slot2.putStack(itemstack2.splitStack(k1));
                            } else {
                                slot2.putStack(itemstack2);
                                playerinventory.setInventorySlotContents(j, ItemStack.EMPTY);
                            }
                        }
                    } else if (slot2.canTakeStack(entityhuman) && slot2.isItemValid(itemstack2)) {
                        k1 = slot2.getItemStackLimit(itemstack2);
                        if (itemstack2.getCount() > k1) {
                            slot2.putStack(itemstack2.splitStack(k1));
                            slot2.onTake(entityhuman, itemstack1);
                            if (!playerinventory.addItemStackToInventory(itemstack1)) {
                                entityhuman.dropItem(itemstack1, true);
                            }
                        } else {
                            slot2.putStack(itemstack2);
                            playerinventory.setInventorySlotContents(j, itemstack1);
                            slot2.onTake(entityhuman, itemstack1);
                        }
                    }
                }
            } else if (inventoryclicktype == ClickType.CLONE && entityhuman.capabilities.isCreativeMode && playerinventory.getItemStack().isEmpty() && i >= 0) {
                slot2 = (Slot) this.inventorySlots.get(i);
                if (slot2 != null && slot2.getHasStack()) {
                    itemstack2 = slot2.getStack().copy();
                    itemstack2.setCount(itemstack2.getMaxStackSize());
                    playerinventory.setItemStack(itemstack2);
                }
            } else if (inventoryclicktype == ClickType.THROW && playerinventory.getItemStack().isEmpty() && i >= 0) {
                slot2 = (Slot) this.inventorySlots.get(i);
                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(entityhuman)) {
                    itemstack2 = slot2.decrStackSize(j == 0 ? 1 : slot2.getStack().getCount());
                    slot2.onTake(entityhuman, itemstack2);
                    entityhuman.dropItem(itemstack2, true);
                }
            } else if (inventoryclicktype == ClickType.PICKUP_ALL && i >= 0) {
                slot2 = (Slot) this.inventorySlots.get(i);
                itemstack2 = playerinventory.getItemStack();
                if (!itemstack2.isEmpty() && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(entityhuman))) {
                    l = j == 0 ? 0 : this.inventorySlots.size() - 1;
                    k1 = j == 0 ? 1 : -1;

                    for (int l1 = 0; l1 < 2; ++l1) {
                        for (int i2 = l; i2 >= 0 && i2 < this.inventorySlots.size() && itemstack2.getCount() < itemstack2.getMaxStackSize(); i2 += k1) {
                            Slot slot3 = (Slot) this.inventorySlots.get(i2);

                            if (slot3.getHasStack() && canAddItemToSlot(slot3, itemstack2, true) && slot3.canTakeStack(entityhuman) && this.canMergeSlot(itemstack2, slot3)) {
                                ItemStack itemstack5 = slot3.getStack();

                                if (l1 != 0 || itemstack5.getCount() != itemstack5.getMaxStackSize()) {
                                    k = Math.min(itemstack2.getMaxStackSize() - itemstack2.getCount(), itemstack5.getCount());
                                    ItemStack itemstack6 = slot3.decrStackSize(k);

                                    itemstack2.grow(k);
                                    if (itemstack6.isEmpty()) {
                                        slot3.putStack(ItemStack.EMPTY);
                                    }

                                    slot3.onTake(entityhuman, itemstack6);
                                }
                            }
                        }
                    }
                }

                this.detectAndSendChanges();
            }
        }

        return itemstack;
    }

    public boolean canMergeSlot(ItemStack itemstack, Slot slot) {
        return true;
    }

    public void onContainerClosed(EntityPlayer entityhuman) {
        InventoryPlayer playerinventory = entityhuman.inventory;

        if (!playerinventory.getItemStack().isEmpty()) {
            entityhuman.dropItem(playerinventory.getItemStack(), false);
            playerinventory.setItemStack(ItemStack.EMPTY);
        }

    }

    protected void clearContainer(EntityPlayer entityhuman, World world, IInventory iinventory) {
        int i;

        if (entityhuman.isEntityAlive() && (!(entityhuman instanceof EntityPlayerMP) || !((EntityPlayerMP) entityhuman).hasDisconnected())) {
            for (i = 0; i < iinventory.getSizeInventory(); ++i) {
                entityhuman.inventory.placeItemBackInInventory(world, iinventory.removeStackFromSlot(i));
            }

        } else {
            for (i = 0; i < iinventory.getSizeInventory(); ++i) {
                entityhuman.dropItem(iinventory.removeStackFromSlot(i), false);
            }

        }
    }

    public void onCraftMatrixChanged(IInventory iinventory) {
        this.detectAndSendChanges();
    }

    public void putStackInSlot(int i, ItemStack itemstack) {
        this.getSlot(i).putStack(itemstack);
    }

    public boolean getCanCraft(EntityPlayer entityhuman) {
        return !this.playerList.contains(entityhuman);
    }

    public void setCanCraft(EntityPlayer entityhuman, boolean flag) {
        if (flag) {
            this.playerList.remove(entityhuman);
        } else {
            this.playerList.add(entityhuman);
        }

    }

    public abstract boolean canInteractWith(EntityPlayer entityhuman);

    protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
        boolean flag1 = false;
        int k = i;

        if (flag) {
            k = j - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (itemstack.isStackable()) {
            while (!itemstack.isEmpty()) {
                if (flag) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();
                if (!itemstack1.isEmpty() && itemstack1.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    int l = itemstack1.getCount() + itemstack.getCount();

                    if (l <= itemstack.getMaxStackSize()) {
                        itemstack.setCount(0);
                        itemstack1.setCount(l);
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.getCount() < itemstack.getMaxStackSize()) {
                        itemstack.shrink(itemstack.getMaxStackSize() - itemstack1.getCount());
                        itemstack1.setCount(itemstack.getMaxStackSize());
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (flag) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (!itemstack.isEmpty()) {
            if (flag) {
                k = j - 1;
            } else {
                k = i;
            }

            while (true) {
                if (flag) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();
                if (itemstack1.isEmpty() && slot.isItemValid(itemstack)) {
                    if (itemstack.getCount() > slot.getSlotStackLimit()) {
                        slot.putStack(itemstack.splitStack(slot.getSlotStackLimit()));
                    } else {
                        slot.putStack(itemstack.splitStack(itemstack.getCount()));
                    }

                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }

                if (flag) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        return flag1;
    }

    public static int extractDragMode(int i) {
        return i >> 2 & 3;
    }

    public static int getDragEvent(int i) {
        return i & 3;
    }

    public static boolean isValidDragMode(int i, EntityPlayer entityhuman) {
        return i == 0 ? true : (i == 1 ? true : i == 2 && entityhuman.capabilities.isCreativeMode);
    }

    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public static boolean canAddItemToSlot(@Nullable Slot slot, ItemStack itemstack, boolean flag) {
        boolean flag1 = slot == null || !slot.getHasStack();

        return !flag1 && itemstack.isItemEqual(slot.getStack()) && ItemStack.areItemStackTagsEqual(slot.getStack(), itemstack) ? slot.getStack().getCount() + (flag ? 0 : itemstack.getCount()) <= itemstack.getMaxStackSize() : flag1;
    }

    public static void computeStackSize(Set<Slot> set, int i, ItemStack itemstack, int j) {
        switch (i) {
        case 0:
            itemstack.setCount(MathHelper.floor((float) itemstack.getCount() / (float) set.size()));
            break;

        case 1:
            itemstack.setCount(1);
            break;

        case 2:
            itemstack.setCount(itemstack.getItem().getItemStackLimit());
        }

        itemstack.grow(j);
    }

    public boolean canDragIntoSlot(Slot slot) {
        return true;
    }

    public static int calcRedstone(@Nullable TileEntity tileentity) {
        return tileentity instanceof IInventory ? calcRedstoneFromInventory((IInventory) tileentity) : 0;
    }

    public static int calcRedstoneFromInventory(@Nullable IInventory iinventory) {
        if (iinventory == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < iinventory.getSizeInventory(); ++j) {
                ItemStack itemstack = iinventory.getStackInSlot(j);

                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(iinventory.getInventoryStackLimit(), itemstack.getMaxStackSize());
                    ++i;
                }
            }

            f /= (float) iinventory.getSizeInventory();
            return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }

    protected void slotChangedCraftingGrid(World world, EntityPlayer entityhuman, InventoryCrafting inventorycrafting, InventoryCraftResult inventorycraftresult) {
        if (!world.isRemote) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityhuman;
            ItemStack itemstack = ItemStack.EMPTY;
            IRecipe irecipe = CraftingManager.findMatchingRecipe(inventorycrafting, world);

            if (irecipe != null && (irecipe.isDynamic() || !world.getGameRules().getBoolean("doLimitedCrafting") || entityplayer.getRecipeBook().isUnlocked(irecipe))) {
                inventorycraftresult.setRecipeUsed(irecipe);
                itemstack = irecipe.getCraftingResult(inventorycrafting);
            }
            itemstack = org.bukkit.craftbukkit.event.CraftEventFactory.callPreCraftEvent(inventorycrafting, itemstack, getBukkitView(), false); // CraftBukkit

            inventorycraftresult.setInventorySlotContents(0, itemstack);
            entityplayer.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
        }
    }
}
