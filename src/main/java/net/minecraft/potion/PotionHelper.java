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

    private static final List<PotionHelper.MixPredicate<PotionType>> POTION_TYPE_CONVERSIONS = Lists.newArrayList();
    private static final List<PotionHelper.MixPredicate<Item>> POTION_ITEM_CONVERSIONS = Lists.newArrayList();
    private static final List<Ingredient> POTION_ITEMS = Lists.newArrayList();
    private static final Predicate<ItemStack> IS_POTION_ITEM = new Predicate() {
        public boolean a(ItemStack itemstack) {
            Iterator iterator = PotionHelper.POTION_ITEMS.iterator();

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

    public static boolean isReagent(ItemStack itemstack) {
        return isItemConversionReagent(itemstack) || isTypeConversionReagent(itemstack);
    }

    protected static boolean isItemConversionReagent(ItemStack itemstack) {
        int i = 0;

        for (int j = PotionHelper.POTION_ITEM_CONVERSIONS.size(); i < j; ++i) {
            if (((PotionHelper.MixPredicate) PotionHelper.POTION_ITEM_CONVERSIONS.get(i)).reagent.apply(itemstack)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean isTypeConversionReagent(ItemStack itemstack) {
        int i = 0;

        for (int j = PotionHelper.POTION_TYPE_CONVERSIONS.size(); i < j; ++i) {
            if (((PotionHelper.MixPredicate) PotionHelper.POTION_TYPE_CONVERSIONS.get(i)).reagent.apply(itemstack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasConversions(ItemStack itemstack, ItemStack itemstack1) {
        return !PotionHelper.IS_POTION_ITEM.apply(itemstack) ? false : hasItemConversions(itemstack, itemstack1) || hasTypeConversions(itemstack, itemstack1);
    }

    protected static boolean hasItemConversions(ItemStack itemstack, ItemStack itemstack1) {
        Item item = itemstack.getItem();
        int i = 0;

        for (int j = PotionHelper.POTION_ITEM_CONVERSIONS.size(); i < j; ++i) {
            PotionHelper.MixPredicate potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.POTION_ITEM_CONVERSIONS.get(i);

            if (potionbrewer_predicatedcombination.input == item && potionbrewer_predicatedcombination.reagent.apply(itemstack1)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean hasTypeConversions(ItemStack itemstack, ItemStack itemstack1) {
        PotionType potionregistry = PotionUtils.getPotionFromItem(itemstack);
        int i = 0;

        for (int j = PotionHelper.POTION_TYPE_CONVERSIONS.size(); i < j; ++i) {
            PotionHelper.MixPredicate potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.POTION_TYPE_CONVERSIONS.get(i);

            if (potionbrewer_predicatedcombination.input == potionregistry && potionbrewer_predicatedcombination.reagent.apply(itemstack1)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack doReaction(ItemStack itemstack, ItemStack itemstack1) {
        if (!itemstack1.isEmpty()) {
            PotionType potionregistry = PotionUtils.getPotionFromItem(itemstack1);
            Item item = itemstack1.getItem();
            int i = 0;

            int j;
            PotionHelper.MixPredicate potionbrewer_predicatedcombination;

            for (j = PotionHelper.POTION_ITEM_CONVERSIONS.size(); i < j; ++i) {
                potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.POTION_ITEM_CONVERSIONS.get(i);
                if (potionbrewer_predicatedcombination.input == item && potionbrewer_predicatedcombination.reagent.apply(itemstack)) {
                    return PotionUtils.addPotionToItemStack(new ItemStack((Item) potionbrewer_predicatedcombination.output), potionregistry);
                }
            }

            i = 0;

            for (j = PotionHelper.POTION_TYPE_CONVERSIONS.size(); i < j; ++i) {
                potionbrewer_predicatedcombination = (PotionHelper.MixPredicate) PotionHelper.POTION_TYPE_CONVERSIONS.get(i);
                if (potionbrewer_predicatedcombination.input == potionregistry && potionbrewer_predicatedcombination.reagent.apply(itemstack)) {
                    return PotionUtils.addPotionToItemStack(new ItemStack(item), (PotionType) potionbrewer_predicatedcombination.output);
                }
            }
        }

        return itemstack1;
    }

    public static void init() {
        addContainer(Items.POTIONITEM);
        addContainer(Items.SPLASH_POTION);
        addContainer(Items.LINGERING_POTION);
        addContainerRecipe(Items.POTIONITEM, Items.GUNPOWDER, Items.SPLASH_POTION);
        addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
        addMix(PotionTypes.WATER, Items.SPECKLED_MELON, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.GHAST_TEAR, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.RABBIT_FOOT, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.BLAZE_POWDER, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.SPIDER_EYE, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.SUGAR, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.MAGMA_CREAM, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.GLOWSTONE_DUST, PotionTypes.THICK);
        addMix(PotionTypes.WATER, Items.REDSTONE, PotionTypes.MUNDANE);
        addMix(PotionTypes.WATER, Items.NETHER_WART, PotionTypes.AWKWARD);
        addMix(PotionTypes.AWKWARD, Items.GOLDEN_CARROT, PotionTypes.NIGHT_VISION);
        addMix(PotionTypes.NIGHT_VISION, Items.REDSTONE, PotionTypes.LONG_NIGHT_VISION);
        addMix(PotionTypes.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.INVISIBILITY);
        addMix(PotionTypes.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_INVISIBILITY);
        addMix(PotionTypes.INVISIBILITY, Items.REDSTONE, PotionTypes.LONG_INVISIBILITY);
        addMix(PotionTypes.AWKWARD, Items.MAGMA_CREAM, PotionTypes.FIRE_RESISTANCE);
        addMix(PotionTypes.FIRE_RESISTANCE, Items.REDSTONE, PotionTypes.LONG_FIRE_RESISTANCE);
        addMix(PotionTypes.AWKWARD, Items.RABBIT_FOOT, PotionTypes.LEAPING);
        addMix(PotionTypes.LEAPING, Items.REDSTONE, PotionTypes.LONG_LEAPING);
        addMix(PotionTypes.LEAPING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_LEAPING);
        addMix(PotionTypes.LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
        addMix(PotionTypes.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
        addMix(PotionTypes.SLOWNESS, Items.REDSTONE, PotionTypes.LONG_SLOWNESS);
        addMix(PotionTypes.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
        addMix(PotionTypes.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
        addMix(PotionTypes.AWKWARD, Items.SUGAR, PotionTypes.SWIFTNESS);
        addMix(PotionTypes.SWIFTNESS, Items.REDSTONE, PotionTypes.LONG_SWIFTNESS);
        addMix(PotionTypes.SWIFTNESS, Items.GLOWSTONE_DUST, PotionTypes.STRONG_SWIFTNESS);
        addMix(PotionTypes.AWKWARD, Ingredient.fromStacks(new ItemStack[] { new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())}), PotionTypes.WATER_BREATHING);
        addMix(PotionTypes.WATER_BREATHING, Items.REDSTONE, PotionTypes.LONG_WATER_BREATHING);
        addMix(PotionTypes.AWKWARD, Items.SPECKLED_MELON, PotionTypes.HEALING);
        addMix(PotionTypes.HEALING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HEALING);
        addMix(PotionTypes.HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        addMix(PotionTypes.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
        addMix(PotionTypes.HARMING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HARMING);
        addMix(PotionTypes.POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        addMix(PotionTypes.LONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        addMix(PotionTypes.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
        addMix(PotionTypes.AWKWARD, Items.SPIDER_EYE, PotionTypes.POISON);
        addMix(PotionTypes.POISON, Items.REDSTONE, PotionTypes.LONG_POISON);
        addMix(PotionTypes.POISON, Items.GLOWSTONE_DUST, PotionTypes.STRONG_POISON);
        addMix(PotionTypes.AWKWARD, Items.GHAST_TEAR, PotionTypes.REGENERATION);
        addMix(PotionTypes.REGENERATION, Items.REDSTONE, PotionTypes.LONG_REGENERATION);
        addMix(PotionTypes.REGENERATION, Items.GLOWSTONE_DUST, PotionTypes.STRONG_REGENERATION);
        addMix(PotionTypes.AWKWARD, Items.BLAZE_POWDER, PotionTypes.STRENGTH);
        addMix(PotionTypes.STRENGTH, Items.REDSTONE, PotionTypes.LONG_STRENGTH);
        addMix(PotionTypes.STRENGTH, Items.GLOWSTONE_DUST, PotionTypes.STRONG_STRENGTH);
        addMix(PotionTypes.WATER, Items.FERMENTED_SPIDER_EYE, PotionTypes.WEAKNESS);
        addMix(PotionTypes.WEAKNESS, Items.REDSTONE, PotionTypes.LONG_WEAKNESS);
    }

    private static void addContainerRecipe(ItemPotion itempotion, Item item, ItemPotion itempotion1) {
        PotionHelper.POTION_ITEM_CONVERSIONS.add(new PotionHelper.MixPredicate(itempotion, Ingredient.fromItems(new Item[] { item}), itempotion1));
    }

    private static void addContainer(ItemPotion itempotion) {
        PotionHelper.POTION_ITEMS.add(Ingredient.fromItems(new Item[] { itempotion}));
    }

    private static void addMix(PotionType potionregistry, Item item, PotionType potionregistry1) {
        addMix(potionregistry, Ingredient.fromItems(new Item[] { item}), potionregistry1);
    }

    private static void addMix(PotionType potionregistry, Ingredient recipeitemstack, PotionType potionregistry1) {
        PotionHelper.POTION_TYPE_CONVERSIONS.add(new PotionHelper.MixPredicate(potionregistry, recipeitemstack, potionregistry1));
    }

    static class MixPredicate<T> {

        final T input;
        final Ingredient reagent;
        final T output;

        public MixPredicate(T t0, Ingredient recipeitemstack, T t1) {
            this.input = t0;
            this.reagent = recipeitemstack;
            this.output = t1;
        }
    }
}
