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

    private SPacketRecipeBook.State field_193646_a;
    private List<IRecipe> field_192596_a;
    private List<IRecipe> field_193647_c;
    private boolean field_192598_c;
    private boolean field_192599_d;

    public SPacketRecipeBook() {}

    public SPacketRecipeBook(SPacketRecipeBook.State packetplayoutrecipes_action, List<IRecipe> list, List<IRecipe> list1, boolean flag, boolean flag1) {
        this.field_193646_a = packetplayoutrecipes_action;
        this.field_192596_a = list;
        this.field_193647_c = list1;
        this.field_192598_c = flag;
        this.field_192599_d = flag1;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_191980_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_193646_a = (SPacketRecipeBook.State) packetdataserializer.func_179257_a(SPacketRecipeBook.State.class);
        this.field_192598_c = packetdataserializer.readBoolean();
        this.field_192599_d = packetdataserializer.readBoolean();
        int i = packetdataserializer.func_150792_a();

        this.field_192596_a = Lists.newArrayList();

        int j;

        for (j = 0; j < i; ++j) {
            this.field_192596_a.add(CraftingManager.func_193374_a(packetdataserializer.func_150792_a()));
        }

        if (this.field_193646_a == SPacketRecipeBook.State.INIT) {
            i = packetdataserializer.func_150792_a();
            this.field_193647_c = Lists.newArrayList();

            for (j = 0; j < i; ++j) {
                this.field_193647_c.add(CraftingManager.func_193374_a(packetdataserializer.func_150792_a()));
            }
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_193646_a);
        packetdataserializer.writeBoolean(this.field_192598_c);
        packetdataserializer.writeBoolean(this.field_192599_d);
        packetdataserializer.func_150787_b(this.field_192596_a.size());
        Iterator iterator = this.field_192596_a.iterator();

        IRecipe irecipe;

        while (iterator.hasNext()) {
            irecipe = (IRecipe) iterator.next();
            packetdataserializer.func_150787_b(CraftingManager.func_193375_a(irecipe));
        }

        if (this.field_193646_a == SPacketRecipeBook.State.INIT) {
            packetdataserializer.func_150787_b(this.field_193647_c.size());
            iterator = this.field_193647_c.iterator();

            while (iterator.hasNext()) {
                irecipe = (IRecipe) iterator.next();
                packetdataserializer.func_150787_b(CraftingManager.func_193375_a(irecipe));
            }
        }

    }

    public static enum State {

        INIT, ADD, REMOVE;

        private State() {}
    }
}
