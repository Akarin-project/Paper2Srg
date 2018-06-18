package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager {

    protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();

    public StatisticsManager() {}

    public void increaseStat(EntityPlayer entityhuman, StatBase statistic, int i) {
        // CraftBukkit start - fire Statistic events
        org.bukkit.event.Cancellable cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, this.readStat(statistic), i);
        if (cancellable != null && cancellable.isCancelled()) {
            return;
        }
        // CraftBukkit end
        this.unlockAchievement(entityhuman, statistic, this.readStat(statistic) + i);
    }

    public void unlockAchievement(EntityPlayer entityhuman, StatBase statistic, int i) {
        TupleIntJsonSerializable statisticwrapper = (TupleIntJsonSerializable) this.statsData.get(statistic);

        if (statisticwrapper == null) {
            statisticwrapper = new TupleIntJsonSerializable();
            this.statsData.put(statistic, statisticwrapper);
        }

        statisticwrapper.setIntegerValue(i);
    }

    public int readStat(StatBase statistic) {
        TupleIntJsonSerializable statisticwrapper = (TupleIntJsonSerializable) this.statsData.get(statistic);

        return statisticwrapper == null ? 0 : statisticwrapper.getIntegerValue();
    }
}
