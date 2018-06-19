package net.minecraft.item.crafting;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class RecipesMapCloning extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class
    public RecipesMapCloning() {
        super("", new ItemStack(Items.field_151148_bJ, 0, -1), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_151148_bJ)));
    }
    // CraftBukkit end

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inventorycrafting.func_70302_i_(); ++j) {
            ItemStack itemstack1 = inventorycrafting.func_70301_a(j);

            if (!itemstack1.func_190926_b()) {
                if (itemstack1.func_77973_b() == Items.field_151098_aY) {
                    if (!itemstack.func_190926_b()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.func_77973_b() != Items.field_151148_bJ) {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.func_190926_b() && i > 0;
    }

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inventorycrafting.func_70302_i_(); ++j) {
            ItemStack itemstack1 = inventorycrafting.func_70301_a(j);

            if (!itemstack1.func_190926_b()) {
                if (itemstack1.func_77973_b() == Items.field_151098_aY) {
                    if (!itemstack.func_190926_b()) {
                        return ItemStack.field_190927_a;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.func_77973_b() != Items.field_151148_bJ) {
                        return ItemStack.field_190927_a;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.func_190926_b() && i >= 1) {
            ItemStack itemstack2 = new ItemStack(Items.field_151098_aY, i + 1, itemstack.func_77960_j());

            if (itemstack.func_82837_s()) {
                itemstack2.func_151001_c(itemstack.func_82833_r());
            }

            if (itemstack.func_77942_o()) {
                itemstack2.func_77982_d(itemstack.func_77978_p());
            }

            return itemstack2;
        } else {
            return ItemStack.field_190927_a;
        }
    }

    public ItemStack func_77571_b() {
        return ItemStack.field_190927_a;
    }

    public NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting) {
        NonNullList nonnulllist = NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inventorycrafting.func_70301_a(i);

            if (itemstack.func_77973_b().func_77634_r()) {
                nonnulllist.set(i, new ItemStack(itemstack.func_77973_b().func_77668_q()));
            }
        }

        return nonnulllist;
    }

    public boolean func_192399_d() {
        return true;
    }
}
