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

    public String getName() {
        return "op";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.op.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.op.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.getPlayerList().addOp(gameprofile);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.op.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            String s = astring[astring.length - 1];
            ArrayList arraylist = Lists.newArrayList();
            GameProfile[] agameprofile = minecraftserver.getOnlinePlayerProfiles();
            int i = agameprofile.length;

            for (int j = 0; j < i; ++j) {
                GameProfile gameprofile = agameprofile[j];

                if (!minecraftserver.getPlayerList().canSendCommands(gameprofile) && doesStringStartWith(s, gameprofile.getName())) {
                    arraylist.add(gameprofile.getName());
                }
            }

            return arraylist;
        } else {
            return Collections.emptyList();
        }
    }
}
