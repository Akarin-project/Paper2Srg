package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPosition implements Packet<INetHandlerPlayClient> {

    public BlockPos spawnBlockPos;

    public SPacketSpawnPosition() {}

    public SPacketSpawnPosition(BlockPos blockposition) {
        this.spawnBlockPos = blockposition;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.spawnBlockPos = packetdataserializer.readBlockPos();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.spawnBlockPos);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnPosition(this);
    }
}
