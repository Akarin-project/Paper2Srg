package net.minecraft.network.play.server;

import java.io.IOException;
import org.apache.commons.lang3.Validate;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SPacketSoundEffect implements Packet<INetHandlerPlayClient> {

    private SoundEvent field_186979_a;
    private SoundCategory field_186980_b;
    private int field_149217_b;
    private int field_149218_c;
    private int field_149215_d;
    private float field_149216_e;
    private float field_149214_f;

    public SPacketSoundEffect() {}

    public SPacketSoundEffect(SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        Validate.notNull(soundeffect, "sound", new Object[0]);
        this.field_186979_a = soundeffect;
        this.field_186980_b = soundcategory;
        this.field_149217_b = (int) (d0 * 8.0D);
        this.field_149218_c = (int) (d1 * 8.0D);
        this.field_149215_d = (int) (d2 * 8.0D);
        this.field_149216_e = f;
        this.field_149214_f = f1;
    }

    @Override
    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_186979_a = SoundEvent.field_187505_a.func_148754_a(packetdataserializer.func_150792_a());
        this.field_186980_b = packetdataserializer.func_179257_a(SoundCategory.class);
        this.field_149217_b = packetdataserializer.readInt();
        this.field_149218_c = packetdataserializer.readInt();
        this.field_149215_d = packetdataserializer.readInt();
        this.field_149216_e = packetdataserializer.readFloat();
        this.field_149214_f = packetdataserializer.readFloat();
    }

    @Override
    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(SoundEvent.field_187505_a.func_148757_b(this.field_186979_a));
        packetdataserializer.func_179249_a(this.field_186980_b);
        packetdataserializer.writeInt(this.field_149217_b);
        packetdataserializer.writeInt(this.field_149218_c);
        packetdataserializer.writeInt(this.field_149215_d);
        packetdataserializer.writeFloat(this.field_149216_e);
        packetdataserializer.writeFloat(this.field_149214_f);
    }

    @Override
    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184327_a(this);
    }
}
