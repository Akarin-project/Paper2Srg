package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// CraftBukkit end

public class ContainerWorkbench extends Container {

    public InventoryCrafting field_75162_e; // CraftBukkit - move initialization into constructor
    public InventoryCraftResult field_75160_f; // CraftBukkit - move initialization into constructor
    private final World field_75161_g;
    private final BlockPos field_178145_h;
    private final EntityPlayer field_192390_i;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerWorkbench(InventoryPlayer playerinventory, World world, BlockPos blockposition) {
        // CraftBukkit start - Switched order of IInventory construction and stored player
        this.field_75160_f = new InventoryCraftResult();
        this.field_75162_e = new InventoryCrafting(this, 3, 3, playerinventory.field_70458_d); // CraftBukkit - pass player
        this.field_75162_e.resultInventory = this.field_75160_f;
        this.player = playerinventory;
        // CraftBukkit end
        this.field_75161_g = world;
        this.field_178145_h = blockposition;
        this.field_192390_i = playerinventory.field_70458_d;
        this.func_75146_a((Slot) (new SlotCrafting(playerinventory.field_70458_d, this.field_75162_e, this.field_75160_f, 0, 124, 35)));

        int i;
        int j;

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                this.func_75146_a(new Slot(this.field_75162_e, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

    }

    public void func_75130_a(IInventory iinventory) {
        this.func_192389_a(this.field_75161_g, this.field_192390_i, this.field_75162_e, this.field_75160_f);
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        if (!this.field_75161_g.field_72995_K) {
            this.func_193327_a(entityhuman, this.field_75161_g, this.field_75162_e);
        }
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_75161_g.func_180495_p(this.field_178145_h).func_177230_c() != Blocks.field_150462_ai ? false : entityhuman.func_70092_e((double) this.field_178145_h.func_177958_n() + 0.5D, (double) this.field_178145_h.func_177956_o() + 0.5D, (double) this.field_178145_h.func_177952_p() + 0.5D) <= 64.0D;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i == 0) {
                itemstack1.func_77973_b().func_77622_d(itemstack1, this.field_75161_g, entityhuman);
                if (!this.func_75135_a(itemstack1, 10, 46, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
            } else if (i >= 10 && i < 37) {
                if (!this.func_75135_a(itemstack1, 37, 46, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.func_75135_a(itemstack1, 10, 37, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 10, 46, false)) {
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
        return slot.field_75224_c != this.field_75160_f && super.func_94530_a(itemstack, slot);
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.field_75162_e, this.field_75160_f);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
