package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPainting implements Packet<INetHandlerPlayClient> {

    private int entityID;
    private UUID uniqueId;
    private BlockPos position;
    private EnumFacing facing;
    private String title;

    public SPacketSpawnPainting() {}

    public SPacketSpawnPainting(EntityPainting entitypainting) {
        this.entityID = entitypainting.getEntityId();
        this.uniqueId = entitypainting.getUniqueID();
        this.position = entitypainting.getHangingPosition();
        this.facing = entitypainting.facingDirection;
        this.title = entitypainting.art.title;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityID = packetdataserializer.readVarInt();
        this.uniqueId = packetdataserializer.readUniqueId();
        this.title = packetdataserializer.readString(EntityPainting.EnumArt.MAX_NAME_LENGTH);
        this.position = packetdataserializer.readBlockPos();
        this.facing = EnumFacing.getHorizontal(packetdataserializer.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityID);
        packetdataserializer.writeUniqueId(this.uniqueId);
        packetdataserializer.writeString(this.title);
        packetdataserializer.writeBlockPos(this.position);
        packetdataserializer.writeByte(this.facing.getHorizontalIndex());
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnPainting(this);
    }
}
