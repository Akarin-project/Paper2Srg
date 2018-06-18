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

    private static final Logger LOGGER = LogManager.getLogger();

    public RecipeBookServer() {}

    public void add(List<IRecipe> list, EntityPlayerMP entityplayer) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (!this.recipes.get(getRecipeId(irecipe)) && !irecipe.isDynamic()) {
                this.unlock(irecipe);
                this.markNew(irecipe);
                arraylist.add(irecipe);
                CriteriaTriggers.RECIPE_UNLOCKED.trigger(entityplayer, irecipe);
            }
        }

        this.sendPacket(SPacketRecipeBook.State.ADD, entityplayer, arraylist);
    }

    public void remove(List<IRecipe> list, EntityPlayerMP entityplayer) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (this.recipes.get(getRecipeId(irecipe))) {
                this.lock(irecipe);
                arraylist.add(irecipe);
            }
        }

        this.sendPacket(SPacketRecipeBook.State.REMOVE, entityplayer, arraylist);
    }

    private void sendPacket(SPacketRecipeBook.State packetplayoutrecipes_action, EntityPlayerMP entityplayer, List<IRecipe> list) {
        entityplayer.connection.sendPacket(new SPacketRecipeBook(packetplayoutrecipes_action, list, Collections.emptyList(), this.isGuiOpen, this.isFilteringCraftable));
    }

    public NBTTagCompound write() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setBoolean("isGuiOpen", this.isGuiOpen);
        nbttagcompound.setBoolean("isFilteringCraftable", this.isFilteringCraftable);
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.getRecipes().iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            // Paper start - ignore missing recipes
            ResourceLocation key = CraftingManager.REGISTRY.getNameForObject(irecipe);
            if (key == null) continue;
            nbttaglist.appendTag(new NBTTagString(key.toString()));
            // Paper end
        }

        nbttagcompound.setTag("recipes", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator1 = this.getDisplayedRecipes().iterator();

        while (iterator1.hasNext()) {
            // Paper start - ignore missing recipes
            IRecipe irecipe = (IRecipe) iterator1.next();

            ResourceLocation key = CraftingManager.REGISTRY.getNameForObject(irecipe);
            if (key == null) continue;
            nbttaglist1.appendTag(new NBTTagString(key.toString()));
            // Paper end
        }

        nbttagcompound.setTag("toBeDisplayed", nbttaglist1);
        return nbttagcompound;
    }

    public void read(NBTTagCompound nbttagcompound) {
        this.isGuiOpen = nbttagcompound.getBoolean("isGuiOpen");
        this.isFilteringCraftable = nbttagcompound.getBoolean("isFilteringCraftable");
        NBTTagList nbttaglist = nbttagcompound.getTagList("recipes", 8);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            ResourceLocation minecraftkey = new ResourceLocation(nbttaglist.getStringTagAt(i));
            IRecipe irecipe = CraftingManager.getRecipe(minecraftkey);

            if (irecipe == null) {
                RecipeBookServer.LOGGER.info("Tried to load unrecognized recipe: {} removed now.", minecraftkey);
            } else {
                this.unlock(irecipe);
            }
        }

        NBTTagList nbttaglist1 = nbttagcompound.getTagList("toBeDisplayed", 8);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
            ResourceLocation minecraftkey1 = new ResourceLocation(nbttaglist1.getStringTagAt(j));
            IRecipe irecipe1 = CraftingManager.getRecipe(minecraftkey1);

            if (irecipe1 == null) {
                RecipeBookServer.LOGGER.info("Tried to load unrecognized recipe: {} removed now.", minecraftkey1);
            } else {
                this.markNew(irecipe1);
            }
        }

    }

    private List<IRecipe> getRecipes() {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = this.recipes.nextSetBit(0); i >= 0; i = this.recipes.nextSetBit(i + 1)) {
            arraylist.add(CraftingManager.REGISTRY.getObjectById(i));
        }

        return arraylist;
    }

    private List<IRecipe> getDisplayedRecipes() {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = this.newRecipes.nextSetBit(0); i >= 0; i = this.newRecipes.nextSetBit(i + 1)) {
            arraylist.add(CraftingManager.REGISTRY.getObjectById(i));
        }

        return arraylist;
    }

    public void init(EntityPlayerMP entityplayer) {
        entityplayer.connection.sendPacket(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.getRecipes(), this.getDisplayedRecipes(), this.isGuiOpen, this.isFilteringCraftable));
    }
}
