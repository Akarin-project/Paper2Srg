package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient> {

    @Nullable
    private ResourceLocation field_194155_a;

    public SPacketSelectAdvancementsTab() {}

    public SPacketSelectAdvancementsTab(@Nullable ResourceLocation minecraftkey) {
        this.field_194155_a = minecraftkey;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_194022_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        if (packetdataserializer.readBoolean()) {
            this.field_194155_a = packetdataserializer.func_192575_l();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.field_194155_a != null);
        if (this.field_194155_a != null) {
            packetdataserializer.func_192572_a(this.field_194155_a);
        }

    }
}
