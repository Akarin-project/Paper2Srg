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

    public String func_71517_b() {
        return "teleport";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.teleport.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        } else {
            Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[0]);

            if (entity.field_70170_p != null) {
                boolean flag = true;
                Vec3d vec3d = icommandlistener.func_174791_d();
                byte b0 = 1;
                int i = b0 + 1;
                CommandBase.CoordinateArg commandabstract_commandnumber = func_175770_a(vec3d.field_72450_a, astring[b0], true);
                CommandBase.CoordinateArg commandabstract_commandnumber1 = func_175767_a(vec3d.field_72448_b, astring[i++], -4096, 4096, false);
                CommandBase.CoordinateArg commandabstract_commandnumber2 = func_175770_a(vec3d.field_72449_c, astring[i++], true);
                Entity entity1 = icommandlistener.func_174793_f() == null ? entity : icommandlistener.func_174793_f();
                CommandBase.CoordinateArg commandabstract_commandnumber3 = func_175770_a(astring.length > i ? (double) entity1.field_70177_z : (double) entity.field_70177_z, astring.length > i ? astring[i] : "~", false);

                ++i;
                CommandBase.CoordinateArg commandabstract_commandnumber4 = func_175770_a(astring.length > i ? (double) entity1.field_70125_A : (double) entity.field_70125_A, astring.length > i ? astring[i] : "~", false);

                func_189862_a(entity, commandabstract_commandnumber, commandabstract_commandnumber1, commandabstract_commandnumber2, commandabstract_commandnumber3, commandabstract_commandnumber4);
                func_152373_a(icommandlistener, (ICommand) this, "commands.teleport.success.coordinates", new Object[] { entity.func_70005_c_(), Double.valueOf(commandabstract_commandnumber.func_179628_a()), Double.valueOf(commandabstract_commandnumber1.func_179628_a()), Double.valueOf(commandabstract_commandnumber2.func_179628_a())});
            }
        }
    }

    private static void func_189862_a(Entity entity, CommandBase.CoordinateArg commandabstract_commandnumber, CommandBase.CoordinateArg commandabstract_commandnumber1, CommandBase.CoordinateArg commandabstract_commandnumber2, CommandBase.CoordinateArg commandabstract_commandnumber3, CommandBase.CoordinateArg commandabstract_commandnumber4) {
        float f;

        if (entity instanceof EntityPlayerMP) {
            EnumSet enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            f = (float) commandabstract_commandnumber3.func_179629_b();
            if (commandabstract_commandnumber3.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            } else {
                f = MathHelper.func_76142_g(f);
            }

            float f1 = (float) commandabstract_commandnumber4.func_179629_b();

            if (commandabstract_commandnumber4.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            } else {
                f1 = MathHelper.func_76142_g(f1);
            }

            entity.func_184210_p();
            ((EntityPlayerMP) entity).field_71135_a.a(commandabstract_commandnumber.func_179628_a(), commandabstract_commandnumber1.func_179628_a(), commandabstract_commandnumber2.func_179628_a(), f, f1, enumset, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND); // CraftBukkit
            entity.func_70034_d(f);
        } else {
            float f2 = (float) MathHelper.func_76138_g(commandabstract_commandnumber3.func_179628_a());

            f = (float) MathHelper.func_76138_g(commandabstract_commandnumber4.func_179628_a());
            f = MathHelper.func_76131_a(f, -90.0F, 90.0F);
            entity.func_70012_b(commandabstract_commandnumber.func_179628_a(), commandabstract_commandnumber1.func_179628_a(), commandabstract_commandnumber2.func_179628_a(), f2, f);
            entity.func_70034_d(f2);
        }

        if (!(entity instanceof EntityLivingBase) || !((EntityLivingBase) entity).func_184613_cA()) {
            entity.field_70181_x = 0.0D;
            entity.field_70122_E = true;
        }

    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockposition) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
