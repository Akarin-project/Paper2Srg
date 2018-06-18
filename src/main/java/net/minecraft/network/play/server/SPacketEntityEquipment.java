package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient> {

    private int entityID;
    private EntityEquipmentSlot equipmentSlot;
    private ItemStack itemStack;

    public SPacketEntityEquipment() {
        this.itemStack = ItemStack.EMPTY;
    }

    public SPacketEntityEquipment(int i, EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        this.itemStack = ItemStack.EMPTY;
        this.entityID = i;
        this.equipmentSlot = enumitemslot;
        this.itemStack = itemstack.copy();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityID = packetdataserializer.readVarInt();
        this.equipmentSlot = (EntityEquipmentSlot) packetdataserializer.readEnumValue(EntityEquipmentSlot.class);
        this.itemStack = packetdataserializer.readItemStack();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityID);
        packetdataserializer.writeEnumValue((Enum) this.equipmentSlot);
        packetdataserializer.writeItemStack(this.itemStack);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityEquipment(this);
    }
}
