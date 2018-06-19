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

    private static final Logger field_147175_a = LogManager.getLogger();
    private final Map<String, ICommand> field_71562_a = Maps.newHashMap();
    private final Set<ICommand> field_71561_b = Sets.newHashSet();

    public CommandHandler() {}

    public int func_71556_a(ICommandSender icommandlistener, String s) {
        s = s.trim();
        if (s.startsWith("/")) {
            s = s.substring(1);
        }

        String[] astring = s.split(" ");
        String s1 = astring[0];

        astring = func_71559_a(astring);
        ICommand icommand = (ICommand) this.field_71562_a.get(s1);
        int i = 0;

        TextComponentTranslation chatmessage;

        try {
            int j = this.func_82370_a(icommand, astring);

            if (icommand == null) {
                chatmessage = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
                chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                icommandlistener.func_145747_a(chatmessage);
            } else if (icommand.func_184882_a(this.func_184879_a(), icommandlistener)) {
                if (j > -1) {
                    List list = EntitySelector.func_179656_b(icommandlistener, astring[j], Entity.class);
                    String s2 = astring[j];

                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    if (list.isEmpty()) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { astring[j]});
                    }

                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        astring[j] = entity.func_189512_bd();
                        if (this.func_175786_a(icommandlistener, astring, icommand, s)) {
                            ++i;
                        }
                    }

                    astring[j] = s2;
                } else {
                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    if (this.func_175786_a(icommandlistener, astring, icommand, s)) {
                        ++i;
                    }
                }
            } else {
                chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                icommandlistener.func_145747_a(chatmessage);
            }
        } catch (CommandException commandexception) {
            chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage);
        }

        icommandlistener.func_174794_a(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }

    protected boolean func_175786_a(ICommandSender icommandlistener, String[] astring, ICommand icommand, String s) {
        TextComponentTranslation chatmessage;

        try {
            icommand.func_184881_a(this.func_184879_a(), icommandlistener, astring);
            return true;
        } catch (WrongUsageException exceptionusage) {
            chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.func_74844_a())});
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage);
        } catch (CommandException commandexception) {
            chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage);
        } catch (Throwable throwable) {
            chatmessage = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            icommandlistener.func_145747_a(chatmessage);
            CommandHandler.field_147175_a.warn("Couldn\'t process command: " + s, throwable);
        }

        return false;
    }

    protected abstract MinecraftServer func_184879_a();

    public ICommand func_71560_a(ICommand icommand) {
        this.field_71562_a.put(icommand.func_71517_b(), icommand);
        this.field_71561_b.add(icommand);
        Iterator iterator = icommand.func_71514_a().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            ICommand icommand1 = (ICommand) this.field_71562_a.get(s);

            if (icommand1 == null || !icommand1.func_71517_b().equals(s)) {
                this.field_71562_a.put(s, icommand);
            }
        }

        return icommand;
    }

    private static String[] func_71559_a(String[] astring) {
        String[] astring1 = new String[astring.length - 1];

        System.arraycopy(astring, 1, astring1, 0, astring.length - 1);
        return astring1;
    }

    public List<String> func_180524_a(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition) {
        String[] astring = s.split(" ", -1);
        String s1 = astring[0];

        if (astring.length == 1) {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = this.field_71562_a.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (CommandBase.func_71523_a(s1, (String) entry.getKey()) && ((ICommand) entry.getValue()).func_184882_a(this.func_184879_a(), icommandlistener)) {
                    arraylist.add(entry.getKey());
                }
            }

            return arraylist;
        } else {
            if (astring.length > 1) {
                ICommand icommand = (ICommand) this.field_71562_a.get(s1);

                if (icommand != null && icommand.func_184882_a(this.func_184879_a(), icommandlistener)) {
                    return icommand.func_184883_a(this.func_184879_a(), icommandlistener, func_71559_a(astring), blockposition);
                }
            }

            return Collections.emptyList();
        }
    }

    public List<ICommand> func_71557_a(ICommandSender icommandlistener) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_71561_b.iterator();

        while (iterator.hasNext()) {
            ICommand icommand = (ICommand) iterator.next();

            if (icommand.func_184882_a(this.func_184879_a(), icommandlistener)) {
                arraylist.add(icommand);
            }
        }

        return arraylist;
    }

    public Map<String, ICommand> func_71555_a() {
        return this.field_71562_a;
    }

    private int func_82370_a(ICommand icommand, String[] astring) throws CommandException {
        if (icommand == null) {
            return -1;
        } else {
            for (int i = 0; i < astring.length; ++i) {
                if (icommand.func_82358_a(astring, i) && EntitySelector.func_82377_a(astring[i])) {
                    return i;
                }
            }

            return -1;
        }
    }
}
