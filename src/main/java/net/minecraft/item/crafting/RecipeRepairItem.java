package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeRepairItem extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class
    public RecipeRepairItem() {
        super("", new ItemStack(Items.field_151024_Q), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_151024_Q)));
    }
    // CraftBukkit end

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
            ItemStack itemstack = inventorycrafting.func_70301_a(i);

            if (!itemstack.func_190926_b()) {
                arraylist.add(itemstack);
                if (arraylist.size() > 1) {
                    ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                    if (itemstack.func_77973_b() != itemstack1.func_77973_b() || itemstack1.func_190916_E() != 1 || itemstack.func_190916_E() != 1 || !itemstack1.func_77973_b().func_77645_m()) {
                        return false;
                    }
                }
            }
        }

        return arraylist.size() == 2;
    }

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        ArrayList arraylist = Lists.newArrayList();

        ItemStack itemstack;

        for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
            itemstack = inventorycrafting.func_70301_a(i);
            if (!itemstack.func_190926_b()) {
                arraylist.add(itemstack);
                if (arraylist.size() > 1) {
                    ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                    if (itemstack.func_77973_b() != itemstack1.func_77973_b() || itemstack1.func_190916_E() != 1 || itemstack.func_190916_E() != 1 || !itemstack1.func_77973_b().func_77645_m()) {
                        return ItemStack.field_190927_a;
                    }
                }
            }
        }

        if (arraylist.size() == 2) {
            ItemStack itemstack2 = (ItemStack) arraylist.get(0);

            itemstack = (ItemStack) arraylist.get(1);
            if (itemstack2.func_77973_b() == itemstack.func_77973_b() && itemstack2.func_190916_E() == 1 && itemstack.func_190916_E() == 1 && itemstack2.func_77973_b().func_77645_m()) {
                Item item = itemstack2.func_77973_b();
                int j = item.func_77612_l() - itemstack2.func_77952_i();
                int k = item.func_77612_l() - itemstack.func_77952_i();
                int l = j + k + item.func_77612_l() * 5 / 100;
                int i1 = item.func_77612_l() - l;

                if (i1 < 0) {
                    i1 = 0;
                }

                // CraftBukkit start - Construct a dummy repair recipe
                ItemStack result = new ItemStack(itemstack.func_77973_b(), 1, i1);
                NonNullList<Ingredient> ingredients = NonNullList.func_191196_a();
                ingredients.add(Ingredient.func_193369_a(new ItemStack[]{itemstack2.func_77946_l()}));
                ingredients.add(Ingredient.func_193369_a(new ItemStack[]{itemstack.func_77946_l()}));
                ShapelessRecipes recipe = new ShapelessRecipes("", result.func_77946_l(), ingredients);
                recipe.key = new ResourceLocation("repairitem");
                inventorycrafting.currentRecipe = recipe;
                result = org.bukkit.craftbukkit.event.CraftEventFactory.callPreCraftEvent(inventorycrafting, result, inventorycrafting.field_70465_c.getBukkitView(), true);
                return result;
                // return new ItemStack(itemstack2.getItem(), 1, i1);
                // CraftBukkit end
            }
        }

        return ItemStack.field_190927_a;
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
