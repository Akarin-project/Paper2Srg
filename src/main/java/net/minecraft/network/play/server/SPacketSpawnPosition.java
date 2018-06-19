package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPosition implements Packet<INetHandlerPlayClient> {

    public BlockPos field_179801_a;

    public SPacketSpawnPosition() {}

    public SPacketSpawnPosition(BlockPos blockposition) {
        this.field_179801_a = blockposition;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179801_a = packetdataserializer.func_179259_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179801_a);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147271_a(this);
    }
}
