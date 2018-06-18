package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AdvancementCommand extends CommandBase {

    public AdvancementCommand() {}

    public String getName() {
        return "advancement";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.advancement.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.advancement.usage", new Object[0]);
        } else {
            AdvancementCommand.ActionType commandadvancement_action = AdvancementCommand.ActionType.byName(astring[0]);

            if (commandadvancement_action != null) {
                if (astring.length < 3) {
                    throw commandadvancement_action.wrongUsage();
                }

                EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[1]);
                AdvancementCommand.Mode commandadvancement_filter = AdvancementCommand.Mode.byName(astring[2]);

                if (commandadvancement_filter == null) {
                    throw commandadvancement_action.wrongUsage();
                }

                this.perform(minecraftserver, icommandlistener, astring, entityplayer, commandadvancement_action, commandadvancement_filter);
            } else {
                if (!"test".equals(astring[0])) {
                    throw new WrongUsageException("commands.advancement.usage", new Object[0]);
                }

                if (astring.length == 3) {
                    this.testAdvancement(icommandlistener, getPlayer(minecraftserver, icommandlistener, astring[1]), findAdvancement(minecraftserver, astring[2]));
                } else {
                    if (astring.length != 4) {
                        throw new WrongUsageException("commands.advancement.test.usage", new Object[0]);
                    }

                    this.testCriterion(icommandlistener, getPlayer(minecraftserver, icommandlistener, astring[1]), findAdvancement(minecraftserver, astring[2]), astring[3]);
                }
            }

        }
    }

    private void perform(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, EntityPlayerMP entityplayer, AdvancementCommand.ActionType commandadvancement_action, AdvancementCommand.Mode commandadvancement_filter) throws CommandException {
        if (commandadvancement_filter == AdvancementCommand.Mode.EVERYTHING) {
            if (astring.length == 3) {
                int i = commandadvancement_action.perform(entityplayer, minecraftserver.getAdvancementManager().getAdvancements());

                if (i == 0) {
                    throw commandadvancement_filter.fail(commandadvancement_action, new Object[] { entityplayer.getName()});
                } else {
                    commandadvancement_filter.success(icommandlistener, this, commandadvancement_action, new Object[] { entityplayer.getName(), Integer.valueOf(i)});
                }
            } else {
                throw commandadvancement_filter.usage(commandadvancement_action);
            }
        } else if (astring.length < 4) {
            throw commandadvancement_filter.usage(commandadvancement_action);
        } else {
            Advancement advancement = findAdvancement(minecraftserver, astring[3]);

            if (commandadvancement_filter == AdvancementCommand.Mode.ONLY && astring.length == 5) {
                String s = astring[4];

                if (!advancement.getCriteria().keySet().contains(s)) {
                    throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.getId(), astring[4]});
                }

                if (!commandadvancement_action.performCriterion(entityplayer, advancement, s)) {
                    throw new CommandException(commandadvancement_action.baseTranslationKey + ".criterion.failed", new Object[] { advancement.getId(), entityplayer.getName(), s});
                }

                notifyCommandListener(icommandlistener, (ICommand) this, commandadvancement_action.baseTranslationKey + ".criterion.success", new Object[] { advancement.getId(), entityplayer.getName(), s});
            } else {
                if (astring.length != 4) {
                    throw commandadvancement_filter.usage(commandadvancement_action);
                }

                List list = this.getAdvancements(advancement, commandadvancement_filter);
                int j = commandadvancement_action.perform(entityplayer, (Iterable) list);

                if (j == 0) {
                    throw commandadvancement_filter.fail(commandadvancement_action, new Object[] { advancement.getId(), entityplayer.getName()});
                }

                commandadvancement_filter.success(icommandlistener, this, commandadvancement_action, new Object[] { advancement.getId(), entityplayer.getName(), Integer.valueOf(j)});
            }

        }
    }

    private void addChildren(Advancement advancement, List<Advancement> list) {
        Iterator iterator = advancement.getChildren().iterator();

        while (iterator.hasNext()) {
            Advancement advancement1 = (Advancement) iterator.next();

            list.add(advancement1);
            this.addChildren(advancement1, list);
        }

    }

    private List<Advancement> getAdvancements(Advancement advancement, AdvancementCommand.Mode commandadvancement_filter) {
        ArrayList arraylist = Lists.newArrayList();

        if (commandadvancement_filter.parents) {
            for (Advancement advancement1 = advancement.getParent(); advancement1 != null; advancement1 = advancement1.getParent()) {
                arraylist.add(advancement1);
            }
        }

        arraylist.add(advancement);
        if (commandadvancement_filter.children) {
            this.addChildren(advancement, (List) arraylist);
        }

        return arraylist;
    }

    private void testCriterion(ICommandSender icommandlistener, EntityPlayerMP entityplayer, Advancement advancement, String s) throws CommandException {
        PlayerAdvancements advancementdataplayer = entityplayer.getAdvancements();
        CriterionProgress criterionprogress = advancementdataplayer.getProgress(advancement).getCriterionProgress(s);

        if (criterionprogress == null) {
            throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.getId(), s});
        } else if (!criterionprogress.isObtained()) {
            throw new CommandException("commands.advancement.test.criterion.notDone", new Object[] { entityplayer.getName(), advancement.getId(), s});
        } else {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.advancement.test.criterion.success", new Object[] { entityplayer.getName(), advancement.getId(), s});
        }
    }

    private void testAdvancement(ICommandSender icommandlistener, EntityPlayerMP entityplayer, Advancement advancement) throws CommandException {
        AdvancementProgress advancementprogress = entityplayer.getAdvancements().getProgress(advancement);

        if (!advancementprogress.isDone()) {
            throw new CommandException("commands.advancement.test.advancement.notDone", new Object[] { entityplayer.getName(), advancement.getId()});
        } else {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.advancement.test.advancement.success", new Object[] { entityplayer.getName(), advancement.getId()});
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return getListOfStringsMatchingLastWord(astring, new String[] { "grant", "revoke", "test"});
        } else {
            AdvancementCommand.ActionType commandadvancement_action = AdvancementCommand.ActionType.byName(astring[0]);

            if (commandadvancement_action != null) {
                if (astring.length == 2) {
                    return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                }

                if (astring.length == 3) {
                    return getListOfStringsMatchingLastWord(astring, AdvancementCommand.Mode.NAMES);
                }

                AdvancementCommand.Mode commandadvancement_filter = AdvancementCommand.Mode.byName(astring[2]);

                if (commandadvancement_filter != null && commandadvancement_filter != AdvancementCommand.Mode.EVERYTHING) {
                    if (astring.length == 4) {
                        return getListOfStringsMatchingLastWord(astring, (Collection) this.getAdvancementNames(minecraftserver));
                    }

                    if (astring.length == 5 && commandadvancement_filter == AdvancementCommand.Mode.ONLY) {
                        Advancement advancement = minecraftserver.getAdvancementManager().getAdvancement(new ResourceLocation(astring[3]));

                        if (advancement != null) {
                            return getListOfStringsMatchingLastWord(astring, (Collection) advancement.getCriteria().keySet());
                        }
                    }
                }
            }

            if ("test".equals(astring[0])) {
                if (astring.length == 2) {
                    return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                }

                if (astring.length == 3) {
                    return getListOfStringsMatchingLastWord(astring, (Collection) this.getAdvancementNames(minecraftserver));
                }

                if (astring.length == 4) {
                    Advancement advancement1 = minecraftserver.getAdvancementManager().getAdvancement(new ResourceLocation(astring[2]));

                    if (advancement1 != null) {
                        return getListOfStringsMatchingLastWord(astring, (Collection) advancement1.getCriteria().keySet());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    private List<ResourceLocation> getAdvancementNames(MinecraftServer minecraftserver) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = minecraftserver.getAdvancementManager().getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            arraylist.add(advancement.getId());
        }

        return arraylist;
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return astring.length > 1 && ("grant".equals(astring[0]) || "revoke".equals(astring[0]) || "test".equals(astring[0])) && i == 1;
    }

    public static Advancement findAdvancement(MinecraftServer minecraftserver, String s) throws CommandException {
        Advancement advancement = minecraftserver.getAdvancementManager().getAdvancement(new ResourceLocation(s));

        if (advancement == null) {
            throw new CommandException("commands.advancement.advancementNotFound", new Object[] { s});
        } else {
            return advancement;
        }
    }

    static enum Mode {

        ONLY("only", false, false), THROUGH("through", true, true), FROM("from", false, true), UNTIL("until", true, false), EVERYTHING("everything", true, true);

        static final String[] NAMES = new String[values().length];
        final String name;
        final boolean parents;
        final boolean children;

        private Mode(String s, boolean flag, boolean flag1) {
            this.name = s;
            this.parents = flag;
            this.children = flag1;
        }

        CommandException fail(AdvancementCommand.ActionType commandadvancement_action, Object... aobject) {
            return new CommandException(commandadvancement_action.baseTranslationKey + "." + this.name + ".failed", aobject);
        }

        CommandException usage(AdvancementCommand.ActionType commandadvancement_action) {
            return new CommandException(commandadvancement_action.baseTranslationKey + "." + this.name + ".usage", new Object[0]);
        }

        void success(ICommandSender icommandlistener, AdvancementCommand commandadvancement, AdvancementCommand.ActionType commandadvancement_action, Object... aobject) {
            CommandBase.notifyCommandListener(icommandlistener, (ICommand) commandadvancement, commandadvancement_action.baseTranslationKey + "." + this.name + ".success", aobject);
        }

        @Nullable
        static AdvancementCommand.Mode byName(String s) {
            AdvancementCommand.Mode[] acommandadvancement_filter = values();
            int i = acommandadvancement_filter.length;

            for (int j = 0; j < i; ++j) {
                AdvancementCommand.Mode commandadvancement_filter = acommandadvancement_filter[j];

                if (commandadvancement_filter.name.equals(s)) {
                    return commandadvancement_filter;
                }
            }

            return null;
        }

        static {
            for (int i = 0; i < values().length; ++i) {
                AdvancementCommand.Mode.NAMES[i] = values()[i].name;
            }

        }
    }

    static enum ActionType {

        GRANT("grant") {;
            protected boolean perform(EntityPlayerMP entityplayer, Advancement advancement) {
                AdvancementProgress advancementprogress = entityplayer.getAdvancements().getProgress(advancement);

                if (advancementprogress.isDone()) {
                    return false;
                } else {
                    Iterator iterator = advancementprogress.getRemaningCriteria().iterator();

                    while (iterator.hasNext()) {
                        String s = (String) iterator.next();

                        entityplayer.getAdvancements().grantCriterion(advancement, s);
                    }

                    return true;
                }
            }

            protected boolean performCriterion(EntityPlayerMP entityplayer, Advancement advancement, String s) {
                return entityplayer.getAdvancements().grantCriterion(advancement, s);
            }
        }, REVOKE("revoke") {;
    protected boolean perform(EntityPlayerMP entityplayer, Advancement advancement) {
        AdvancementProgress advancementprogress = entityplayer.getAdvancements().getProgress(advancement);

        if (!advancementprogress.hasProgress()) {
            return false;
        } else {
            Iterator iterator = advancementprogress.getCompletedCriteria().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                entityplayer.getAdvancements().revokeCriterion(advancement, s);
            }

            return true;
        }
    }

    protected boolean performCriterion(EntityPlayerMP entityplayer, Advancement advancement, String s) {
        return entityplayer.getAdvancements().revokeCriterion(advancement, s);
    }
};

        final String name;
        final String baseTranslationKey;

        private ActionType(String s) {
            this.name = s;
            this.baseTranslationKey = "commands.advancement." + s;
        }

        @Nullable
        static AdvancementCommand.ActionType byName(String s) {
            AdvancementCommand.ActionType[] acommandadvancement_action = values();
            int i = acommandadvancement_action.length;

            for (int j = 0; j < i; ++j) {
                AdvancementCommand.ActionType commandadvancement_action = acommandadvancement_action[j];

                if (commandadvancement_action.name.equals(s)) {
                    return commandadvancement_action;
                }
            }

            return null;
        }

        CommandException wrongUsage() {
            return new CommandException(this.baseTranslationKey + ".usage", new Object[0]);
        }

        public int perform(EntityPlayerMP entityplayer, Iterable<Advancement> iterable) {
            int i = 0;
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                Advancement advancement = (Advancement) iterator.next();

                if (this.perform(entityplayer, advancement)) {
                    ++i;
                }
            }

            return i;
        }

        protected abstract boolean perform(EntityPlayerMP entityplayer, Advancement advancement);

        protected abstract boolean performCriterion(EntityPlayerMP entityplayer, Advancement advancement, String s);

        ActionType(String s, Object object) {
            this(s);
        }
    }
}
