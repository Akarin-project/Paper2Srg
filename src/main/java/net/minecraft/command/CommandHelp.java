package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class CommandHelp extends CommandBase {

    private static final String[] field_184901_a = new String[] { "Yolo", "Ask for help on twitter", "/deop @p", "Scoreboard deleted, commands blocked", "Contact helpdesk for help", "/testfornoob @p", "/trigger warning", "Oh my god, it\'s full of stats", "/kill @p[name=!Searge]", "Have you tried turning it off and on again?", "Sorry, no help today"};
    private final Random field_184902_b = new Random();

    public CommandHelp() {}

    public String func_71517_b() {
        return "help";
    }

    public int func_82362_a() {
        return 0;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.help.usage";
    }

    public List<String> func_71514_a() {
        return Arrays.asList(new String[] { "?"});
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (icommandlistener instanceof CommandBlockBaseLogic) {
            icommandlistener.func_145747_a((new TextComponentString("Searge says: ")).func_150258_a(CommandHelp.field_184901_a[this.field_184902_b.nextInt(CommandHelp.field_184901_a.length) % CommandHelp.field_184901_a.length]));
        } else {
            List list = this.func_184900_a(icommandlistener, minecraftserver);
            boolean flag = true;
            int i = (list.size() - 1) / 7;
            boolean flag1 = false;

            int j;

            try {
                j = astring.length == 0 ? 0 : func_175764_a(astring[0], 1, i + 1) - 1;
            } catch (NumberInvalidException exceptioninvalidnumber) {
                Map map = this.func_184899_a(minecraftserver);
                ICommand icommand = (ICommand) map.get(astring[0]);

                if (icommand != null) {
                    throw new WrongUsageException(icommand.func_71518_a(icommandlistener), new Object[0]);
                }

                if (MathHelper.func_82715_a(astring[0], -1) == -1 && MathHelper.func_82715_a(astring[0], -2) == -2) {
                    throw new CommandNotFoundException();
                }

                throw exceptioninvalidnumber;
            }

            int k = Math.min((j + 1) * 7, list.size());
            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.help.header", new Object[] { Integer.valueOf(j + 1), Integer.valueOf(i + 1)});

            chatmessage.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage);

            for (int l = j * 7; l < k; ++l) {
                ICommand icommand1 = (ICommand) list.get(l);
                TextComponentTranslation chatmessage1 = new TextComponentTranslation(icommand1.func_71518_a(icommandlistener), new Object[0]);

                chatmessage1.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.func_71517_b() + " "));
                icommandlistener.func_145747_a(chatmessage1);
            }

            if (j == 0) {
                TextComponentTranslation chatmessage2 = new TextComponentTranslation("commands.help.footer", new Object[0]);

                chatmessage2.func_150256_b().func_150238_a(TextFormatting.GREEN);
                icommandlistener.func_145747_a(chatmessage2);
            }

        }
    }

    protected List<ICommand> func_184900_a(ICommandSender icommandlistener, MinecraftServer minecraftserver) {
        List list = minecraftserver.func_71187_D().func_71557_a(icommandlistener);

        Collections.sort(list);
        return list;
    }

    protected Map<String, ICommand> func_184899_a(MinecraftServer minecraftserver) {
        return minecraftserver.func_71187_D().func_71555_a();
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            Set set = this.func_184899_a(minecraftserver).keySet();

            return func_71530_a(astring, (String[]) set.toArray(new String[set.size()]));
        } else {
            return Collections.emptyList();
        }
    }
}
