package net.minecraft.command;

import java.util.Iterator;

import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ServerCommandManager extends CommandHandler implements ICommandListener {

    private final MinecraftServer server;

    public ServerCommandManager(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
        this.registerCommand((ICommand) (new CommandTime()));
        this.registerCommand((ICommand) (new CommandGameMode()));
        this.registerCommand((ICommand) (new CommandDifficulty()));
        this.registerCommand((ICommand) (new CommandDefaultGameMode()));
        this.registerCommand((ICommand) (new CommandKill()));
        this.registerCommand((ICommand) (new CommandToggleDownfall()));
        this.registerCommand((ICommand) (new CommandWeather()));
        this.registerCommand((ICommand) (new CommandXP()));
        this.registerCommand((ICommand) (new CommandTP()));
        this.registerCommand((ICommand) (new CommandTeleport()));
        this.registerCommand((ICommand) (new CommandGive()));
        this.registerCommand((ICommand) (new CommandReplaceItem()));
        this.registerCommand((ICommand) (new CommandStats()));
        this.registerCommand((ICommand) (new CommandEffect()));
        this.registerCommand((ICommand) (new CommandEnchant()));
        this.registerCommand((ICommand) (new CommandParticle()));
        this.registerCommand((ICommand) (new CommandEmote()));
        this.registerCommand((ICommand) (new CommandShowSeed()));
        this.registerCommand((ICommand) (new CommandHelp()));
        this.registerCommand((ICommand) (new CommandDebug()));
        this.registerCommand((ICommand) (new CommandMessage()));
        this.registerCommand((ICommand) (new CommandBroadcast()));
        this.registerCommand((ICommand) (new CommandSetSpawnpoint()));
        this.registerCommand((ICommand) (new CommandSetDefaultSpawnpoint()));
        this.registerCommand((ICommand) (new CommandGameRule()));
        this.registerCommand((ICommand) (new CommandClearInventory()));
        this.registerCommand((ICommand) (new CommandTestFor()));
        this.registerCommand((ICommand) (new CommandSpreadPlayers()));
        this.registerCommand((ICommand) (new CommandPlaySound()));
        this.registerCommand((ICommand) (new CommandScoreboard()));
        this.registerCommand((ICommand) (new CommandExecuteAt()));
        this.registerCommand((ICommand) (new CommandTrigger()));
        this.registerCommand((ICommand) (new AdvancementCommand()));
        this.registerCommand((ICommand) (new RecipeCommand()));
        this.registerCommand((ICommand) (new CommandSummon()));
        this.registerCommand((ICommand) (new CommandSetBlock()));
        this.registerCommand((ICommand) (new CommandFill()));
        this.registerCommand((ICommand) (new CommandClone()));
        this.registerCommand((ICommand) (new CommandCompare()));
        this.registerCommand((ICommand) (new CommandBlockData()));
        this.registerCommand((ICommand) (new CommandTestForBlock()));
        this.registerCommand((ICommand) (new CommandMessageRaw()));
        this.registerCommand((ICommand) (new CommandWorldBorder()));
        this.registerCommand((ICommand) (new CommandTitle()));
        this.registerCommand((ICommand) (new CommandEntityData()));
        this.registerCommand((ICommand) (new CommandStopSound()));
        this.registerCommand((ICommand) (new CommandLocate()));
        this.registerCommand((ICommand) (new CommandReload()));
        this.registerCommand((ICommand) (new CommandFunction()));
        if (minecraftserver.isDedicatedServer()) {
            this.registerCommand((ICommand) (new CommandOp()));
            this.registerCommand((ICommand) (new CommandDeOp()));
            this.registerCommand((ICommand) (new CommandStop()));
            this.registerCommand((ICommand) (new CommandSaveAll()));
            this.registerCommand((ICommand) (new CommandSaveOff()));
            this.registerCommand((ICommand) (new CommandSaveOn()));
            this.registerCommand((ICommand) (new CommandBanIp()));
            this.registerCommand((ICommand) (new CommandPardonIp()));
            this.registerCommand((ICommand) (new CommandBanPlayer()));
            this.registerCommand((ICommand) (new CommandListBans()));
            this.registerCommand((ICommand) (new CommandPardonPlayer()));
            this.registerCommand((ICommand) (new CommandServerKick()));
            this.registerCommand((ICommand) (new CommandListPlayers()));
            this.registerCommand((ICommand) (new CommandWhitelist()));
            this.registerCommand((ICommand) (new CommandSetPlayerTimeout()));
        } else {
            this.registerCommand((ICommand) (new CommandPublishLocalServer()));
        }

        CommandBase.setCommandListener((ICommandListener) this);
    }

    public void notifyListener(ICommandSender icommandlistener, ICommand icommand, int i, String s, Object... aobject) {
        boolean flag = true;
        MinecraftServer minecraftserver = this.server;

        if (!icommandlistener.sendCommandFeedback()) {
            flag = false;
        }

        TextComponentTranslation chatmessage = new TextComponentTranslation("chat.type.admin", new Object[] { icommandlistener.getName(), new TextComponentTranslation(s, aobject)});

        chatmessage.getStyle().setColor(TextFormatting.GRAY);
        chatmessage.getStyle().setItalic(Boolean.valueOf(true));
        if (flag) {
            Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (entityhuman != icommandlistener && minecraftserver.getPlayerList().canSendCommands(entityhuman.getGameProfile()) && icommand.checkPermission(this.server, icommandlistener)) {
                    boolean flag1 = icommandlistener instanceof MinecraftServer && this.server.shouldBroadcastConsoleToOps();
                    boolean flag2 = icommandlistener instanceof RConConsoleSource && this.server.shouldBroadcastRconToOps();

                    if (flag1 || flag2 || !(icommandlistener instanceof RConConsoleSource) && !(icommandlistener instanceof MinecraftServer)) {
                        entityhuman.sendMessage(chatmessage);
                    }
                }
            }
        }

        if (icommandlistener != minecraftserver && minecraftserver.worlds[0].getGameRules().getBoolean("logAdminCommands") && !org.spigotmc.SpigotConfig.silentCommandBlocks) { // Spigot
            minecraftserver.sendMessage(chatmessage);
        }

        boolean flag3 = minecraftserver.worlds[0].getGameRules().getBoolean("sendCommandFeedback");

        if (icommandlistener instanceof CommandBlockBaseLogic) {
            flag3 = ((CommandBlockBaseLogic) icommandlistener).shouldTrackOutput();
        }

        if ((i & 1) != 1 && flag3 || icommandlistener instanceof MinecraftServer) {
            icommandlistener.sendMessage(new TextComponentTranslation(s, aobject));
        }

    }

    protected MinecraftServer getServer() {
        return this.server;
    }
}
