package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandLocate extends CommandBase {

    public CommandLocate() {}

    public String func_71517_b() {
        return "locate";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.locate.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1) {
            throw new WrongUsageException("commands.locate.usage", new Object[0]);
        } else {
            String s = astring[0];
            BlockPos blockposition = icommandlistener.func_130014_f_().func_190528_a(s, icommandlistener.func_180425_c(), false);

            if (blockposition != null) {
                icommandlistener.func_145747_a(new TextComponentTranslation("commands.locate.success", new Object[] { s, Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177952_p())}));
            } else {
                throw new CommandException("commands.locate.failure", new Object[] { s});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft"}) : Collections.emptyList();
    }
}
