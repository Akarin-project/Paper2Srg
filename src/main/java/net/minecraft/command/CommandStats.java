package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandStats extends CommandBase {

    public CommandStats() {}

    public String func_71517_b() {
        return "stats";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.stats.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        } else {
            boolean flag;

            if ("entity".equals(astring[0])) {
                flag = false;
            } else {
                if (!"block".equals(astring[0])) {
                    throw new WrongUsageException("commands.stats.usage", new Object[0]);
                }

                flag = true;
            }

            byte b0;

            if (flag) {
                if (astring.length < 5) {
                    throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
                }

                b0 = 4;
            } else {
                if (astring.length < 3) {
                    throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
                }

                b0 = 2;
            }

            int i = b0 + 1;
            String s = astring[b0];

            if ("set".equals(s)) {
                if (astring.length < i + 3) {
                    if (i == 5) {
                        throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                    }

                    throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
                }
            } else {
                if (!"clear".equals(s)) {
                    throw new WrongUsageException("commands.stats.usage", new Object[0]);
                }

                if (astring.length < i + 1) {
                    if (i == 5) {
                        throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                    }

                    throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
                }
            }

            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.func_179635_a(astring[i++]);

            if (commandobjectiveexecutor_enumcommandresult == null) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            } else {
                World world = icommandlistener.func_130014_f_();
                CommandResultStats commandobjectiveexecutor;
                BlockPos blockposition;
                TileEntity tileentity;

                if (flag) {
                    blockposition = func_175757_a(icommandlistener, astring, 1, false);
                    tileentity = world.func_175625_s(blockposition);
                    if (tileentity == null) {
                        throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                    }

                    if (tileentity instanceof TileEntityCommandBlock) {
                        commandobjectiveexecutor = ((TileEntityCommandBlock) tileentity).func_175124_c();
                    } else {
                        if (!(tileentity instanceof TileEntitySign)) {
                            throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                        }

                        commandobjectiveexecutor = ((TileEntitySign) tileentity).func_174880_d();
                    }
                } else {
                    Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[1]);

                    commandobjectiveexecutor = entity.func_174807_aT();
                }

                if ("set".equals(s)) {
                    String s1 = astring[i++];
                    String s2 = astring[i];

                    if (s1.isEmpty() || s2.isEmpty()) {
                        throw new CommandException("commands.stats.failed", new Object[0]);
                    }

                    CommandResultStats.func_179667_a(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult, s1, s2);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.stats.success", new Object[] { commandobjectiveexecutor_enumcommandresult.func_179637_b(), s2, s1});
                } else if ("clear".equals(s)) {
                    CommandResultStats.func_179667_a(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult, (String) null, (String) null);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.stats.cleared", new Object[] { commandobjectiveexecutor_enumcommandresult.func_179637_b()});
                }

                if (flag) {
                    blockposition = func_175757_a(icommandlistener, astring, 1, false);
                    tileentity = world.func_175625_s(blockposition);
                    tileentity.func_70296_d();
                }

            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "entity", "block"}) : (astring.length == 2 && "entity".equals(astring[0]) ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length >= 2 && astring.length <= 4 && "block".equals(astring[0]) ? func_175771_a(astring, 1, blockposition) : ((astring.length != 3 || !"entity".equals(astring[0])) && (astring.length != 5 || !"block".equals(astring[0])) ? ((astring.length != 4 || !"entity".equals(astring[0])) && (astring.length != 6 || !"block".equals(astring[0])) ? ((astring.length != 6 || !"entity".equals(astring[0])) && (astring.length != 8 || !"block".equals(astring[0])) ? Collections.emptyList() : func_175762_a(astring, (Collection) this.func_184927_a(minecraftserver))) : func_71530_a(astring, CommandResultStats.Type.func_179634_c())) : func_71530_a(astring, new String[] { "set", "clear"}))));
    }

    protected List<String> func_184927_a(MinecraftServer minecraftserver) {
        Collection collection = minecraftserver.func_71218_a(0).func_96441_U().func_96514_c();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (!scoreboardobjective.func_96680_c().func_96637_b()) {
                arraylist.add(scoreboardobjective.func_96679_b());
            }
        }

        return arraylist;
    }

    public boolean func_82358_a(String[] astring, int i) {
        return astring.length > 0 && "entity".equals(astring[0]) && i == 1;
    }
}
