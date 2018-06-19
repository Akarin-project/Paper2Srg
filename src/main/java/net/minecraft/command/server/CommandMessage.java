package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends CommandBase {

    public CommandMessage() {}

    public List<String> func_71514_a() {
        return Arrays.asList(new String[] { "w", "msg"});
    }

    public String func_71517_b() {
        return "tell";
    }

    public int func_82362_a() {
        return 0;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.message.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[0]);

            if (entityplayer == icommandlistener) {
                throw new PlayerNotFoundException("commands.message.sameTarget");
            } else {
                ITextComponent ichatbasecomponent = func_147176_a(icommandlistener, astring, 1, !(icommandlistener instanceof EntityPlayer));
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.message.display.incoming", new Object[] { icommandlistener.func_145748_c_(), ichatbasecomponent.func_150259_f()});
                TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.message.display.outgoing", new Object[] { entityplayer.func_145748_c_(), ichatbasecomponent.func_150259_f()});

                chatmessage.func_150256_b().func_150238_a(TextFormatting.GRAY).func_150217_b(Boolean.valueOf(true));
                chatmessage1.func_150256_b().func_150238_a(TextFormatting.GRAY).func_150217_b(Boolean.valueOf(true));
                entityplayer.func_145747_a(chatmessage);
                icommandlistener.func_145747_a(chatmessage1);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return func_71530_a(astring, minecraftserver.func_71213_z());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
