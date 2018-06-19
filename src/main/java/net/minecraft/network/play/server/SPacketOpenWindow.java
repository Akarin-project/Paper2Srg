package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketOpenWindow implements Packet<INetHandlerPlayClient> {

    private int field_148909_a;
    private String field_148907_b;
    private ITextComponent field_148908_c;
    private int field_148905_d;
    private int field_148904_f;

    public SPacketOpenWindow() {}

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent) {
        this(i, s, ichatbasecomponent, 0);
    }

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent, int j) {
        this.field_148909_a = i;
        this.field_148907_b = s;
        this.field_148908_c = ichatbasecomponent;
        this.field_148905_d = j;
    }

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent, int j, int k) {
        this(i, s, ichatbasecomponent, j);
        this.field_148904_f = k;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147265_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148909_a = packetdataserializer.readUnsignedByte();
        this.field_148907_b = packetdataserializer.func_150789_c(32);
        this.field_148908_c = packetdataserializer.func_179258_d();
        this.field_148905_d = packetdataserializer.readUnsignedByte();
        if (this.field_148907_b.equals("EntityHorse")) {
            this.field_148904_f = packetdataserializer.readInt();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_148909_a);
        packetdataserializer.func_180714_a(this.field_148907_b);
        packetdataserializer.func_179256_a(this.field_148908_c);
        packetdataserializer.writeByte(this.field_148905_d);
        if (this.field_148907_b.equals("EntityHorse")) {
            packetdataserializer.writeInt(this.field_148904_f);
        }

    }
}
