package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager {

    protected final Map<StatBase, TupleIntJsonSerializable> field_150875_a = Maps.newConcurrentMap();

    public StatisticsManager() {}

    public void func_150871_b(EntityPlayer entityhuman, StatBase statistic, int i) {
        // CraftBukkit start - fire Statistic events
        org.bukkit.event.Cancellable cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, this.func_77444_a(statistic), i);
        if (cancellable != null && cancellable.isCancelled()) {
            return;
        }
        // CraftBukkit end
        this.func_150873_a(entityhuman, statistic, this.func_77444_a(statistic) + i);
    }

    public void func_150873_a(EntityPlayer entityhuman, StatBase statistic, int i) {
        TupleIntJsonSerializable statisticwrapper = (TupleIntJsonSerializable) this.field_150875_a.get(statistic);

        if (statisticwrapper == null) {
            statisticwrapper = new TupleIntJsonSerializable();
            this.field_150875_a.put(statistic, statisticwrapper);
        }

        statisticwrapper.func_151188_a(i);
    }

    public int func_77444_a(StatBase statistic) {
        TupleIntJsonSerializable statisticwrapper = (TupleIntJsonSerializable) this.field_150875_a.get(statistic);

        return statisticwrapper == null ? 0 : statisticwrapper.func_151189_a();
    }
}
