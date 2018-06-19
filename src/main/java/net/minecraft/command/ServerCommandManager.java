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

    private final MinecraftServer field_184880_a;

    public ServerCommandManager(MinecraftServer minecraftserver) {
        this.field_184880_a = minecraftserver;
        this.func_71560_a((ICommand) (new CommandTime()));
        this.func_71560_a((ICommand) (new CommandGameMode()));
        this.func_71560_a((ICommand) (new CommandDifficulty()));
        this.func_71560_a((ICommand) (new CommandDefaultGameMode()));
        this.func_71560_a((ICommand) (new CommandKill()));
        this.func_71560_a((ICommand) (new CommandToggleDownfall()));
        this.func_71560_a((ICommand) (new CommandWeather()));
        this.func_71560_a((ICommand) (new CommandXP()));
        this.func_71560_a((ICommand) (new CommandTP()));
        this.func_71560_a((ICommand) (new CommandTeleport()));
        this.func_71560_a((ICommand) (new CommandGive()));
        this.func_71560_a((ICommand) (new CommandReplaceItem()));
        this.func_71560_a((ICommand) (new CommandStats()));
        this.func_71560_a((ICommand) (new CommandEffect()));
        this.func_71560_a((ICommand) (new CommandEnchant()));
        this.func_71560_a((ICommand) (new CommandParticle()));
        this.func_71560_a((ICommand) (new CommandEmote()));
        this.func_71560_a((ICommand) (new CommandShowSeed()));
        this.func_71560_a((ICommand) (new CommandHelp()));
        this.func_71560_a((ICommand) (new CommandDebug()));
        this.func_71560_a((ICommand) (new CommandMessage()));
        this.func_71560_a((ICommand) (new CommandBroadcast()));
        this.func_71560_a((ICommand) (new CommandSetSpawnpoint()));
        this.func_71560_a((ICommand) (new CommandSetDefaultSpawnpoint()));
        this.func_71560_a((ICommand) (new CommandGameRule()));
        this.func_71560_a((ICommand) (new CommandClearInventory()));
        this.func_71560_a((ICommand) (new CommandTestFor()));
        this.func_71560_a((ICommand) (new CommandSpreadPlayers()));
        this.func_71560_a((ICommand) (new CommandPlaySound()));
        this.func_71560_a((ICommand) (new CommandScoreboard()));
        this.func_71560_a((ICommand) (new CommandExecuteAt()));
        this.func_71560_a((ICommand) (new CommandTrigger()));
        this.func_71560_a((ICommand) (new AdvancementCommand()));
        this.func_71560_a((ICommand) (new RecipeCommand()));
        this.func_71560_a((ICommand) (new CommandSummon()));
        this.func_71560_a((ICommand) (new CommandSetBlock()));
        this.func_71560_a((ICommand) (new CommandFill()));
        this.func_71560_a((ICommand) (new CommandClone()));
        this.func_71560_a((ICommand) (new CommandCompare()));
        this.func_71560_a((ICommand) (new CommandBlockData()));
        this.func_71560_a((ICommand) (new CommandTestForBlock()));
        this.func_71560_a((ICommand) (new CommandMessageRaw()));
        this.func_71560_a((ICommand) (new CommandWorldBorder()));
        this.func_71560_a((ICommand) (new CommandTitle()));
        this.func_71560_a((ICommand) (new CommandEntityData()));
        this.func_71560_a((ICommand) (new CommandStopSound()));
        this.func_71560_a((ICommand) (new CommandLocate()));
        this.func_71560_a((ICommand) (new CommandReload()));
        this.func_71560_a((ICommand) (new CommandFunction()));
        if (minecraftserver.func_71262_S()) {
            this.func_71560_a((ICommand) (new CommandOp()));
            this.func_71560_a((ICommand) (new CommandDeOp()));
            this.func_71560_a((ICommand) (new CommandStop()));
            this.func_71560_a((ICommand) (new CommandSaveAll()));
            this.func_71560_a((ICommand) (new CommandSaveOff()));
            this.func_71560_a((ICommand) (new CommandSaveOn()));
            this.func_71560_a((ICommand) (new CommandBanIp()));
            this.func_71560_a((ICommand) (new CommandPardonIp()));
            this.func_71560_a((ICommand) (new CommandBanPlayer()));
            this.func_71560_a((ICommand) (new CommandListBans()));
            this.func_71560_a((ICommand) (new CommandPardonPlayer()));
            this.func_71560_a((ICommand) (new CommandServerKick()));
            this.func_71560_a((ICommand) (new CommandListPlayers()));
            this.func_71560_a((ICommand) (new CommandWhitelist()));
            this.func_71560_a((ICommand) (new CommandSetPlayerTimeout()));
        } else {
            this.func_71560_a((ICommand) (new CommandPublishLocalServer()));
        }

        CommandBase.func_71529_a((ICommandListener) this);
    }

    public void func_152372_a(ICommandSender icommandlistener, ICommand icommand, int i, String s, Object... aobject) {
        boolean flag = true;
        MinecraftServer minecraftserver = this.field_184880_a;

        if (!icommandlistener.func_174792_t_()) {
            flag = false;
        }

        TextComponentTranslation chatmessage = new TextComponentTranslation("chat.type.admin", new Object[] { icommandlistener.func_70005_c_(), new TextComponentTranslation(s, aobject)});

        chatmessage.func_150256_b().func_150238_a(TextFormatting.GRAY);
        chatmessage.func_150256_b().func_150217_b(Boolean.valueOf(true));
        if (flag) {
            Iterator iterator = minecraftserver.func_184103_al().func_181057_v().iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (entityhuman != icommandlistener && minecraftserver.func_184103_al().func_152596_g(entityhuman.func_146103_bH()) && icommand.func_184882_a(this.field_184880_a, icommandlistener)) {
                    boolean flag1 = icommandlistener instanceof MinecraftServer && this.field_184880_a.func_183002_r();
                    boolean flag2 = icommandlistener instanceof RConConsoleSource && this.field_184880_a.func_181034_q();

                    if (flag1 || flag2 || !(icommandlistener instanceof RConConsoleSource) && !(icommandlistener instanceof MinecraftServer)) {
                        entityhuman.func_145747_a(chatmessage);
                    }
                }
            }
        }

        if (icommandlistener != minecraftserver && minecraftserver.field_71305_c[0].func_82736_K().func_82766_b("logAdminCommands") && !org.spigotmc.SpigotConfig.silentCommandBlocks) { // Spigot
            minecraftserver.func_145747_a(chatmessage);
        }

        boolean flag3 = minecraftserver.field_71305_c[0].func_82736_K().func_82766_b("sendCommandFeedback");

        if (icommandlistener instanceof CommandBlockBaseLogic) {
            flag3 = ((CommandBlockBaseLogic) icommandlistener).func_175571_m();
        }

        if ((i & 1) != 1 && flag3 || icommandlistener instanceof MinecraftServer) {
            icommandlistener.func_145747_a(new TextComponentTranslation(s, aobject));
        }

    }

    protected MinecraftServer func_184879_a() {
        return this.field_184880_a;
    }
}
