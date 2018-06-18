package net.minecraft.item.crafting;

import java.util.Collection;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeTippedArrow extends ShapedRecipes implements IRecipe { // CraftBukkit

    // CraftBukkit start
    public RecipeTippedArrow() {
        super("", 3, 3, NonNullList.from(Ingredient.EMPTY,
                Ingredient.fromItem(Items.ARROW), Ingredient.fromItem(Items.ARROW), Ingredient.fromItem(Items.ARROW),
                Ingredient.fromItem(Items.ARROW), Ingredient.fromItem(Items.LINGERING_POTION), Ingredient.fromItem(Items.ARROW),
                Ingredient.fromItem(Items.ARROW), Ingredient.fromItem(Items.ARROW), Ingredient.fromItem(Items.ARROW)),
                new ItemStack(Items.TIPPED_ARROW, 8));
    }
    // CraftBukkit end

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        if (inventorycrafting.getWidth() == 3 && inventorycrafting.getHeight() == 3) {
            for (int i = 0; i < inventorycrafting.getWidth(); ++i) {
                for (int j = 0; j < inventorycrafting.getHeight(); ++j) {
                    ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(i, j);

                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    Item item = itemstack.getItem();

                    if (i == 1 && j == 1) {
                        if (item != Items.LINGERING_POTION) {
                            return false;
                        }
                    } else if (item != Items.ARROW) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(1, 1);

        if (itemstack.getItem() != Items.LINGERING_POTION) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, 8);

            PotionUtils.addPotionToItemStack(itemstack1, PotionUtils.getPotionFromItem(itemstack));
            PotionUtils.appendEffects(itemstack1, (Collection) PotionUtils.getFullEffectsFromItem(itemstack));
            return itemstack1;
        }
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting) {
        return NonNullList.withSize(inventorycrafting.getSizeInventory(), ItemStack.EMPTY);
    }

    public boolean isDynamic() {
        return true;
    }
}
