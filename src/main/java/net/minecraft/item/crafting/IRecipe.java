package net.minecraft.item.crafting;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public interface IRecipe {

    boolean matches(InventoryCrafting inventorycrafting, World world);

    ItemStack getCraftingResult(InventoryCrafting inventorycrafting);

    ItemStack getRecipeOutput();

    NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting);

    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    default boolean isDynamic() {
        return false;
    }

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit

    void setKey(ResourceLocation key); // CraftBukkit
}
