package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerHopper extends Container {

    private final IInventory field_94538_a;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory = new CraftInventory(this.field_94538_a);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerHopper(InventoryPlayer playerinventory, IInventory iinventory, EntityPlayer entityhuman) {
        this.field_94538_a = iinventory;
        this.player = playerinventory; // CraftBukkit - save player
        iinventory.func_174889_b(entityhuman);
        boolean flag = true;

        int i;

        for (i = 0; i < iinventory.func_70302_i_(); ++i) {
            this.func_75146_a(new Slot(iinventory, i, 44 + i * 18, 20));
        }

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(playerinventory, i, 8 + i * 18, 109));
        }

    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_94538_a.func_70300_a(entityhuman);
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i < this.field_94538_a.func_70302_i_()) {
                if (!this.func_75135_a(itemstack1, this.field_94538_a.func_70302_i_(), this.field_75151_b.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 0, this.field_94538_a.func_70302_i_(), false)) {
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
        this.field_94538_a.func_174886_c(entityhuman);
    }
}
