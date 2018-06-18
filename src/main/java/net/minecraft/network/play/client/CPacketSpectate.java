package net.minecraft.network.play.client;

import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;

public class CPacketSpectate implements Packet<INetHandlerPlayServer> {

    private UUID id;

    public CPacketSpectate() {}

    public CPacketSpectate(UUID uuid) {
        this.id = uuid;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.id = packetdataserializer.readUniqueId();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeUniqueId(this.id);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.handleSpectate(this);
    }

    @Nullable
    public Entity getEntity(WorldServer worldserver) {
        return worldserver.getEntityFromUuid(this.id);
    }
}
