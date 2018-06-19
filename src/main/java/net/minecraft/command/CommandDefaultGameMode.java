package net.minecraft.command;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode extends CommandGameMode {

    public CommandDefaultGameMode() {}

    public String func_71517_b() {
        return "defaultgamemode";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.defaultgamemode.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        } else {
            GameType enumgamemode = this.func_71539_b(icommandlistener, astring[0]);

            this.func_184896_a(enumgamemode, minecraftserver);
            func_152373_a(icommandlistener, (ICommand) this, "commands.defaultgamemode.success", new Object[] { new TextComponentTranslation("gameMode." + enumgamemode.func_77149_b(), new Object[0])});
        }
    }

    protected void func_184896_a(GameType enumgamemode, MinecraftServer minecraftserver) {
        minecraftserver.func_71235_a(enumgamemode);
        if (minecraftserver.func_104056_am()) {
            Iterator iterator = minecraftserver.func_184103_al().func_181057_v().iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.func_71033_a(enumgamemode);
            }
        }

    }
}
