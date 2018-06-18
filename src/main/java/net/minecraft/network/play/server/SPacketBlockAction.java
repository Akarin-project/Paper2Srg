package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockAction implements Packet<INetHandlerPlayClient> {

    private BlockPos blockPosition;
    private int instrument;
    private int pitch;
    private Block block;

    public SPacketBlockAction() {}

    public SPacketBlockAction(BlockPos blockposition, Block block, int i, int j) {
        this.blockPosition = blockposition;
        this.instrument = i;
        this.pitch = j;
        this.block = block;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.blockPosition = packetdataserializer.readBlockPos();
        this.instrument = packetdataserializer.readUnsignedByte();
        this.pitch = packetdataserializer.readUnsignedByte();
        this.block = Block.getBlockById(packetdataserializer.readVarInt() & 4095);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.blockPosition);
        packetdataserializer.writeByte(this.instrument);
        packetdataserializer.writeByte(this.pitch);
        packetdataserializer.writeVarInt(Block.getIdFromBlock(this.block) & 4095);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleBlockAction(this);
    }
}
