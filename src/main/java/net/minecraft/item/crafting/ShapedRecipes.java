package net.minecraft.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;
// CraftBukkit end

public class ShapedRecipes implements IRecipe {

    private final int recipeWidth;
    private final int recipeHeight;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final String group;
    // CraftBukkit start
    public ResourceLocation key;

    @Override
    public void setKey(ResourceLocation key) {
        this.key = key;
    }
    // CraftBukkit end

    public ShapedRecipes(String s, int i, int j, NonNullList<Ingredient> nonnulllist, ItemStack itemstack) {
        this.group = s;
        this.recipeWidth = i;
        this.recipeHeight = j;
        this.recipeItems = nonnulllist;
        this.recipeOutput = itemstack;
    }

    // CraftBukkit start
    @Override
    public org.bukkit.inventory.ShapedRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.recipeOutput);
        CraftShapedRecipe recipe = new CraftShapedRecipe(result, this);
        switch (this.recipeHeight) {
        case 1:
            switch (this.recipeWidth) {
            case 1:
                recipe.shape("a");
                break;
            case 2:
                recipe.shape("ab");
                break;
            case 3:
                recipe.shape("abc");
                break;
            }
            break;
        case 2:
            switch (this.recipeWidth) {
            case 1:
                recipe.shape("a","b");
                break;
            case 2:
                recipe.shape("ab","cd");
                break;
            case 3:
                recipe.shape("abc","def");
                break;
            }
            break;
        case 3:
            switch (this.recipeWidth) {
            case 1:
                recipe.shape("a","b","c");
                break;
            case 2:
                recipe.shape("ab","cd","ef");
                break;
            case 3:
                recipe.shape("abc","def","ghi");
                break;
            }
            break;
        }
        char c = 'a';
        for (Ingredient list : this.recipeItems) {
            if (list != null && list.matchingStacks.length > 0) {
                net.minecraft.item.ItemStack stack = list.matchingStacks[0];
                recipe.setIngredient(c, org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(stack.getItem()), (list.matchingStacks.length) > 1 ? 32767 : stack.getMetadata());
            }
            c++;
        }
        return recipe;
    }
    // CraftBukkit end

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
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

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(inventorycrafting, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(inventorycrafting, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting inventorycrafting, int i, int j, boolean flag) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - i;
                int j1 = l - j;
                Ingredient recipeitemstack = Ingredient.EMPTY;

                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight) {
                    if (flag) {
                        recipeitemstack = this.recipeItems.get(this.recipeWidth - i1 - 1 + j1 * this.recipeWidth);
                    } else {
                        recipeitemstack = this.recipeItems.get(i1 + j1 * this.recipeWidth);
                    }
                }

                if (!recipeitemstack.apply(inventorycrafting.getStackInRowAndColumn(k, l))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        return this.getRecipeOutput().copy();
    }

    public int getWidth() {
        return this.recipeWidth;
    }

    public int getHeight() {
        return this.recipeHeight;
    }

    public static ShapedRecipes deserialize(JsonObject jsonobject) {
        String s = JsonUtils.getString(jsonobject, "group", "");
        Map map = deserializeKey(JsonUtils.getJsonObject(jsonobject, "key"));
        String[] astring = shrink(patternFromJson(JsonUtils.getJsonArray(jsonobject, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList nonnulllist = deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = deserializeItem(JsonUtils.getJsonObject(jsonobject, "result"), true);

        return new ShapedRecipes(s, i, j, nonnulllist, itemstack);
    }

    private static NonNullList<Ingredient> deserializeIngredients(String[] astring, Map<String, Ingredient> map, int i, int j) {
        NonNullList nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        HashSet hashset = Sets.newHashSet(map.keySet());

        hashset.remove(" ");

        for (int k = 0; k < astring.length; ++k) {
            for (int l = 0; l < astring[k].length(); ++l) {
                String s = astring[k].substring(l, l + 1);
                Ingredient recipeitemstack = map.get(s);

                if (recipeitemstack == null) {
                    throw new JsonSyntaxException("Pattern references symbol \'" + s + "\' but it\'s not defined in the key");
                }

                hashset.remove(s);
                nonnulllist.set(l + i * k, recipeitemstack);
            }
        }

        if (!hashset.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren\'t used in pattern: " + hashset);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... astring) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < astring.length; ++i1) {
            String s = astring[i1];

            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);

            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (astring.length == l) {
            return new String[0];
        } else {
            String[] astring1 = new String[astring.length - l - k];

            for (int k1 = 0; k1 < astring1.length; ++k1) {
                astring1[k1] = astring[k1 + k].substring(i, j + 1);
            }

            return astring1;
        }
    }

    private static int firstNonSpace(String s) {
        int i;

        for (i = 0; i < s.length() && s.charAt(i) == 32; ++i) {
            ;
        }

        return i;
    }

    private static int lastNonSpace(String s) {
        int i;

        for (i = s.length() - 1; i >= 0 && s.charAt(i) == 32; --i) {
            ;
        }

        return i;
    }

    private static String[] patternFromJson(JsonArray jsonarray) {
        String[] astring = new String[jsonarray.size()];

        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = JsonUtils.getString(jsonarray.get(i), "pattern[" + i + "]");

                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    private static Map<String, Ingredient> deserializeKey(JsonObject jsonobject) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = jsonobject.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: \'" + (String) entry.getKey() + "\' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: \' \' is a reserved symbol.");
            }

            hashmap.put(entry.getKey(), deserializeIngredient((JsonElement) entry.getValue()));
        }

        hashmap.put(" ", Ingredient.EMPTY);
        return hashmap;
    }

    public static Ingredient deserializeIngredient(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            if (jsonelement.isJsonObject()) {
                return Ingredient.fromStacks(new ItemStack[] { deserializeItem(jsonelement.getAsJsonObject(), false)});
            } else if (!jsonelement.isJsonArray()) {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            } else {
                JsonArray jsonarray = jsonelement.getAsJsonArray();

                if (jsonarray.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    ItemStack[] aitemstack = new ItemStack[jsonarray.size()];

                    for (int i = 0; i < jsonarray.size(); ++i) {
                        aitemstack[i] = deserializeItem(JsonUtils.getJsonObject(jsonarray.get(i), "item"), false);
                    }

                    return Ingredient.fromStacks(aitemstack);
                }
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static ItemStack deserializeItem(JsonObject jsonobject, boolean flag) {
        String s = JsonUtils.getString(jsonobject, "item");
        Item item = Item.REGISTRY.getObject(new ResourceLocation(s));

        if (item == null) {
            throw new JsonSyntaxException("Unknown item \'" + s + "\'");
        } else if (item.getHasSubtypes() && !jsonobject.has("data")) {
            throw new JsonParseException("Missing data for item \'" + s + "\'");
        } else {
            int i = JsonUtils.getInt(jsonobject, "data", 0);
            int j = flag ? JsonUtils.getInt(jsonobject, "count", 1) : 1;

            return new ItemStack(item, j, i);
        }
    }
}
