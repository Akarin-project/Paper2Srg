package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

// CraftBukkit end

public class ContainerFurnace extends Container {

    private final IInventory field_75158_e;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryFurnace inventory = new CraftInventoryFurnace((TileEntityFurnace) this.field_75158_e);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerFurnace(InventoryPlayer playerinventory, IInventory iinventory) {
        this.field_75158_e = iinventory;
        this.func_75146_a(new Slot(iinventory, 0, 56, 17));
        this.func_75146_a((Slot) (new SlotFurnaceFuel(iinventory, 1, 56, 53)));
        this.func_75146_a((Slot) (new SlotFurnaceOutput(playerinventory.field_70458_d, iinventory, 2, 116, 35)));
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

    public void func_75132_a(IContainerListener icrafting) {
        super.func_75132_a(icrafting);
        icrafting.func_175173_a(this, this.field_75158_e);
    }

    public void func_75142_b() {
        super.func_75142_b();

        for (int i = 0; i < this.field_75149_d.size(); ++i) {
            IContainerListener icrafting = (IContainerListener) this.field_75149_d.get(i);

            if (this.field_178152_f != this.field_75158_e.func_174887_a_(2)) {
                icrafting.func_71112_a(this, 2, this.field_75158_e.func_174887_a_(2));
            }

            if (this.field_178154_h != this.field_75158_e.func_174887_a_(0)) {
                icrafting.func_71112_a(this, 0, this.field_75158_e.func_174887_a_(0));
            }

            if (this.field_178155_i != this.field_75158_e.func_174887_a_(1)) {
                icrafting.func_71112_a(this, 1, this.field_75158_e.func_174887_a_(1));
            }

            if (this.field_178153_g != this.field_75158_e.func_174887_a_(3)) {
                icrafting.func_71112_a(this, 3, this.field_75158_e.func_174887_a_(3));
            }
        }

        this.field_178152_f = this.field_75158_e.func_174887_a_(2);
        this.field_178154_h = this.field_75158_e.func_174887_a_(0);
        this.field_178155_i = this.field_75158_e.func_174887_a_(1);
        this.field_178153_g = this.field_75158_e.func_174887_a_(3);
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_75158_e.func_70300_a(entityhuman);
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
            } else if (i != 1 && i != 0) {
                if (!FurnaceRecipes.func_77602_a().func_151395_a(itemstack1).func_190926_b()) {
                    if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (TileEntityFurnace.func_145954_b(itemstack1)) {
                    if (!this.func_75135_a(itemstack1, 1, 2, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (i >= 3 && i < 30) {
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
}
