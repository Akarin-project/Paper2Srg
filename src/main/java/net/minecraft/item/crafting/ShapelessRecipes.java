package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Iterator;
import net;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;
// CraftBukkit end

public class ShapelessRecipes implements IRecipe {

    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final String group;
    // CraftBukkit start
    public ResourceLocation key;

    @Override
    public void setKey(ResourceLocation key) {
        this.key = key;
    }
    // CraftBukkit end

    public ShapelessRecipes(String s, ItemStack itemstack, NonNullList<Ingredient> nonnulllist) {
        this.group = s;
        this.recipeOutput = itemstack;
        this.recipeItems = nonnulllist;
    }

    // CraftBukkit start
    @SuppressWarnings("unchecked")
    public org.bukkit.inventory.ShapelessRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.recipeOutput);
        CraftShapelessRecipe recipe = new CraftShapelessRecipe(result, this);
        for (Ingredient list : this.recipeItems) {
            if (list != null) {
                net.minecraft.item.ItemStack stack = list.matchingStacks[0];
                recipe.addIngredient(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(stack.getItem()), (list.matchingStacks.length) > 1 ? 32767 : stack.getMetadata());
            }
        }
        return recipe;
    }
    // CraftBukkit end

    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
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

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        ArrayList arraylist = Lists.newArrayList(this.recipeItems);

        for (int i = 0; i < inventorycrafting.getHeight(); ++i) {
            for (int j = 0; j < inventorycrafting.getWidth(); ++j) {
                ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(j, i);

                if (!itemstack.isEmpty()) {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        Ingredient recipeitemstack = (Ingredient) iterator.next();

                        if (recipeitemstack.apply(itemstack)) {
                            flag = true;
                            arraylist.remove(recipeitemstack);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        return this.recipeOutput.copy();
    }

    public static ShapelessRecipes deserialize(JsonObject jsonobject) {
        String s = JsonUtils.getString(jsonobject, "group", "");
        NonNullList nonnulllist = deserializeIngredients(JsonUtils.getJsonArray(jsonobject, "ingredients"));

        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        } else {
            ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(jsonobject, "result"), true);

            return new ShapelessRecipes(s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> deserializeIngredients(JsonArray jsonarray) {
        NonNullList nonnulllist = NonNullList.create();

        for (int i = 0; i < jsonarray.size(); ++i) {
            Ingredient recipeitemstack = ShapedRecipes.deserializeIngredient(jsonarray.get(i));

            if (recipeitemstack != Ingredient.EMPTY) {
                nonnulllist.add(recipeitemstack);
            }
        }

        return nonnulllist;
    }
}
