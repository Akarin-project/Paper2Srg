package net.minecraft.network.play.server;

import java.io.IOException;
import org.apache.commons.lang3.Validate;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient> {

    private String field_149219_a;
    private SoundCategory field_186933_b;
    private int field_186934_c;
    private int field_186935_d = Integer.MAX_VALUE;
    private int field_186936_e;
    private float field_186937_f;
    private float field_186938_g;

    public SPacketCustomSound() {}

    public SPacketCustomSound(String s, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        Validate.notNull(s, "name", new Object[0]);
        this.field_149219_a = s;
        this.field_186933_b = soundcategory;
        this.field_186934_c = (int) (d0 * 8.0D);
        this.field_186935_d = (int) (d1 * 8.0D);
        this.field_186936_e = (int) (d2 * 8.0D);
        this.field_186937_f = f;
        this.field_186938_g = f1;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149219_a = packetdataserializer.func_150789_c(256);
        this.field_186933_b = (SoundCategory) packetdataserializer.func_179257_a(SoundCategory.class);
        this.field_186934_c = packetdataserializer.readInt();
        this.field_186935_d = packetdataserializer.readInt();
        this.field_186936_e = packetdataserializer.readInt();
        this.field_186937_f = packetdataserializer.readFloat();
        this.field_186938_g = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149219_a);
        packetdataserializer.func_179249_a((Enum) this.field_186933_b);
        packetdataserializer.writeInt(this.field_186934_c);
        packetdataserializer.writeInt(this.field_186935_d);
        packetdataserializer.writeInt(this.field_186936_e);
        packetdataserializer.writeFloat(this.field_186937_f);
        packetdataserializer.writeFloat(this.field_186938_g);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184329_a(this);
    }
}
