package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase {

    public CommandGameRule() {}

    public String func_71517_b() {
        return "gamerule";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.gamerule.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        GameRules gamerules = icommandlistener.func_130014_f_().func_82736_K(); // CraftBukkit - Use current world
        String s = astring.length > 0 ? astring[0] : "";
        String s1 = astring.length > 1 ? func_180529_a(astring, 1) : "";

        switch (astring.length) {
        case 0:
            icommandlistener.func_145747_a(new TextComponentString(func_71527_a((Object[]) gamerules.func_82763_b())));
            break;

        case 1:
            if (!gamerules.func_82765_e(s)) {
                throw new CommandException("commands.gamerule.norule", new Object[] { s});
            }

            String s2 = gamerules.func_82767_a(s);

            icommandlistener.func_145747_a((new TextComponentString(s)).func_150258_a(" = ").func_150258_a(s2));
            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, gamerules.func_180263_c(s));
            break;

        default:
            if (gamerules.func_180264_a(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1)) {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] { s1});
            }

            gamerules.func_82764_b(s, s1);
            func_184898_a(gamerules, s, minecraftserver);
            func_152373_a(icommandlistener, (ICommand) this, "commands.gamerule.success", new Object[] { s, s1});
        }

    }

    public static void func_184898_a(GameRules gamerules, String s, MinecraftServer minecraftserver) {
        if ("reducedDebugInfo".equals(s)) {
            int i = gamerules.func_82766_b(s) ? 22 : 23;
            Iterator iterator = minecraftserver.func_184103_al().func_181057_v().iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.field_71135_a.func_147359_a(new SPacketEntityStatus(entityplayer, (byte) i));
            }
        }

    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return func_71530_a(astring, this.func_184897_a(minecraftserver).func_82763_b());
        } else {
            if (astring.length == 2) {
                GameRules gamerules = this.func_184897_a(minecraftserver);

                if (gamerules.func_180264_a(astring[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                    return func_71530_a(astring, new String[] { "true", "false"});
                }

                if (gamerules.func_180264_a(astring[0], GameRules.ValueType.FUNCTION)) {
                    return func_175762_a(astring, (Collection) minecraftserver.func_193030_aL().func_193066_d().keySet());
                }
            }

            return Collections.emptyList();
        }
    }

    private GameRules func_184897_a(MinecraftServer minecraftserver) {
        return minecraftserver.func_71218_a(0).func_82736_K();
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
