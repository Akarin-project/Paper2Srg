package net.minecraft.command;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand> {

    String getName();

    String getUsage(ICommandSender icommandlistener);

    List<String> getAliases();

    void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException;

    boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener);

    List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition);

    boolean isUsernameIndex(String[] astring, int i);
}
