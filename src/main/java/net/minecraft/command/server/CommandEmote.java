package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEmote extends CommandBase {

    public CommandEmote() {}

    public String func_71517_b() {
        return "me";
    }

    public int func_82362_a() {
        return 0;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.me.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        } else {
            ITextComponent ichatbasecomponent = func_147176_a(icommandlistener, astring, 0, !(icommandlistener instanceof EntityPlayer));

            minecraftserver.func_184103_al().func_148539_a(new TextComponentTranslation("chat.type.emote", new Object[] { icommandlistener.func_145748_c_(), ichatbasecomponent}));
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return func_71530_a(astring, minecraftserver.func_71213_z());
    }
}
