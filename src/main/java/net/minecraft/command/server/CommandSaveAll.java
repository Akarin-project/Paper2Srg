package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase {

    public CommandSaveAll() {}

    public String func_71517_b() {
        return "save-all";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.save.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        icommandlistener.func_145747_a(new TextComponentTranslation("commands.save.start", new Object[0]));
        if (minecraftserver.func_184103_al() != null) {
            minecraftserver.func_184103_al().func_72389_g();
        }

        try {
            int i;
            WorldServer worldserver;
            boolean flag;

            for (i = 0; i < minecraftserver.field_71305_c.length; ++i) {
                if (minecraftserver.field_71305_c[i] != null) {
                    worldserver = minecraftserver.field_71305_c[i];
                    flag = worldserver.field_73058_d;
                    worldserver.field_73058_d = false;
                    worldserver.func_73044_a(true, (IProgressUpdate) null);
                    worldserver.field_73058_d = flag;
                }
            }

            if (astring.length > 0 && "flush".equals(astring[0])) {
                icommandlistener.func_145747_a(new TextComponentTranslation("commands.save.flushStart", new Object[0]));

                for (i = 0; i < minecraftserver.field_71305_c.length; ++i) {
                    if (minecraftserver.field_71305_c[i] != null) {
                        worldserver = minecraftserver.field_71305_c[i];
                        flag = worldserver.field_73058_d;
                        worldserver.field_73058_d = false;
                        worldserver.func_104140_m();
                        worldserver.field_73058_d = flag;
                    }
                }

                icommandlistener.func_145747_a(new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        } catch (MinecraftException exceptionworldconflict) {
            func_152373_a(icommandlistener, (ICommand) this, "commands.save.failed", new Object[] { exceptionworldconflict.getMessage()});
            return;
        }

        func_152373_a(icommandlistener, (ICommand) this, "commands.save.success", new Object[0]);
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "flush"}) : Collections.emptyList();
    }
}
