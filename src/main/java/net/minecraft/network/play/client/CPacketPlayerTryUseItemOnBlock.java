package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer> {

    private BlockPos position;
    private EnumFacing placedBlockDirection;
    private EnumHand hand;
    private float facingX;
    private float facingY;
    private float facingZ;
    public long timestamp;

    public CPacketPlayerTryUseItemOnBlock() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.position = packetdataserializer.readBlockPos();
        this.placedBlockDirection = (EnumFacing) packetdataserializer.readEnumValue(EnumFacing.class);
        this.hand = (EnumHand) packetdataserializer.readEnumValue(EnumHand.class);
        this.facingX = packetdataserializer.readFloat();
        this.facingY = packetdataserializer.readFloat();
        this.facingZ = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.position);
        packetdataserializer.writeEnumValue((Enum) this.placedBlockDirection);
        packetdataserializer.writeEnumValue((Enum) this.hand);
        packetdataserializer.writeFloat(this.facingX);
        packetdataserializer.writeFloat(this.facingY);
        packetdataserializer.writeFloat(this.facingZ);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processTryUseItemOnBlock(this);
    }

    public BlockPos getPos() {
        return this.position;
    }

    public EnumFacing getDirection() {
        return this.placedBlockDirection;
    }

    public EnumHand getHand() {
        return this.hand;
    }

    public float getFacingX() {
        return this.facingX;
    }

    public float getFacingY() {
        return this.facingY;
    }

    public float getFacingZ() {
        return this.facingZ;
    }
}
