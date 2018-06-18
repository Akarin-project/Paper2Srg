package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemKnowledgeBook extends Item {

    private static final Logger LOGGER = LogManager.getLogger();

    public ItemKnowledgeBook() {
        this.setMaxStackSize(1);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        if (!entityhuman.capabilities.isCreativeMode) {
            entityhuman.setHeldItem(enumhand, ItemStack.EMPTY);
        }

        if (nbttagcompound != null && nbttagcompound.hasKey("Recipes", 9)) {
            if (!world.isRemote) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 8);
                ArrayList arraylist = Lists.newArrayList();

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    String s = nbttaglist.getStringTagAt(i);
                    IRecipe irecipe = CraftingManager.getRecipe(new ResourceLocation(s));

                    if (irecipe == null) {
                        ItemKnowledgeBook.LOGGER.error("Invalid recipe: " + s);
                        return new ActionResult(EnumActionResult.FAIL, itemstack);
                    }

                    arraylist.add(irecipe);
                }

                entityhuman.unlockRecipes((List) arraylist);
                entityhuman.addStat(StatList.getObjectUseStats((Item) this));
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            ItemKnowledgeBook.LOGGER.error("Tag not valid: " + nbttagcompound);
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
