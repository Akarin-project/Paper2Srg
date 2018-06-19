package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer> {

    private String field_149420_a;
    private boolean field_186990_b;
    @Nullable
    private BlockPos field_179710_b;

    public CPacketTabComplete() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149420_a = packetdataserializer.func_150789_c(32767);
        this.field_186990_b = packetdataserializer.readBoolean();
        boolean flag = packetdataserializer.readBoolean();

        if (flag) {
            this.field_179710_b = packetdataserializer.func_179259_c();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(StringUtils.substring(this.field_149420_a, 0, 32767));
        packetdataserializer.writeBoolean(this.field_186990_b);
        boolean flag = this.field_179710_b != null;

        packetdataserializer.writeBoolean(flag);
        if (flag) {
            packetdataserializer.func_179255_a(this.field_179710_b);
        }

    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147341_a(this);
    }

    public String func_149419_c() {
        return this.field_149420_a;
    }

    @Nullable
    public BlockPos func_179709_b() {
        return this.field_179710_b;
    }

    public boolean func_186989_c() {
        return this.field_186990_b;
    }
}
