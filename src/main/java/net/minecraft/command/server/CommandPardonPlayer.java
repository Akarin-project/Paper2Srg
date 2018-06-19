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

public class CommandPardonPlayer extends CommandBase {

    public CommandPardonPlayer() {}

    public String func_71517_b() {
        return "pardon";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.unban.usage";
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.func_184103_al().func_152608_h().func_152689_b() && super.func_184882_a(minecraftserver, icommandlistener);
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.func_184103_al().func_152608_h().func_152703_a(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.unban.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.func_184103_al().func_152608_h().func_152684_c(gameprofile);
                func_152373_a(icommandlistener, (ICommand) this, "commands.unban.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_184103_al().func_152608_h().func_152685_a()) : Collections.emptyList();
    }
}
