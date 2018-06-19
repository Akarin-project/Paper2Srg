package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer> {

    private int field_149554_a;
    private int field_149552_b;
    private int field_149553_c;
    private short field_149550_d;
    private ItemStack field_149551_e;
    private ClickType field_149549_f;

    public CPacketClickWindow() {
        this.field_149551_e = ItemStack.field_190927_a;
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147351_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149554_a = packetdataserializer.readByte();
        this.field_149552_b = packetdataserializer.readShort();
        this.field_149553_c = packetdataserializer.readByte();
        this.field_149550_d = packetdataserializer.readShort();
        this.field_149549_f = (ClickType) packetdataserializer.func_179257_a(ClickType.class);
        this.field_149551_e = packetdataserializer.func_150791_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149554_a);
        packetdataserializer.writeShort(this.field_149552_b);
        packetdataserializer.writeByte(this.field_149553_c);
        packetdataserializer.writeShort(this.field_149550_d);
        packetdataserializer.func_179249_a((Enum) this.field_149549_f);
        packetdataserializer.func_150788_a(this.field_149551_e);
    }

    public int func_149548_c() {
        return this.field_149554_a;
    }

    public int func_149544_d() {
        return this.field_149552_b;
    }

    public int func_149543_e() {
        return this.field_149553_c;
    }

    public short func_149547_f() {
        return this.field_149550_d;
    }

    public ItemStack func_149546_g() {
        return this.field_149551_e;
    }

    public ClickType func_186993_f() {
        return this.field_149549_f;
    }
}
