package net.minecraft.item.crafting;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class FurnaceRecipes {

    private static final FurnaceRecipes SMELTING_BASE = new FurnaceRecipes();
    public Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.newHashMap();
    public Map<ItemStack,ItemStack> customRecipes = Maps.newHashMap(); // CraftBukkit - add field
    public Map<ItemStack,Float> customExperience = Maps.newHashMap(); // CraftBukkit - add field

    public static FurnaceRecipes instance() {
        return FurnaceRecipes.SMELTING_BASE;
    }

    public FurnaceRecipes() {
        this.addSmeltingRecipeForBlock(Blocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        this.addSmeltingRecipeForBlock(Blocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        this.addSmeltingRecipeForBlock(Blocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        this.addSmeltingRecipeForBlock(Blocks.SAND, new ItemStack(Blocks.GLASS), 0.1F);
        this.addSmelting(Items.PORKCHOP, new ItemStack(Items.COOKED_PORKCHOP), 0.35F);
        this.addSmelting(Items.BEEF, new ItemStack(Items.COOKED_BEEF), 0.35F);
        this.addSmelting(Items.CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 0.35F);
        this.addSmelting(Items.RABBIT, new ItemStack(Items.COOKED_RABBIT), 0.35F);
        this.addSmelting(Items.MUTTON, new ItemStack(Items.COOKED_MUTTON), 0.35F);
        this.addSmeltingRecipeForBlock(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CRACKED_META), 0.1F);
        this.addSmelting(Items.CLAY_BALL, new ItemStack(Items.BRICK), 0.3F);
        this.addSmeltingRecipeForBlock(Blocks.CLAY, new ItemStack(Blocks.HARDENED_CLAY), 0.35F);
        this.addSmeltingRecipeForBlock(Blocks.CACTUS, new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 0.2F);
        this.addSmeltingRecipeForBlock(Blocks.LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.addSmeltingRecipeForBlock(Blocks.LOG2, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.addSmeltingRecipeForBlock(Blocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0F);
        this.addSmelting(Items.POTATO, new ItemStack(Items.BAKED_POTATO), 0.35F);
        this.addSmeltingRecipeForBlock(Blocks.NETHERRACK, new ItemStack(Items.NETHERBRICK), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Blocks.SPONGE, 1, 0), 0.15F);
        this.addSmelting(Items.CHORUS_FRUIT, new ItemStack(Items.CHORUS_FRUIT_POPPED), 0.1F);
        ItemFishFood.FishType[] aitemfish_enumfish = ItemFishFood.FishType.values();
        int i = aitemfish_enumfish.length;

        for (int j = 0; j < i; ++j) {
            ItemFishFood.FishType itemfish_enumfish = aitemfish_enumfish[j];

            if (itemfish_enumfish.canCook()) {
                this.addSmeltingRecipe(new ItemStack(Items.FISH, 1, itemfish_enumfish.getMetadata()), new ItemStack(Items.COOKED_FISH, 1, itemfish_enumfish.getMetadata()), 0.35F);
            }
        }

        this.addSmeltingRecipeForBlock(Blocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        this.addSmeltingRecipeForBlock(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        this.addSmeltingRecipeForBlock(Blocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2F);
        this.addSmeltingRecipeForBlock(Blocks.QUARTZ_ORE, new ItemStack(Items.QUARTZ), 0.2F);
        this.addSmelting((Item) Items.CHAINMAIL_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.CHAINMAIL_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.CHAINMAIL_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.CHAINMAIL_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_PICKAXE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_SHOVEL, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_AXE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_HOE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_SWORD, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.IRON_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.IRON_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting((Item) Items.IRON_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.IRON_HORSE_ARMOR, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_PICKAXE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_SHOVEL, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_AXE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_HOE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_SWORD, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting((Item) Items.GOLDEN_HELMET, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting((Item) Items.GOLDEN_CHESTPLATE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting((Item) Items.GOLDEN_LEGGINGS, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting((Item) Items.GOLDEN_BOOTS, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmelting(Items.GOLDEN_HORSE_ARMOR, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata()), new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.ORANGE.getMetadata()), new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.MAGENTA.getMetadata()), new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIGHT_BLUE.getMetadata()), new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.YELLOW.getMetadata()), new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIME.getMetadata()), new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PINK.getMetadata()), new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GRAY.getMetadata()), new ItemStack(Blocks.GRAY_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.SILVER.getMetadata()), new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.CYAN.getMetadata()), new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PURPLE.getMetadata()), new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLUE.getMetadata()), new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BROWN.getMetadata()), new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GREEN.getMetadata()), new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata()), new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), 0.1F);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLACK.getMetadata()), new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), 0.1F);
    }

    // CraftBukkit start - add method
    public void registerRecipe(ItemStack itemstack, ItemStack itemstack1, float f) {
        this.customRecipes.put(itemstack, itemstack1);
        this.customExperience.put(itemstack, f);
    }
    // CraftBukkit end

    public void addSmeltingRecipeForBlock(Block block, ItemStack itemstack, float f) {
        this.addSmelting(Item.getItemFromBlock(block), itemstack, f);
    }

    public void addSmelting(Item item, ItemStack itemstack, float f) {
        this.addSmeltingRecipe(new ItemStack(item, 1, 32767), itemstack, f);
    }

    public void addSmeltingRecipe(ItemStack itemstack, ItemStack itemstack1, float f) {
        this.smeltingList.put(itemstack, itemstack1);
        this.experienceList.put(itemstack1, Float.valueOf(f));
    }

    public ItemStack getSmeltingResult(ItemStack itemstack) {
        // CraftBukkit start - initialize to customRecipes
        boolean vanilla = false;
        Iterator<Entry<ItemStack, ItemStack>> iterator = this.customRecipes.entrySet().iterator();
        // CraftBukkit end

        Entry entry;

        do {
            if (!iterator.hasNext()) {
                // CraftBukkit start - fall back to vanilla recipes
                if (!vanilla && !this.smeltingList.isEmpty()) {
                    iterator = this.smeltingList.entrySet().iterator();
                    vanilla = true;
                } else {
                    return ItemStack.EMPTY;
                }
                // CraftBukkit end
            }

            entry = (Entry) iterator.next();
        } while (!this.compareItemStacks(itemstack, (ItemStack) entry.getKey()));

        return (ItemStack) entry.getValue();
    }

    private boolean compareItemStacks(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() == itemstack.getItem() && (itemstack1.getMetadata() == 32767 || itemstack1.getMetadata() == itemstack.getMetadata());
    }

    public Map<ItemStack, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }

    public float getSmeltingExperience(ItemStack itemstack) {
        // CraftBukkit start - initialize to customRecipes
        boolean vanilla = false;
        Iterator<Entry<ItemStack, Float>> iterator = this.customExperience.entrySet().iterator();
        // CraftBukkit end

        Entry entry;

        do {
            if (!iterator.hasNext()) {
                // CraftBukkit start - fall back to vanilla recipes
                if (!vanilla && !this.experienceList.isEmpty()) {
                    iterator = this.experienceList.entrySet().iterator();
                    vanilla = true;
                } else {
                    return 0.0F;
                }
                // CraftBukkit end
            }

            entry = (Entry) iterator.next();
        } while (!this.compareItemStacks(itemstack, (ItemStack) entry.getKey()));

        return ((Float) entry.getValue()).floatValue();
    }
}
