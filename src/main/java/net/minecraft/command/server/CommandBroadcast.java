package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBroadcast extends CommandBase {

    public CommandBroadcast() {}

    public String func_71517_b() {
        return "say";
    }

    public int func_82362_a() {
        return 1;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.say.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0 && astring[0].length() > 0) {
            ITextComponent ichatbasecomponent = func_147176_a(icommandlistener, astring, 0, true);

            minecraftserver.func_184103_al().func_148539_a(new TextComponentTranslation("chat.type.announcement", new Object[] { icommandlistener.func_145748_c_(), ichatbasecomponent}));
        } else {
            throw new WrongUsageException("commands.say.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }
}
