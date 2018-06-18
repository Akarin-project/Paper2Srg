package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketEffect implements Packet<INetHandlerPlayClient> {

    private int soundType;
    private BlockPos soundPos;
    private int soundData;
    private boolean serverWide;

    public SPacketEffect() {}

    public SPacketEffect(int i, BlockPos blockposition, int j, boolean flag) {
        this.soundType = i;
        this.soundPos = blockposition;
        this.soundData = j;
        this.serverWide = flag;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.soundType = packetdataserializer.readInt();
        this.soundPos = packetdataserializer.readBlockPos();
        this.soundData = packetdataserializer.readInt();
        this.serverWide = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.soundType);
        packetdataserializer.writeBlockPos(this.soundPos);
        packetdataserializer.writeInt(this.soundData);
        packetdataserializer.writeBoolean(this.serverWide);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEffect(this);
    }
}
