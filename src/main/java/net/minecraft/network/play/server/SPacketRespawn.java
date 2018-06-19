package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketRespawn implements Packet<INetHandlerPlayClient> {

    private int field_149088_a;
    private EnumDifficulty field_149086_b;
    private GameType field_149087_c;
    private WorldType field_149085_d;

    public SPacketRespawn() {}

    public SPacketRespawn(int i, EnumDifficulty enumdifficulty, WorldType worldtype, GameType enumgamemode) {
        this.field_149088_a = i;
        this.field_149086_b = enumdifficulty;
        this.field_149087_c = enumgamemode;
        this.field_149085_d = worldtype;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147280_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149088_a = packetdataserializer.readInt();
        this.field_149086_b = EnumDifficulty.func_151523_a(packetdataserializer.readUnsignedByte());
        this.field_149087_c = GameType.func_77146_a(packetdataserializer.readUnsignedByte());
        this.field_149085_d = WorldType.func_77130_a(packetdataserializer.func_150789_c(16));
        if (this.field_149085_d == null) {
            this.field_149085_d = WorldType.field_77137_b;
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149088_a);
        packetdataserializer.writeByte(this.field_149086_b.func_151525_a());
        packetdataserializer.writeByte(this.field_149087_c.func_77148_a());
        packetdataserializer.func_180714_a(this.field_149085_d.func_77127_a());
    }
}
