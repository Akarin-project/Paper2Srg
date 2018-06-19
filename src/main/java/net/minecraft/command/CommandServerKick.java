package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick extends CommandBase {

    public CommandServerKick() {}

    public String func_71517_b() {
        return "kick";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.kick.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0 && astring[0].length() > 1) {
            EntityPlayerMP entityplayer = minecraftserver.func_184103_al().func_152612_a(astring[0]);

            if (entityplayer == null) {
                throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { astring[0]});
            } else {
                if (astring.length >= 2) {
                    ITextComponent ichatbasecomponent = func_147178_a(icommandlistener, astring, 1);

                    entityplayer.field_71135_a.func_194028_b(ichatbasecomponent);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.kick.success.reason", new Object[] { entityplayer.func_70005_c_(), ichatbasecomponent.func_150260_c()});
                } else {
                    entityplayer.field_71135_a.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
                    func_152373_a(icommandlistener, (ICommand) this, "commands.kick.success", new Object[] { entityplayer.func_70005_c_()});
                }

            }
        } else {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList();
    }
}
