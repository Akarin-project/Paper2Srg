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

    private final ItemStack field_77580_a;
    private final NonNullList<Ingredient> field_77579_b;
    private final String field_194138_c;
    // CraftBukkit start
    public ResourceLocation key;

    @Override
    public void setKey(ResourceLocation key) {
        this.key = key;
    }
    // CraftBukkit end

    public ShapelessRecipes(String s, ItemStack itemstack, NonNullList<Ingredient> nonnulllist) {
        this.field_194138_c = s;
        this.field_77580_a = itemstack;
        this.field_77579_b = nonnulllist;
    }

    // CraftBukkit start
    @SuppressWarnings("unchecked")
    public org.bukkit.inventory.ShapelessRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.field_77580_a);
        CraftShapelessRecipe recipe = new CraftShapelessRecipe(result, this);
        for (Ingredient list : this.field_77579_b) {
            if (list != null) {
                net.minecraft.item.ItemStack stack = list.field_193371_b[0];
                recipe.addIngredient(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(stack.func_77973_b()), (list.field_193371_b.length) > 1 ? 32767 : stack.func_77960_j());
            }
        }
        return recipe;
    }
    // CraftBukkit end

    public ItemStack func_77571_b() {
        return this.field_77580_a;
    }

    public NonNullList<Ingredient> func_192400_c() {
        return this.field_77579_b;
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

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        ArrayList arraylist = Lists.newArrayList(this.field_77579_b);

        for (int i = 0; i < inventorycrafting.func_174923_h(); ++i) {
            for (int j = 0; j < inventorycrafting.func_174922_i(); ++j) {
                ItemStack itemstack = inventorycrafting.func_70463_b(j, i);

                if (!itemstack.func_190926_b()) {
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

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        return this.field_77580_a.func_77946_l();
    }

    public static ShapelessRecipes func_193363_a(JsonObject jsonobject) {
        String s = JsonUtils.func_151219_a(jsonobject, "group", "");
        NonNullList nonnulllist = func_193364_a(JsonUtils.func_151214_t(jsonobject, "ingredients"));

        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        } else {
            ItemStack itemstack = ShapedRecipes.func_192405_a(JsonUtils.func_152754_s(jsonobject, "result"), true);

            return new ShapelessRecipes(s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> func_193364_a(JsonArray jsonarray) {
        NonNullList nonnulllist = NonNullList.func_191196_a();

        for (int i = 0; i < jsonarray.size(); ++i) {
            Ingredient recipeitemstack = ShapedRecipes.func_193361_a(jsonarray.get(i));

            if (recipeitemstack != Ingredient.field_193370_a) {
                nonnulllist.add(recipeitemstack);
            }
        }

        return nonnulllist;
    }
}
