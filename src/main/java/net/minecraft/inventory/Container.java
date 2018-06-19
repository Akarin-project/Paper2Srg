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

    public NonNullList<ItemStack> field_75153_a = NonNullList.func_191196_a();
    public List<Slot> field_75151_b = Lists.newArrayList();
    public int field_75152_c;
    private int field_94535_f = -1;
    private int field_94536_g;
    private final Set<Slot> field_94537_h = Sets.newHashSet();
    protected List<IContainerListener> field_75149_d = Lists.newArrayList();
    private final Set<EntityPlayer> field_75148_f = Sets.newHashSet();
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

    protected Slot func_75146_a(Slot slot) {
        slot.field_75222_d = this.field_75151_b.size();
        this.field_75151_b.add(slot);
        this.field_75153_a.add(ItemStack.field_190927_a);
        return slot;
    }

    public void func_75132_a(IContainerListener icrafting) {
        if (this.field_75149_d.contains(icrafting)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.field_75149_d.add(icrafting);
            icrafting.func_71110_a(this, this.func_75138_a());
            this.func_75142_b();
        }
    }

    public NonNullList<ItemStack> func_75138_a() {
        NonNullList nonnulllist = NonNullList.func_191196_a();

        for (int i = 0; i < this.field_75151_b.size(); ++i) {
            nonnulllist.add(((Slot) this.field_75151_b.get(i)).func_75211_c());
        }

        return nonnulllist;
    }

    public void func_75142_b() {
        for (int i = 0; i < this.field_75151_b.size(); ++i) {
            ItemStack itemstack = ((Slot) this.field_75151_b.get(i)).func_75211_c();
            ItemStack itemstack1 = (ItemStack) this.field_75153_a.get(i);

            if (!ItemStack.fastMatches(itemstack1, itemstack) || (tickCount % org.spigotmc.SpigotConfig.itemDirtyTicks == 0 && !ItemStack.func_77989_b(itemstack1, itemstack))) { // Spigot
                itemstack1 = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.func_77946_l();
                this.field_75153_a.set(i, itemstack1);

                for (int j = 0; j < this.field_75149_d.size(); ++j) {
                    ((IContainerListener) this.field_75149_d.get(j)).func_71111_a(this, i, itemstack1);
                }
            }
        }
        tickCount++; // Spigot

    }

    public boolean func_75140_a(EntityPlayer entityhuman, int i) {
        return false;
    }

    @Nullable
    public Slot func_75147_a(IInventory iinventory, int i) {
        for (int j = 0; j < this.field_75151_b.size(); ++j) {
            Slot slot = (Slot) this.field_75151_b.get(j);

            if (slot.func_75217_a(iinventory, i)) {
                return slot;
            }
        }

        return null;
    }

    public Slot func_75139_a(int i) {
        return (Slot) this.field_75151_b.get(i);
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        Slot slot = (Slot) this.field_75151_b.get(i);

        return slot != null ? slot.func_75211_c() : ItemStack.field_190927_a;
    }

    public ItemStack func_184996_a(int i, int j, ClickType inventoryclicktype, EntityPlayer entityhuman) {
        ItemStack itemstack = ItemStack.field_190927_a;
        InventoryPlayer playerinventory = entityhuman.field_71071_by;
        ItemStack itemstack1;
        int k;
        ItemStack itemstack2;
        int l;

        if (inventoryclicktype == ClickType.QUICK_CRAFT) {
            int i1 = this.field_94536_g;

            this.field_94536_g = func_94532_c(j);
            if ((i1 != 1 || this.field_94536_g != 2) && i1 != this.field_94536_g) {
                this.func_94533_d();
            } else if (playerinventory.func_70445_o().func_190926_b()) {
                this.func_94533_d();
            } else if (this.field_94536_g == 0) {
                this.field_94535_f = func_94529_b(j);
                if (func_180610_a(this.field_94535_f, entityhuman)) {
                    this.field_94536_g = 1;
                    this.field_94537_h.clear();
                } else {
                    this.func_94533_d();
                }
            } else if (this.field_94536_g == 1) {
                Slot slot = i < this.field_75151_b.size() ? this.field_75151_b.get(i) : null; // Paper - Ensure drag in bounds

                itemstack1 = playerinventory.func_70445_o();
                if (slot != null && func_94527_a(slot, itemstack1, true) && slot.func_75214_a(itemstack1) && (this.field_94535_f == 2 || itemstack1.func_190916_E() > this.field_94537_h.size()) && this.func_94531_b(slot)) {
                    this.field_94537_h.add(slot);
                }
            } else if (this.field_94536_g == 2) {
                if (!this.field_94537_h.isEmpty()) {
                    itemstack2 = playerinventory.func_70445_o().func_77946_l();
                    l = playerinventory.func_70445_o().func_190916_E();
                    Iterator iterator = this.field_94537_h.iterator();

                    Map<Integer, ItemStack> draggedSlots = new HashMap<Integer, ItemStack>(); // CraftBukkit - Store slots from drag in map (raw slot id -> new stack)
                    while (iterator.hasNext()) {
                        Slot slot1 = (Slot) iterator.next();
                        ItemStack itemstack3 = playerinventory.func_70445_o();

                        if (slot1 != null && func_94527_a(slot1, itemstack3, true) && slot1.func_75214_a(itemstack3) && (this.field_94535_f == 2 || itemstack3.func_190916_E() >= this.field_94537_h.size()) && this.func_94531_b(slot1)) {
                            ItemStack itemstack4 = itemstack2.func_77946_l();
                            int j1 = slot1.func_75216_d() ? slot1.func_75211_c().func_190916_E() : 0;

                            func_94525_a(this.field_94537_h, this.field_94535_f, itemstack4, j1);
                            k = Math.min(itemstack4.func_77976_d(), slot1.func_178170_b(itemstack4));
                            if (itemstack4.func_190916_E() > k) {
                                itemstack4.func_190920_e(k);
                            }

                            l -= itemstack4.func_190916_E() - j1;
                            // slot1.set(itemstack4);
                            draggedSlots.put(slot1.field_75222_d, itemstack4); // CraftBukkit - Put in map instead of setting
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
                    ItemStack oldCursor = playerinventory.func_70445_o();
                    playerinventory.func_70437_b(CraftItemStack.asNMSCopy(newcursor));

                    InventoryDragEvent event = new InventoryDragEvent(view, (newcursor.getType() != org.bukkit.Material.AIR ? newcursor : null), CraftItemStack.asBukkitCopy(oldCursor), this.field_94535_f == 1, eventmap);
                    entityhuman.field_70170_p.getServer().getPluginManager().callEvent(event);

                    // Whether or not a change was made to the inventory that requires an update.
                    boolean needsUpdate = event.getResult() != Result.DEFAULT;

                    if (event.getResult() != Result.DENY) {
                        for (Map.Entry<Integer, ItemStack> dslot : draggedSlots.entrySet()) {
                            view.setItem(dslot.getKey(), CraftItemStack.asBukkitCopy(dslot.getValue()));
                        }
                        // The only time the carried item will be set to null is if the inventory is closed by the server.
                        // If the inventory is closed by the server, then the cursor items are dropped.  This is why we change the cursor early.
                        if (playerinventory.func_70445_o() != null) {
                            playerinventory.func_70437_b(CraftItemStack.asNMSCopy(event.getCursor()));
                            needsUpdate = true;
                        }
                    } else {
                        playerinventory.func_70437_b(oldCursor);
                    }

                    if (needsUpdate && entityhuman instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) entityhuman).func_71120_a(this);
                    }
                    // CraftBukkit end
                }

                this.func_94533_d();
            } else {
                this.func_94533_d();
            }
        } else if (this.field_94536_g != 0) {
            this.func_94533_d();
        } else {
            Slot slot2;
            int k1;

            if ((inventoryclicktype == ClickType.PICKUP || inventoryclicktype == ClickType.QUICK_MOVE) && (j == 0 || j == 1)) {
                if (i == -999) {
                    if (!playerinventory.func_70445_o().func_190926_b()) {
                        if (j == 0) {
                            // CraftBukkit start
                            ItemStack carried = playerinventory.func_70445_o();
                            playerinventory.func_70437_b(ItemStack.field_190927_a);
                            entityhuman.func_71019_a(carried, true);
                            // CraftBukkit start
                        }

                        if (j == 1) {
                            entityhuman.func_71019_a(playerinventory.func_70445_o().func_77979_a(1), true);
                        }
                    }
                } else if (inventoryclicktype == ClickType.QUICK_MOVE) {
                    if (i < 0) {
                        return ItemStack.field_190927_a;
                    }

                    slot2 = (Slot) this.field_75151_b.get(i);
                    if (slot2 == null || !slot2.func_82869_a(entityhuman)) {
                        return ItemStack.field_190927_a;
                    }

                    for (itemstack2 = this.func_82846_b(entityhuman, i); !itemstack2.func_190926_b() && ItemStack.func_179545_c(slot2.func_75211_c(), itemstack2); itemstack2 = this.func_82846_b(entityhuman, i)) {
                        itemstack = itemstack2.func_77946_l();
                    }
                } else {
                    if (i < 0) {
                        return ItemStack.field_190927_a;
                    }

                    slot2 = (Slot) this.field_75151_b.get(i);
                    if (slot2 != null) {
                        itemstack2 = slot2.func_75211_c();
                        itemstack1 = playerinventory.func_70445_o();
                        if (!itemstack2.func_190926_b()) {
                            itemstack = itemstack2.func_77946_l();
                        }

                        if (itemstack2.func_190926_b()) {
                            if (!itemstack1.func_190926_b() && slot2.func_75214_a(itemstack1)) {
                                k1 = j == 0 ? itemstack1.func_190916_E() : 1;
                                if (k1 > slot2.func_178170_b(itemstack1)) {
                                    k1 = slot2.func_178170_b(itemstack1);
                                }

                                slot2.func_75215_d(itemstack1.func_77979_a(k1));
                            }
                        } else if (slot2.func_82869_a(entityhuman)) {
                            if (itemstack1.func_190926_b()) {
                                if (itemstack2.func_190926_b()) {
                                    slot2.func_75215_d(ItemStack.field_190927_a);
                                    playerinventory.func_70437_b(ItemStack.field_190927_a);
                                } else {
                                    k1 = j == 0 ? itemstack2.func_190916_E() : (itemstack2.func_190916_E() + 1) / 2;
                                    playerinventory.func_70437_b(slot2.func_75209_a(k1));
                                    if (itemstack2.func_190926_b()) {
                                        slot2.func_75215_d(ItemStack.field_190927_a);
                                    }

                                    slot2.func_190901_a(entityhuman, playerinventory.func_70445_o());
                                }
                            } else if (slot2.func_75214_a(itemstack1)) {
                                if (itemstack2.func_77973_b() == itemstack1.func_77973_b() && itemstack2.func_77960_j() == itemstack1.func_77960_j() && ItemStack.func_77970_a(itemstack2, itemstack1)) {
                                    k1 = j == 0 ? itemstack1.func_190916_E() : 1;
                                    if (k1 > slot2.func_178170_b(itemstack1) - itemstack2.func_190916_E()) {
                                        k1 = slot2.func_178170_b(itemstack1) - itemstack2.func_190916_E();
                                    }

                                    if (k1 > itemstack1.func_77976_d() - itemstack2.func_190916_E()) {
                                        k1 = itemstack1.func_77976_d() - itemstack2.func_190916_E();
                                    }

                                    itemstack1.func_190918_g(k1);
                                    itemstack2.func_190917_f(k1);
                                } else if (itemstack1.func_190916_E() <= slot2.func_178170_b(itemstack1)) {
                                    slot2.func_75215_d(itemstack1);
                                    playerinventory.func_70437_b(itemstack2);
                                }
                            } else if (itemstack2.func_77973_b() == itemstack1.func_77973_b() && itemstack1.func_77976_d() > 1 && (!itemstack2.func_77981_g() || itemstack2.func_77960_j() == itemstack1.func_77960_j()) && ItemStack.func_77970_a(itemstack2, itemstack1) && !itemstack2.func_190926_b()) {
                                k1 = itemstack2.func_190916_E();
                                if (k1 + itemstack1.func_190916_E() <= itemstack1.func_77976_d()) {
                                    itemstack1.func_190917_f(k1);
                                    itemstack2 = slot2.func_75209_a(k1);
                                    if (itemstack2.func_190926_b()) {
                                        slot2.func_75215_d(ItemStack.field_190927_a);
                                    }

                                    slot2.func_190901_a(entityhuman, playerinventory.func_70445_o());
                                }
                            }
                        }

                        slot2.func_75218_e();
                        // CraftBukkit start - Make sure the client has the right slot contents
                        if (entityhuman instanceof EntityPlayerMP && slot2.func_75219_a() != 64) {
                            ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketSetSlot(this.field_75152_c, slot2.field_75222_d, slot2.func_75211_c()));
                            // Updating a crafting inventory makes the client reset the result slot, have to send it again
                            if (this.getBukkitView().getType() == InventoryType.WORKBENCH || this.getBukkitView().getType() == InventoryType.CRAFTING) {
                                ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketSetSlot(this.field_75152_c, 0, this.func_75139_a(0).func_75211_c()));
                            }
                        }
                        // CraftBukkit end
                    }
                }
            } else if (inventoryclicktype == ClickType.SWAP && j >= 0 && j < 9) {
                slot2 = (Slot) this.field_75151_b.get(i);
                itemstack2 = playerinventory.func_70301_a(j);
                itemstack1 = slot2.func_75211_c();
                if (!itemstack2.func_190926_b() || !itemstack1.func_190926_b()) {
                    if (itemstack2.func_190926_b()) {
                        if (slot2.func_82869_a(entityhuman)) {
                            playerinventory.func_70299_a(j, itemstack1);
                            slot2.func_190900_b(itemstack1.func_190916_E());
                            slot2.func_75215_d(ItemStack.field_190927_a);
                            slot2.func_190901_a(entityhuman, itemstack1);
                        }
                    } else if (itemstack1.func_190926_b()) {
                        if (slot2.func_75214_a(itemstack2)) {
                            k1 = slot2.func_178170_b(itemstack2);
                            if (itemstack2.func_190916_E() > k1) {
                                slot2.func_75215_d(itemstack2.func_77979_a(k1));
                            } else {
                                slot2.func_75215_d(itemstack2);
                                playerinventory.func_70299_a(j, ItemStack.field_190927_a);
                            }
                        }
                    } else if (slot2.func_82869_a(entityhuman) && slot2.func_75214_a(itemstack2)) {
                        k1 = slot2.func_178170_b(itemstack2);
                        if (itemstack2.func_190916_E() > k1) {
                            slot2.func_75215_d(itemstack2.func_77979_a(k1));
                            slot2.func_190901_a(entityhuman, itemstack1);
                            if (!playerinventory.func_70441_a(itemstack1)) {
                                entityhuman.func_71019_a(itemstack1, true);
                            }
                        } else {
                            slot2.func_75215_d(itemstack2);
                            playerinventory.func_70299_a(j, itemstack1);
                            slot2.func_190901_a(entityhuman, itemstack1);
                        }
                    }
                }
            } else if (inventoryclicktype == ClickType.CLONE && entityhuman.field_71075_bZ.field_75098_d && playerinventory.func_70445_o().func_190926_b() && i >= 0) {
                slot2 = (Slot) this.field_75151_b.get(i);
                if (slot2 != null && slot2.func_75216_d()) {
                    itemstack2 = slot2.func_75211_c().func_77946_l();
                    itemstack2.func_190920_e(itemstack2.func_77976_d());
                    playerinventory.func_70437_b(itemstack2);
                }
            } else if (inventoryclicktype == ClickType.THROW && playerinventory.func_70445_o().func_190926_b() && i >= 0) {
                slot2 = (Slot) this.field_75151_b.get(i);
                if (slot2 != null && slot2.func_75216_d() && slot2.func_82869_a(entityhuman)) {
                    itemstack2 = slot2.func_75209_a(j == 0 ? 1 : slot2.func_75211_c().func_190916_E());
                    slot2.func_190901_a(entityhuman, itemstack2);
                    entityhuman.func_71019_a(itemstack2, true);
                }
            } else if (inventoryclicktype == ClickType.PICKUP_ALL && i >= 0) {
                slot2 = (Slot) this.field_75151_b.get(i);
                itemstack2 = playerinventory.func_70445_o();
                if (!itemstack2.func_190926_b() && (slot2 == null || !slot2.func_75216_d() || !slot2.func_82869_a(entityhuman))) {
                    l = j == 0 ? 0 : this.field_75151_b.size() - 1;
                    k1 = j == 0 ? 1 : -1;

                    for (int l1 = 0; l1 < 2; ++l1) {
                        for (int i2 = l; i2 >= 0 && i2 < this.field_75151_b.size() && itemstack2.func_190916_E() < itemstack2.func_77976_d(); i2 += k1) {
                            Slot slot3 = (Slot) this.field_75151_b.get(i2);

                            if (slot3.func_75216_d() && func_94527_a(slot3, itemstack2, true) && slot3.func_82869_a(entityhuman) && this.func_94530_a(itemstack2, slot3)) {
                                ItemStack itemstack5 = slot3.func_75211_c();

                                if (l1 != 0 || itemstack5.func_190916_E() != itemstack5.func_77976_d()) {
                                    k = Math.min(itemstack2.func_77976_d() - itemstack2.func_190916_E(), itemstack5.func_190916_E());
                                    ItemStack itemstack6 = slot3.func_75209_a(k);

                                    itemstack2.func_190917_f(k);
                                    if (itemstack6.func_190926_b()) {
                                        slot3.func_75215_d(ItemStack.field_190927_a);
                                    }

                                    slot3.func_190901_a(entityhuman, itemstack6);
                                }
                            }
                        }
                    }
                }

                this.func_75142_b();
            }
        }

        return itemstack;
    }

    public boolean func_94530_a(ItemStack itemstack, Slot slot) {
        return true;
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        InventoryPlayer playerinventory = entityhuman.field_71071_by;

        if (!playerinventory.func_70445_o().func_190926_b()) {
            entityhuman.func_71019_a(playerinventory.func_70445_o(), false);
            playerinventory.func_70437_b(ItemStack.field_190927_a);
        }

    }

    protected void func_193327_a(EntityPlayer entityhuman, World world, IInventory iinventory) {
        int i;

        if (entityhuman.func_70089_S() && (!(entityhuman instanceof EntityPlayerMP) || !((EntityPlayerMP) entityhuman).func_193105_t())) {
            for (i = 0; i < iinventory.func_70302_i_(); ++i) {
                entityhuman.field_71071_by.func_191975_a(world, iinventory.func_70304_b(i));
            }

        } else {
            for (i = 0; i < iinventory.func_70302_i_(); ++i) {
                entityhuman.func_71019_a(iinventory.func_70304_b(i), false);
            }

        }
    }

    public void func_75130_a(IInventory iinventory) {
        this.func_75142_b();
    }

    public void func_75141_a(int i, ItemStack itemstack) {
        this.func_75139_a(i).func_75215_d(itemstack);
    }

    public boolean func_75129_b(EntityPlayer entityhuman) {
        return !this.field_75148_f.contains(entityhuman);
    }

    public void func_75128_a(EntityPlayer entityhuman, boolean flag) {
        if (flag) {
            this.field_75148_f.remove(entityhuman);
        } else {
            this.field_75148_f.add(entityhuman);
        }

    }

    public abstract boolean func_75145_c(EntityPlayer entityhuman);

    protected boolean func_75135_a(ItemStack itemstack, int i, int j, boolean flag) {
        boolean flag1 = false;
        int k = i;

        if (flag) {
            k = j - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (itemstack.func_77985_e()) {
            while (!itemstack.func_190926_b()) {
                if (flag) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = (Slot) this.field_75151_b.get(k);
                itemstack1 = slot.func_75211_c();
                if (!itemstack1.func_190926_b() && itemstack1.func_77973_b() == itemstack.func_77973_b() && (!itemstack.func_77981_g() || itemstack.func_77960_j() == itemstack1.func_77960_j()) && ItemStack.func_77970_a(itemstack, itemstack1)) {
                    int l = itemstack1.func_190916_E() + itemstack.func_190916_E();

                    if (l <= itemstack.func_77976_d()) {
                        itemstack.func_190920_e(0);
                        itemstack1.func_190920_e(l);
                        slot.func_75218_e();
                        flag1 = true;
                    } else if (itemstack1.func_190916_E() < itemstack.func_77976_d()) {
                        itemstack.func_190918_g(itemstack.func_77976_d() - itemstack1.func_190916_E());
                        itemstack1.func_190920_e(itemstack.func_77976_d());
                        slot.func_75218_e();
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

        if (!itemstack.func_190926_b()) {
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

                slot = (Slot) this.field_75151_b.get(k);
                itemstack1 = slot.func_75211_c();
                if (itemstack1.func_190926_b() && slot.func_75214_a(itemstack)) {
                    if (itemstack.func_190916_E() > slot.func_75219_a()) {
                        slot.func_75215_d(itemstack.func_77979_a(slot.func_75219_a()));
                    } else {
                        slot.func_75215_d(itemstack.func_77979_a(itemstack.func_190916_E()));
                    }

                    slot.func_75218_e();
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

    public static int func_94529_b(int i) {
        return i >> 2 & 3;
    }

    public static int func_94532_c(int i) {
        return i & 3;
    }

    public static boolean func_180610_a(int i, EntityPlayer entityhuman) {
        return i == 0 ? true : (i == 1 ? true : i == 2 && entityhuman.field_71075_bZ.field_75098_d);
    }

    protected void func_94533_d() {
        this.field_94536_g = 0;
        this.field_94537_h.clear();
    }

    public static boolean func_94527_a(@Nullable Slot slot, ItemStack itemstack, boolean flag) {
        boolean flag1 = slot == null || !slot.func_75216_d();

        return !flag1 && itemstack.func_77969_a(slot.func_75211_c()) && ItemStack.func_77970_a(slot.func_75211_c(), itemstack) ? slot.func_75211_c().func_190916_E() + (flag ? 0 : itemstack.func_190916_E()) <= itemstack.func_77976_d() : flag1;
    }

    public static void func_94525_a(Set<Slot> set, int i, ItemStack itemstack, int j) {
        switch (i) {
        case 0:
            itemstack.func_190920_e(MathHelper.func_76141_d((float) itemstack.func_190916_E() / (float) set.size()));
            break;

        case 1:
            itemstack.func_190920_e(1);
            break;

        case 2:
            itemstack.func_190920_e(itemstack.func_77973_b().func_77639_j());
        }

        itemstack.func_190917_f(j);
    }

    public boolean func_94531_b(Slot slot) {
        return true;
    }

    public static int func_178144_a(@Nullable TileEntity tileentity) {
        return tileentity instanceof IInventory ? func_94526_b((IInventory) tileentity) : 0;
    }

    public static int func_94526_b(@Nullable IInventory iinventory) {
        if (iinventory == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < iinventory.func_70302_i_(); ++j) {
                ItemStack itemstack = iinventory.func_70301_a(j);

                if (!itemstack.func_190926_b()) {
                    f += (float) itemstack.func_190916_E() / (float) Math.min(iinventory.func_70297_j_(), itemstack.func_77976_d());
                    ++i;
                }
            }

            f /= (float) iinventory.func_70302_i_();
            return MathHelper.func_76141_d(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }

    protected void func_192389_a(World world, EntityPlayer entityhuman, InventoryCrafting inventorycrafting, InventoryCraftResult inventorycraftresult) {
        if (!world.field_72995_K) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityhuman;
            ItemStack itemstack = ItemStack.field_190927_a;
            IRecipe irecipe = CraftingManager.func_192413_b(inventorycrafting, world);

            if (irecipe != null && (irecipe.func_192399_d() || !world.func_82736_K().func_82766_b("doLimitedCrafting") || entityplayer.func_192037_E().func_193830_f(irecipe))) {
                inventorycraftresult.func_193056_a(irecipe);
                itemstack = irecipe.func_77572_b(inventorycrafting);
            }
            itemstack = org.bukkit.craftbukkit.event.CraftEventFactory.callPreCraftEvent(inventorycrafting, itemstack, getBukkitView(), false); // CraftBukkit

            inventorycraftresult.func_70299_a(0, itemstack);
            entityplayer.field_71135_a.func_147359_a(new SPacketSetSlot(this.field_75152_c, 0, itemstack));
        }
    }
}
