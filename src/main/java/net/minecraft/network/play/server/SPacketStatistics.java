package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class SPacketStatistics implements Packet<INetHandlerPlayClient> {

    private Map<StatBase, Integer> field_148976_a;

    public SPacketStatistics() {}

    public SPacketStatistics(Map<StatBase, Integer> map) {
        this.field_148976_a = map;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147293_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        int i = packetdataserializer.func_150792_a();

        this.field_148976_a = Maps.newHashMap();

        for (int j = 0; j < i; ++j) {
            StatBase statistic = StatList.func_151177_a(packetdataserializer.func_150789_c(32767));
            int k = packetdataserializer.func_150792_a();

            if (statistic != null) {
                this.field_148976_a.put(statistic, Integer.valueOf(k));
            }
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148976_a.size());
        Iterator iterator = this.field_148976_a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.func_180714_a(((StatBase) entry.getKey()).field_75975_e);
            packetdataserializer.func_150787_b(((Integer) entry.getValue()).intValue());
        }

    }
}
