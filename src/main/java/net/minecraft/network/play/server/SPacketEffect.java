package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketEffect implements Packet<INetHandlerPlayClient> {

    private int field_149251_a;
    private BlockPos field_179747_b;
    private int field_149249_b;
    private boolean field_149246_f;

    public SPacketEffect() {}

    public SPacketEffect(int i, BlockPos blockposition, int j, boolean flag) {
        this.field_149251_a = i;
        this.field_179747_b = blockposition;
        this.field_149249_b = j;
        this.field_149246_f = flag;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149251_a = packetdataserializer.readInt();
        this.field_179747_b = packetdataserializer.func_179259_c();
        this.field_149249_b = packetdataserializer.readInt();
        this.field_149246_f = packetdataserializer.readBoolean();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149251_a);
        packetdataserializer.func_179255_a(this.field_179747_b);
        packetdataserializer.writeInt(this.field_149249_b);
        packetdataserializer.writeBoolean(this.field_149246_f);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147277_a(this);
    }
}
