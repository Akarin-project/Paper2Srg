package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient> {

    private int field_149206_a;
    private boolean field_149204_b;
    private GameType field_149205_c;
    private int field_149202_d;
    private EnumDifficulty field_149203_e;
    private int field_149200_f;
    private WorldType field_149201_g;
    private boolean field_179745_h;

    public SPacketJoinGame() {}

    public SPacketJoinGame(int i, GameType enumgamemode, boolean flag, int j, EnumDifficulty enumdifficulty, int k, WorldType worldtype, boolean flag1) {
        this.field_149206_a = i;
        this.field_149202_d = j;
        this.field_149203_e = enumdifficulty;
        this.field_149205_c = enumgamemode;
        this.field_149200_f = k;
        this.field_149204_b = flag;
        this.field_149201_g = worldtype;
        this.field_179745_h = flag1;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149206_a = packetdataserializer.readInt();
        short short0 = packetdataserializer.readUnsignedByte();

        this.field_149204_b = (short0 & 8) == 8;
        int i = short0 & -9;

        this.field_149205_c = GameType.func_77146_a(i);
        this.field_149202_d = packetdataserializer.readInt();
        this.field_149203_e = EnumDifficulty.func_151523_a(packetdataserializer.readUnsignedByte());
        this.field_149200_f = packetdataserializer.readUnsignedByte();
        this.field_149201_g = WorldType.func_77130_a(packetdataserializer.func_150789_c(16));
        if (this.field_149201_g == null) {
            this.field_149201_g = WorldType.field_77137_b;
        }

        this.field_179745_h = packetdataserializer.readBoolean();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149206_a);
        int i = this.field_149205_c.func_77148_a();

        if (this.field_149204_b) {
            i |= 8;
        }

        packetdataserializer.writeByte(i);
        packetdataserializer.writeInt(this.field_149202_d);
        packetdataserializer.writeByte(this.field_149203_e.func_151525_a());
        packetdataserializer.writeByte(this.field_149200_f);
        packetdataserializer.func_180714_a(this.field_149201_g.func_77127_a());
        packetdataserializer.writeBoolean(this.field_179745_h);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147282_a(this);
    }
}
