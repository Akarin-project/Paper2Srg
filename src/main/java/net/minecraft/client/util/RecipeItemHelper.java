package net.minecraft.client.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
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

    public final Int2IntMap field_194124_a = new Int2IntOpenHashMap();

    public RecipeItemHelper() {}

    public void func_194112_a(ItemStack itemstack) {
        if (!itemstack.func_190926_b() && !itemstack.func_77951_h() && !itemstack.func_77948_v() && !itemstack.func_82837_s()) {
            int i = func_194113_b(itemstack);
            int j = itemstack.func_190916_E();

            this.func_194117_b(i, j);
        }

    }

    public static int func_194113_b(ItemStack itemstack) {
        Item item = itemstack.func_77973_b();
        int i = item.func_77614_k() ? itemstack.func_77960_j() : 0;

        return Item.field_150901_e.func_148757_b(item) << 16 | i & '\uffff';
    }

    public boolean func_194120_a(int i) {
        return this.field_194124_a.get(i) > 0;
    }

    public int func_194122_a(int i, int j) {
        int k = this.field_194124_a.get(i);

        if (k >= j) {
            this.field_194124_a.put(i, k - j);
            return i;
        } else {
            return 0;
        }
    }

    private void func_194117_b(int i, int j) {
        this.field_194124_a.put(i, this.field_194124_a.get(i) + j);
    }

    public boolean func_194116_a(IRecipe irecipe, @Nullable IntList intlist) {
        return this.func_194118_a(irecipe, intlist, 1);
    }

    public boolean func_194118_a(IRecipe irecipe, @Nullable IntList intlist, int i) {
        return (new RecipeItemHelper.a(irecipe)).a(i, intlist);
    }

    public int func_194114_b(IRecipe irecipe, @Nullable IntList intlist) {
        return this.func_194121_a(irecipe, Integer.MAX_VALUE, intlist);
    }

    public int func_194121_a(IRecipe irecipe, int i, @Nullable IntList intlist) {
        return (new RecipeItemHelper.a(irecipe)).b(i, intlist);
    }

    public static ItemStack func_194115_b(int i) {
        return i == 0 ? ItemStack.field_190927_a : new ItemStack(Item.func_150899_d(i >> 16 & '\uffff'), 1, i & '\uffff');
    }

    public void func_194119_a() {
        this.field_194124_a.clear();
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
            this.c.addAll(irecipe.func_192400_c());
            this.c.removeIf((var0) -> {
                return var0 == Ingredient.field_193370_a;
            });
            this.d = this.c.size();
            this.e = this.a();
            this.f = this.e.length;
            this.g = new BitSet(this.d + this.f + this.d + this.d * this.f);

            for (int i = 0; i < this.c.size(); ++i) {
                IntList intlist = this.c.get(i).func_194139_b();

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
                    RecipeItemHelper.this.func_194122_a(this.e[this.h.getInt(0)], i);
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
                NonNullList nonnulllist = this.b.func_192400_c();

                for (int j1 = 0; j1 < nonnulllist.size(); ++j1) {
                    if (flag1 && nonnulllist.get(j1) == Ingredient.field_193370_a) {
                        intlist.add(0);
                    } else {
                        for (int k1 = 0; k1 < this.f; ++k1) {
                            if (this.b(false, i1, k1)) {
                                this.c(true, k1, i1);
                                RecipeItemHelper.this.func_194117_b(this.e[k1], i);
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

                intavltreeset.addAll(recipeitemstack.func_194139_b());
            }

            IntIterator intiterator = intavltreeset.iterator();

            while (intiterator.hasNext()) {
                if (!RecipeItemHelper.this.func_194120_a(intiterator.nextInt())) {
                    intiterator.remove();
                }
            }

            return intavltreeset.toIntArray();
        }

        private boolean a(int i) {
            int j = this.f;

            for (int k = 0; k < j; ++k) {
                if (RecipeItemHelper.this.field_194124_a.get(this.e[k]) >= i) {
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

                for (IntListIterator intlistiterator = recipeitemstack.func_194139_b().iterator(); intlistiterator.hasNext(); j = Math.max(j, RecipeItemHelper.this.field_194124_a.get(k))) {
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
