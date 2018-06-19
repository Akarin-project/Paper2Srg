package net.minecraft.stats;

import java.util.BitSet;
import javax.annotation.Nullable;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBook {

    protected final BitSet recipes = new BitSet();
    protected final BitSet newRecipes = new BitSet();
    protected boolean isGuiOpen;
    protected boolean isFilteringCraftable;

    public RecipeBook() {}

    public void copyFrom(RecipeBook recipebook) {
        this.recipes.clear();
        this.newRecipes.clear();
        this.recipes.or(recipebook.recipes);
        this.newRecipes.or(recipebook.newRecipes);
    }

    public void unlock(IRecipe irecipe) {
        if (!irecipe.isDynamic()) {
            this.recipes.set(getRecipeId(irecipe));
        }

    }

    public boolean isUnlocked(@Nullable IRecipe irecipe) {
        return this.recipes.get(getRecipeId(irecipe));
    }

    public void lock(IRecipe irecipe) {
        int i = getRecipeId(irecipe);

        this.recipes.clear(i);
        this.newRecipes.clear(i);
    }

    protected static int getRecipeId(@Nullable IRecipe irecipe) {
        return CraftingManager.REGISTRY.getIDForObject(irecipe);
    }

    public void markSeen(IRecipe irecipe) {
        this.newRecipes.clear(getRecipeId(irecipe));
    }

    public void markNew(IRecipe irecipe) {
        this.newRecipes.set(getRecipeId(irecipe));
    }

    public void setGuiOpen(boolean flag) {
        this.isGuiOpen = flag;
    }

    public void setFilteringCraftable(boolean flag) {
        this.isFilteringCraftable = flag;
    }
}
