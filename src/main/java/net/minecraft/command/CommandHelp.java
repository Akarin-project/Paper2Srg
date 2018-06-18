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

    private static final String[] SEARGE_SAYS = new String[] { "Yolo", "Ask for help on twitter", "/deop @p", "Scoreboard deleted, commands blocked", "Contact helpdesk for help", "/testfornoob @p", "/trigger warning", "Oh my god, it\'s full of stats", "/kill @p[name=!Searge]", "Have you tried turning it off and on again?", "Sorry, no help today"};
    private final Random rand = new Random();

    public CommandHelp() {}

    public String getName() {
        return "help";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.help.usage";
    }

    public List<String> getAliases() {
        return Arrays.asList(new String[] { "?"});
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (icommandlistener instanceof CommandBlockBaseLogic) {
            icommandlistener.sendMessage((new TextComponentString("Searge says: ")).appendText(CommandHelp.SEARGE_SAYS[this.rand.nextInt(CommandHelp.SEARGE_SAYS.length) % CommandHelp.SEARGE_SAYS.length]));
        } else {
            List list = this.getSortedPossibleCommands(icommandlistener, minecraftserver);
            boolean flag = true;
            int i = (list.size() - 1) / 7;
            boolean flag1 = false;

            int j;

            try {
                j = astring.length == 0 ? 0 : parseInt(astring[0], 1, i + 1) - 1;
            } catch (NumberInvalidException exceptioninvalidnumber) {
                Map map = this.getCommandMap(minecraftserver);
                ICommand icommand = (ICommand) map.get(astring[0]);

                if (icommand != null) {
                    throw new WrongUsageException(icommand.getUsage(icommandlistener), new Object[0]);
                }

                if (MathHelper.getInt(astring[0], -1) == -1 && MathHelper.getInt(astring[0], -2) == -2) {
                    throw new CommandNotFoundException();
                }

                throw exceptioninvalidnumber;
            }

            int k = Math.min((j + 1) * 7, list.size());
            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.help.header", new Object[] { Integer.valueOf(j + 1), Integer.valueOf(i + 1)});

            chatmessage.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage);

            for (int l = j * 7; l < k; ++l) {
                ICommand icommand1 = (ICommand) list.get(l);
                TextComponentTranslation chatmessage1 = new TextComponentTranslation(icommand1.getUsage(icommandlistener), new Object[0]);

                chatmessage1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getName() + " "));
                icommandlistener.sendMessage(chatmessage1);
            }

            if (j == 0) {
                TextComponentTranslation chatmessage2 = new TextComponentTranslation("commands.help.footer", new Object[0]);

                chatmessage2.getStyle().setColor(TextFormatting.GREEN);
                icommandlistener.sendMessage(chatmessage2);
            }

        }
    }

    protected List<ICommand> getSortedPossibleCommands(ICommandSender icommandlistener, MinecraftServer minecraftserver) {
        List list = minecraftserver.getCommandManager().getPossibleCommands(icommandlistener);

        Collections.sort(list);
        return list;
    }

    protected Map<String, ICommand> getCommandMap(MinecraftServer minecraftserver) {
        return minecraftserver.getCommandManager().getCommands();
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            Set set = this.getCommandMap(minecraftserver).keySet();

            return getListOfStringsMatchingLastWord(astring, (String[]) set.toArray(new String[set.size()]));
        } else {
            return Collections.emptyList();
        }
    }
}
