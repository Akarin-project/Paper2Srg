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

    private Map<StatBase, Integer> statisticMap;

    public SPacketStatistics() {}

    public SPacketStatistics(Map<StatBase, Integer> map) {
        this.statisticMap = map;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleStatistics(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        int i = packetdataserializer.readVarInt();

        this.statisticMap = Maps.newHashMap();

        for (int j = 0; j < i; ++j) {
            StatBase statistic = StatList.getOneShotStat(packetdataserializer.readString(32767));
            int k = packetdataserializer.readVarInt();

            if (statistic != null) {
                this.statisticMap.put(statistic, Integer.valueOf(k));
            }
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.statisticMap.size());
        Iterator iterator = this.statisticMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.writeString(((StatBase) entry.getKey()).statId);
            packetdataserializer.writeVarInt(((Integer) entry.getValue()).intValue());
        }

    }
}
