package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanPlayer extends CommandBase {

    public CommandBanPlayer() {}

    public String func_71517_b() {
        return "ban";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.ban.usage";
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.func_184103_al().func_152608_h().func_152689_b() && super.func_184882_a(minecraftserver, icommandlistener);
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.ban.failed", new Object[] { astring[0]});
            } else {
                String s = null;

                if (astring.length >= 2) {
                    s = func_147178_a(icommandlistener, astring, 1).func_150260_c();
                }

                UserListBansEntry gameprofilebanentry = new UserListBansEntry(gameprofile, (Date) null, icommandlistener.func_70005_c_(), (Date) null, s);

                minecraftserver.func_184103_al().func_152608_h().func_152687_a(gameprofilebanentry);
                EntityPlayerMP entityplayer = minecraftserver.func_184103_al().func_152612_a(astring[0]);

                if (entityplayer != null) {
                    entityplayer.field_71135_a.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.banned", new Object[0]));
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.ban.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }
}
