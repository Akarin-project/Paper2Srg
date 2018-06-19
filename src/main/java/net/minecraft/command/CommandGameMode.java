package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

public class CommandGameMode extends CommandBase {

    public CommandGameMode() {}

    public String func_71517_b() {
        return "gamemode";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.gamemode.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        } else {
            GameType enumgamemode = this.func_71539_b(icommandlistener, astring[0]);
            EntityPlayerMP entityplayer = astring.length >= 2 ? func_184888_a(minecraftserver, icommandlistener, astring[1]) : func_71521_c(icommandlistener);

            entityplayer.func_71033_a(enumgamemode);
            // CraftBukkit start - handle event cancelling the change
            if (entityplayer.field_71134_c.func_73081_b() != enumgamemode) {
                icommandlistener.func_145747_a(new TextComponentString("Failed to set the gamemode of '" + entityplayer.func_70005_c_() + "'"));
                return;
            }
            // CraftBukkit end
            TextComponentTranslation chatmessage = new TextComponentTranslation("gameMode." + enumgamemode.func_77149_b(), new Object[0]);

            if (icommandlistener.func_130014_f_().func_82736_K().func_82766_b("sendCommandFeedback")) {
                entityplayer.func_145747_a(new TextComponentTranslation("gameMode.changed", new Object[] { chatmessage}));
            }

            if (entityplayer == icommandlistener) {
                func_152374_a(icommandlistener, this, 1, "commands.gamemode.success.self", new Object[] { chatmessage});
            } else {
                func_152374_a(icommandlistener, this, 1, "commands.gamemode.success.other", new Object[] { entityplayer.func_70005_c_(), chatmessage});
            }

        }
    }

    protected GameType func_71539_b(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        GameType enumgamemode = GameType.func_185328_a(s, GameType.NOT_SET);

        return enumgamemode == GameType.NOT_SET ? WorldSettings.func_77161_a(func_175764_a(s, 0, GameType.values().length - 2)) : enumgamemode;
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "survival", "creative", "adventure", "spectator"}) : (astring.length == 2 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.<String>emptyList()); // CraftBukkit - decompile error
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 1;
    }

    // CraftBukkit start - fix decompiler error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
