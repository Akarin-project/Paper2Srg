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

    public String func_71517_b() {
        return "particle";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.particle.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        } else {
            boolean flag = false;
            EnumParticleTypes enumparticle = EnumParticleTypes.func_186831_a(astring[0]);

            if (enumparticle == null) {
                throw new CommandException("commands.particle.notFound", new Object[] { astring[0]});
            } else {
                String s = astring[0];
                Vec3d vec3d = icommandlistener.func_174791_d();
                double d0 = (double) ((float) func_175761_b(vec3d.field_72450_a, astring[1], true));
                double d1 = (double) ((float) func_175761_b(vec3d.field_72448_b, astring[2], true));
                double d2 = (double) ((float) func_175761_b(vec3d.field_72449_c, astring[3], true));
                double d3 = (double) ((float) func_175765_c(astring[4]));
                double d4 = (double) ((float) func_175765_c(astring[5]));
                double d5 = (double) ((float) func_175765_c(astring[6]));
                double d6 = (double) ((float) func_175765_c(astring[7]));
                int i = 0;

                if (astring.length > 8) {
                    i = func_180528_a(astring[8], 0);
                }

                boolean flag1 = false;

                if (astring.length > 9 && "force".equals(astring[9])) {
                    flag1 = true;
                }

                EntityPlayerMP entityplayer;

                if (astring.length > 10) {
                    entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[10]);
                } else {
                    entityplayer = null;
                }

                int[] aint = new int[enumparticle.func_179345_d()];

                for (int j = 0; j < aint.length; ++j) {
                    if (astring.length > 11 + j) {
                        try {
                            aint[j] = Integer.parseInt(astring[11 + j]);
                        } catch (NumberFormatException numberformatexception) {
                            throw new CommandException("commands.particle.invalidParam", new Object[] { astring[11 + j]});
                        }
                    }
                }

                World world = icommandlistener.func_130014_f_();

                if (world instanceof WorldServer) {
                    WorldServer worldserver = (WorldServer) world;

                    if (entityplayer == null) {
                        worldserver.func_180505_a(enumparticle, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    } else {
                        worldserver.func_184161_a(entityplayer, enumparticle, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    }

                    func_152373_a(icommandlistener, (ICommand) this, "commands.particle.success", new Object[] { s, Integer.valueOf(Math.max(i, 1))});
                }

            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_175762_a(astring, (Collection) EnumParticleTypes.func_186832_a()) : (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockposition) : (astring.length == 10 ? func_71530_a(astring, new String[] { "normal", "force"}) : (astring.length == 11 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList())));
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 10;
    }
}
