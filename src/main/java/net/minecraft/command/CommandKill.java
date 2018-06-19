package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKill extends CommandBase {

    public CommandKill() {}

    public String func_71517_b() {
        return "kill";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.kill.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 0) {
            EntityPlayerMP entityplayer = func_71521_c(icommandlistener);

            entityplayer.func_174812_G();
            func_152373_a(icommandlistener, (ICommand) this, "commands.kill.successful", new Object[] { entityplayer.func_145748_c_()});
        } else {
            Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[0]);

            entity.func_174812_G();
            func_152373_a(icommandlistener, (ICommand) this, "commands.kill.successful", new Object[] { entity.func_145748_c_()});
        }
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }
}
