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

    public String getName() {
        return "tellraw";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.tellraw.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[0]);
            String s = buildString(astring, 1);

            try {
                ITextComponent ichatbasecomponent = ITextComponent.Serializer.jsonToComponent(s);

                entityplayer.sendMessage(TextComponentUtils.processComponent(icommandlistener, ichatbasecomponent, entityplayer));
            } catch (JsonParseException jsonparseexception) {
                throw toSyntaxException(jsonparseexception);
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
