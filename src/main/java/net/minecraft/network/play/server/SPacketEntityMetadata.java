package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityMetadata implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketEntityMetadata() {}

    public SPacketEntityMetadata(int i, EntityDataManager datawatcher, boolean flag) {
        this.entityId = i;
        if (flag) {
            this.dataManagerEntries = datawatcher.getAll();
            datawatcher.setClean();
        } else {
            this.dataManagerEntries = datawatcher.getDirty();
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.dataManagerEntries = EntityDataManager.readEntries(packetdataserializer);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        EntityDataManager.writeEntries(this.dataManagerEntries, packetdataserializer);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityMetadata(this);
    }
}
