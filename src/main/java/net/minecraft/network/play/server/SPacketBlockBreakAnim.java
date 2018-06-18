package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient> {

    private int breakerId;
    private BlockPos position;
    private int progress;

    public SPacketBlockBreakAnim() {}

    public SPacketBlockBreakAnim(int i, BlockPos blockposition, int j) {
        this.breakerId = i;
        this.position = blockposition;
        this.progress = j;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.breakerId = packetdataserializer.readVarInt();
        this.position = packetdataserializer.readBlockPos();
        this.progress = packetdataserializer.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.breakerId);
        packetdataserializer.writeBlockPos(this.position);
        packetdataserializer.writeByte(this.progress);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleBlockBreakAnim(this);
    }
}
