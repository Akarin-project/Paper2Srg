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

    public String func_71517_b() {
        return "recipe";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.recipe.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.recipe.usage", new Object[0]);
        } else {
            boolean flag = "give".equalsIgnoreCase(astring[0]);
            boolean flag1 = "take".equalsIgnoreCase(astring[0]);

            if (!flag && !flag1) {
                throw new WrongUsageException("commands.recipe.usage", new Object[0]);
            } else {
                List list = func_193513_a(minecraftserver, icommandlistener, astring[1]);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                    if ("*".equals(astring[2])) {
                        if (flag) {
                            entityplayer.func_192021_a(this.func_192556_d());
                            func_152373_a(icommandlistener, (ICommand) this, "commands.recipe.give.success.all", new Object[] { entityplayer.func_70005_c_()});
                        } else {
                            entityplayer.func_192022_b(this.func_192556_d());
                            func_152373_a(icommandlistener, (ICommand) this, "commands.recipe.take.success.all", new Object[] { entityplayer.func_70005_c_()});
                        }
                    } else {
                        IRecipe irecipe = CraftingManager.func_193373_a(new ResourceLocation(astring[2]));

                        if (irecipe == null) {
                            throw new CommandException("commands.recipe.unknownrecipe", new Object[] { astring[2]});
                        }

                        if (irecipe.func_192399_d()) {
                            throw new CommandException("commands.recipe.unsupported", new Object[] { astring[2]});
                        }

                        ArrayList arraylist = Lists.newArrayList(new IRecipe[] { irecipe});

                        if (flag == entityplayer.func_192037_E().func_193830_f(irecipe)) {
                            String s = flag ? "commands.recipe.alreadyHave" : "commands.recipe.dontHave";

                            throw new CommandException(s, new Object[] { entityplayer.func_70005_c_(), irecipe.func_77571_b().func_82833_r()});
                        }

                        if (flag) {
                            entityplayer.func_192021_a((List) arraylist);
                            func_152373_a(icommandlistener, (ICommand) this, "commands.recipe.give.success.one", new Object[] { entityplayer.func_70005_c_(), irecipe.func_77571_b().func_82833_r()});
                        } else {
                            entityplayer.func_192022_b((List) arraylist);
                            func_152373_a(icommandlistener, (ICommand) this, "commands.recipe.take.success.one", new Object[] { irecipe.func_77571_b().func_82833_r(), entityplayer.func_70005_c_()});
                        }
                    }
                }

            }
        }
    }

    private List<IRecipe> func_192556_d() {
        return Lists.newArrayList(CraftingManager.field_193380_a);
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "give", "take"}) : (astring.length == 2 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 3 ? func_175762_a(astring, (Collection) CraftingManager.field_193380_a.func_148742_b()) : Collections.emptyList()));
    }
}
