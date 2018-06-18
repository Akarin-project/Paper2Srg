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

    public String getName() {
        return "gamerule";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.gamerule.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        GameRules gamerules = icommandlistener.getEntityWorld().getGameRules(); // CraftBukkit - Use current world
        String s = astring.length > 0 ? astring[0] : "";
        String s1 = astring.length > 1 ? buildString(astring, 1) : "";

        switch (astring.length) {
        case 0:
            icommandlistener.sendMessage(new TextComponentString(joinNiceString((Object[]) gamerules.getRules())));
            break;

        case 1:
            if (!gamerules.hasRule(s)) {
                throw new CommandException("commands.gamerule.norule", new Object[] { s});
            }

            String s2 = gamerules.getString(s);

            icommandlistener.sendMessage((new TextComponentString(s)).appendText(" = ").appendText(s2));
            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
            break;

        default:
            if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1)) {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] { s1});
            }

            gamerules.setOrCreateGameRule(s, s1);
            notifyGameRuleChange(gamerules, s, minecraftserver);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.gamerule.success", new Object[] { s, s1});
        }

    }

    public static void notifyGameRuleChange(GameRules gamerules, String s, MinecraftServer minecraftserver) {
        if ("reducedDebugInfo".equals(s)) {
            int i = gamerules.getBoolean(s) ? 22 : 23;
            Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.connection.sendPacket(new SPacketEntityStatus(entityplayer, (byte) i));
            }
        }

    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return getListOfStringsMatchingLastWord(astring, this.getOverWorldGameRules(minecraftserver).getRules());
        } else {
            if (astring.length == 2) {
                GameRules gamerules = this.getOverWorldGameRules(minecraftserver);

                if (gamerules.areSameType(astring[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                    return getListOfStringsMatchingLastWord(astring, new String[] { "true", "false"});
                }

                if (gamerules.areSameType(astring[0], GameRules.ValueType.FUNCTION)) {
                    return getListOfStringsMatchingLastWord(astring, (Collection) minecraftserver.getFunctionManager().getFunctions().keySet());
                }
            }

            return Collections.emptyList();
        }
    }

    private GameRules getOverWorldGameRules(MinecraftServer minecraftserver) {
        return minecraftserver.getWorld(0).getGameRules();
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
