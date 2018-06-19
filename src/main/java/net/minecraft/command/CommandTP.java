package net.minecraft.command;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CommandTP extends CommandBase {

    public CommandTP() {}

    public String func_71517_b() {
        return "tp";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.tp.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        } else {
            byte b0 = 0;
            Object object;

            if (astring.length != 2 && astring.length != 4 && astring.length != 6) {
                object = func_71521_c(icommandlistener);
            } else {
                object = func_184885_b(minecraftserver, icommandlistener, astring[0]);
                b0 = 1;
            }

            if (astring.length != 1 && astring.length != 2) {
                if (astring.length < b0 + 3) {
                    throw new WrongUsageException("commands.tp.usage", new Object[0]);
                } else if (((Entity) object).field_70170_p != null) {
                    boolean flag = true;
                    int i = b0 + 1;
                    CommandBase.CoordinateArg commandabstract_commandnumber = func_175770_a(((Entity) object).field_70165_t, astring[b0], true);
                    CommandBase.CoordinateArg commandabstract_commandnumber1 = func_175767_a(((Entity) object).field_70163_u, astring[i++], -4096, 4096, false);
                    CommandBase.CoordinateArg commandabstract_commandnumber2 = func_175770_a(((Entity) object).field_70161_v, astring[i++], true);
                    CommandBase.CoordinateArg commandabstract_commandnumber3 = func_175770_a((double) ((Entity) object).field_70177_z, astring.length > i ? astring[i++] : "~", false);
                    CommandBase.CoordinateArg commandabstract_commandnumber4 = func_175770_a((double) ((Entity) object).field_70125_A, astring.length > i ? astring[i] : "~", false);

                    func_189863_a((Entity) object, commandabstract_commandnumber, commandabstract_commandnumber1, commandabstract_commandnumber2, commandabstract_commandnumber3, commandabstract_commandnumber4);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.tp.success.coordinates", new Object[] { ((Entity) object).func_70005_c_(), Double.valueOf(commandabstract_commandnumber.func_179628_a()), Double.valueOf(commandabstract_commandnumber1.func_179628_a()), Double.valueOf(commandabstract_commandnumber2.func_179628_a())});
                }
            } else {
                Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[astring.length - 1]);

                // CraftBukkit Start
                // Use Bukkit teleport method in all cases. It has cross dimensional handling, events
                if (((Entity) object).getBukkitEntity().teleport(entity.getBukkitEntity(), org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND)) {
                    func_152373_a(icommandlistener, (ICommand) this, "commands.tp.success", new Object[] { ((Entity) object).func_70005_c_(), entity.func_70005_c_()});
                    // CraftBukkit End
                }
            }
        }
    }

    private static void func_189863_a(Entity entity, CommandBase.CoordinateArg commandabstract_commandnumber, CommandBase.CoordinateArg commandabstract_commandnumber1, CommandBase.CoordinateArg commandabstract_commandnumber2, CommandBase.CoordinateArg commandabstract_commandnumber3, CommandBase.CoordinateArg commandabstract_commandnumber4) {
        float f;

        if (entity instanceof EntityPlayerMP) {
            EnumSet enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            if (commandabstract_commandnumber.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.X);
            }

            if (commandabstract_commandnumber1.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.Y);
            }

            if (commandabstract_commandnumber2.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.Z);
            }

            if (commandabstract_commandnumber4.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }

            if (commandabstract_commandnumber3.func_179630_c()) {
                enumset.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }

            f = (float) commandabstract_commandnumber3.func_179629_b();
            if (!commandabstract_commandnumber3.func_179630_c()) {
                f = MathHelper.func_76142_g(f);
            }

            float f1 = (float) commandabstract_commandnumber4.func_179629_b();

            if (!commandabstract_commandnumber4.func_179630_c()) {
                f1 = MathHelper.func_76142_g(f1);
            }

            entity.func_184210_p();
            ((EntityPlayerMP) entity).field_71135_a.a(commandabstract_commandnumber.func_179629_b(), commandabstract_commandnumber1.func_179629_b(), commandabstract_commandnumber2.func_179629_b(), f, f1, enumset, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND); // CraftBukkit
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
        return astring.length != 1 && astring.length != 2 ? Collections.emptyList() : func_71530_a(astring, minecraftserver.func_71213_z());
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
