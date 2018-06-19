package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerPlayer extends Container {

    private static final EntityEquipmentSlot[] field_185003_h = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public InventoryCrafting field_75181_e = new InventoryCrafting(this, 2, 2);
    public InventoryCraftResult field_75179_f = new InventoryCraftResult();
    public boolean field_75180_g;
    private final EntityPlayer field_82862_h;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerPlayer(final InventoryPlayer playerinventory, boolean flag, EntityPlayer entityhuman) {
        this.field_75180_g = flag;
        this.field_82862_h = entityhuman;
        // CraftBukkit start
        this.field_75179_f = new InventoryCraftResult(); // CraftBukkit - moved to before InventoryCrafting construction
        this.field_75181_e = new InventoryCrafting(this, 2, 2, playerinventory.field_70458_d); // CraftBukkit - pass player
        this.field_75181_e.resultInventory = this.field_75179_f; // CraftBukkit - let InventoryCrafting know about its result slot
        this.player = playerinventory; // CraftBukkit - save player
        // CraftBukkit end
        this.func_75146_a((Slot) (new SlotCrafting(playerinventory.field_70458_d, this.field_75181_e, this.field_75179_f, 0, 154, 28)));

        int i;
        int j;

        for (i = 0; i < 2; ++i) {
            for (j = 0; j < 2; ++j) {
                this.func_75146_a(new Slot(this.field_75181_e, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            final EntityEquipmentSlot enumitemslot1 = ContainerPlayer.field_185003_h[i];

            this.func_75146_a(new Slot(playerinventory, 36 + (3 - i), 8, 8 + i * 18) {
                public int func_75219_a() {
                    return 1;
                }

                public boolean func_75214_a(ItemStack itemstack) {
                    return enumitemslot1 == EntityLiving.func_184640_d(itemstack); // CraftBukkit - decompile error
                }

                public boolean func_82869_a(EntityPlayer entityhuman) {
                    ItemStack itemstack = this.func_75211_c();

                    return !itemstack.func_190926_b() && !entityhuman.func_184812_l_() && EnchantmentHelper.func_190938_b(itemstack) ? false : super.func_82869_a(entityhuman);
                }
            });
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(playerinventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

        this.func_75146_a(new Slot(playerinventory, 40, 77, 62) {
        });
    }

    public void func_75130_a(IInventory iinventory) {
        this.func_192389_a(this.field_82862_h.field_70170_p, this.field_82862_h, this.field_75181_e, this.field_75179_f);
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        this.field_75179_f.func_174888_l();
        if (!entityhuman.field_70170_p.field_72995_K) {
            this.func_193327_a(entityhuman, entityhuman.field_70170_p, this.field_75181_e);
        }
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        return true;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            EntityEquipmentSlot enumitemslot = EntityLiving.func_184640_d(itemstack);

            if (i == 0) {
                if (!this.func_75135_a(itemstack1, 9, 45, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
            } else if (i >= 1 && i < 5) {
                if (!this.func_75135_a(itemstack1, 9, 45, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.func_75135_a(itemstack1, 9, 45, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.ARMOR && !((Slot) this.field_75151_b.get(8 - enumitemslot.func_188454_b())).func_75216_d()) {
                int j = 8 - enumitemslot.func_188454_b();

                if (!this.func_75135_a(itemstack1, j, j + 1, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (enumitemslot == EntityEquipmentSlot.OFFHAND && !((Slot) this.field_75151_b.get(45)).func_75216_d()) {
                if (!this.func_75135_a(itemstack1, 45, 46, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 9 && i < 36) {
                if (!this.func_75135_a(itemstack1, 36, 45, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.func_75135_a(itemstack1, 9, 36, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 9, 45, false)) {
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

            ItemStack itemstack2 = slot.func_190901_a(entityhuman, itemstack1);

            if (i == 0) {
                entityhuman.func_71019_a(itemstack2, false);
            }
        }

        return itemstack;
    }

    public boolean func_94530_a(ItemStack itemstack, Slot slot) {
        return slot.field_75224_c != this.field_75179_f && super.func_94530_a(itemstack, slot);
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.field_75181_e, this.field_75179_f);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
