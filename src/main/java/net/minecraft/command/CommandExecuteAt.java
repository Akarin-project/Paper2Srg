package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;

// CraftBukkit start
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
// CraftBukkit end

public class CommandExecuteAt extends CommandBase {

    public CommandExecuteAt() {}

    public String getName() {
        return "execute";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.execute.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        } else {
            Entity entity = getEntity(minecraftserver, icommandlistener, astring[0], Entity.class);
            double d0 = parseDouble(entity.posX, astring[1], false);
            double d1 = parseDouble(entity.posY, astring[2], false);
            double d2 = parseDouble(entity.posZ, astring[3], false);

            new BlockPos(d0, d1, d2);
            byte b0 = 4;

            if ("detect".equals(astring[4]) && astring.length > 10) {
                World world = entity.getEntityWorld();
                double d3 = parseDouble(d0, astring[5], false);
                double d4 = parseDouble(d1, astring[6], false);
                double d5 = parseDouble(d2, astring[7], false);
                Block block = getBlockByText(icommandlistener, astring[8]);
                BlockPos blockposition = new BlockPos(d3, d4, d5);

                if (!world.isBlockLoaded(blockposition)) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName()});
                }

                IBlockState iblockdata = world.getBlockState(blockposition);

                if (iblockdata.getBlock() != block) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName()});
                }

                if (!CommandBase.convertArgToBlockStatePredicate(block, astring[9]).apply(iblockdata)) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName()});
                }

                b0 = 10;
            }

            String s = buildString(astring, b0);
            CommandSenderWrapper commandlistenerwrapper = CommandSenderWrapper.create(icommandlistener).withEntity(entity, new Vec3d(d0, d1, d2)).withSendCommandFeedback(minecraftserver.worlds.get(0).getGameRules().getBoolean("commandBlockOutput")); // CraftBukkit
            ICommandManager icommandhandler = minecraftserver.getCommandManager();

            try {
                // CraftBukkit start
                org.bukkit.command.CommandSender sender = CommandBlockBaseLogic.unwrapSender(icommandlistener);
                int i = CommandBlockBaseLogic.executeCommand(commandlistenerwrapper, new ProxiedNativeCommandSender(commandlistenerwrapper, sender, entity.getBukkitEntity()), s); 
                // CraftBukkit end

                if (i < 1) {
                    throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s});
                }
            } catch (Throwable throwable) {
                // CraftBukkit start
                if (throwable instanceof CommandException) {
                    throw (CommandException) throwable;
                }
                // CraftBukkit end
                throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName()});
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length > 1 && astring.length <= 4 ? getTabCompletionCoordinate(astring, 1, blockposition) : (astring.length > 5 && astring.length <= 8 && "detect".equals(astring[4]) ? getTabCompletionCoordinate(astring, 5, blockposition) : (astring.length == 9 && "detect".equals(astring[4]) ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : Collections.<String>emptyList()))); // CraftBukkit - decompile error
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }

    // CraftBukkit start - fix decompiler error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
