package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
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

public class CommandOp extends CommandBase {

    public CommandOp() {}

    public String func_71517_b() {
        return "op";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.op.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.op.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.func_184103_al().func_152605_a(gameprofile);
                func_152373_a(icommandlistener, (ICommand) this, "commands.op.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            String s = astring[astring.length - 1];
            ArrayList arraylist = Lists.newArrayList();
            GameProfile[] agameprofile = minecraftserver.func_152357_F();
            int i = agameprofile.length;

            for (int j = 0; j < i; ++j) {
                GameProfile gameprofile = agameprofile[j];

                if (!minecraftserver.func_184103_al().func_152596_g(gameprofile) && func_71523_a(s, gameprofile.getName())) {
                    arraylist.add(gameprofile.getName());
                }
            }

            return arraylist;
        } else {
            return Collections.emptyList();
        }
    }
}
