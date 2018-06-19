package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.storage.MapDecoration;

public class SPacketMaps implements Packet<INetHandlerPlayClient> {

    private int field_149191_a;
    private byte field_179739_b;
    private boolean field_186950_c;
    private MapDecoration[] field_179740_c;
    private int field_179737_d;
    private int field_179738_e;
    private int field_179735_f;
    private int field_179736_g;
    private byte[] field_179741_h;

    public SPacketMaps() {}

    public SPacketMaps(int i, byte b0, boolean flag, Collection<MapDecoration> collection, byte[] abyte, int j, int k, int l, int i1) {
        this.field_149191_a = i;
        this.field_179739_b = b0;
        this.field_186950_c = flag;
        this.field_179740_c = (MapDecoration[]) collection.toArray(new MapDecoration[collection.size()]);
        this.field_179737_d = j;
        this.field_179738_e = k;
        this.field_179735_f = l;
        this.field_179736_g = i1;
        this.field_179741_h = new byte[l * i1];

        for (int j1 = 0; j1 < l; ++j1) {
            for (int k1 = 0; k1 < i1; ++k1) {
                this.field_179741_h[j1 + k1 * l] = abyte[j + j1 + (k + k1) * 128];
            }
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149191_a = packetdataserializer.func_150792_a();
        this.field_179739_b = packetdataserializer.readByte();
        this.field_186950_c = packetdataserializer.readBoolean();
        this.field_179740_c = new MapDecoration[packetdataserializer.func_150792_a()];

        for (int i = 0; i < this.field_179740_c.length; ++i) {
            short short0 = (short) packetdataserializer.readByte();

            this.field_179740_c[i] = new MapDecoration(MapDecoration.Type.func_191159_a((byte) (short0 >> 4 & 15)), packetdataserializer.readByte(), packetdataserializer.readByte(), (byte) (short0 & 15));
        }

        this.field_179735_f = packetdataserializer.readUnsignedByte();
        if (this.field_179735_f > 0) {
            this.field_179736_g = packetdataserializer.readUnsignedByte();
            this.field_179737_d = packetdataserializer.readUnsignedByte();
            this.field_179738_e = packetdataserializer.readUnsignedByte();
            this.field_179741_h = packetdataserializer.func_179251_a();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149191_a);
        packetdataserializer.writeByte(this.field_179739_b);
        packetdataserializer.writeBoolean(this.field_186950_c);
        packetdataserializer.func_150787_b(this.field_179740_c.length);
        MapDecoration[] amapicon = this.field_179740_c;
        int i = amapicon.length;

        for (int j = 0; j < i; ++j) {
            MapDecoration mapicon = amapicon[j];

            packetdataserializer.writeByte((mapicon.func_176110_a() & 15) << 4 | mapicon.func_176111_d() & 15);
            packetdataserializer.writeByte(mapicon.func_176112_b());
            packetdataserializer.writeByte(mapicon.func_176113_c());
        }

        packetdataserializer.writeByte(this.field_179735_f);
        if (this.field_179735_f > 0) {
            packetdataserializer.writeByte(this.field_179736_g);
            packetdataserializer.writeByte(this.field_179737_d);
            packetdataserializer.writeByte(this.field_179738_e);
            packetdataserializer.func_179250_a(this.field_179741_h);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147264_a(this);
    }
}
