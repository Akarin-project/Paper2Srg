package net.minecraft.advancements;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.advancements.critereon.PositionTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.VillagerTradeTrigger;
import net.minecraft.util.ResourceLocation;

public class CriteriaTriggers {

    private static final Map<ResourceLocation, ICriterionTrigger<?>> REGISTRY = Maps.newHashMap();
    public static final ImpossibleTrigger IMPOSSIBLE = (ImpossibleTrigger) register((ICriterionTrigger) (new ImpossibleTrigger()));
    public static final KilledTrigger PLAYER_KILLED_ENTITY = (KilledTrigger) register((ICriterionTrigger) (new KilledTrigger(new ResourceLocation("player_killed_entity"))));
    public static final KilledTrigger ENTITY_KILLED_PLAYER = (KilledTrigger) register((ICriterionTrigger) (new KilledTrigger(new ResourceLocation("entity_killed_player"))));
    public static final EnterBlockTrigger ENTER_BLOCK = (EnterBlockTrigger) register((ICriterionTrigger) (new EnterBlockTrigger()));
    public static final InventoryChangeTrigger INVENTORY_CHANGED = (InventoryChangeTrigger) register((ICriterionTrigger) (new InventoryChangeTrigger()));
    public static final RecipeUnlockedTrigger RECIPE_UNLOCKED = (RecipeUnlockedTrigger) register((ICriterionTrigger) (new RecipeUnlockedTrigger()));
    public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY = (PlayerHurtEntityTrigger) register((ICriterionTrigger) (new PlayerHurtEntityTrigger()));
    public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER = (EntityHurtPlayerTrigger) register((ICriterionTrigger) (new EntityHurtPlayerTrigger()));
    public static final EnchantedItemTrigger ENCHANTED_ITEM = (EnchantedItemTrigger) register((ICriterionTrigger) (new EnchantedItemTrigger()));
    public static final BrewedPotionTrigger BREWED_POTION = (BrewedPotionTrigger) register((ICriterionTrigger) (new BrewedPotionTrigger()));
    public static final ConstructBeaconTrigger CONSTRUCT_BEACON = (ConstructBeaconTrigger) register((ICriterionTrigger) (new ConstructBeaconTrigger()));
    public static final UsedEnderEyeTrigger USED_ENDER_EYE = (UsedEnderEyeTrigger) register((ICriterionTrigger) (new UsedEnderEyeTrigger()));
    public static final SummonedEntityTrigger SUMMONED_ENTITY = (SummonedEntityTrigger) register((ICriterionTrigger) (new SummonedEntityTrigger()));
    public static final BredAnimalsTrigger BRED_ANIMALS = (BredAnimalsTrigger) register((ICriterionTrigger) (new BredAnimalsTrigger()));
    public static final PositionTrigger LOCATION = (PositionTrigger) register((ICriterionTrigger) (new PositionTrigger(new ResourceLocation("location"))));
    public static final PositionTrigger SLEPT_IN_BED = (PositionTrigger) register((ICriterionTrigger) (new PositionTrigger(new ResourceLocation("slept_in_bed"))));
    public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER = (CuredZombieVillagerTrigger) register((ICriterionTrigger) (new CuredZombieVillagerTrigger()));
    public static final VillagerTradeTrigger VILLAGER_TRADE = (VillagerTradeTrigger) register((ICriterionTrigger) (new VillagerTradeTrigger()));
    public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED = (ItemDurabilityTrigger) register((ICriterionTrigger) (new ItemDurabilityTrigger()));
    public static final LevitationTrigger LEVITATION = (LevitationTrigger) register((ICriterionTrigger) (new LevitationTrigger()));
    public static final ChangeDimensionTrigger CHANGED_DIMENSION = (ChangeDimensionTrigger) register((ICriterionTrigger) (new ChangeDimensionTrigger()));
    public static final TickTrigger TICK = (TickTrigger) register((ICriterionTrigger) (new TickTrigger()));
    public static final TameAnimalTrigger TAME_ANIMAL = (TameAnimalTrigger) register((ICriterionTrigger) (new TameAnimalTrigger()));
    public static final PlacedBlockTrigger PLACED_BLOCK = (PlacedBlockTrigger) register((ICriterionTrigger) (new PlacedBlockTrigger()));
    public static final ConsumeItemTrigger CONSUME_ITEM = (ConsumeItemTrigger) register((ICriterionTrigger) (new ConsumeItemTrigger()));
    public static final EffectsChangedTrigger EFFECTS_CHANGED = (EffectsChangedTrigger) register((ICriterionTrigger) (new EffectsChangedTrigger()));
    public static final UsedTotemTrigger USED_TOTEM = (UsedTotemTrigger) register((ICriterionTrigger) (new UsedTotemTrigger()));
    public static final NetherTravelTrigger NETHER_TRAVEL = (NetherTravelTrigger) register((ICriterionTrigger) (new NetherTravelTrigger()));

    private static <T extends ICriterionTrigger> T register(T t0) {
        if (CriteriaTriggers.REGISTRY.containsKey(t0.getId())) {
            throw new IllegalArgumentException("Duplicate criterion id " + t0.getId());
        } else {
            CriteriaTriggers.REGISTRY.put(t0.getId(), t0);
            return t0;
        }
    }

    @Nullable
    public static <T extends ICriterionInstance> ICriterionTrigger<T> get(ResourceLocation minecraftkey) {
        return (ICriterionTrigger) CriteriaTriggers.REGISTRY.get(minecraftkey);
    }

    public static Iterable<? extends ICriterionTrigger<?>> getAll() {
        return CriteriaTriggers.REGISTRY.values();
    }
}
