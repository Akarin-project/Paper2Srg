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

    public static final Ingredient EMPTY = new Ingredient(new ItemStack[0], null) {
        public boolean apply(@Nullable ItemStack itemstack) {
            return itemstack.isEmpty();
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((ItemStack) object);
        }
    };
    public final ItemStack[] matchingStacks;
    private IntList matchingStacksPacked;

    private Ingredient(ItemStack... aitemstack) {
        this.matchingStacks = aitemstack;
    }

    public boolean apply(@Nullable ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else {
            ItemStack[] aitemstack = this.matchingStacks;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack1 = aitemstack[j];

                if (itemstack1.getItem() == itemstack.getItem()) {
                    int k = itemstack1.getMetadata();

                    if (k == 32767 || k == itemstack.getMetadata()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public IntList getValidItemStacksPacked() {
        if (this.matchingStacksPacked == null) {
            this.matchingStacksPacked = new IntArrayList(this.matchingStacks.length);
            ItemStack[] aitemstack = this.matchingStacks;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = aitemstack[j];

                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }

            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.matchingStacksPacked;
    }

    public static Ingredient fromItem(Item item) {
        return fromStacks(new ItemStack[] { new ItemStack(item, 1, 32767)});
    }

    public static Ingredient fromItems(Item... aitem) {
        ItemStack[] aitemstack = new ItemStack[aitem.length];

        for (int i = 0; i < aitem.length; ++i) {
            aitemstack[i] = new ItemStack(aitem[i]);
        }

        return fromStacks(aitemstack);
    }

    public static Ingredient fromStacks(ItemStack... aitemstack) {
        if (aitemstack.length > 0) {
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = aitemstack1[j];

                if (!itemstack.isEmpty()) {
                    return new Ingredient(aitemstack);
                }
            }
        }

        return Ingredient.EMPTY;
    }

    public boolean apply(@Nullable Object object) {
        return this.apply((ItemStack) object);
    }

    Ingredient(ItemStack[] aitemstack, Object object) {
        this(aitemstack);
    }
}
