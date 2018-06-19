package net.minecraft.client.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class RecipeItemHelper {

    public final Int2IntMap itemToCount = new Int2IntOpenHashMap();

    public RecipeItemHelper() {}

    public void accountStack(ItemStack itemstack) {
        if (!itemstack.isEmpty() && !itemstack.isItemDamaged() && !itemstack.isItemEnchanted() && !itemstack.hasDisplayName()) {
            int i = pack(itemstack);
            int j = itemstack.getCount();

            this.increment(i, j);
        }

    }

    public static int pack(ItemStack itemstack) {
        Item item = itemstack.getItem();
        int i = item.getHasSubtypes() ? itemstack.getMetadata() : 0;

        return Item.REGISTRY.getIDForObject(item) << 16 | i & '\uffff';
    }

    public boolean containsItem(int i) {
        return this.itemToCount.get(i) > 0;
    }

    public int tryTake(int i, int j) {
        int k = this.itemToCount.get(i);

        if (k >= j) {
            this.itemToCount.put(i, k - j);
            return i;
        } else {
            return 0;
        }
    }

    private void increment(int i, int j) {
        this.itemToCount.put(i, this.itemToCount.get(i) + j);
    }

    public boolean canCraft(IRecipe irecipe, @Nullable IntList intlist) {
        return this.canCraft(irecipe, intlist, 1);
    }

    public boolean canCraft(IRecipe irecipe, @Nullable IntList intlist, int i) {
        return (new AutoRecipeStackManager.a(irecipe)).a(i, intlist);
    }

    public int getBiggestCraftableStack(IRecipe irecipe, @Nullable IntList intlist) {
        return this.getBiggestCraftableStack(irecipe, Integer.MAX_VALUE, intlist);
    }

    public int getBiggestCraftableStack(IRecipe irecipe, int i, @Nullable IntList intlist) {
        return (new AutoRecipeStackManager.a(irecipe)).b(i, intlist);
    }

    public static ItemStack unpack(int i) {
        return i == 0 ? ItemStack.EMPTY : new ItemStack(Item.getItemById(i >> 16 & '\uffff'), 1, i & '\uffff');
    }

    public void clear() {
        this.itemToCount.clear();
    }

    class a {

        private final IRecipe b;
        private final List<Ingredient> c = Lists.newArrayList();
        private final int d;
        private final int[] e;
        private final int f;
        private final BitSet g;
        private IntList h = new IntArrayList();

        public a(IRecipe irecipe) {
            this.b = irecipe;
            this.c.addAll(irecipe.getIngredients());
            this.c.removeIf((var0) -> {
                return recipeitemstack == Ingredient.EMPTY;
            });
            this.d = this.c.size();
            this.e = this.a();
            this.f = this.e.length;
            this.g = new BitSet(this.d + this.f + this.d + this.d * this.f);

            for (int i = 0; i < this.c.size(); ++i) {
                IntList intlist = this.c.get(i).getValidItemStacksPacked();

                for (int j = 0; j < this.f; ++j) {
                    if (intlist.contains(this.e[j])) {
                        this.g.set(this.d(true, j, i));
                    }
                }
            }

        }

        public boolean a(int i, @Nullable IntList intlist) {
            if (i <= 0) {
                return true;
            } else {
                int j;

                for (j = 0; this.a(i); ++j) {
                    RecipeItemHelper.this.tryTake(this.e[this.h.getInt(0)], i);
                    int k = this.h.size() - 1;

                    this.c(this.h.getInt(k));

                    for (int l = 0; l < k; ++l) {
                        this.c((l & 1) == 0, this.h.get(l).intValue(), this.h.get(l + 1).intValue());
                    }

                    this.h.clear();
                    this.g.clear(0, this.d + this.f);
                }

                boolean flag = j == this.d;
                boolean flag1 = flag && intlist != null;

                if (flag1) {
                    intlist.clear();
                }

                this.g.clear(0, this.d + this.f + this.d);
                int i1 = 0;
                NonNullList nonnulllist = this.b.getIngredients();

                for (int j1 = 0; j1 < nonnulllist.size(); ++j1) {
                    if (flag1 && nonnulllist.get(j1) == Ingredient.EMPTY) {
                        intlist.add(0);
                    } else {
                        for (int k1 = 0; k1 < this.f; ++k1) {
                            if (this.b(false, i1, k1)) {
                                this.c(true, k1, i1);
                                RecipeItemHelper.this.increment(this.e[k1], i);
                                if (flag1) {
                                    intlist.add(this.e[k1]);
                                }
                            }
                        }

                        ++i1;
                    }
                }

                return flag;
            }
        }

        private int[] a() {
            IntAVLTreeSet intavltreeset = new IntAVLTreeSet();
            Iterator iterator = this.c.iterator();

            while (iterator.hasNext()) {
                Ingredient recipeitemstack = (Ingredient) iterator.next();

                intavltreeset.addAll(recipeitemstack.getValidItemStacksPacked());
            }

            IntIterator intiterator = intavltreeset.iterator();

            while (intiterator.hasNext()) {
                if (!RecipeItemHelper.this.containsItem(intiterator.nextInt())) {
                    intiterator.remove();
                }
            }

            return intavltreeset.toIntArray();
        }

        private boolean a(int i) {
            int j = this.f;

            for (int k = 0; k < j; ++k) {
                if (RecipeItemHelper.this.itemToCount.get(this.e[k]) >= i) {
                    this.a(false, k);

                    while (!this.h.isEmpty()) {
                        int l = this.h.size();
                        boolean flag = (l & 1) == 1;
                        int i1 = this.h.getInt(l - 1);

                        if (!flag && !this.b(i1)) {
                            break;
                        }

                        int j1 = flag ? this.d : j;

                        int k1;

                        for (k1 = 0; k1 < j1; ++k1) {
                            if (!this.b(flag, k1) && this.a(flag, i1, k1) && this.b(flag, i1, k1)) {
                                this.a(flag, k1);
                                break;
                            }
                        }

                        k1 = this.h.size();
                        if (k1 == l) {
                            this.h.removeInt(k1 - 1);
                        }
                    }

                    if (!this.h.isEmpty()) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean b(int i) {
            return this.g.get(this.d(i));
        }

        private void c(int i) {
            this.g.set(this.d(i));
        }

        private int d(int i) {
            return this.d + this.f + i;
        }

        private boolean a(boolean flag, int i, int j) {
            return this.g.get(this.d(flag, i, j));
        }

        private boolean b(boolean flag, int i, int j) {
            return flag != this.g.get(1 + this.d(flag, i, j));
        }

        private void c(boolean flag, int i, int j) {
            this.g.flip(1 + this.d(flag, i, j));
        }

        private int d(boolean flag, int i, int j) {
            int k = flag ? i * this.d + j : j * this.d + i;

            return this.d + this.f + this.d + 2 * k;
        }

        private void a(boolean flag, int i) {
            this.g.set(this.c(flag, i));
            this.h.add(i);
        }

        private boolean b(boolean flag, int i) {
            return this.g.get(this.c(flag, i));
        }

        private int c(boolean flag, int i) {
            return (flag ? 0 : this.d) + i;
        }

        public int b(int i, @Nullable IntList intlist) {
            int j = 0;
            int k = Math.min(i, this.b()) + 1;

            while (true) {
                while (true) {
                    int l = (j + k) / 2;

                    if (this.a(l, (IntList) null)) {
                        if (k - j <= 1) {
                            if (l > 0) {
                                this.a(l, intlist);
                            }

                            return l;
                        }

                        j = l;
                    } else {
                        k = l;
                    }
                }
            }
        }

        private int b() {
            int i = Integer.MAX_VALUE;
            Iterator iterator = this.c.iterator();

            while (iterator.hasNext()) {
                Ingredient recipeitemstack = (Ingredient) iterator.next();
                int j = 0;

                int k;

                for (IntListIterator intlistiterator = recipeitemstack.getValidItemStacksPacked().iterator(); intlistiterator.hasNext(); j = Math.max(j, RecipeItemHelper.this.itemToCount.get(k))) {
                    k = intlistiterator.next().intValue();
                }

                if (i > 0) {
                    i = Math.min(i, j);
                }
            }

            return i;
        }
    }
}
