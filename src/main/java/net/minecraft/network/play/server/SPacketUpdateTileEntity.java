package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketUpdateTileEntity implements Packet<INetHandlerPlayClient> {

    private BlockPos blockPos;
    private int tileEntityType;
    private NBTTagCompound nbt;

    public SPacketUpdateTileEntity() {}

    public SPacketUpdateTileEntity(BlockPos blockposition, int i, NBTTagCompound nbttagcompound) {
        this.blockPos = blockposition;
        this.tileEntityType = i;
        this.nbt = nbttagcompound;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.blockPos = packetdataserializer.readBlockPos();
        this.tileEntityType = packetdataserializer.readUnsignedByte();
        this.nbt = packetdataserializer.readCompoundTag();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.blockPos);
        packetdataserializer.writeByte((byte) this.tileEntityType);
        packetdataserializer.writeCompoundTag(this.nbt);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleUpdateTileEntity(this);
    }
}
