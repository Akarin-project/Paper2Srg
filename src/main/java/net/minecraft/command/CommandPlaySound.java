package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandPlaySound extends CommandBase {

    public CommandPlaySound() {}

    public String getName() {
        return "playsound";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.playsound.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException(this.getUsage(icommandlistener), new Object[0]);
        } else {
            byte b0 = 0;
            int i = b0 + 1;
            String s = astring[b0];
            String s1 = astring[i++];
            SoundCategory soundcategory = SoundCategory.getByName(s1);

            if (soundcategory == null) {
                throw new CommandException("commands.playsound.unknownSoundSource", new Object[] { s1});
            } else {
                EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[i++]);
                Vec3d vec3d = icommandlistener.getPositionVector();
                double d0 = astring.length > i ? parseDouble(vec3d.x, astring[i++], true) : vec3d.x;
                double d1 = astring.length > i ? parseDouble(vec3d.y, astring[i++], 0, 0, false) : vec3d.y;
                double d2 = astring.length > i ? parseDouble(vec3d.z, astring[i++], true) : vec3d.z;
                double d3 = astring.length > i ? parseDouble(astring[i++], 0.0D, 3.4028234663852886E38D) : 1.0D;
                double d4 = astring.length > i ? parseDouble(astring[i++], 0.0D, 2.0D) : 1.0D;
                double d5 = astring.length > i ? parseDouble(astring[i], 0.0D, 1.0D) : 0.0D;
                double d6 = d3 > 1.0D ? d3 * 16.0D : 16.0D;
                double d7 = entityplayer.getDistance(d0, d1, d2);

                if (d7 > d6) {
                    if (d5 <= 0.0D) {
                        throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayer.getName()});
                    }

                    double d8 = d0 - entityplayer.posX;
                    double d9 = d1 - entityplayer.posY;
                    double d10 = d2 - entityplayer.posZ;
                    double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 > 0.0D) {
                        d0 = entityplayer.posX + d8 / d11 * 2.0D;
                        d1 = entityplayer.posY + d9 / d11 * 2.0D;
                        d2 = entityplayer.posZ + d10 / d11 * 2.0D;
                    }

                    d3 = d5;
                }

                entityplayer.connection.sendPacket(new SPacketCustomSound(s, soundcategory, d0, d1, d2, (float) d3, (float) d4));
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.playsound.success", new Object[] { s, entityplayer.getName()});
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, (Collection) SoundEvent.REGISTRY.getKeys()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) SoundCategory.getSoundCategoryNames()) : (astring.length == 3 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length > 3 && astring.length <= 6 ? getTabCompletionCoordinate(astring, 3, blockposition) : Collections.emptyList())));
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 2;
    }
}
