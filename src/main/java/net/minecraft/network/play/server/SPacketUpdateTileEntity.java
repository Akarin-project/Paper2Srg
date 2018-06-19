package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketUpdateTileEntity implements Packet<INetHandlerPlayClient> {

    private BlockPos field_179824_a;
    private int field_148859_d;
    private NBTTagCompound field_148860_e;

    public SPacketUpdateTileEntity() {}

    public SPacketUpdateTileEntity(BlockPos blockposition, int i, NBTTagCompound nbttagcompound) {
        this.field_179824_a = blockposition;
        this.field_148859_d = i;
        this.field_148860_e = nbttagcompound;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179824_a = packetdataserializer.func_179259_c();
        this.field_148859_d = packetdataserializer.readUnsignedByte();
        this.field_148860_e = packetdataserializer.func_150793_b();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179824_a);
        packetdataserializer.writeByte((byte) this.field_148859_d);
        packetdataserializer.func_150786_a(this.field_148860_e);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147273_a(this);
    }
}
