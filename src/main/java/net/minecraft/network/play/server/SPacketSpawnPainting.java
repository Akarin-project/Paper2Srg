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

    private int field_148973_a;
    private UUID field_186896_b;
    private BlockPos field_179838_b;
    private EnumFacing field_179839_c;
    private String field_148968_f;

    public SPacketSpawnPainting() {}

    public SPacketSpawnPainting(EntityPainting entitypainting) {
        this.field_148973_a = entitypainting.func_145782_y();
        this.field_186896_b = entitypainting.func_110124_au();
        this.field_179838_b = entitypainting.func_174857_n();
        this.field_179839_c = entitypainting.field_174860_b;
        this.field_148968_f = entitypainting.field_70522_e.field_75702_A;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148973_a = packetdataserializer.func_150792_a();
        this.field_186896_b = packetdataserializer.func_179253_g();
        this.field_148968_f = packetdataserializer.func_150789_c(EntityPainting.EnumArt.field_180001_A);
        this.field_179838_b = packetdataserializer.func_179259_c();
        this.field_179839_c = EnumFacing.func_176731_b(packetdataserializer.readUnsignedByte());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148973_a);
        packetdataserializer.func_179252_a(this.field_186896_b);
        packetdataserializer.func_180714_a(this.field_148968_f);
        packetdataserializer.func_179255_a(this.field_179838_b);
        packetdataserializer.writeByte(this.field_179839_c.func_176736_b());
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147288_a(this);
    }
}
