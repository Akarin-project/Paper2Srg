package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList {

    protected static final Map<String, StatBase> ID_TO_STAT_MAP = Maps.newHashMap();
    public static final List<StatBase> ALL_STATS = Lists.newArrayList();
    public static final List<StatBase> BASIC_STATS = Lists.newArrayList();
    public static final List<StatCrafting> USE_ITEM_STATS = Lists.newArrayList();
    public static final List<StatCrafting> MINE_BLOCK_STATS = Lists.newArrayList();
    public static final StatBase LEAVE_GAME = (new StatBasic("stat.leaveGame", new TextComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
    public static final StatBase PLAY_ONE_MINUTE = (new StatBasic("stat.playOneMinute", new TextComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase TIME_SINCE_DEATH = (new StatBasic("stat.timeSinceDeath", new TextComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase SNEAK_TIME = (new StatBasic("stat.sneakTime", new TextComponentTranslation("stat.sneakTime", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase WALK_ONE_CM = (new StatBasic("stat.walkOneCm", new TextComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase CROUCH_ONE_CM = (new StatBasic("stat.crouchOneCm", new TextComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase SPRINT_ONE_CM = (new StatBasic("stat.sprintOneCm", new TextComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase SWIM_ONE_CM = (new StatBasic("stat.swimOneCm", new TextComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase FALL_ONE_CM = (new StatBasic("stat.fallOneCm", new TextComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase CLIMB_ONE_CM = (new StatBasic("stat.climbOneCm", new TextComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase FLY_ONE_CM = (new StatBasic("stat.flyOneCm", new TextComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase DIVE_ONE_CM = (new StatBasic("stat.diveOneCm", new TextComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase MINECART_ONE_CM = (new StatBasic("stat.minecartOneCm", new TextComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase BOAT_ONE_CM = (new StatBasic("stat.boatOneCm", new TextComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase PIG_ONE_CM = (new StatBasic("stat.pigOneCm", new TextComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase HORSE_ONE_CM = (new StatBasic("stat.horseOneCm", new TextComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase AVIATE_ONE_CM = (new StatBasic("stat.aviateOneCm", new TextComponentTranslation("stat.aviateOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase JUMP = (new StatBasic("stat.jump", new TextComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
    public static final StatBase DROP = (new StatBasic("stat.drop", new TextComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
    public static final StatBase DAMAGE_DEALT = (new StatBasic("stat.damageDealt", new TextComponentTranslation("stat.damageDealt", new Object[0]), StatBase.divideByTen)).registerStat();
    public static final StatBase DAMAGE_TAKEN = (new StatBasic("stat.damageTaken", new TextComponentTranslation("stat.damageTaken", new Object[0]), StatBase.divideByTen)).registerStat();
    public static final StatBase DEATHS = (new StatBasic("stat.deaths", new TextComponentTranslation("stat.deaths", new Object[0]))).registerStat();
    public static final StatBase MOB_KILLS = (new StatBasic("stat.mobKills", new TextComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
    public static final StatBase ANIMALS_BRED = (new StatBasic("stat.animalsBred", new TextComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
    public static final StatBase PLAYER_KILLS = (new StatBasic("stat.playerKills", new TextComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
    public static final StatBase FISH_CAUGHT = (new StatBasic("stat.fishCaught", new TextComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
    public static final StatBase TALKED_TO_VILLAGER = (new StatBasic("stat.talkedToVillager", new TextComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
    public static final StatBase TRADED_WITH_VILLAGER = (new StatBasic("stat.tradedWithVillager", new TextComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
    public static final StatBase CAKE_SLICES_EATEN = (new StatBasic("stat.cakeSlicesEaten", new TextComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
    public static final StatBase CAULDRON_FILLED = (new StatBasic("stat.cauldronFilled", new TextComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
    public static final StatBase CAULDRON_USED = (new StatBasic("stat.cauldronUsed", new TextComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
    public static final StatBase ARMOR_CLEANED = (new StatBasic("stat.armorCleaned", new TextComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
    public static final StatBase BANNER_CLEANED = (new StatBasic("stat.bannerCleaned", new TextComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
    public static final StatBase BREWINGSTAND_INTERACTION = (new StatBasic("stat.brewingstandInteraction", new TextComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
    public static final StatBase BEACON_INTERACTION = (new StatBasic("stat.beaconInteraction", new TextComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
    public static final StatBase DROPPER_INSPECTED = (new StatBasic("stat.dropperInspected", new TextComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
    public static final StatBase HOPPER_INSPECTED = (new StatBasic("stat.hopperInspected", new TextComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
    public static final StatBase DISPENSER_INSPECTED = (new StatBasic("stat.dispenserInspected", new TextComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
    public static final StatBase NOTEBLOCK_PLAYED = (new StatBasic("stat.noteblockPlayed", new TextComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
    public static final StatBase NOTEBLOCK_TUNED = (new StatBasic("stat.noteblockTuned", new TextComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
    public static final StatBase FLOWER_POTTED = (new StatBasic("stat.flowerPotted", new TextComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
    public static final StatBase TRAPPED_CHEST_TRIGGERED = (new StatBasic("stat.trappedChestTriggered", new TextComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
    public static final StatBase ENDERCHEST_OPENED = (new StatBasic("stat.enderchestOpened", new TextComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
    public static final StatBase ITEM_ENCHANTED = (new StatBasic("stat.itemEnchanted", new TextComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
    public static final StatBase RECORD_PLAYED = (new StatBasic("stat.recordPlayed", new TextComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
    public static final StatBase FURNACE_INTERACTION = (new StatBasic("stat.furnaceInteraction", new TextComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
    public static final StatBase CRAFTING_TABLE_INTERACTION = (new StatBasic("stat.craftingTableInteraction", new TextComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
    public static final StatBase CHEST_OPENED = (new StatBasic("stat.chestOpened", new TextComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
    public static final StatBase SLEEP_IN_BED = (new StatBasic("stat.sleepInBed", new TextComponentTranslation("stat.sleepInBed", new Object[0]))).registerStat();
    public static final StatBase OPEN_SHULKER_BOX = (new StatBasic("stat.shulkerBoxOpened", new TextComponentTranslation("stat.shulkerBoxOpened", new Object[0]))).registerStat();
    private static final StatBase[] BLOCKS_STATS = new StatBase[4096];
    private static final StatBase[] CRAFTS_STATS = new StatBase[32000];
    private static final StatBase[] OBJECT_USE_STATS = new StatBase[32000];
    private static final StatBase[] OBJECT_BREAK_STATS = new StatBase[32000];
    private static final StatBase[] OBJECTS_PICKED_UP_STATS = new StatBase[32000];
    private static final StatBase[] OBJECTS_DROPPED_STATS = new StatBase[32000];

    @Nullable
    public static StatBase getBlockStats(Block block) {
        return StatList.BLOCKS_STATS[Block.getIdFromBlock(block)];
    }

    @Nullable
    public static StatBase getCraftStats(Item item) {
        return StatList.CRAFTS_STATS[Item.getIdFromItem(item)];
    }

    @Nullable
    public static StatBase getObjectUseStats(Item item) {
        return StatList.OBJECT_USE_STATS[Item.getIdFromItem(item)];
    }

    @Nullable
    public static StatBase getObjectBreakStats(Item item) {
        return StatList.OBJECT_BREAK_STATS[Item.getIdFromItem(item)];
    }

    @Nullable
    public static StatBase getObjectsPickedUpStats(Item item) {
        return StatList.OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(item)];
    }

    @Nullable
    public static StatBase getDroppedObjectStats(Item item) {
        return StatList.OBJECTS_DROPPED_STATS[Item.getIdFromItem(item)];
    }

    public static void init() {
        initMiningStats();
        initStats();
        initItemDepleteStats();
        initCraftableStats();
        initPickedUpAndDroppedStats();
    }

    private static void initCraftableStats() {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = CraftingManager.REGISTRY.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();
            ItemStack itemstack = irecipe.getRecipeOutput();

            if (!itemstack.isEmpty()) {
                hashset.add(irecipe.getRecipeOutput().getItem());
            }
        }

        iterator = FurnaceRecipes.instance().getSmeltingList().values().iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack1 = (ItemStack) iterator.next();

            hashset.add(itemstack1.getItem());
        }

        iterator = hashset.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item != null) {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null) {
                    StatList.CRAFTS_STATS[i] = (new StatCrafting("stat.craftItem.", s, new TextComponentTranslation("stat.craftItem", new Object[] { (new ItemStack(item)).getTextComponent()}), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(StatList.CRAFTS_STATS);
    }

    private static void initMiningStats() {
        Iterator iterator = Block.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();
            Item item = Item.getItemFromBlock(block);

            if (item != Items.AIR) {
                int i = Block.getIdFromBlock(block);
                String s = getItemName(item);

                if (s != null && block.getEnableStats()) {
                    StatList.BLOCKS_STATS[i] = (new StatCrafting("stat.mineBlock.", s, new TextComponentTranslation("stat.mineBlock", new Object[] { (new ItemStack(block)).getTextComponent()}), item)).registerStat();
                    StatList.MINE_BLOCK_STATS.add((StatCrafting) StatList.BLOCKS_STATS[i]);
                }
            }
        }

        replaceAllSimilarBlocks(StatList.BLOCKS_STATS);
    }

    private static void initStats() {
        Iterator iterator = Item.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item != null) {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null) {
                    StatList.OBJECT_USE_STATS[i] = (new StatCrafting("stat.useItem.", s, new TextComponentTranslation("stat.useItem", new Object[] { (new ItemStack(item)).getTextComponent()}), item)).registerStat();
                    if (!(item instanceof ItemBlock)) {
                        StatList.USE_ITEM_STATS.add((StatCrafting) StatList.OBJECT_USE_STATS[i]);
                    }
                }
            }
        }

        replaceAllSimilarBlocks(StatList.OBJECT_USE_STATS);
    }

    private static void initItemDepleteStats() {
        Iterator iterator = Item.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item != null) {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null && item.isDamageable()) {
                    StatList.OBJECT_BREAK_STATS[i] = (new StatCrafting("stat.breakItem.", s, new TextComponentTranslation("stat.breakItem", new Object[] { (new ItemStack(item)).getTextComponent()}), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(StatList.OBJECT_BREAK_STATS);
    }

    private static void initPickedUpAndDroppedStats() {
        Iterator iterator = Item.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item != null) {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null) {
                    StatList.OBJECTS_PICKED_UP_STATS[i] = (new StatCrafting("stat.pickup.", s, new TextComponentTranslation("stat.pickup", new Object[] { (new ItemStack(item)).getTextComponent()}), item)).registerStat();
                    StatList.OBJECTS_DROPPED_STATS[i] = (new StatCrafting("stat.drop.", s, new TextComponentTranslation("stat.drop", new Object[] { (new ItemStack(item)).getTextComponent()}), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(StatList.OBJECT_BREAK_STATS);
    }

    private static String getItemName(Item item) {
        ResourceLocation minecraftkey = (ResourceLocation) Item.REGISTRY.getNameForObject(item);

        return minecraftkey != null ? minecraftkey.toString().replace(':', '.') : null;
    }

    private static void replaceAllSimilarBlocks(StatBase[] astatistic) {
        mergeStatBases(astatistic, Blocks.WATER, Blocks.FLOWING_WATER);
        mergeStatBases(astatistic, Blocks.LAVA, Blocks.FLOWING_LAVA);
        mergeStatBases(astatistic, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
        mergeStatBases(astatistic, Blocks.LIT_FURNACE, Blocks.FURNACE);
        mergeStatBases(astatistic, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
        mergeStatBases(astatistic, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
        mergeStatBases(astatistic, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
        mergeStatBases(astatistic, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
        mergeStatBases(astatistic, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
        mergeStatBases(astatistic, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
        mergeStatBases(astatistic, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
        mergeStatBases(astatistic, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
        mergeStatBases(astatistic, Blocks.GRASS, Blocks.DIRT);
        mergeStatBases(astatistic, Blocks.FARMLAND, Blocks.DIRT);
    }

    private static void mergeStatBases(StatBase[] astatistic, Block block, Block block1) {
        int i = Block.getIdFromBlock(block);
        int j = Block.getIdFromBlock(block1);

        if (astatistic[i] != null && astatistic[j] == null) {
            astatistic[j] = astatistic[i];
        } else {
            StatList.ALL_STATS.remove(astatistic[i]);
            StatList.MINE_BLOCK_STATS.remove(astatistic[i]);
            StatList.BASIC_STATS.remove(astatistic[i]);
            astatistic[i] = astatistic[j];
        }
    }

    public static StatBase getStatKillEntity(EntityList.EntityEggInfo entitytypes_monsteregginfo) {
        String s = EntityList.getTranslationName(entitytypes_monsteregginfo.spawnedID);

        return s == null ? null : (new StatBase("stat.killEntity." + s, new TextComponentTranslation("stat.entityKill", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
    }

    public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo entitytypes_monsteregginfo) {
        String s = EntityList.getTranslationName(entitytypes_monsteregginfo.spawnedID);

        return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new TextComponentTranslation("stat.entityKilledBy", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
    }

    @Nullable
    public static StatBase getOneShotStat(String s) {
        return (StatBase) StatList.ID_TO_STAT_MAP.get(s);
    }
}
