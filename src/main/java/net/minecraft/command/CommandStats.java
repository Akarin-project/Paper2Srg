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

    public String getName() {
        return "stats";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.stats.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
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

            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.getTypeByName(astring[i++]);

            if (commandobjectiveexecutor_enumcommandresult == null) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            } else {
                World world = icommandlistener.getEntityWorld();
                CommandResultStats commandobjectiveexecutor;
                BlockPos blockposition;
                TileEntity tileentity;

                if (flag) {
                    blockposition = parseBlockPos(icommandlistener, astring, 1, false);
                    tileentity = world.getTileEntity(blockposition);
                    if (tileentity == null) {
                        throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                    }

                    if (tileentity instanceof TileEntityCommandBlock) {
                        commandobjectiveexecutor = ((TileEntityCommandBlock) tileentity).getCommandResultStats();
                    } else {
                        if (!(tileentity instanceof TileEntitySign)) {
                            throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                        }

                        commandobjectiveexecutor = ((TileEntitySign) tileentity).getStats();
                    }
                } else {
                    Entity entity = getEntity(minecraftserver, icommandlistener, astring[1]);

                    commandobjectiveexecutor = entity.getCommandStats();
                }

                if ("set".equals(s)) {
                    String s1 = astring[i++];
                    String s2 = astring[i];

                    if (s1.isEmpty() || s2.isEmpty()) {
                        throw new CommandException("commands.stats.failed", new Object[0]);
                    }

                    CommandResultStats.setScoreBoardStat(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult, s1, s2);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.stats.success", new Object[] { commandobjectiveexecutor_enumcommandresult.getTypeName(), s2, s1});
                } else if ("clear".equals(s)) {
                    CommandResultStats.setScoreBoardStat(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult, (String) null, (String) null);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.stats.cleared", new Object[] { commandobjectiveexecutor_enumcommandresult.getTypeName()});
                }

                if (flag) {
                    blockposition = parseBlockPos(icommandlistener, astring, 1, false);
                    tileentity = world.getTileEntity(blockposition);
                    tileentity.markDirty();
                }

            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "entity", "block"}) : (astring.length == 2 && "entity".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length >= 2 && astring.length <= 4 && "block".equals(astring[0]) ? getTabCompletionCoordinate(astring, 1, blockposition) : ((astring.length != 3 || !"entity".equals(astring[0])) && (astring.length != 5 || !"block".equals(astring[0])) ? ((astring.length != 4 || !"entity".equals(astring[0])) && (astring.length != 6 || !"block".equals(astring[0])) ? ((astring.length != 6 || !"entity".equals(astring[0])) && (astring.length != 8 || !"block".equals(astring[0])) ? Collections.emptyList() : getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(minecraftserver))) : getListOfStringsMatchingLastWord(astring, CommandResultStats.Type.getTypeNames())) : getListOfStringsMatchingLastWord(astring, new String[] { "set", "clear"}))));
    }

    protected List<String> getObjectiveNames(MinecraftServer minecraftserver) {
        Collection collection = minecraftserver.getWorld(0).getScoreboard().getScoreObjectives();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (!scoreboardobjective.getCriteria().isReadOnly()) {
                arraylist.add(scoreboardobjective.getName());
            }
        }

        return arraylist;
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return astring.length > 0 && "entity".equals(astring[0]) && i == 1;
    }
}
