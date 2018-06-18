package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerRepair extends Container {

    private static final Logger LOGGER = LogManager.getLogger();
    private final IInventory outputSlot = new InventoryCraftResult();
    private final IInventory inputSlots = new InventoryBasic("Repair", true, 2) {
        public void markDirty() {
            super.markDirty();
            ContainerRepair.this.onCraftMatrixChanged((IInventory) this);
        }
    };
    private final World world;
    private final BlockPos selfPosition;
    public int maximumCost;
    private int materialCost;
    public String repairedItemName;
    private final EntityPlayer player;
    // CraftBukkit start
    private int lastLevelCost;
    private CraftInventoryView bukkitEntity;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerRepair(InventoryPlayer playerinventory, final World world, final BlockPos blockposition, EntityPlayer entityhuman) {
        this.player = playerinventory; // CraftBukkit
        this.selfPosition = blockposition;
        this.world = world;
        this.player = entityhuman;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47) {
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }

            public boolean canTakeStack(EntityPlayer entityhuman) {
                return (entityhuman.capabilities.isCreativeMode || entityhuman.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }

            public ItemStack onTake(EntityPlayer entityhuman, ItemStack itemstack) {
                if (!entityhuman.capabilities.isCreativeMode) {
                    entityhuman.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }

                ContainerRepair.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
                if (ContainerRepair.this.materialCost > 0) {
                    ItemStack itemstack1 = ContainerRepair.this.inputSlots.getStackInSlot(1);

                    if (!itemstack1.isEmpty() && itemstack1.getCount() > ContainerRepair.this.materialCost) {
                        itemstack1.shrink(ContainerRepair.this.materialCost);
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack1);
                    } else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                } else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                }

                ContainerRepair.this.maximumCost = 0;
                IBlockState iblockdata = world.getBlockState(blockposition);

                if (!entityhuman.capabilities.isCreativeMode && !world.isRemote && iblockdata.getBlock() == Blocks.ANVIL && entityhuman.getRNG().nextFloat() < 0.12F) {
                    int i = ((Integer) iblockdata.getValue(BlockAnvil.DAMAGE)).intValue();

                    ++i;
                    if (i > 2) {
                        world.setBlockToAir(blockposition);
                        world.playEvent(1029, blockposition, 0);
                    } else {
                        world.setBlockState(blockposition, iblockdata.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(i)), 2);
                        world.playEvent(1030, blockposition, 0);
                    }
                } else if (!world.isRemote) {
                    world.playEvent(1030, blockposition, 0);
                }

                return itemstack;
            }
        });

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

    public void onCraftMatrixChanged(IInventory iinventory) {
        super.onCraftMatrixChanged(iinventory);
        if (iinventory == this.inputSlots) {
            this.updateRepairOutput();
        }

    }

    public void updateRepairOutput() {
        ItemStack itemstack = this.inputSlots.getStackInSlot(0);

        this.maximumCost = 1;
        int i = 0;
        byte b0 = 0;
        byte b1 = 0;

        if (itemstack.isEmpty()) {
            org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.EMPTY); // CraftBukkit
            this.maximumCost = 0;
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
            Map map = EnchantmentHelper.getEnchantments(itemstack1);
            int j = b0 + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());

            this.materialCost = 0;
            if (!itemstack2.isEmpty()) {
                boolean flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();
                int k;
                int l;
                int i1;

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
                    k = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    if (k <= 0) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.EMPTY); // CraftBukkit
                        this.maximumCost = 0;
                        return;
                    }

                    for (l = 0; k > 0 && l < itemstack2.getCount(); ++l) {
                        i1 = itemstack1.getItemDamage() - k;
                        itemstack1.setItemDamage(i1);
                        ++i;
                        k = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    this.materialCost = l;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.EMPTY); // CraftBukkit
                        this.maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag) {
                        k = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        l = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                        i1 = l + itemstack1.getMaxDamage() * 12 / 100;
                        int j1 = k + i1;
                        int k1 = itemstack1.getMaxDamage() - j1;

                        if (k1 < 0) {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.getMetadata()) {
                            itemstack1.setItemDamage(k1);
                            i += 2;
                        }
                    }

                    Map map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag1 = false;
                    boolean flag2 = false;
                    Iterator iterator = map1.keySet().iterator();

                    while (iterator.hasNext()) {
                        Enchantment enchantment = (Enchantment) iterator.next();

                        if (enchantment != null) {
                            int l1 = map.containsKey(enchantment) ? ((Integer) map.get(enchantment)).intValue() : 0;
                            int i2 = ((Integer) map1.get(enchantment)).intValue();

                            i2 = l1 == i2 ? i2 + 1 : Math.max(i2, l1);
                            boolean flag3 = enchantment.canApply(itemstack);

                            if (this.player.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                                flag3 = true;
                            }

                            Iterator iterator1 = map.keySet().iterator();

                            while (iterator1.hasNext()) {
                                Enchantment enchantment1 = (Enchantment) iterator1.next();

                                if (enchantment1 != enchantment && !enchantment.isCompatibleWith(enchantment1)) {
                                    flag3 = false;
                                    ++i;
                                }
                            }

                            if (!flag3) {
                                flag2 = true;
                            } else {
                                flag1 = true;
                                if (i2 > enchantment.getMaxLevel()) {
                                    i2 = enchantment.getMaxLevel();
                                }

                                map.put(enchantment, Integer.valueOf(i2));
                                int j2 = 0;

                                switch (enchantment.getRarity()) {
                                case COMMON:
                                    j2 = 1;
                                    break;

                                case UNCOMMON:
                                    j2 = 2;
                                    break;

                                case RARE:
                                    j2 = 4;
                                    break;

                                case VERY_RARE:
                                    j2 = 8;
                                }

                                if (flag) {
                                    j2 = Math.max(1, j2 / 2);
                                }

                                i += j2 * i2;
                                if (itemstack.getCount() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag2 && !flag1) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.EMPTY); // CraftBukkit
                        this.maximumCost = 0;
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    b1 = 1;
                    i += b1;
                    itemstack1.clearCustomName();
                }
            } else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
                b1 = 1;
                i += b1;
                itemstack1.setStackDisplayName(this.repairedItemName);
            }

            this.maximumCost = j + i;
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (b1 == i && b1 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }

            if (this.maximumCost >= 40 && !this.player.capabilities.isCreativeMode) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getRepairCost();

                if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
                    k2 = itemstack2.getRepairCost();
                }

                if (b1 != i || b1 == 0) {
                    k2 = k2 * 2 + 1;
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), itemstack1); // CraftBukkit
            this.detectAndSendChanges();
        }
    }

    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        icrafting.sendWindowProperty(this, 0, this.maximumCost);
    }

    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        if (!this.world.isRemote) {
            this.clearContainer(entityhuman, this.world, this.inputSlots);
        }
    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.world.getBlockState(this.selfPosition).getBlock() != Blocks.ANVIL ? false : entityhuman.getDistanceSq((double) this.selfPosition.getX() + 0.5D, (double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
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

    public void updateItemName(String s) {
        this.repairedItemName = s;
        if (this.getSlot(2).getHasStack()) {
            ItemStack itemstack = this.getSlot(2).getStack();

            if (StringUtils.isBlank(s)) {
                itemstack.clearCustomName();
            } else {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }

        this.updateRepairOutput();
    }

    // CraftBukkit start
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icrafting = (IContainerListener) this.listeners.get(i);

            //if (this.lastLevelCost != this.levelCost) { // Paper - this was the wrong solution to this, fixing it correctly in CraftPlayer
                icrafting.sendWindowProperty(this, 0, this.maximumCost);
            //} // Paper
        }

        this.lastLevelCost = this.maximumCost;
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryAnvil(
                new org.bukkit.Location(world.getWorld(), selfPosition.getX(), selfPosition.getY(), selfPosition.getZ()), this.inputSlots, this.outputSlot, this);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
