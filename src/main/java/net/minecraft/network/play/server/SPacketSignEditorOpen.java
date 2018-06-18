package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSignEditorOpen implements Packet<INetHandlerPlayClient> {

    private BlockPos signPosition;

    public SPacketSignEditorOpen() {}

    public SPacketSignEditorOpen(BlockPos blockposition) {
        this.signPosition = blockposition;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSignEditorOpen(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.signPosition = packetdataserializer.readBlockPos();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.signPosition);
    }
}
