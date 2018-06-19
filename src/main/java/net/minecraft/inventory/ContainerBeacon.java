package net.minecraft.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

public class ContainerBeacon extends Container {

    private final IInventory field_82866_e;
    private final ContainerBeacon.BeaconSlot field_82864_f;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerBeacon(IInventory iinventory, IInventory iinventory1) {
        player = (InventoryPlayer) iinventory; // CraftBukkit - TODO: check this
        this.field_82866_e = iinventory1;
        this.field_82864_f = new ContainerBeacon.BeaconSlot(iinventory1, 0, 136, 110);
        this.func_75146_a((Slot) this.field_82864_f);
        boolean flag = true;
        boolean flag1 = true;

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(iinventory, j + i * 9 + 9, 36 + j * 18, 137 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(iinventory, i, 36 + i * 18, 195));
        }

    }

    public void func_75132_a(IContainerListener icrafting) {
        super.func_75132_a(icrafting);
        icrafting.func_175173_a(this, this.field_82866_e);
    }

    public IInventory func_180611_e() {
        return this.field_82866_e;
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        if (!entityhuman.field_70170_p.field_72995_K) {
            ItemStack itemstack = this.field_82864_f.func_75209_a(this.field_82864_f.func_75219_a());

            if (!itemstack.func_190926_b()) {
                entityhuman.func_71019_a(itemstack, false);
            }

        }
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_82866_e.func_70300_a(entityhuman);
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i == 0) {
                if (!this.func_75135_a(itemstack1, 1, 37, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
            } else if (!this.field_82864_f.func_75216_d() && this.field_82864_f.func_75214_a(itemstack1) && itemstack1.func_190916_E() == 1) {
                if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 1 && i < 28) {
                if (!this.func_75135_a(itemstack1, 28, 37, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 28 && i < 37) {
                if (!this.func_75135_a(itemstack1, 1, 28, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 1, 37, false)) {
                return ItemStack.field_190927_a;
            }

            if (itemstack1.func_190926_b()) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }

            if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }

            slot.func_190901_a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    class BeaconSlot extends Slot {

        public BeaconSlot(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        public boolean func_75214_a(ItemStack itemstack) {
            Item item = itemstack.func_77973_b();

            return item == Items.field_151166_bC || item == Items.field_151045_i || item == Items.field_151043_k || item == Items.field_151042_j;
        }

        public int func_75219_a() {
            return 1;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryBeacon((TileEntityBeacon) this.field_82866_e); // TODO - check this
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
