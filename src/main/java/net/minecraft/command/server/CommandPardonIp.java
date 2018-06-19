package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonIp extends CommandBase {

    public CommandPardonIp() {}

    public String func_71517_b() {
        return "pardon-ip";
    }

    public int func_82362_a() {
        return 3;
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.func_184103_al().func_72363_f().func_152689_b() && super.func_184882_a(minecraftserver, icommandlistener);
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.unbanip.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 1) {
            Matcher matcher = CommandBanIp.field_147211_a.matcher(astring[0]);

            if (matcher.matches()) {
                minecraftserver.func_184103_al().func_72363_f().func_152684_c(astring[0]);
                func_152373_a(icommandlistener, (ICommand) this, "commands.unbanip.success", new Object[] { astring[0]});
            } else {
                throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
            }
        } else {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_184103_al().func_72363_f().func_152685_a()) : Collections.emptyList();
    }
}
