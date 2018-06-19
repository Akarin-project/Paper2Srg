package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHandSide;

public class CPacketClientSettings implements Packet<INetHandlerPlayServer> {

    private String field_149530_a;
    private int field_149528_b;
    private EntityPlayer.EnumChatVisibility field_149529_c;
    private boolean field_149526_d;
    private int field_179711_e;
    private EnumHandSide field_186992_f;

    public CPacketClientSettings() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149530_a = packetdataserializer.func_150789_c(16);
        this.field_149528_b = packetdataserializer.readByte();
        this.field_149529_c = (EntityPlayer.EnumChatVisibility) packetdataserializer.func_179257_a(EntityPlayer.EnumChatVisibility.class);
        this.field_149526_d = packetdataserializer.readBoolean();
        this.field_179711_e = packetdataserializer.readUnsignedByte();
        this.field_186992_f = (EnumHandSide) packetdataserializer.func_179257_a(EnumHandSide.class);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149530_a);
        packetdataserializer.writeByte(this.field_149528_b);
        packetdataserializer.func_179249_a((Enum) this.field_149529_c);
        packetdataserializer.writeBoolean(this.field_149526_d);
        packetdataserializer.writeByte(this.field_179711_e);
        packetdataserializer.func_179249_a((Enum) this.field_186992_f);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147352_a(this);
    }

    public String func_149524_c() {
        return this.field_149530_a;
    }

    public EntityPlayer.EnumChatVisibility func_149523_e() {
        return this.field_149529_c;
    }

    public boolean func_149520_f() {
        return this.field_149526_d;
    }

    public int func_149521_d() {
        return this.field_179711_e;
    }

    public EnumHandSide func_186991_f() {
        return this.field_186992_f;
    }
}
