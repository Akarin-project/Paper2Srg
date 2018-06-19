package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

// CraftBukkit end

public class ContainerBrewingStand extends Container {

    private final IInventory tileBrewingStand;
    private final Slot slot;
    private int prevBrewTime;
    private int prevFuel;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerBrewingStand(InventoryPlayer playerinventory, IInventory iinventory) {
        player = playerinventory; // CraftBukkit
        this.tileBrewingStand = iinventory;
        this.addSlotToContainer((new ContainerBrewingStand.Potion(iinventory, 0, 56, 51)));
        this.addSlotToContainer((new ContainerBrewingStand.Potion(iinventory, 1, 79, 58)));
        this.addSlotToContainer((new ContainerBrewingStand.Potion(iinventory, 2, 102, 51)));
        this.slot = this.addSlotToContainer((new ContainerBrewingStand.Ingredient(iinventory, 3, 79, 17)));
        this.addSlotToContainer((new ContainerBrewingStand.a(iinventory, 4, 17, 17)));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

    }

    @Override
    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        icrafting.sendAllWindowProperties(this, this.tileBrewingStand);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icrafting = this.listeners.get(i);

            if (this.prevBrewTime != this.tileBrewingStand.getField(0)) {
                icrafting.sendWindowProperty(this, 0, this.tileBrewingStand.getField(0));
            }

            if (this.prevFuel != this.tileBrewingStand.getField(1)) {
                icrafting.sendWindowProperty(this, 1, this.tileBrewingStand.getField(1));
            }
        }

        this.prevBrewTime = this.tileBrewingStand.getField(0);
        this.prevFuel = this.tileBrewingStand.getField(1);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.tileBrewingStand.isUsableByPlayer(entityhuman);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if ((i < 0 || i > 2) && i != 3 && i != 4) {
                if (this.slot.isItemValid(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ContainerBrewingStand.Potion.canHoldPotion(itemstack) && itemstack.getCount() == 1) {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ContainerBrewingStand.a.b_(itemstack)) {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (i >= 5 && i < 32) {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (i >= 32 && i < 41) {
                    if (!this.mergeItemStack(itemstack1, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(entityhuman, itemstack1);
        }

        return itemstack;
    }

    static class a extends Slot {

        public a(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isItemValid(ItemStack itemstack) {
            return b_(itemstack);
        }

        public static boolean b_(ItemStack itemstack) {
            return itemstack.getItem() == Items.BLAZE_POWDER;
        }

        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Ingredient extends Slot {

        public Ingredient(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isItemValid(ItemStack itemstack) {
            return PotionHelper.isReagent(itemstack);
        }

        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Potion extends Slot {

        public Potion(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isItemValid(ItemStack itemstack) {
            return canHoldPotion(itemstack);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public ItemStack onTake(EntityPlayer entityhuman, ItemStack itemstack) {
            PotionType potionregistry = PotionUtils.getPotionFromItem(itemstack);

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.BREWED_POTION.trigger((EntityPlayerMP) entityhuman, potionregistry);
            }

            super.onTake(entityhuman, itemstack);
            return itemstack;
        }

        public static boolean canHoldPotion(ItemStack itemstack) {
            Item item = itemstack.getItem();

            return item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryBrewer inventory = new CraftInventoryBrewer(this.tileBrewingStand);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
