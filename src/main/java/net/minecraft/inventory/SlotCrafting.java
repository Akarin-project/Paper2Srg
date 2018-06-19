package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

public class SlotCrafting extends Slot {

    private final InventoryCrafting field_75239_a;
    private final EntityPlayer field_75238_b;
    private int field_75237_g;

    public SlotCrafting(EntityPlayer entityhuman, InventoryCrafting inventorycrafting, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.field_75238_b = entityhuman;
        this.field_75239_a = inventorycrafting;
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return false;
    }

    public ItemStack func_75209_a(int i) {
        if (this.func_75216_d()) {
            this.field_75237_g += Math.min(i, this.func_75211_c().func_190916_E());
        }

        return super.func_75209_a(i);
    }

    protected void func_75210_a(ItemStack itemstack, int i) {
        this.field_75237_g += i;
        this.func_75208_c(itemstack);
    }

    protected void func_190900_b(int i) {
        this.field_75237_g += i;
    }

    protected void func_75208_c(ItemStack itemstack) {
        if (this.field_75237_g > 0) {
            itemstack.func_77980_a(this.field_75238_b.field_70170_p, this.field_75238_b, this.field_75237_g);
        }

        this.field_75237_g = 0;
        InventoryCraftResult inventorycraftresult = (InventoryCraftResult) this.field_75224_c;
        IRecipe irecipe = inventorycraftresult.func_193055_i();

        if (irecipe != null && !irecipe.func_192399_d()) {
            this.field_75238_b.func_192021_a((List) Lists.newArrayList(new IRecipe[] { irecipe}));
            inventorycraftresult.func_193056_a((IRecipe) null);
        }

    }

    public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
        this.func_75208_c(itemstack);
        NonNullList nonnulllist = CraftingManager.func_180303_b(this.field_75239_a, entityhuman.field_70170_p);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack1 = this.field_75239_a.func_70301_a(i);
            ItemStack itemstack2 = (ItemStack) nonnulllist.get(i);

            if (!itemstack1.func_190926_b()) {
                this.field_75239_a.func_70298_a(i, 1);
                itemstack1 = this.field_75239_a.func_70301_a(i);
            }

            if (!itemstack2.func_190926_b()) {
                if (itemstack1.func_190926_b()) {
                    this.field_75239_a.func_70299_a(i, itemstack2);
                } else if (ItemStack.func_179545_c(itemstack1, itemstack2) && ItemStack.func_77970_a(itemstack1, itemstack2)) {
                    itemstack2.func_190917_f(itemstack1.func_190916_E());
                    this.field_75239_a.func_70299_a(i, itemstack2);
                } else if (!this.field_75238_b.field_71071_by.func_70441_a(itemstack2)) {
                    this.field_75238_b.func_71019_a(itemstack2, false);
                }
            }
        }

        return itemstack;
    }
}
