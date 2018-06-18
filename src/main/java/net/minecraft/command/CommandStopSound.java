package net.minecraft.command;

import io.netty.buffer.Unpooled;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class CommandStopSound extends CommandBase {

    public CommandStopSound() {}

    public String getName() {
        return "stopsound";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.stopsound.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring.length <= 3) {
            byte b0 = 0;
            int i = b0 + 1;
            EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[b0]);
            String s = "";
            String s1 = "";

            if (astring.length >= 2) {
                String s2 = astring[i++];
                SoundCategory soundcategory = SoundCategory.getByName(s2);

                if (soundcategory == null) {
                    throw new CommandException("commands.stopsound.unknownSoundSource", new Object[] { s2});
                }

                s = soundcategory.getName();
            }

            if (astring.length == 3) {
                s1 = astring[i++];
            }

            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.writeString(s);
            packetdataserializer.writeString(s1);
            entityplayer.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetdataserializer));
            if (s.isEmpty() && s1.isEmpty()) {
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.stopsound.success.all", new Object[] { entityplayer.getName()});
            } else if (s1.isEmpty()) {
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.stopsound.success.soundSource", new Object[] { s, entityplayer.getName()});
            } else {
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.stopsound.success.individualSound", new Object[] { s1, s, entityplayer.getName()});
            }

        } else {
            throw new WrongUsageException(this.getUsage(icommandlistener), new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) SoundCategory.getSoundCategoryNames()) : (astring.length == 3 ? getListOfStringsMatchingLastWord(astring, (Collection) SoundEvent.REGISTRY.getKeys()) : Collections.emptyList()));
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
