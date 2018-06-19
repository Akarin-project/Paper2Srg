package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient> {

    private int field_149394_a;
    private EntityEquipmentSlot field_149392_b;
    private ItemStack field_149393_c;

    public SPacketEntityEquipment() {
        this.field_149393_c = ItemStack.field_190927_a;
    }

    public SPacketEntityEquipment(int i, EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        this.field_149393_c = ItemStack.field_190927_a;
        this.field_149394_a = i;
        this.field_149392_b = enumitemslot;
        this.field_149393_c = itemstack.func_77946_l();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149394_a = packetdataserializer.func_150792_a();
        this.field_149392_b = (EntityEquipmentSlot) packetdataserializer.func_179257_a(EntityEquipmentSlot.class);
        this.field_149393_c = packetdataserializer.func_150791_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149394_a);
        packetdataserializer.func_179249_a((Enum) this.field_149392_b);
        packetdataserializer.func_150788_a(this.field_149393_c);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147242_a(this);
    }
}
