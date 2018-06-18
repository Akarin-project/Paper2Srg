package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlaceRecipe implements Packet<INetHandlerPlayServer> {

    private int field_194320_a;
    private IRecipe field_194321_b;
    private boolean field_194322_c;

    public CPacketPlaceRecipe() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.field_194320_a = packetdataserializer.readByte();
        this.field_194321_b = CraftingManager.getRecipeById(packetdataserializer.readVarInt());
        this.field_194322_c = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_194320_a);
        packetdataserializer.writeVarInt(CraftingManager.getIDForRecipe(this.field_194321_b));
        packetdataserializer.writeBoolean(this.field_194322_c);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_194308_a(this);
    }

    public int func_194318_a() {
        return this.field_194320_a;
    }

    public IRecipe func_194317_b() {
        return this.field_194321_b;
    }

    public boolean func_194319_c() {
        return this.field_194322_c;
    }
}
