package net.minecraft.command.server;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CommandTeleport extends CommandBase {

    public CommandTeleport() {}

    public String getName() {
        return "teleport";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.teleport.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        } else {
            Entity entity = getEntity(minecraftserver, icommandlistener, astring[0]);

            if (entity.world != null) {
                boolean flag = true;
                Vec3d vec3d = icommandlistener.getPositionVector();
                byte b0 = 1;
                int i = b0 + 1;
                CommandBase.CoordinateArg commandabstract_commandnumber = parseCoordinate(vec3d.x, astring[b0], true);
                CommandBase.CoordinateArg commandabstract_commandnumber1 = parseCoordinate(vec3d.y, astring[i++], -4096, 4096, false);
                CommandBase.CoordinateArg commandabstract_commandnumber2 = parseCoordinate(vec3d.z, astring[i++], true);
                Entity entity1 = icommandlistener.getCommandSenderEntity() == null ? entity : icommandlistener.getCommandSenderEntity();
                CommandBase.CoordinateArg commandabstract_commandnumber3 = parseCoordinate(astring.length > i ? (double) entity1.rotationYaw : (double) entity.rotationYaw, astring.length > i ? astring[i] : "~", false);

                ++i;
                CommandBase.CoordinateArg commandabstract_commandnumber4 = parseCoordinate(astring.length > i ? (double) entity1.rotationPitch : (double) entity.rotationPitch, astring.length > i ? astring[i] : "~", false);

                doTeleport(entity, commandabstract_commandnumber, commandabstract_commandnumber1, commandabstract_commandnumber2, commandabstract_commandnumber3, commandabstract_commandnumber4);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.teleport.success.coordinates", new Object[] { entity.getName(), Double.valueOf(commandabstract_commandnumber.getResult()), Double.valueOf(commandabstract_commandnumber1.getResult()), Double.valueOf(commandabstract_commandnumber2.getResult())});
            }
        }
    }

    private static void doTeleport(Entity entity, CommandBase.CoordinateArg commandabstract_commandnumber, CommandBase.CoordinateArg commandabstract_commandnumber1, CommandBase.CoordinateArg commandabstract_commandnumber2, CommandBase.CoordinateArg commandabstract_commandnumber3, CommandBase.CoordinateArg commandabstract_commandnumber4) {
        float f;

        if (entity instanceof EntityPlayerMP) {
            EnumSet enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            f = (float) commandabstract_commandnumber3.getAmount();
            if (commandabstract_commandnumber3.isRelative()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            } else {
                f = MathHelper.wrapDegrees(f);
            }

            float f1 = (float) commandabstract_commandnumber4.getAmount();

            if (commandabstract_commandnumber4.isRelative()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            } else {
                f1 = MathHelper.wrapDegrees(f1);
            }

            entity.dismountRidingEntity();
            ((EntityPlayerMP) entity).connection.a(commandabstract_commandnumber.getResult(), commandabstract_commandnumber1.getResult(), commandabstract_commandnumber2.getResult(), f, f1, enumset, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND); // CraftBukkit
            entity.setRotationYawHead(f);
        } else {
            float f2 = (float) MathHelper.wrapDegrees(commandabstract_commandnumber3.getResult());

            f = (float) MathHelper.wrapDegrees(commandabstract_commandnumber4.getResult());
            f = MathHelper.clamp(f, -90.0F, 90.0F);
            entity.setLocationAndAngles(commandabstract_commandnumber.getResult(), commandabstract_commandnumber1.getResult(), commandabstract_commandnumber2.getResult(), f2, f);
            entity.setRotationYawHead(f2);
        }

        if (!(entity instanceof EntityLivingBase) || !((EntityLivingBase) entity).isElytraFlying()) {
            entity.motionY = 0.0D;
            entity.onGround = true;
        }

    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length > 1 && astring.length <= 4 ? getTabCompletionCoordinate(astring, 1, blockposition) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
