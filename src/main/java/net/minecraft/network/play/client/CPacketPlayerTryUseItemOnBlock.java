package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer> {

    private BlockPos field_179725_b;
    private EnumFacing field_149579_d;
    private EnumHand field_187027_c;
    private float field_149577_f;
    private float field_149578_g;
    private float field_149584_h;
    public long timestamp;

    public CPacketPlayerTryUseItemOnBlock() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.field_179725_b = packetdataserializer.func_179259_c();
        this.field_149579_d = (EnumFacing) packetdataserializer.func_179257_a(EnumFacing.class);
        this.field_187027_c = (EnumHand) packetdataserializer.func_179257_a(EnumHand.class);
        this.field_149577_f = packetdataserializer.readFloat();
        this.field_149578_g = packetdataserializer.readFloat();
        this.field_149584_h = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179725_b);
        packetdataserializer.func_179249_a((Enum) this.field_149579_d);
        packetdataserializer.func_179249_a((Enum) this.field_187027_c);
        packetdataserializer.writeFloat(this.field_149577_f);
        packetdataserializer.writeFloat(this.field_149578_g);
        packetdataserializer.writeFloat(this.field_149584_h);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_184337_a(this);
    }

    public BlockPos func_187023_a() {
        return this.field_179725_b;
    }

    public EnumFacing func_187024_b() {
        return this.field_149579_d;
    }

    public EnumHand func_187022_c() {
        return this.field_187027_c;
    }

    public float func_187026_d() {
        return this.field_149577_f;
    }

    public float func_187025_e() {
        return this.field_149578_g;
    }

    public float func_187020_f() {
        return this.field_149584_h;
    }
}
