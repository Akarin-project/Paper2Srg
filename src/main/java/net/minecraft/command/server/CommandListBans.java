package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans extends CommandBase {

    public CommandListBans() {}

    public String func_71517_b() {
        return "banlist";
    }

    public int func_82362_a() {
        return 3;
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return (minecraftserver.func_184103_al().func_72363_f().func_152689_b() || minecraftserver.func_184103_al().func_152608_h().func_152689_b()) && super.func_184882_a(minecraftserver, icommandlistener);
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.banlist.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && "ips".equalsIgnoreCase(astring[0])) {
            icommandlistener.func_145747_a(new TextComponentTranslation("commands.banlist.ips", new Object[] { Integer.valueOf(minecraftserver.func_184103_al().func_72363_f().func_152685_a().length)}));
            icommandlistener.func_145747_a(new TextComponentString(func_71527_a((Object[]) minecraftserver.func_184103_al().func_72363_f().func_152685_a())));
        } else {
            icommandlistener.func_145747_a(new TextComponentTranslation("commands.banlist.players", new Object[] { Integer.valueOf(minecraftserver.func_184103_al().func_152608_h().func_152685_a().length)}));
            icommandlistener.func_145747_a(new TextComponentString(func_71527_a((Object[]) minecraftserver.func_184103_al().func_152608_h().func_152685_a())));
        }

    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "players", "ips"}) : Collections.emptyList();
    }
}
