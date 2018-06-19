package net.minecraft.command;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public interface ICommandManager {

    int func_71556_a(ICommandSender icommandlistener, String s);

    List<String> func_180524_a(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition);

    List<ICommand> func_71557_a(ICommandSender icommandlistener);

    Map<String, ICommand> func_71555_a();
}
