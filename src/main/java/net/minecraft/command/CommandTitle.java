package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class CommandTitle extends CommandBase {

    private static final Logger LOGGER = LogManager.getLogger();

    public CommandTitle() {}

    public String getName() {
        return "title";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.title.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        } else {
            if (astring.length < 3) {
                if ("title".equals(astring[1]) || "subtitle".equals(astring[1]) || "actionbar".equals(astring[1])) {
                    throw new WrongUsageException("commands.title.usage.title", new Object[0]);
                }

                if ("times".equals(astring[1])) {
                    throw new WrongUsageException("commands.title.usage.times", new Object[0]);
                }
            }

            EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[0]);
            SPacketTitle.Type packetplayouttitle_enumtitleaction = SPacketTitle.Type.byName(astring[1]);

            if (packetplayouttitle_enumtitleaction != SPacketTitle.Type.CLEAR && packetplayouttitle_enumtitleaction != SPacketTitle.Type.RESET) {
                if (packetplayouttitle_enumtitleaction == SPacketTitle.Type.TIMES) {
                    if (astring.length != 5) {
                        throw new WrongUsageException("commands.title.usage", new Object[0]);
                    } else {
                        int i = parseInt(astring[2]);
                        int j = parseInt(astring[3]);
                        int k = parseInt(astring[4]);
                        SPacketTitle packetplayouttitle = new SPacketTitle(i, j, k);

                        entityplayer.connection.sendPacket(packetplayouttitle);
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
                    }
                } else if (astring.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                } else {
                    String s = buildString(astring, 2);

                    ITextComponent ichatbasecomponent;

                    try {
                        ichatbasecomponent = ITextComponent.Serializer.jsonToComponent(s);
                    } catch (JsonParseException jsonparseexception) {
                        throw toSyntaxException(jsonparseexception);
                    }

                    SPacketTitle packetplayouttitle1 = new SPacketTitle(packetplayouttitle_enumtitleaction, TextComponentUtils.processComponent(icommandlistener, ichatbasecomponent, entityplayer));

                    entityplayer.connection.sendPacket(packetplayouttitle1);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
                }
            } else if (astring.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            } else {
                SPacketTitle packetplayouttitle2 = new SPacketTitle(packetplayouttitle_enumtitleaction, (ITextComponent) null);

                entityplayer.connection.sendPacket(packetplayouttitle2);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, SPacketTitle.Type.getNames()) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
