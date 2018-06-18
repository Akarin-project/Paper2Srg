package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlaceGhostRecipe implements Packet<INetHandlerPlayClient> {

    private int field_194314_a;
    private IRecipe field_194315_b;

    public SPacketPlaceGhostRecipe() {}

    public SPacketPlaceGhostRecipe(int i, IRecipe irecipe) {
        this.field_194314_a = i;
        this.field_194315_b = irecipe;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.field_194314_a = packetdataserializer.readByte();
        this.field_194315_b = CraftingManager.getRecipeById(packetdataserializer.readVarInt());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_194314_a);
        packetdataserializer.writeVarInt(CraftingManager.getIDForRecipe(this.field_194315_b));
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_194307_a(this);
    }
}
