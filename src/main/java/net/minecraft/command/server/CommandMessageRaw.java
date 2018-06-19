package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class CommandMessageRaw extends CommandBase {

    public CommandMessageRaw() {}

    public String func_71517_b() {
        return "tellraw";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.tellraw.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[0]);
            String s = func_180529_a(astring, 1);

            try {
                ITextComponent ichatbasecomponent = ITextComponent.Serializer.func_150699_a(s);

                entityplayer.func_145747_a(TextComponentUtils.func_179985_a(icommandlistener, ichatbasecomponent, entityplayer));
            } catch (JsonParseException jsonparseexception) {
                throw func_184889_a(jsonparseexception);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
