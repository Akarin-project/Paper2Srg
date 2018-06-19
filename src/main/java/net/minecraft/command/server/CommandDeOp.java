package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDeOp extends CommandBase {

    public CommandDeOp() {}

    public String func_71517_b() {
        return "deop";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.deop.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.func_184103_al().func_152603_m().func_152700_a(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.deop.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.func_184103_al().func_152610_b(gameprofile);
                func_152373_a(icommandlistener, (ICommand) this, "commands.deop.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_184103_al().func_152606_n()) : Collections.emptyList();
    }
}
