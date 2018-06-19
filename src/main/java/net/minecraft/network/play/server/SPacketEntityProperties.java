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

    private int field_149445_a;
    private final List<SPacketEntityProperties.Snapshot> field_149444_b = Lists.newArrayList();

    public SPacketEntityProperties() {}

    public SPacketEntityProperties(int i, Collection<IAttributeInstance> collection) {
        this.field_149445_a = i;
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            this.field_149444_b.add(new SPacketEntityProperties.Snapshot(attributeinstance.func_111123_a().func_111108_a(), attributeinstance.func_111125_b(), attributeinstance.func_111122_c()));
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149445_a = packetdataserializer.func_150792_a();
        int i = packetdataserializer.readInt();

        for (int j = 0; j < i; ++j) {
            String s = packetdataserializer.func_150789_c(64);
            double d0 = packetdataserializer.readDouble();
            ArrayList arraylist = Lists.newArrayList();
            int k = packetdataserializer.func_150792_a();

            for (int l = 0; l < k; ++l) {
                UUID uuid = packetdataserializer.func_179253_g();

                arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", packetdataserializer.readDouble(), packetdataserializer.readByte()));
            }

            this.field_149444_b.add(new SPacketEntityProperties.Snapshot(s, d0, arraylist));
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149445_a);
        packetdataserializer.writeInt(this.field_149444_b.size());
        Iterator iterator = this.field_149444_b.iterator();

        while (iterator.hasNext()) {
            SPacketEntityProperties.Snapshot packetplayoutupdateattributes_attributesnapshot = (SPacketEntityProperties.Snapshot) iterator.next();

            packetdataserializer.func_180714_a(packetplayoutupdateattributes_attributesnapshot.func_151409_a());
            packetdataserializer.writeDouble(packetplayoutupdateattributes_attributesnapshot.func_151410_b());
            packetdataserializer.func_150787_b(packetplayoutupdateattributes_attributesnapshot.func_151408_c().size());
            Iterator iterator1 = packetplayoutupdateattributes_attributesnapshot.func_151408_c().iterator();

            while (iterator1.hasNext()) {
                AttributeModifier attributemodifier = (AttributeModifier) iterator1.next();

                packetdataserializer.func_179252_a(attributemodifier.func_111167_a());
                packetdataserializer.writeDouble(attributemodifier.func_111164_d());
                packetdataserializer.writeByte(attributemodifier.func_111169_c());
            }
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147290_a(this);
    }

    public class Snapshot {

        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection<AttributeModifier> field_151411_d;

        public Snapshot(String s, double d0, Collection collection) {
            this.field_151412_b = s;
            this.field_151413_c = d0;
            this.field_151411_d = collection;
        }

        public String func_151409_a() {
            return this.field_151412_b;
        }

        public double func_151410_b() {
            return this.field_151413_c;
        }

        public Collection<AttributeModifier> func_151408_c() {
            return this.field_151411_d;
        }
    }
}
