package net.minecraft.command;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand> {

    String func_71517_b();

    String func_71518_a(ICommandSender icommandlistener);

    List<String> func_71514_a();

    void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException;

    boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener);

    List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition);

    boolean func_82358_a(String[] astring, int i);
}
