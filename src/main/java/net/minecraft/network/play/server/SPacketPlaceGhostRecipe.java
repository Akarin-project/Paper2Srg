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

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_194314_a = packetdataserializer.readByte();
        this.field_194315_b = CraftingManager.func_193374_a(packetdataserializer.func_150792_a());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_194314_a);
        packetdataserializer.func_150787_b(CraftingManager.func_193375_a(this.field_194315_b));
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_194307_a(this);
    }
}
