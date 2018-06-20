package org.bukkit.craftbukkit.command;

import java.util.Iterator;
import java.util.List;


import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

public final class VanillaCommandWrapper extends BukkitCommand {
    protected final CommandBase vanillaCommand;

    public VanillaCommandWrapper(CommandBase vanillaCommand, String usage) {
        super(vanillaCommand.func_71517_b(), "A Mojang provided command.", usage, vanillaCommand.func_71514_a());
        this.vanillaCommand = vanillaCommand;
        this.setPermission("minecraft.command." + vanillaCommand.func_71517_b());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        ICommandSender icommandlistener = getListener(sender);
        try {
            dispatchVanillaCommand(sender, icommandlistener, args);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.func_184883_a(MinecraftServer.getServer(), getListener(sender), args, (location) == null ? null : new BlockPos(location.getX(), location.getY(), location.getZ()));
    }

    public static CommandSender lastSender = null; // Nasty :(

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandSender icommandlistener, String[] as) throws CommandException {
        // Copied from net.minecraft.server.CommandHandler
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        WorldServer[] prev = MinecraftServer.getServer().field_71305_c;
        MinecraftServer server = MinecraftServer.getServer();
        server.field_71305_c = new WorldServer[server.worlds.size()];
        server.field_71305_c[0] = (WorldServer) icommandlistener.func_130014_f_();
        int bpos = 0;
        for (int pos = 1; pos < server.field_71305_c.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.field_71305_c[0] == world) {
                pos--;
                continue;
            }
            server.field_71305_c[pos] = world;
        }

        try {
            if (vanillaCommand.func_184882_a(server, icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>)EntitySelector.func_179656_b(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];

                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        CommandSender oldSender = lastSender;
                        lastSender = bSender;
                        try {
                            as[i] = entity.func_110124_au().toString();
                            vanillaCommand.func_184881_a(server, icommandlistener, as);
                            j++;
                        } catch (WrongUsageException exceptionusage) {
                            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.func_74844_a())});
                            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                            icommandlistener.func_145747_a(chatmessage);
                        } catch (CommandException commandexception) {
                            CommandBase.func_152374_a(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.func_74844_a());
                        } finally {
                            lastSender = oldSender;
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    vanillaCommand.func_184881_a(server, icommandlistener, as);
                    j++;
                }
            } else {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                icommandlistener.func_145747_a(chatmessage);
            }
        } catch (WrongUsageException exceptionusage) {
            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.func_74844_a()) });
            chatmessage1.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage1);
        } catch (CommandException commandexception) {
            CommandBase.func_152374_a(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.func_74844_a());
        } catch (Throwable throwable) {
            TextComponentTranslation chatmessage3 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage3.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage3);
            if (icommandlistener.func_174793_f() instanceof EntityMinecartCommandBlock) {
                MinecraftServer.field_147145_h.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.func_180425_c().func_177958_n(), icommandlistener.func_180425_c().func_177956_o(), icommandlistener.func_180425_c().func_177952_p()), throwable);
            } else if(icommandlistener instanceof CommandBlockBaseLogic) {
                CommandBlockBaseLogic listener = (CommandBlockBaseLogic) icommandlistener;
                MinecraftServer.field_147145_h.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.func_180425_c().func_177958_n(), listener.func_180425_c().func_177956_o(), listener.func_180425_c().func_177952_p()), throwable);
            } else {
                MinecraftServer.field_147145_h.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            icommandlistener.func_174794_a(CommandResultStats.Type.SUCCESS_COUNT, j);
            MinecraftServer.getServer().field_71305_c = prev;
        }
        return j;
    }

    private ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).func_145822_e();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer)MinecraftServer.getServer()).field_184115_n;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        if (sender instanceof CraftFunctionCommandSender) {
            return ((CraftFunctionCommandSender) sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String as[]) throws CommandException {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.func_82358_a(as, i) && EntitySelector.func_82377_a(as[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
