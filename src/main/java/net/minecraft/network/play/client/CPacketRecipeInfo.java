package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer> {

    private CPacketRecipeInfo.Purpose purpose;
    private IRecipe recipe;
    private boolean isGuiOpen;
    private boolean filteringCraftable;

    public CPacketRecipeInfo() {}

    public CPacketRecipeInfo(IRecipe irecipe) {
        this.purpose = CPacketRecipeInfo.Purpose.SHOWN;
        this.recipe = irecipe;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.purpose = (CPacketRecipeInfo.Purpose) packetdataserializer.readEnumValue(CPacketRecipeInfo.Purpose.class);
        if (this.purpose == CPacketRecipeInfo.Purpose.SHOWN) {
            this.recipe = CraftingManager.getRecipeById(packetdataserializer.readInt());
        } else if (this.purpose == CPacketRecipeInfo.Purpose.SETTINGS) {
            this.isGuiOpen = packetdataserializer.readBoolean();
            this.filteringCraftable = packetdataserializer.readBoolean();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.purpose);
        if (this.purpose == CPacketRecipeInfo.Purpose.SHOWN) {
            packetdataserializer.writeInt(CraftingManager.getIDForRecipe(this.recipe));
        } else if (this.purpose == CPacketRecipeInfo.Purpose.SETTINGS) {
            packetdataserializer.writeBoolean(this.isGuiOpen);
            packetdataserializer.writeBoolean(this.filteringCraftable);
        }

    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.handleRecipeBookUpdate(this);
    }

    public CPacketRecipeInfo.Purpose getPurpose() {
        return this.purpose;
    }

    public IRecipe getRecipe() {
        return this.recipe;
    }

    public boolean isGuiOpen() {
        return this.isGuiOpen;
    }

    public boolean isFilteringCraftable() {
        return this.filteringCraftable;
    }

    public static enum Purpose {

        SHOWN, SETTINGS;

        private Purpose() {}
    }
}
