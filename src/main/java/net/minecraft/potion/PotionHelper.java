package net.minecraft.potion;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class PotionHelper {

    private static final List<PotionHelper.MixPredicate<PotionType>> field_185213_a = Lists.newArrayList();
    private static final List<PotionHelper.MixPredicate<Item>> field_185214_b = Lists.newArrayList();
    private static final List<Ingredient> field_185215_c = Lists.newArrayList();
    private static final Predicate<ItemStack> field_185216_d = new Predicate() {
        public boolean a(ItemStack itemstack) {
            Iterator iterator = PotionHelper.field_185215_c.iterator();

            Ingredient recipeitemstack;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                recipeitemstack = (Ingredient) iterator.next();
            } while (!recipeitemstack.apply(itemstack));

            return true;
        }

        public boolean apply(Object object) {
            return this.a((ItemStack) object);
        }
    };

    public static boolean func_185205_a(ItemStack itemstack) {
        return func_185203_b(itemstack) || func_185211_c(itemstack);
    }

    protected static boolean func_185203_b(ItemStack itemstack) {
        int i = 0;

        for (int j = PotionHelper.field_185214_b.size(); i < j; ++i) {
            if (((PotionHelper.MixPredicate) PotionHelper.field_185214_b.get(i)).field_185199_b.apply(itemstack)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean func_185211_c(ItemStack itemstack) {
        int i = 0;

        for (int j = PotionHelper.field_185213_a.size(); i < j; ++i) {
            if (((PotionHelper.MixPredicate) PotionHelper.field_185213_a.get(i)).field_185199_b.apply(itemstack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean func_185208_a(ItemStack itemstack, ItemStack itemstack1) {
        return !PotionHelper.field_185216_d.apply(itemstack) ? false : func_185206_b(itemstack, itemstack1) || func_185209_c(itemstack, itemstack1);
    }

    protected static boolean func_185206_b(ItemStack itemstack, ItemStack itemstack1) {
        Item item = itemstack.func_77973_b();
        int i = 0;

        for (int j = PotionHelper.field_185214_b.size(); i < j; ++i) {
            PotionHelper.MixPredicate potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.field_185214_b.get(i);

            if (potionbrewer_predicatedcombination.field_185198_a == item && potionbrewer_predicatedcombination.field_185199_b.apply(itemstack1)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean func_185209_c(ItemStack itemstack, ItemStack itemstack1) {
        PotionType potionregistry = PotionUtils.func_185191_c(itemstack);
        int i = 0;

        for (int j = PotionHelper.field_185213_a.size(); i < j; ++i) {
            PotionHelper.MixPredicate potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.field_185213_a.get(i);

            if (potionbrewer_predicatedcombination.field_185198_a == potionregistry && potionbrewer_predicatedcombination.field_185199_b.apply(itemstack1)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack func_185212_d(ItemStack itemstack, ItemStack itemstack1) {
        if (!itemstack1.func_190926_b()) {
            PotionType potionregistry = PotionUtils.func_185191_c(itemstack1);
            Item item = itemstack1.func_77973_b();
            int i = 0;

            int j;
            PotionHelper.MixPredicate potionbrewer_predicatedcombination;

            for (j = PotionHelper.field_185214_b.size(); i < j; ++i) {
                potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.field_185214_b.get(i);
                if (potionbrewer_predicatedcombination.field_185198_a == item && potionbrewer_predicatedcombination.field_185199_b.apply(itemstack)) {
                    return PotionUtils.func_185188_a(new ItemStack((Item) potionbrewer_predicatedcombination.field_185200_c), potionregistry);
                }
            }

            i = 0;

            for (j = PotionHelper.field_185213_a.size(); i < j; ++i) {
                potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.field_185213_a.get(i);
                if (potionbrewer_predicatedcombination.field_185198_a == potionregistry && potionbrewer_predicatedcombination.field_185199_b.apply(itemstack)) {
                    return PotionUtils.func_185188_a(new ItemStack(item), (PotionType) potionbrewer_predicatedcombination.field_185200_c);
                }
            }
        }

        return itemstack1;
    }

    public static void func_185207_a() {
        func_193354_a(Items.field_151068_bn);
        func_193354_a(Items.field_185155_bH);
        func_193354_a(Items.field_185156_bI);
        func_193355_a(Items.field_151068_bn, Items.field_151016_H, Items.field_185155_bH);
        func_193355_a(Items.field_185155_bH, Items.field_185157_bK, Items.field_185156_bI);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151060_bw, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151073_bk, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_179556_br, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151065_br, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151070_bp, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151102_aT, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151064_bs, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151114_aO, PotionTypes.field_185232_d);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151137_ax, PotionTypes.field_185231_c);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151075_bm, PotionTypes.field_185233_e);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151150_bK, PotionTypes.field_185234_f);
        func_193357_a(PotionTypes.field_185234_f, Items.field_151137_ax, PotionTypes.field_185235_g);
        func_193357_a(PotionTypes.field_185234_f, Items.field_151071_bq, PotionTypes.field_185236_h);
        func_193357_a(PotionTypes.field_185235_g, Items.field_151071_bq, PotionTypes.field_185237_i);
        func_193357_a(PotionTypes.field_185236_h, Items.field_151137_ax, PotionTypes.field_185237_i);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151064_bs, PotionTypes.field_185241_m);
        func_193357_a(PotionTypes.field_185241_m, Items.field_151137_ax, PotionTypes.field_185242_n);
        func_193357_a(PotionTypes.field_185233_e, Items.field_179556_br, PotionTypes.field_185238_j);
        func_193357_a(PotionTypes.field_185238_j, Items.field_151137_ax, PotionTypes.field_185239_k);
        func_193357_a(PotionTypes.field_185238_j, Items.field_151114_aO, PotionTypes.field_185240_l);
        func_193357_a(PotionTypes.field_185238_j, Items.field_151071_bq, PotionTypes.field_185246_r);
        func_193357_a(PotionTypes.field_185239_k, Items.field_151071_bq, PotionTypes.field_185247_s);
        func_193357_a(PotionTypes.field_185246_r, Items.field_151137_ax, PotionTypes.field_185247_s);
        func_193357_a(PotionTypes.field_185243_o, Items.field_151071_bq, PotionTypes.field_185246_r);
        func_193357_a(PotionTypes.field_185244_p, Items.field_151071_bq, PotionTypes.field_185247_s);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151102_aT, PotionTypes.field_185243_o);
        func_193357_a(PotionTypes.field_185243_o, Items.field_151137_ax, PotionTypes.field_185244_p);
        func_193357_a(PotionTypes.field_185243_o, Items.field_151114_aO, PotionTypes.field_185245_q);
        func_193356_a(PotionTypes.field_185233_e, Ingredient.func_193369_a(new ItemStack[] { new ItemStack(Items.field_151115_aP, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a())}), PotionTypes.field_185248_t);
        func_193357_a(PotionTypes.field_185248_t, Items.field_151137_ax, PotionTypes.field_185249_u);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151060_bw, PotionTypes.field_185250_v);
        func_193357_a(PotionTypes.field_185250_v, Items.field_151114_aO, PotionTypes.field_185251_w);
        func_193357_a(PotionTypes.field_185250_v, Items.field_151071_bq, PotionTypes.field_185252_x);
        func_193357_a(PotionTypes.field_185251_w, Items.field_151071_bq, PotionTypes.field_185253_y);
        func_193357_a(PotionTypes.field_185252_x, Items.field_151114_aO, PotionTypes.field_185253_y);
        func_193357_a(PotionTypes.field_185254_z, Items.field_151071_bq, PotionTypes.field_185252_x);
        func_193357_a(PotionTypes.field_185218_A, Items.field_151071_bq, PotionTypes.field_185252_x);
        func_193357_a(PotionTypes.field_185219_B, Items.field_151071_bq, PotionTypes.field_185253_y);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151070_bp, PotionTypes.field_185254_z);
        func_193357_a(PotionTypes.field_185254_z, Items.field_151137_ax, PotionTypes.field_185218_A);
        func_193357_a(PotionTypes.field_185254_z, Items.field_151114_aO, PotionTypes.field_185219_B);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151073_bk, PotionTypes.field_185220_C);
        func_193357_a(PotionTypes.field_185220_C, Items.field_151137_ax, PotionTypes.field_185221_D);
        func_193357_a(PotionTypes.field_185220_C, Items.field_151114_aO, PotionTypes.field_185222_E);
        func_193357_a(PotionTypes.field_185233_e, Items.field_151065_br, PotionTypes.field_185223_F);
        func_193357_a(PotionTypes.field_185223_F, Items.field_151137_ax, PotionTypes.field_185224_G);
        func_193357_a(PotionTypes.field_185223_F, Items.field_151114_aO, PotionTypes.field_185225_H);
        func_193357_a(PotionTypes.field_185230_b, Items.field_151071_bq, PotionTypes.field_185226_I);
        func_193357_a(PotionTypes.field_185226_I, Items.field_151137_ax, PotionTypes.field_185227_J);
    }

    private static void func_193355_a(ItemPotion itempotion, Item item, ItemPotion itempotion1) {
        PotionHelper.field_185214_b.add(new PotionHelper.MixPredicate(itempotion, Ingredient.func_193368_a(new Item[] { item}), itempotion1));
    }

    private static void func_193354_a(ItemPotion itempotion) {
        PotionHelper.field_185215_c.add(Ingredient.func_193368_a(new Item[] { itempotion}));
    }

    private static void func_193357_a(PotionType potionregistry, Item item, PotionType potionregistry1) {
        func_193356_a(potionregistry, Ingredient.func_193368_a(new Item[] { item}), potionregistry1);
    }

    private static void func_193356_a(PotionType potionregistry, Ingredient recipeitemstack, PotionType potionregistry1) {
        PotionHelper.field_185213_a.add(new PotionHelper.MixPredicate(potionregistry, recipeitemstack, potionregistry1));
    }

    static class MixPredicate<T> {

        final T field_185198_a;
        final Ingredient field_185199_b;
        final T field_185200_c;

        public MixPredicate(T t0, Ingredient recipeitemstack, T t1) {
            this.field_185198_a = t0;
            this.field_185199_b = recipeitemstack;
            this.field_185200_c = t1;
        }
    }
}
