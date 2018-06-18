package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketRecipeBook implements Packet<INetHandlerPlayClient> {

    private SPacketRecipeBook.State state;
    private List<IRecipe> recipes;
    private List<IRecipe> displayedRecipes;
    private boolean guiOpen;
    private boolean filteringCraftable;

    public SPacketRecipeBook() {}

    public SPacketRecipeBook(SPacketRecipeBook.State packetplayoutrecipes_action, List<IRecipe> list, List<IRecipe> list1, boolean flag, boolean flag1) {
        this.state = packetplayoutrecipes_action;
        this.recipes = list;
        this.displayedRecipes = list1;
        this.guiOpen = flag;
        this.filteringCraftable = flag1;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleRecipeBook(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.state = (SPacketRecipeBook.State) packetdataserializer.readEnumValue(SPacketRecipeBook.State.class);
        this.guiOpen = packetdataserializer.readBoolean();
        this.filteringCraftable = packetdataserializer.readBoolean();
        int i = packetdataserializer.readVarInt();

        this.recipes = Lists.newArrayList();

        int j;

        for (j = 0; j < i; ++j) {
            this.recipes.add(CraftingManager.getRecipeById(packetdataserializer.readVarInt()));
        }

        if (this.state == SPacketRecipeBook.State.INIT) {
            i = packetdataserializer.readVarInt();
            this.displayedRecipes = Lists.newArrayList();

            for (j = 0; j < i; ++j) {
                this.displayedRecipes.add(CraftingManager.getRecipeById(packetdataserializer.readVarInt()));
            }
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.state);
        packetdataserializer.writeBoolean(this.guiOpen);
        packetdataserializer.writeBoolean(this.filteringCraftable);
        packetdataserializer.writeVarInt(this.recipes.size());
        Iterator iterator = this.recipes.iterator();

        IRecipe irecipe;

        while (iterator.hasNext()) {
            irecipe = (IRecipe) iterator.next();
            packetdataserializer.writeVarInt(CraftingManager.getIDForRecipe(irecipe));
        }

        if (this.state == SPacketRecipeBook.State.INIT) {
            packetdataserializer.writeVarInt(this.displayedRecipes.size());
            iterator = this.displayedRecipes.iterator();

            while (iterator.hasNext()) {
                irecipe = (IRecipe) iterator.next();
                packetdataserializer.writeVarInt(CraftingManager.getIDForRecipe(irecipe));
            }
        }

    }

    public static enum State {

        INIT, ADD, REMOVE;

        private State() {}
    }
}
