package net.minecraft.item.crafting;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public interface IRecipe {

    boolean func_77569_a(InventoryCrafting inventorycrafting, World world);

    ItemStack func_77572_b(InventoryCrafting inventorycrafting);

    ItemStack func_77571_b();

    NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting);

    default NonNullList<Ingredient> func_192400_c() {
        return NonNullList.func_191196_a();
    }

    default boolean func_192399_d() {
        return false;
    }

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit

    void setKey(ResourceLocation key); // CraftBukkit
}
