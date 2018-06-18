package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient> {

    @Nullable
    private ResourceLocation tab;

    public SPacketSelectAdvancementsTab() {}

    public SPacketSelectAdvancementsTab(@Nullable ResourceLocation minecraftkey) {
        this.tab = minecraftkey;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSelectAdvancementsTab(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        if (packetdataserializer.readBoolean()) {
            this.tab = packetdataserializer.readResourceLocation();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.tab != null);
        if (this.tab != null) {
            packetdataserializer.writeResourceLocation(this.tab);
        }

    }
}
