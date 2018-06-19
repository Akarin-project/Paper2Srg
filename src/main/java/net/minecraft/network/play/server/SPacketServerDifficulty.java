package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient> {

    private EnumDifficulty field_179833_a;
    private boolean field_179832_b;

    public SPacketServerDifficulty() {}

    public SPacketServerDifficulty(EnumDifficulty enumdifficulty, boolean flag) {
        this.field_179833_a = enumdifficulty;
        this.field_179832_b = flag;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175101_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179833_a = EnumDifficulty.func_151523_a(packetdataserializer.readUnsignedByte());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_179833_a.func_151525_a());
    }
}
