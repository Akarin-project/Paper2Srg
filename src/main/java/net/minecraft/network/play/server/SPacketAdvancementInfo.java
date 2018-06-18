package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient> {

    private boolean firstSync;
    private Map<ResourceLocation, Advancement.Builder> advancementsToAdd;
    private Set<ResourceLocation> advancementsToRemove;
    private Map<ResourceLocation, AdvancementProgress> progressUpdates;

    public SPacketAdvancementInfo() {}

    public SPacketAdvancementInfo(boolean flag, Collection<Advancement> collection, Set<ResourceLocation> set, Map<ResourceLocation, AdvancementProgress> map) {
        this.firstSync = flag;
        this.advancementsToAdd = Maps.newHashMap();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.advancementsToAdd.put(advancement.getId(), advancement.copy());
        }

        this.advancementsToRemove = set;
        this.progressUpdates = Maps.newHashMap(map);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleAdvancementInfo(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.firstSync = packetdataserializer.readBoolean();
        this.advancementsToAdd = Maps.newHashMap();
        this.advancementsToRemove = Sets.newLinkedHashSet();
        this.progressUpdates = Maps.newHashMap();
        int i = packetdataserializer.readVarInt();

        int j;
        ResourceLocation minecraftkey;

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.readResourceLocation();
            Advancement.Builder advancement_serializedadvancement = Advancement.Builder.readFrom(packetdataserializer);

            this.advancementsToAdd.put(minecraftkey, advancement_serializedadvancement);
        }

        i = packetdataserializer.readVarInt();

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.readResourceLocation();
            this.advancementsToRemove.add(minecraftkey);
        }

        i = packetdataserializer.readVarInt();

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.readResourceLocation();
            this.progressUpdates.put(minecraftkey, AdvancementProgress.fromNetwork(packetdataserializer));
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.firstSync);
        packetdataserializer.writeVarInt(this.advancementsToAdd.size());
        Iterator iterator = this.advancementsToAdd.entrySet().iterator();

        Entry entry;

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            ResourceLocation minecraftkey = (ResourceLocation) entry.getKey();
            Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) entry.getValue();

            packetdataserializer.writeResourceLocation(minecraftkey);
            advancement_serializedadvancement.writeTo(packetdataserializer);
        }

        packetdataserializer.writeVarInt(this.advancementsToRemove.size());
        iterator = this.advancementsToRemove.iterator();

        while (iterator.hasNext()) {
            ResourceLocation minecraftkey1 = (ResourceLocation) iterator.next();

            packetdataserializer.writeResourceLocation(minecraftkey1);
        }

        packetdataserializer.writeVarInt(this.progressUpdates.size());
        iterator = this.progressUpdates.entrySet().iterator();

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            packetdataserializer.writeResourceLocation((ResourceLocation) entry.getKey());
            ((AdvancementProgress) entry.getValue()).serializeToNetwork(packetdataserializer);
        }

    }
}
