package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase {

    public CommandParticle() {}

    public String getName() {
        return "particle";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.particle.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        } else {
            boolean flag = false;
            EnumParticleTypes enumparticle = EnumParticleTypes.getByName(astring[0]);

            if (enumparticle == null) {
                throw new CommandException("commands.particle.notFound", new Object[] { astring[0]});
            } else {
                String s = astring[0];
                Vec3d vec3d = icommandlistener.getPositionVector();
                double d0 = (double) ((float) parseDouble(vec3d.x, astring[1], true));
                double d1 = (double) ((float) parseDouble(vec3d.y, astring[2], true));
                double d2 = (double) ((float) parseDouble(vec3d.z, astring[3], true));
                double d3 = (double) ((float) parseDouble(astring[4]));
                double d4 = (double) ((float) parseDouble(astring[5]));
                double d5 = (double) ((float) parseDouble(astring[6]));
                double d6 = (double) ((float) parseDouble(astring[7]));
                int i = 0;

                if (astring.length > 8) {
                    i = parseInt(astring[8], 0);
                }

                boolean flag1 = false;

                if (astring.length > 9 && "force".equals(astring[9])) {
                    flag1 = true;
                }

                EntityPlayerMP entityplayer;

                if (astring.length > 10) {
                    entityplayer = getPlayer(minecraftserver, icommandlistener, astring[10]);
                } else {
                    entityplayer = null;
                }

                int[] aint = new int[enumparticle.getArgumentCount()];

                for (int j = 0; j < aint.length; ++j) {
                    if (astring.length > 11 + j) {
                        try {
                            aint[j] = Integer.parseInt(astring[11 + j]);
                        } catch (NumberFormatException numberformatexception) {
                            throw new CommandException("commands.particle.invalidParam", new Object[] { astring[11 + j]});
                        }
                    }
                }

                World world = icommandlistener.getEntityWorld();

                if (world instanceof WorldServer) {
                    WorldServer worldserver = (WorldServer) world;

                    if (entityplayer == null) {
                        worldserver.spawnParticle(enumparticle, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    } else {
                        worldserver.spawnParticle(entityplayer, enumparticle, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    }

                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.particle.success", new Object[] { s, Integer.valueOf(Math.max(i, 1))});
                }

            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, (Collection) EnumParticleTypes.getParticleNames()) : (astring.length > 1 && astring.length <= 4 ? getTabCompletionCoordinate(astring, 1, blockposition) : (astring.length == 10 ? getListOfStringsMatchingLastWord(astring, new String[] { "normal", "force"}) : (astring.length == 11 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList())));
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 10;
    }
}
