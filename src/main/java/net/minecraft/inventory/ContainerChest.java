package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerChest extends Container {

    private final IInventory field_75155_e;
    private final int field_75154_f;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory;
        if (this.field_75155_e instanceof InventoryPlayer) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((InventoryPlayer) this.field_75155_e);
        } else if (this.field_75155_e instanceof InventoryLargeChest) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) this.field_75155_e);
        } else {
            inventory = new CraftInventory(this.field_75155_e);
        }

        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerChest(IInventory iinventory, IInventory iinventory1, EntityPlayer entityhuman) {
        this.field_75155_e = iinventory1;
        this.field_75154_f = iinventory1.func_70302_i_() / 9;
        iinventory1.func_174889_b(entityhuman);
        int i = (this.field_75154_f - 4) * 18;

        // CraftBukkit start - Save player
        // TODO: Should we check to make sure it really is an InventoryPlayer?
        this.player = (InventoryPlayer) iinventory;
        // CraftBukkit end

        int j;
        int k;

        for (j = 0; j < this.field_75154_f; ++j) {
            for (k = 0; k < 9; ++k) {
                this.func_75146_a(new Slot(iinventory1, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.func_75146_a(new Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(iinventory, j, 8 + j * 18, 161 + i));
        }

    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_75155_e.func_70300_a(entityhuman);
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i < this.field_75154_f * 9) {
                if (!this.func_75135_a(itemstack1, this.field_75154_f * 9, this.field_75151_b.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 0, this.field_75154_f * 9, false)) {
                return ItemStack.field_190927_a;
            }

            if (itemstack1.func_190926_b()) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }
        }

        return itemstack;
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        this.field_75155_e.func_174886_c(entityhuman);
    }

    public IInventory func_85151_d() {
        return this.field_75155_e;
    }
}
