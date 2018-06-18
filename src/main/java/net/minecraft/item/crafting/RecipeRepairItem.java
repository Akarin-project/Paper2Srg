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
        super("", new ItemStack(Items.LEATHER_HELMET), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.LEATHER_HELMET)));
    }
    // CraftBukkit end

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            ItemStack itemstack = inventorycrafting.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                arraylist.add(itemstack);
                if (arraylist.size() > 1) {
                    ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1 || !itemstack1.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
        }

        return arraylist.size() == 2;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ArrayList arraylist = Lists.newArrayList();

        ItemStack itemstack;

        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            itemstack = inventorycrafting.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                arraylist.add(itemstack);
                if (arraylist.size() > 1) {
                    ItemStack itemstack1 = (ItemStack) arraylist.get(0);

                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1 || !itemstack1.getItem().isDamageable()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (arraylist.size() == 2) {
            ItemStack itemstack2 = (ItemStack) arraylist.get(0);

            itemstack = (ItemStack) arraylist.get(1);
            if (itemstack2.getItem() == itemstack.getItem() && itemstack2.getCount() == 1 && itemstack.getCount() == 1 && itemstack2.getItem().isDamageable()) {
                Item item = itemstack2.getItem();
                int j = item.getMaxDamage() - itemstack2.getItemDamage();
                int k = item.getMaxDamage() - itemstack.getItemDamage();
                int l = j + k + item.getMaxDamage() * 5 / 100;
                int i1 = item.getMaxDamage() - l;

                if (i1 < 0) {
                    i1 = 0;
                }

                // CraftBukkit start - Construct a dummy repair recipe
                ItemStack result = new ItemStack(itemstack.getItem(), 1, i1);
                NonNullList<Ingredient> ingredients = NonNullList.create();
                ingredients.add(Ingredient.fromStacks(new ItemStack[]{itemstack2.copy()}));
                ingredients.add(Ingredient.fromStacks(new ItemStack[]{itemstack.copy()}));
                ShapelessRecipes recipe = new ShapelessRecipes("", result.copy(), ingredients);
                recipe.key = new ResourceLocation("repairitem");
                inventorycrafting.currentRecipe = recipe;
                result = org.bukkit.craftbukkit.event.CraftEventFactory.callPreCraftEvent(inventorycrafting, result, inventorycrafting.eventHandler.getBukkitView(), true);
                return result;
                // return new ItemStack(itemstack2.getItem(), 1, i1);
                // CraftBukkit end
            }
        }

        return ItemStack.EMPTY;
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting) {
        NonNullList nonnulllist = NonNullList.withSize(inventorycrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inventorycrafting.getStackInSlot(i);

            if (itemstack.getItem().hasContainerItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }

        return nonnulllist;
    }

    public boolean isDynamic() {
        return true;
    }
}
