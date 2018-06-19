package org.bukkit.craftbukkit;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.stats.StatList;

import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

import net;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stats.StatBase statistic) {
        return getBukkitStatisticByName(statistic.field_75975_e);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity.")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy.")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem.")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem.")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock.")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem.")) {
            name = "stat.craftItem";
        }
        if (name.startsWith("stat.drop.")) {
            name = "stat.drop";
        }
        if (name.startsWith("stat.pickup.")) {
            name = "stat.pickup";
        }
        return statistics.get(name);
    }

    public static net.minecraft.stats.StatBase getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatList.func_151177_a(statistics.inverse().get(statistic));
    }

    public static net.minecraft.stats.StatBase getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.func_188055_a(CraftMagicNumbers.getBlock(material)); // PAIL: getMineBlockStatistic
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.func_188060_a(CraftMagicNumbers.getItem(material)); // PAIL: getCraftItemStatistic
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.func_188057_b(CraftMagicNumbers.getItem(material)); // PAIL: getUseItemStatistic
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.func_188059_c(CraftMagicNumbers.getItem(material)); // PAIL: getBreakItemStatistic
            }
            if (stat == Statistic.PICKUP) {
                return StatList.func_188056_d(CraftMagicNumbers.getItem(material)); // PAIL: getPickupStatistic
            }
            if (stat == Statistic.DROP) {
                return StatList.func_188058_e(CraftMagicNumbers.getItem(material)); // PAIL: getDropItemStatistic
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stats.StatBase getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        EntityEggInfo monsteregginfo = (EntityEggInfo) EntityList.field_75627_a.get(new ResourceLocation(entity.getName()));

        if (monsteregginfo != null) {
            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return monsteregginfo.field_151512_d;
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.field_151513_e;
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.field_75975_e;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.field_75975_e;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.field_150901_e.func_82594_a(new ResourceLocation(val));
        if (item != null) {
            return Material.getMaterial(Item.func_150891_b(item));
        }
        Block block = (Block) Block.field_149771_c.func_82594_a(new ResourceLocation(val));
        if (block != null) {
            return Material.getMaterial(Block.func_149682_b(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
