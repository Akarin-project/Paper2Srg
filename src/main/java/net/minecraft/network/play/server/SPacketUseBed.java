package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketUseBed implements Packet<INetHandlerPlayClient> {

    private int playerID;
    private BlockPos bedPos;

    public SPacketUseBed() {}

    public SPacketUseBed(EntityPlayer entityhuman, BlockPos blockposition) {
        this.playerID = entityhuman.getEntityId();
        this.bedPos = blockposition;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.playerID = packetdataserializer.readVarInt();
        this.bedPos = packetdataserializer.readBlockPos();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.playerID);
        packetdataserializer.writeBlockPos(this.bedPos);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleUseBed(this);
    }
}
