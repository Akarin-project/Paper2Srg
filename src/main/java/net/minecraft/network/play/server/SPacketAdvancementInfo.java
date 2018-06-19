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

    private boolean field_192605_a;
    private Map<ResourceLocation, Advancement.Builder> field_192606_b;
    private Set<ResourceLocation> field_192607_c;
    private Map<ResourceLocation, AdvancementProgress> field_192608_d;

    public SPacketAdvancementInfo() {}

    public SPacketAdvancementInfo(boolean flag, Collection<Advancement> collection, Set<ResourceLocation> set, Map<ResourceLocation, AdvancementProgress> map) {
        this.field_192605_a = flag;
        this.field_192606_b = Maps.newHashMap();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.field_192606_b.put(advancement.func_192067_g(), advancement.func_192075_a());
        }

        this.field_192607_c = set;
        this.field_192608_d = Maps.newHashMap(map);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_191981_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_192605_a = packetdataserializer.readBoolean();
        this.field_192606_b = Maps.newHashMap();
        this.field_192607_c = Sets.newLinkedHashSet();
        this.field_192608_d = Maps.newHashMap();
        int i = packetdataserializer.func_150792_a();

        int j;
        ResourceLocation minecraftkey;

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.func_192575_l();
            Advancement.Builder advancement_serializedadvancement = Advancement.Builder.func_192060_b(packetdataserializer);

            this.field_192606_b.put(minecraftkey, advancement_serializedadvancement);
        }

        i = packetdataserializer.func_150792_a();

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.func_192575_l();
            this.field_192607_c.add(minecraftkey);
        }

        i = packetdataserializer.func_150792_a();

        for (j = 0; j < i; ++j) {
            minecraftkey = packetdataserializer.func_192575_l();
            this.field_192608_d.put(minecraftkey, AdvancementProgress.func_192100_b(packetdataserializer));
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.field_192605_a);
        packetdataserializer.func_150787_b(this.field_192606_b.size());
        Iterator iterator = this.field_192606_b.entrySet().iterator();

        Entry entry;

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            ResourceLocation minecraftkey = (ResourceLocation) entry.getKey();
            Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) entry.getValue();

            packetdataserializer.func_192572_a(minecraftkey);
            advancement_serializedadvancement.func_192057_a(packetdataserializer);
        }

        packetdataserializer.func_150787_b(this.field_192607_c.size());
        iterator = this.field_192607_c.iterator();

        while (iterator.hasNext()) {
            ResourceLocation minecraftkey1 = (ResourceLocation) iterator.next();

            packetdataserializer.func_192572_a(minecraftkey1);
        }

        packetdataserializer.func_150787_b(this.field_192608_d.size());
        iterator = this.field_192608_d.entrySet().iterator();

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            packetdataserializer.func_192572_a((ResourceLocation) entry.getKey());
            ((AdvancementProgress) entry.getValue()).func_192104_a(packetdataserializer);
        }

    }
}
