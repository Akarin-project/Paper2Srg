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

    private static final Logger field_194126_a = LogManager.getLogger();

    public ItemKnowledgeBook() {
        this.func_77625_d(1);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        if (!entityhuman.field_71075_bZ.field_75098_d) {
            entityhuman.func_184611_a(enumhand, ItemStack.field_190927_a);
        }

        if (nbttagcompound != null && nbttagcompound.func_150297_b("Recipes", 9)) {
            if (!world.field_72995_K) {
                NBTTagList nbttaglist = nbttagcompound.func_150295_c("Recipes", 8);
                ArrayList arraylist = Lists.newArrayList();

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    String s = nbttaglist.func_150307_f(i);
                    IRecipe irecipe = CraftingManager.func_193373_a(new ResourceLocation(s));

                    if (irecipe == null) {
                        ItemKnowledgeBook.field_194126_a.error("Invalid recipe: " + s);
                        return new ActionResult(EnumActionResult.FAIL, itemstack);
                    }

                    arraylist.add(irecipe);
                }

                entityhuman.func_192021_a((List) arraylist);
                entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            ItemKnowledgeBook.field_194126_a.error("Tag not valid: " + nbttagcompound);
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
