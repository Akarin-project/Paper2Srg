package net.minecraft.stats;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.util.ResourceLocation;

public class RecipeBookServer extends RecipeBook {

    private static final Logger field_192828_d = LogManager.getLogger();

    public RecipeBookServer() {}

    public void func_193835_a(List<IRecipe> list, EntityPlayerMP entityplayer) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (!this.field_194077_a.get(func_194075_d(irecipe)) && !irecipe.func_192399_d()) {
                this.func_194073_a(irecipe);
                this.func_193825_e(irecipe);
                arraylist.add(irecipe);
                CriteriaTriggers.field_192126_f.func_192225_a(entityplayer, irecipe);
            }
        }

        this.func_194081_a(SPacketRecipeBook.State.ADD, entityplayer, arraylist);
    }

    public void func_193834_b(List<IRecipe> list, EntityPlayerMP entityplayer) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (this.field_194077_a.get(func_194075_d(irecipe))) {
                this.func_193831_b(irecipe);
                arraylist.add(irecipe);
            }
        }

        this.func_194081_a(SPacketRecipeBook.State.REMOVE, entityplayer, arraylist);
    }

    private void func_194081_a(SPacketRecipeBook.State packetplayoutrecipes_action, EntityPlayerMP entityplayer, List<IRecipe> list) {
        entityplayer.field_71135_a.func_147359_a(new SPacketRecipeBook(packetplayoutrecipes_action, list, Collections.emptyList(), this.field_192818_b, this.field_192819_c));
    }

    public NBTTagCompound func_192824_e() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74757_a("isGuiOpen", this.field_192818_b);
        nbttagcompound.func_74757_a("isFilteringCraftable", this.field_192819_c);
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.func_194079_d().iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            // Paper start - ignore missing recipes
            ResourceLocation key = CraftingManager.field_193380_a.func_177774_c(irecipe);
            if (key == null) continue;
            nbttaglist.func_74742_a(new NBTTagString(key.toString()));
            // Paper end
        }

        nbttagcompound.func_74782_a("recipes", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator1 = this.func_194080_e().iterator();

        while (iterator1.hasNext()) {
            // Paper start - ignore missing recipes
            IRecipe irecipe = (IRecipe) iterator1.next();

            ResourceLocation key = CraftingManager.field_193380_a.func_177774_c(irecipe);
            if (key == null) continue;
            nbttaglist1.func_74742_a(new NBTTagString(key.toString()));
            // Paper end
        }

        nbttagcompound.func_74782_a("toBeDisplayed", nbttaglist1);
        return nbttagcompound;
    }

    public void func_192825_a(NBTTagCompound nbttagcompound) {
        this.field_192818_b = nbttagcompound.func_74767_n("isGuiOpen");
        this.field_192819_c = nbttagcompound.func_74767_n("isFilteringCraftable");
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("recipes", 8);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            ResourceLocation minecraftkey = new ResourceLocation(nbttaglist.func_150307_f(i));
            IRecipe irecipe = CraftingManager.func_193373_a(minecraftkey);

            if (irecipe == null) {
                RecipeBookServer.field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", minecraftkey);
            } else {
                this.func_194073_a(irecipe);
            }
        }

        NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("toBeDisplayed", 8);

        for (int j = 0; j < nbttaglist1.func_74745_c(); ++j) {
            ResourceLocation minecraftkey1 = new ResourceLocation(nbttaglist1.func_150307_f(j));
            IRecipe irecipe1 = CraftingManager.func_193373_a(minecraftkey1);

            if (irecipe1 == null) {
                RecipeBookServer.field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", minecraftkey1);
            } else {
                this.func_193825_e(irecipe1);
            }
        }

    }

    private List<IRecipe> func_194079_d() {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = this.field_194077_a.nextSetBit(0); i >= 0; i = this.field_194077_a.nextSetBit(i + 1)) {
            arraylist.add(CraftingManager.field_193380_a.func_148754_a(i));
        }

        return arraylist;
    }

    private List<IRecipe> func_194080_e() {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = this.field_194078_b.nextSetBit(0); i >= 0; i = this.field_194078_b.nextSetBit(i + 1)) {
            arraylist.add(CraftingManager.field_193380_a.func_148754_a(i));
        }

        return arraylist;
    }

    public void func_192826_c(EntityPlayerMP entityplayer) {
        entityplayer.field_71135_a.func_147359_a(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.func_194079_d(), this.func_194080_e(), this.field_192818_b, this.field_192819_c));
    }
}
