package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RecipeCommand extends CommandBase {

    public RecipeCommand() {}

    public String getName() {
        return "recipe";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.recipe.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.recipe.usage", new Object[0]);
        } else {
            boolean flag = "give".equalsIgnoreCase(astring[0]);
            boolean flag1 = "take".equalsIgnoreCase(astring[0]);

            if (!flag && !flag1) {
                throw new WrongUsageException("commands.recipe.usage", new Object[0]);
            } else {
                List list = getPlayers(minecraftserver, icommandlistener, astring[1]);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                    if ("*".equals(astring[2])) {
                        if (flag) {
                            entityplayer.unlockRecipes(this.getRecipes());
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.recipe.give.success.all", new Object[] { entityplayer.getName()});
                        } else {
                            entityplayer.resetRecipes(this.getRecipes());
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.recipe.take.success.all", new Object[] { entityplayer.getName()});
                        }
                    } else {
                        IRecipe irecipe = CraftingManager.getRecipe(new ResourceLocation(astring[2]));

                        if (irecipe == null) {
                            throw new CommandException("commands.recipe.unknownrecipe", new Object[] { astring[2]});
                        }

                        if (irecipe.isDynamic()) {
                            throw new CommandException("commands.recipe.unsupported", new Object[] { astring[2]});
                        }

                        ArrayList arraylist = Lists.newArrayList(new IRecipe[] { irecipe});

                        if (flag == entityplayer.getRecipeBook().isUnlocked(irecipe)) {
                            String s = flag ? "commands.recipe.alreadyHave" : "commands.recipe.dontHave";

                            throw new CommandException(s, new Object[] { entityplayer.getName(), irecipe.getRecipeOutput().getDisplayName()});
                        }

                        if (flag) {
                            entityplayer.unlockRecipes((List) arraylist);
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.recipe.give.success.one", new Object[] { entityplayer.getName(), irecipe.getRecipeOutput().getDisplayName()});
                        } else {
                            entityplayer.resetRecipes((List) arraylist);
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.recipe.take.success.one", new Object[] { irecipe.getRecipeOutput().getDisplayName(), entityplayer.getName()});
                        }
                    }
                }

            }
        }
    }

    private List<IRecipe> getRecipes() {
        return Lists.newArrayList(CraftingManager.REGISTRY);
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "give", "take"}) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 3 ? getListOfStringsMatchingLastWord(astring, (Collection) CraftingManager.REGISTRY.getKeys()) : Collections.emptyList()));
    }
}
