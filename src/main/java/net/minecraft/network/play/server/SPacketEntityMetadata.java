package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityMetadata implements Packet<INetHandlerPlayClient> {

    private int field_149379_a;
    private List<EntityDataManager.DataEntry<?>> field_149378_b;

    public SPacketEntityMetadata() {}

    public SPacketEntityMetadata(int i, EntityDataManager datawatcher, boolean flag) {
        this.field_149379_a = i;
        if (flag) {
            this.field_149378_b = datawatcher.func_187231_c();
            datawatcher.func_187230_e();
        } else {
            this.field_149378_b = datawatcher.func_187221_b();
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149379_a = packetdataserializer.func_150792_a();
        this.field_149378_b = EntityDataManager.func_187215_b(packetdataserializer);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149379_a);
        EntityDataManager.func_187229_a(this.field_149378_b, packetdataserializer);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147284_a(this);
    }
}
