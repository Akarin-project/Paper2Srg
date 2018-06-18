package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandWhitelist extends CommandBase {

    public CommandWhitelist() {}

    public String getName() {
        return "whitelist";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.whitelist.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        } else {
            if ("on".equals(astring[0])) {
                minecraftserver.getPlayerList().setWhiteListEnabled(true);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.whitelist.enabled", new Object[0]);
            } else if ("off".equals(astring[0])) {
                minecraftserver.getPlayerList().setWhiteListEnabled(false);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.whitelist.disabled", new Object[0]);
            } else if ("list".equals(astring[0])) {
                icommandlistener.sendMessage(new TextComponentTranslation("commands.whitelist.list", new Object[] { Integer.valueOf(minecraftserver.getPlayerList().getWhitelistedPlayerNames().length), Integer.valueOf(minecraftserver.getPlayerList().getAvailablePlayerDat().length)}));
                String[] astring1 = minecraftserver.getPlayerList().getWhitelistedPlayerNames();

                icommandlistener.sendMessage(new TextComponentString(joinNiceString((Object[]) astring1)));
            } else {
                GameProfile gameprofile;

                if ("add".equals(astring[0])) {
                    if (astring.length < 2) {
                        throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
                    }

                    // Paper start - Handle offline mode as well
                    /*
                    gameprofile = minecraftserver.getUserCache().getProfile(astring[1]);
                    if (gameprofile == null) {
                        throw new CommandException("commands.whitelist.add.failed", new Object[] { astring[1]});
                    }

                    minecraftserver.getPlayerList().addWhitelist(gameprofile);
                    */
                    this.whitelist(minecraftserver, astring[1], true);
                    // Paper end
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.whitelist.add.success", new Object[] { astring[1]});
                } else if ("remove".equals(astring[0])) {
                    if (astring.length < 2) {
                        throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
                    }

                    // Paper start - Handle offline mode as well
                    /*
                    gameprofile = minecraftserver.getPlayerList().getWhitelist().a(astring[1]);
                    if (gameprofile == null) {
                        throw new CommandException("commands.whitelist.remove.failed", new Object[] { astring[1]});
                    }

                    minecraftserver.getPlayerList().removeWhitelist(gameprofile);

                    */
                    this.whitelist(minecraftserver, astring[1], false);
                    // Paper end
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.whitelist.remove.success", new Object[] { astring[1]});
                } else if ("reload".equals(astring[0])) {
                    minecraftserver.getPlayerList().reloadWhitelist();
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.whitelist.reloaded", new Object[0]);
                }
            }

        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return getListOfStringsMatchingLastWord(astring, new String[] { "on", "off", "list", "add", "remove", "reload"});
        } else {
            if (astring.length == 2) {
                if ("remove".equals(astring[0])) {
                    return getListOfStringsMatchingLastWord(astring, minecraftserver.getPlayerList().getWhitelistedPlayerNames());
                }

                if ("add".equals(astring[0])) {
                    return getListOfStringsMatchingLastWord(astring, minecraftserver.getPlayerProfileCache().getUsernames());
                }
            }

            return Collections.emptyList();
        }
    }

    // Paper start
    /**
     * Adds or removes a player from the game whitelist
     *
     * @param mcserver running instance of MinecraftServer
     * @param playerName the player we're going to be whitelisting
     * @param add whether we're adding or removing from the whitelist
     */
    private void whitelist(MinecraftServer mcserver, String playerName, boolean add) throws CommandException {
        if (mcserver.isServerInOnlineMode()) {
            // The reason we essentially copy/pasta NMS code here is because the NMS online-only version
            // is capable of providing feedback to the person running the command based on whether or
            // not the player is a real online-mode account
            GameProfile gameprofile = mcserver.getPlayerProfileCache().getGameProfileForUsername(playerName);
            if (gameprofile == null) {
                if (add) {
                    throw new CommandException("commands.whitelist.add.failed", new Object[] { playerName});
                } else {
                    throw new CommandException("commands.whitelist.remove.failed", new Object[] { playerName});
                }
            }

            if (add) {
                mcserver.getPlayerList().addWhitelistedPlayer(gameprofile);
            } else {
                mcserver.getPlayerList().removePlayerFromWhitelist(gameprofile);
            }
        } else {
            // versus our offline version, which will always report success all of the time
            org.bukkit.OfflinePlayer offlinePlayer = org.bukkit.Bukkit.getOfflinePlayer(playerName);
            if (add) {
                offlinePlayer.setWhitelisted(true);
            } else {
                offlinePlayer.setWhitelisted(false);
            }
        }
    }
    // Paper end
}
