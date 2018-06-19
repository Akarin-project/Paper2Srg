package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetDefaultSpawnpoint extends CommandBase {

    public CommandSetDefaultSpawnpoint() {}

    public String func_71517_b() {
        return "setworldspawn";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.setworldspawn.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        BlockPos blockposition;

        if (astring.length == 0) {
            blockposition = func_71521_c(icommandlistener).func_180425_c();
        } else {
            if (astring.length != 3 || icommandlistener.func_130014_f_() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }

            blockposition = func_175757_a(icommandlistener, astring, 0, true);
        }

        icommandlistener.func_130014_f_().func_175652_B(blockposition);
        minecraftserver.func_184103_al().func_148540_a(new SPacketSpawnPosition(blockposition));
        func_152373_a(icommandlistener, (ICommand) this, "commands.setworldspawn.success", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : Collections.emptyList();
    }
}
