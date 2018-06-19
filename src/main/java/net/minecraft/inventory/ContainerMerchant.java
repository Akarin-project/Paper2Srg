package net.minecraft.inventory;


import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

public class ContainerMerchant extends Container {

    private final IMerchant field_75178_e;
    private final InventoryMerchant field_75176_f;
    private final World field_75177_g;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), new org.bukkit.craftbukkit.inventory.CraftInventoryMerchant((InventoryMerchant) field_75176_f), this);
        }
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerMerchant(InventoryPlayer playerinventory, IMerchant imerchant, World world) {
        this.field_75178_e = imerchant;
        this.field_75177_g = world;
        this.field_75176_f = new InventoryMerchant(playerinventory.field_70458_d, imerchant);
        this.func_75146_a(new Slot(this.field_75176_f, 0, 36, 53));
        this.func_75146_a(new Slot(this.field_75176_f, 1, 62, 53));
        this.func_75146_a((Slot) (new SlotMerchantResult(playerinventory.field_70458_d, imerchant, this.field_75176_f, 2, 120, 53)));
        this.player = playerinventory; // CraftBukkit - save player

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

    }

    public InventoryMerchant func_75174_d() {
        return this.field_75176_f;
    }

    public void func_75130_a(IInventory iinventory) {
        this.field_75176_f.func_70470_g();
        super.func_75130_a(iinventory);
    }

    public void func_75175_c(int i) {
        this.field_75176_f.func_70471_c(i);
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        return this.field_75178_e.func_70931_l_() == entityhuman;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i == 2) {
                if (!this.func_75135_a(itemstack1, 3, 39, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 30) {
                    if (!this.func_75135_a(itemstack1, 30, 39, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (i >= 30 && i < 39 && !this.func_75135_a(itemstack1, 3, 30, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 3, 39, false)) {
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

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        this.field_75178_e.func_70932_a_((EntityPlayer) null);
        super.func_75134_a(entityhuman);
        if (!this.field_75177_g.field_72995_K) {
            ItemStack itemstack = this.field_75176_f.func_70304_b(0);

            if (!itemstack.func_190926_b()) {
                entityhuman.func_71019_a(itemstack, false);
            }

            itemstack = this.field_75176_f.func_70304_b(1);
            if (!itemstack.func_190926_b()) {
                entityhuman.func_71019_a(itemstack, false);
            }

        }
    }
}
