package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public abstract class CommandHandler implements ICommandManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, ICommand> commandMap = Maps.newHashMap();
    private final Set<ICommand> commandSet = Sets.newHashSet();

    public CommandHandler() {}

    public int executeCommand(ICommandSender icommandlistener, String s) {
        s = s.trim();
        if (s.startsWith("/")) {
            s = s.substring(1);
        }

        String[] astring = s.split(" ");
        String s1 = astring[0];

        astring = dropFirstString(astring);
        ICommand icommand = (ICommand) this.commandMap.get(s1);
        int i = 0;

        TextComponentTranslation chatmessage;

        try {
            int j = this.getUsernameIndex(icommand, astring);

            if (icommand == null) {
                chatmessage = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
                chatmessage.getStyle().setColor(TextFormatting.RED);
                icommandlistener.sendMessage(chatmessage);
            } else if (icommand.checkPermission(this.getServer(), icommandlistener)) {
                if (j > -1) {
                    List list = EntitySelector.matchEntities(icommandlistener, astring[j], Entity.class);
                    String s2 = astring[j];

                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    if (list.isEmpty()) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { astring[j]});
                    }

                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        astring[j] = entity.getCachedUniqueIdString();
                        if (this.tryExecute(icommandlistener, astring, icommand, s)) {
                            ++i;
                        }
                    }

                    astring[j] = s2;
                } else {
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    if (this.tryExecute(icommandlistener, astring, icommand, s)) {
                        ++i;
                    }
                }
            } else {
                chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.getStyle().setColor(TextFormatting.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (CommandException commandexception) {
            chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
        }

        icommandlistener.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }

    protected boolean tryExecute(ICommandSender icommandlistener, String[] astring, ICommand icommand, String s) {
        TextComponentTranslation chatmessage;

        try {
            icommand.execute(this.getServer(), icommandlistener, astring);
            return true;
        } catch (WrongUsageException exceptionusage) {
            chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects())});
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
        } catch (CommandException commandexception) {
            chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
        } catch (Throwable throwable) {
            chatmessage = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
            CommandHandler.LOGGER.warn("Couldn\'t process command: " + s, throwable);
        }

        return false;
    }

    protected abstract MinecraftServer getServer();

    public ICommand registerCommand(ICommand icommand) {
        this.commandMap.put(icommand.getName(), icommand);
        this.commandSet.add(icommand);
        Iterator iterator = icommand.getAliases().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            ICommand icommand1 = (ICommand) this.commandMap.get(s);

            if (icommand1 == null || !icommand1.getName().equals(s)) {
                this.commandMap.put(s, icommand);
            }
        }

        return icommand;
    }

    private static String[] dropFirstString(String[] astring) {
        String[] astring1 = new String[astring.length - 1];

        System.arraycopy(astring, 1, astring1, 0, astring.length - 1);
        return astring1;
    }

    public List<String> getTabCompletions(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition) {
        String[] astring = s.split(" ", -1);
        String s1 = astring[0];

        if (astring.length == 1) {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = this.commandMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (CommandBase.doesStringStartWith(s1, (String) entry.getKey()) && ((ICommand) entry.getValue()).checkPermission(this.getServer(), icommandlistener)) {
                    arraylist.add(entry.getKey());
                }
            }

            return arraylist;
        } else {
            if (astring.length > 1) {
                ICommand icommand = (ICommand) this.commandMap.get(s1);

                if (icommand != null && icommand.checkPermission(this.getServer(), icommandlistener)) {
                    return icommand.getTabCompletions(this.getServer(), icommandlistener, dropFirstString(astring), blockposition);
                }
            }

            return Collections.emptyList();
        }
    }

    public List<ICommand> getPossibleCommands(ICommandSender icommandlistener) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.commandSet.iterator();

        while (iterator.hasNext()) {
            ICommand icommand = (ICommand) iterator.next();

            if (icommand.checkPermission(this.getServer(), icommandlistener)) {
                arraylist.add(icommand);
            }
        }

        return arraylist;
    }

    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    private int getUsernameIndex(ICommand icommand, String[] astring) throws CommandException {
        if (icommand == null) {
            return -1;
        } else {
            for (int i = 0; i < astring.length; ++i) {
                if (icommand.isUsernameIndex(astring, i) && EntitySelector.matchesMultiplePlayers(astring[i])) {
                    return i;
                }
            }

            return -1;
        }
    }
}
