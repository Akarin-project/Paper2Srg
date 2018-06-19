package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetSpawnpoint extends CommandBase {

    public CommandSetSpawnpoint() {}

    public String func_71517_b() {
        return "spawnpoint";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.spawnpoint.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 1 && astring.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = astring.length > 0 ? func_184888_a(minecraftserver, icommandlistener, astring[0]) : func_71521_c(icommandlistener);
            BlockPos blockposition = astring.length > 3 ? func_175757_a(icommandlistener, astring, 1, true) : entityplayer.func_180425_c();

            if (entityplayer.field_70170_p != null) {
                entityplayer.func_180473_a(blockposition, true);
                func_152373_a(icommandlistener, (ICommand) this, "commands.spawnpoint.success", new Object[] { entityplayer.func_70005_c_(), Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
            }

        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockposition) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
