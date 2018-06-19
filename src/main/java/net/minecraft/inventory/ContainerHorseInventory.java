package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerHorseInventory extends Container {

    private final IInventory field_111243_a;
    private final AbstractHorse field_111242_f;

    // CraftBukkit start
    org.bukkit.craftbukkit.inventory.CraftInventoryView bukkitEntity;
    InventoryPlayer player;

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        return bukkitEntity = new CraftInventoryView(player.field_70458_d.getBukkitEntity(), field_111243_a.getOwner().getInventory(), this);
    }

    public ContainerHorseInventory(IInventory iinventory, final IInventory iinventory1, final AbstractHorse entityhorseabstract, EntityPlayer entityhuman) {
        player = (InventoryPlayer) iinventory;
        // CraftBukkit end
        this.field_111243_a = iinventory1;
        this.field_111242_f = entityhorseabstract;
        boolean flag = true;

        iinventory1.func_174889_b(entityhuman);
        boolean flag1 = true;

        this.func_75146_a(new Slot(iinventory1, 0, 8, 18) {
            public boolean func_75214_a(ItemStack itemstack) {
                return itemstack.func_77973_b() == Items.field_151141_av && !this.func_75216_d() && entityhorseabstract.func_190685_dA();
            }
        });
        this.func_75146_a(new Slot(iinventory1, 1, 8, 36) {
            public boolean func_75214_a(ItemStack itemstack) {
                return entityhorseabstract.func_190682_f(itemstack);
            }

            public int func_75219_a() {
                return 1;
            }
        });
        int i;
        int j;

        if (entityhorseabstract instanceof AbstractChestHorse && ((AbstractChestHorse) entityhorseabstract).func_190695_dh()) {
            for (i = 0; i < 3; ++i) {
                for (j = 0; j < ((AbstractChestHorse) entityhorseabstract).func_190696_dl(); ++j) {
                    this.func_75146_a(new Slot(iinventory1, 2 + j + i * ((AbstractChestHorse) entityhorseabstract).func_190696_dl(), 80 + j * 18, 18 + i * 18));
                }
            }
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(iinventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18 + -18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(iinventory, i, 8 + i * 18, 142));
        }

    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        return this.field_111243_a.func_70300_a(entityhuman) && this.field_111242_f.func_70089_S() && this.field_111242_f.func_70032_d((Entity) entityhuman) < 8.0F;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i < this.field_111243_a.func_70302_i_()) {
                if (!this.func_75135_a(itemstack1, this.field_111243_a.func_70302_i_(), this.field_75151_b.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.func_75139_a(1).func_75214_a(itemstack1) && !this.func_75139_a(1).func_75216_d()) {
                if (!this.func_75135_a(itemstack1, 1, 2, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.func_75139_a(0).func_75214_a(itemstack1)) {
                if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.field_111243_a.func_70302_i_() <= 2 || !this.func_75135_a(itemstack1, 2, this.field_111243_a.func_70302_i_(), false)) {
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
        this.field_111243_a.func_174886_c(entityhuman);
    }
}
