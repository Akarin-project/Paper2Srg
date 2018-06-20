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

    private final int field_77576_b;
    private final int field_77577_c;
    private final NonNullList<Ingredient> field_77574_d;
    private final ItemStack field_77575_e;
    private final String field_194137_e;
    // CraftBukkit start
    public ResourceLocation key;

    @Override
    public void setKey(ResourceLocation key) {
        this.key = key;
    }
    // CraftBukkit end

    public ShapedRecipes(String s, int i, int j, NonNullList<Ingredient> nonnulllist, ItemStack itemstack) {
        this.field_194137_e = s;
        this.field_77576_b = i;
        this.field_77577_c = j;
        this.field_77574_d = nonnulllist;
        this.field_77575_e = itemstack;
    }

    // CraftBukkit start
    @Override
    public org.bukkit.inventory.ShapedRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.field_77575_e);
        CraftShapedRecipe recipe = new CraftShapedRecipe(result, this);
        switch (this.field_77577_c) {
        case 1:
            switch (this.field_77576_b) {
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
            switch (this.field_77576_b) {
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
            switch (this.field_77576_b) {
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
        for (Ingredient list : this.field_77574_d) {
            if (list != null && list.field_193371_b.length > 0) {
                net.minecraft.item.ItemStack stack = list.field_193371_b[0];
                recipe.setIngredient(c, org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(stack.func_77973_b()), (list.field_193371_b.length) > 1 ? 32767 : stack.func_77960_j());
            }
            c++;
        }
        return recipe;
    }
    // CraftBukkit end

    @Override
    public ItemStack func_77571_b() {
        return this.field_77575_e;
    }

    @Override
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

    @Override
    public NonNullList<Ingredient> func_192400_c() {
        return this.field_77574_d;
    }

    @Override
    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        for (int i = 0; i <= 3 - this.field_77576_b; ++i) {
            for (int j = 0; j <= 3 - this.field_77577_c; ++j) {
                if (this.func_77573_a(inventorycrafting, i, j, true)) {
                    return true;
                }

                if (this.func_77573_a(inventorycrafting, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean func_77573_a(InventoryCrafting inventorycrafting, int i, int j, boolean flag) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - i;
                int j1 = l - j;
                Ingredient recipeitemstack = Ingredient.field_193370_a;

                if (i1 >= 0 && j1 >= 0 && i1 < this.field_77576_b && j1 < this.field_77577_c) {
                    if (flag) {
                        recipeitemstack = this.field_77574_d.get(this.field_77576_b - i1 - 1 + j1 * this.field_77576_b);
                    } else {
                        recipeitemstack = this.field_77574_d.get(i1 + j1 * this.field_77576_b);
                    }
                }

                if (!recipeitemstack.apply(inventorycrafting.func_70463_b(k, l))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        return this.func_77571_b().func_77946_l();
    }

    public int func_192403_f() {
        return this.field_77576_b;
    }

    public int func_192404_g() {
        return this.field_77577_c;
    }

    public static ShapedRecipes func_193362_a(JsonObject jsonobject) {
        String s = JsonUtils.func_151219_a(jsonobject, "group", "");
        Map map = func_192408_a(JsonUtils.func_152754_s(jsonobject, "key"));
        String[] astring = func_194134_a(func_192407_a(JsonUtils.func_151214_t(jsonobject, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList nonnulllist = func_192402_a(astring, map, i, j);
        ItemStack itemstack = func_192405_a(JsonUtils.func_152754_s(jsonobject, "result"), true);

        return new ShapedRecipes(s, i, j, nonnulllist, itemstack);
    }

    private static NonNullList<Ingredient> func_192402_a(String[] astring, Map<String, Ingredient> map, int i, int j) {
        NonNullList nonnulllist = NonNullList.func_191197_a(i * j, Ingredient.field_193370_a);
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
    static String[] func_194134_a(String... astring) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < astring.length; ++i1) {
            String s = astring[i1];

            i = Math.min(i, func_194135_a(s));
            int j1 = func_194136_b(s);

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

    private static int func_194135_a(String s) {
        int i;

        for (i = 0; i < s.length() && s.charAt(i) == 32; ++i) {
            ;
        }

        return i;
    }

    private static int func_194136_b(String s) {
        int i;

        for (i = s.length() - 1; i >= 0 && s.charAt(i) == 32; --i) {
            ;
        }

        return i;
    }

    private static String[] func_192407_a(JsonArray jsonarray) {
        String[] astring = new String[jsonarray.size()];

        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = JsonUtils.func_151206_a(jsonarray.get(i), "pattern[" + i + "]");

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

    private static Map<String, Ingredient> func_192408_a(JsonObject jsonobject) {
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

            hashmap.put(entry.getKey(), func_193361_a((JsonElement) entry.getValue()));
        }

        hashmap.put(" ", Ingredient.field_193370_a);
        return hashmap;
    }

    public static Ingredient func_193361_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            if (jsonelement.isJsonObject()) {
                return Ingredient.func_193369_a(new ItemStack[] { func_192405_a(jsonelement.getAsJsonObject(), false)});
            } else if (!jsonelement.isJsonArray()) {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            } else {
                JsonArray jsonarray = jsonelement.getAsJsonArray();

                if (jsonarray.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    ItemStack[] aitemstack = new ItemStack[jsonarray.size()];

                    for (int i = 0; i < jsonarray.size(); ++i) {
                        aitemstack[i] = func_192405_a(JsonUtils.func_151210_l(jsonarray.get(i), "item"), false);
                    }

                    return Ingredient.func_193369_a(aitemstack);
                }
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static ItemStack func_192405_a(JsonObject jsonobject, boolean flag) {
        String s = JsonUtils.func_151200_h(jsonobject, "item");
        Item item = Item.field_150901_e.func_82594_a(new ResourceLocation(s));

        if (item == null) {
            throw new JsonSyntaxException("Unknown item \'" + s + "\'");
        } else if (item.func_77614_k() && !jsonobject.has("data")) {
            throw new JsonParseException("Missing data for item \'" + s + "\'");
        } else {
            int i = JsonUtils.func_151208_a(jsonobject, "data", 0);
            int j = flag ? JsonUtils.func_151208_a(jsonobject, "count", 1) : 1;

            return new ItemStack(item, j, i);
        }
    }
}
