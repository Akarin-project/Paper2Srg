package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack> {

    public static final Ingredient field_193370_a = new Ingredient(new ItemStack[0], null) {
        public boolean apply(@Nullable ItemStack itemstack) {
            return itemstack.func_190926_b();
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((ItemStack) object);
        }
    };
    public final ItemStack[] field_193371_b;
    private IntList field_194140_c;

    private Ingredient(ItemStack... aitemstack) {
        this.field_193371_b = aitemstack;
    }

    public boolean apply(@Nullable ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else {
            ItemStack[] aitemstack = this.field_193371_b;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack1 = aitemstack[j];

                if (itemstack1.func_77973_b() == itemstack.func_77973_b()) {
                    int k = itemstack1.func_77960_j();

                    if (k == 32767 || k == itemstack.func_77960_j()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public IntList func_194139_b() {
        if (this.field_194140_c == null) {
            this.field_194140_c = new IntArrayList(this.field_193371_b.length);
            ItemStack[] aitemstack = this.field_193371_b;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = aitemstack[j];

                this.field_194140_c.add(RecipeItemHelper.func_194113_b(itemstack));
            }

            this.field_194140_c.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.field_194140_c;
    }

    public static Ingredient func_193367_a(Item item) {
        return func_193369_a(new ItemStack[] { new ItemStack(item, 1, 32767)});
    }

    public static Ingredient func_193368_a(Item... aitem) {
        ItemStack[] aitemstack = new ItemStack[aitem.length];

        for (int i = 0; i < aitem.length; ++i) {
            aitemstack[i] = new ItemStack(aitem[i]);
        }

        return func_193369_a(aitemstack);
    }

    public static Ingredient func_193369_a(ItemStack... aitemstack) {
        if (aitemstack.length > 0) {
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = aitemstack1[j];

                if (!itemstack.func_190926_b()) {
                    return new Ingredient(aitemstack);
                }
            }
        }

        return Ingredient.field_193370_a;
    }

    public boolean apply(@Nullable Object object) {
        return this.apply((ItemStack) object);
    }

    Ingredient(ItemStack[] aitemstack, Object object) {
        this(aitemstack);
    }
}
