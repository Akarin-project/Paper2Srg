package net.minecraft.command;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public interface ICommandManager {

    int executeCommand(ICommandSender icommandlistener, String s);

    List<String> getTabCompletions(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition);

    List<ICommand> getPossibleCommands(ICommandSender icommandlistener);

    Map<String, ICommand> getCommands();
}
