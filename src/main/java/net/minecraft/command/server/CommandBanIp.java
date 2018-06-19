package net.minecraft.command.server;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListIPBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanIp extends CommandBase {

    public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public CommandBanIp() {}

    public String func_71517_b() {
        return "ban-ip";
    }

    public int func_82362_a() {
        return 3;
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.func_184103_al().func_72363_f().func_152689_b() && super.func_184882_a(minecraftserver, icommandlistener);
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.banip.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring[0].length() > 1) {
            ITextComponent ichatbasecomponent = astring.length >= 2 ? func_147178_a(icommandlistener, astring, 1) : null;
            Matcher matcher = CommandBanIp.field_147211_a.matcher(astring[0]);

            if (matcher.matches()) {
                this.func_184892_a(minecraftserver, icommandlistener, astring[0], ichatbasecomponent == null ? null : ichatbasecomponent.func_150260_c());
            } else {
                EntityPlayerMP entityplayer = minecraftserver.func_184103_al().func_152612_a(astring[0]);

                if (entityplayer == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid");
                }

                this.func_184892_a(minecraftserver, icommandlistener, entityplayer.func_71114_r(), ichatbasecomponent == null ? null : ichatbasecomponent.func_150260_c());
            }

        } else {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }

    protected void func_184892_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s, @Nullable String s1) {
        UserListIPBansEntry ipbanentry = new UserListIPBansEntry(s, (Date) null, icommandlistener.func_70005_c_(), (Date) null, s1);

        minecraftserver.func_184103_al().func_72363_f().func_152687_a(ipbanentry);
        List list = minecraftserver.func_184103_al().func_72382_j(s);
        String[] astring = new String[list.size()];
        int i = 0;

        EntityPlayerMP entityplayer;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); astring[i++] = entityplayer.func_70005_c_()) {
            entityplayer = (EntityPlayerMP) iterator.next();
            entityplayer.field_71135_a.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.ip_banned", new Object[0]));
        }

        if (list.isEmpty()) {
            func_152373_a(icommandlistener, (ICommand) this, "commands.banip.success", new Object[] { s});
        } else {
            func_152373_a(icommandlistener, (ICommand) this, "commands.banip.success.players", new Object[] { s, func_71527_a((Object[]) astring)});
        }

    }
}
