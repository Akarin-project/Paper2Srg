package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandXP extends CommandBase {

    public CommandXP() {}

    public String func_71517_b() {
        return "xp";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.xp.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        } else {
            String s = astring[0];
            boolean flag = s.endsWith("l") || s.endsWith("L");

            if (flag && s.length() > 1) {
                s = s.substring(0, s.length() - 1);
            }

            int i = func_175755_a(s);
            boolean flag1 = i < 0;

            if (flag1) {
                i *= -1;
            }

            EntityPlayerMP entityplayer = astring.length > 1 ? func_184888_a(minecraftserver, icommandlistener, astring[1]) : func_71521_c(icommandlistener);

            if (flag) {
                icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, entityplayer.field_71068_ca);
                if (flag1) {
                    entityplayer.func_82242_a(-i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.xp.success.negative.levels", new Object[] { Integer.valueOf(i), entityplayer.func_70005_c_()});
                } else {
                    entityplayer.func_82242_a(i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.xp.success.levels", new Object[] { Integer.valueOf(i), entityplayer.func_70005_c_()});
                }
            } else {
                icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, entityplayer.field_71067_cb);
                if (flag1) {
                    throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
                }

                entityplayer.func_71023_q(i);
                func_152373_a(icommandlistener, (ICommand) this, "commands.xp.success", new Object[] { Integer.valueOf(i), entityplayer.func_70005_c_()});
            }

        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 2 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 1;
    }
}
