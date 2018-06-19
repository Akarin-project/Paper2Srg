package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer> {

    private CPacketRecipeInfo.Purpose field_194157_a;
    private IRecipe field_193649_d;
    private boolean field_192631_e;
    private boolean field_192632_f;

    public CPacketRecipeInfo() {}

    public CPacketRecipeInfo(IRecipe irecipe) {
        this.field_194157_a = CPacketRecipeInfo.Purpose.SHOWN;
        this.field_193649_d = irecipe;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_194157_a = (CPacketRecipeInfo.Purpose) packetdataserializer.func_179257_a(CPacketRecipeInfo.Purpose.class);
        if (this.field_194157_a == CPacketRecipeInfo.Purpose.SHOWN) {
            this.field_193649_d = CraftingManager.func_193374_a(packetdataserializer.readInt());
        } else if (this.field_194157_a == CPacketRecipeInfo.Purpose.SETTINGS) {
            this.field_192631_e = packetdataserializer.readBoolean();
            this.field_192632_f = packetdataserializer.readBoolean();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_194157_a);
        if (this.field_194157_a == CPacketRecipeInfo.Purpose.SHOWN) {
            packetdataserializer.writeInt(CraftingManager.func_193375_a(this.field_193649_d));
        } else if (this.field_194157_a == CPacketRecipeInfo.Purpose.SETTINGS) {
            packetdataserializer.writeBoolean(this.field_192631_e);
            packetdataserializer.writeBoolean(this.field_192632_f);
        }

    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_191984_a(this);
    }

    public CPacketRecipeInfo.Purpose func_194156_a() {
        return this.field_194157_a;
    }

    public IRecipe func_193648_b() {
        return this.field_193649_d;
    }

    public boolean func_192624_c() {
        return this.field_192631_e;
    }

    public boolean func_192625_d() {
        return this.field_192632_f;
    }

    public static enum Purpose {

        SHOWN, SETTINGS;

        private Purpose() {}
    }
}
