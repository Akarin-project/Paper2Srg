package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private final List<SPacketEntityProperties.Snapshot> snapshots = Lists.newArrayList();

    public SPacketEntityProperties() {}

    public SPacketEntityProperties(int i, Collection<IAttributeInstance> collection) {
        this.entityId = i;
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            this.snapshots.add(new SPacketEntityProperties.Snapshot(attributeinstance.getAttribute().getName(), attributeinstance.getBaseValue(), attributeinstance.getModifiers()));
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        int i = packetdataserializer.readInt();

        for (int j = 0; j < i; ++j) {
            String s = packetdataserializer.readString(64);
            double d0 = packetdataserializer.readDouble();
            ArrayList arraylist = Lists.newArrayList();
            int k = packetdataserializer.readVarInt();

            for (int l = 0; l < k; ++l) {
                UUID uuid = packetdataserializer.readUniqueId();

                arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", packetdataserializer.readDouble(), packetdataserializer.readByte()));
            }

            this.snapshots.add(new SPacketEntityProperties.Snapshot(s, d0, arraylist));
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeInt(this.snapshots.size());
        Iterator iterator = this.snapshots.iterator();

        while (iterator.hasNext()) {
            SPacketEntityProperties.Snapshot packetplayoutupdateattributes_attributesnapshot = (SPacketEntityProperties.Snapshot) iterator.next();

            packetdataserializer.writeString(packetplayoutupdateattributes_attributesnapshot.getName());
            packetdataserializer.writeDouble(packetplayoutupdateattributes_attributesnapshot.getBaseValue());
            packetdataserializer.writeVarInt(packetplayoutupdateattributes_attributesnapshot.getModifiers().size());
            Iterator iterator1 = packetplayoutupdateattributes_attributesnapshot.getModifiers().iterator();

            while (iterator1.hasNext()) {
                AttributeModifier attributemodifier = (AttributeModifier) iterator1.next();

                packetdataserializer.writeUniqueId(attributemodifier.getID());
                packetdataserializer.writeDouble(attributemodifier.getAmount());
                packetdataserializer.writeByte(attributemodifier.getOperation());
            }
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityProperties(this);
    }

    public class Snapshot {

        private final String name;
        private final double baseValue;
        private final Collection<AttributeModifier> modifiers;

        public Snapshot(String s, double d0, Collection collection) {
            this.name = s;
            this.baseValue = d0;
            this.modifiers = collection;
        }

        public String getName() {
            return this.name;
        }

        public double getBaseValue() {
            return this.baseValue;
        }

        public Collection<AttributeModifier> getModifiers() {
            return this.modifiers;
        }
    }
}
