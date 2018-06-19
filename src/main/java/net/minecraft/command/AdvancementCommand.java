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

    public String func_71517_b() {
        return "advancement";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.advancement.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.advancement.usage", new Object[0]);
        } else {
            AdvancementCommand.ActionType commandadvancement_action = AdvancementCommand.ActionType.func_193536_a(astring[0]);

            if (commandadvancement_action != null) {
                if (astring.length < 3) {
                    throw commandadvancement_action.func_193534_a();
                }

                EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[1]);
                AdvancementCommand.Mode commandadvancement_filter = AdvancementCommand.Mode.func_193547_a(astring[2]);

                if (commandadvancement_filter == null) {
                    throw commandadvancement_action.func_193534_a();
                }

                this.func_193516_a(minecraftserver, icommandlistener, astring, entityplayer, commandadvancement_action, commandadvancement_filter);
            } else {
                if (!"test".equals(astring[0])) {
                    throw new WrongUsageException("commands.advancement.usage", new Object[0]);
                }

                if (astring.length == 3) {
                    this.func_192552_c(icommandlistener, func_184888_a(minecraftserver, icommandlistener, astring[1]), func_192551_a(minecraftserver, astring[2]));
                } else {
                    if (astring.length != 4) {
                        throw new WrongUsageException("commands.advancement.test.usage", new Object[0]);
                    }

                    this.func_192554_c(icommandlistener, func_184888_a(minecraftserver, icommandlistener, astring[1]), func_192551_a(minecraftserver, astring[2]), astring[3]);
                }
            }

        }
    }

    private void func_193516_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, EntityPlayerMP entityplayer, AdvancementCommand.ActionType commandadvancement_action, AdvancementCommand.Mode commandadvancement_filter) throws CommandException {
        if (commandadvancement_filter == AdvancementCommand.Mode.EVERYTHING) {
            if (astring.length == 3) {
                int i = commandadvancement_action.func_193532_a(entityplayer, minecraftserver.func_191949_aK().func_192780_b());

                if (i == 0) {
                    throw commandadvancement_filter.func_193543_a(commandadvancement_action, new Object[] { entityplayer.func_70005_c_()});
                } else {
                    commandadvancement_filter.func_193546_a(icommandlistener, this, commandadvancement_action, new Object[] { entityplayer.func_70005_c_(), Integer.valueOf(i)});
                }
            } else {
                throw commandadvancement_filter.func_193544_a(commandadvancement_action);
            }
        } else if (astring.length < 4) {
            throw commandadvancement_filter.func_193544_a(commandadvancement_action);
        } else {
            Advancement advancement = func_192551_a(minecraftserver, astring[3]);

            if (commandadvancement_filter == AdvancementCommand.Mode.ONLY && astring.length == 5) {
                String s = astring[4];

                if (!advancement.func_192073_f().keySet().contains(s)) {
                    throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.func_192067_g(), astring[4]});
                }

                if (!commandadvancement_action.func_193535_a(entityplayer, advancement, s)) {
                    throw new CommandException(commandadvancement_action.field_193541_d + ".criterion.failed", new Object[] { advancement.func_192067_g(), entityplayer.func_70005_c_(), s});
                }

                func_152373_a(icommandlistener, (ICommand) this, commandadvancement_action.field_193541_d + ".criterion.success", new Object[] { advancement.func_192067_g(), entityplayer.func_70005_c_(), s});
            } else {
                if (astring.length != 4) {
                    throw commandadvancement_filter.func_193544_a(commandadvancement_action);
                }

                List list = this.func_193514_a(advancement, commandadvancement_filter);
                int j = commandadvancement_action.func_193532_a(entityplayer, (Iterable) list);

                if (j == 0) {
                    throw commandadvancement_filter.func_193543_a(commandadvancement_action, new Object[] { advancement.func_192067_g(), entityplayer.func_70005_c_()});
                }

                commandadvancement_filter.func_193546_a(icommandlistener, this, commandadvancement_action, new Object[] { advancement.func_192067_g(), entityplayer.func_70005_c_(), Integer.valueOf(j)});
            }

        }
    }

    private void func_193515_a(Advancement advancement, List<Advancement> list) {
        Iterator iterator = advancement.func_192069_e().iterator();

        while (iterator.hasNext()) {
            Advancement advancement1 = (Advancement) iterator.next();

            list.add(advancement1);
            this.func_193515_a(advancement1, list);
        }

    }

    private List<Advancement> func_193514_a(Advancement advancement, AdvancementCommand.Mode commandadvancement_filter) {
        ArrayList arraylist = Lists.newArrayList();

        if (commandadvancement_filter.field_193555_h) {
            for (Advancement advancement1 = advancement.func_192070_b(); advancement1 != null; advancement1 = advancement1.func_192070_b()) {
                arraylist.add(advancement1);
            }
        }

        arraylist.add(advancement);
        if (commandadvancement_filter.field_193556_i) {
            this.func_193515_a(advancement, (List) arraylist);
        }

        return arraylist;
    }

    private void func_192554_c(ICommandSender icommandlistener, EntityPlayerMP entityplayer, Advancement advancement, String s) throws CommandException {
        PlayerAdvancements advancementdataplayer = entityplayer.func_192039_O();
        CriterionProgress criterionprogress = advancementdataplayer.func_192747_a(advancement).func_192106_c(s);

        if (criterionprogress == null) {
            throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.func_192067_g(), s});
        } else if (!criterionprogress.func_192151_a()) {
            throw new CommandException("commands.advancement.test.criterion.notDone", new Object[] { entityplayer.func_70005_c_(), advancement.func_192067_g(), s});
        } else {
            func_152373_a(icommandlistener, (ICommand) this, "commands.advancement.test.criterion.success", new Object[] { entityplayer.func_70005_c_(), advancement.func_192067_g(), s});
        }
    }

    private void func_192552_c(ICommandSender icommandlistener, EntityPlayerMP entityplayer, Advancement advancement) throws CommandException {
        AdvancementProgress advancementprogress = entityplayer.func_192039_O().func_192747_a(advancement);

        if (!advancementprogress.func_192105_a()) {
            throw new CommandException("commands.advancement.test.advancement.notDone", new Object[] { entityplayer.func_70005_c_(), advancement.func_192067_g()});
        } else {
            func_152373_a(icommandlistener, (ICommand) this, "commands.advancement.test.advancement.success", new Object[] { entityplayer.func_70005_c_(), advancement.func_192067_g()});
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return func_71530_a(astring, new String[] { "grant", "revoke", "test"});
        } else {
            AdvancementCommand.ActionType commandadvancement_action = AdvancementCommand.ActionType.func_193536_a(astring[0]);

            if (commandadvancement_action != null) {
                if (astring.length == 2) {
                    return func_71530_a(astring, minecraftserver.func_71213_z());
                }

                if (astring.length == 3) {
                    return func_71530_a(astring, AdvancementCommand.Mode.field_193553_f);
                }

                AdvancementCommand.Mode commandadvancement_filter = AdvancementCommand.Mode.func_193547_a(astring[2]);

                if (commandadvancement_filter != null && commandadvancement_filter != AdvancementCommand.Mode.EVERYTHING) {
                    if (astring.length == 4) {
                        return func_175762_a(astring, (Collection) this.func_193517_a(minecraftserver));
                    }

                    if (astring.length == 5 && commandadvancement_filter == AdvancementCommand.Mode.ONLY) {
                        Advancement advancement = minecraftserver.func_191949_aK().func_192778_a(new ResourceLocation(astring[3]));

                        if (advancement != null) {
                            return func_175762_a(astring, (Collection) advancement.func_192073_f().keySet());
                        }
                    }
                }
            }

            if ("test".equals(astring[0])) {
                if (astring.length == 2) {
                    return func_71530_a(astring, minecraftserver.func_71213_z());
                }

                if (astring.length == 3) {
                    return func_175762_a(astring, (Collection) this.func_193517_a(minecraftserver));
                }

                if (astring.length == 4) {
                    Advancement advancement1 = minecraftserver.func_191949_aK().func_192778_a(new ResourceLocation(astring[2]));

                    if (advancement1 != null) {
                        return func_175762_a(astring, (Collection) advancement1.func_192073_f().keySet());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    private List<ResourceLocation> func_193517_a(MinecraftServer minecraftserver) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = minecraftserver.func_191949_aK().func_192780_b().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            arraylist.add(advancement.func_192067_g());
        }

        return arraylist;
    }

    public boolean func_82358_a(String[] astring, int i) {
        return astring.length > 1 && ("grant".equals(astring[0]) || "revoke".equals(astring[0]) || "test".equals(astring[0])) && i == 1;
    }

    public static Advancement func_192551_a(MinecraftServer minecraftserver, String s) throws CommandException {
        Advancement advancement = minecraftserver.func_191949_aK().func_192778_a(new ResourceLocation(s));

        if (advancement == null) {
            throw new CommandException("commands.advancement.advancementNotFound", new Object[] { s});
        } else {
            return advancement;
        }
    }

    static enum Mode {

        ONLY("only", false, false), THROUGH("through", true, true), FROM("from", false, true), UNTIL("until", true, false), EVERYTHING("everything", true, true);

        static final String[] field_193553_f = new String[values().length];
        final String field_193554_g;
        final boolean field_193555_h;
        final boolean field_193556_i;

        private Mode(String s, boolean flag, boolean flag1) {
            this.field_193554_g = s;
            this.field_193555_h = flag;
            this.field_193556_i = flag1;
        }

        CommandException func_193543_a(AdvancementCommand.ActionType commandadvancement_action, Object... aobject) {
            return new CommandException(commandadvancement_action.field_193541_d + "." + this.field_193554_g + ".failed", aobject);
        }

        CommandException func_193544_a(AdvancementCommand.ActionType commandadvancement_action) {
            return new CommandException(commandadvancement_action.field_193541_d + "." + this.field_193554_g + ".usage", new Object[0]);
        }

        void func_193546_a(ICommandSender icommandlistener, AdvancementCommand commandadvancement, AdvancementCommand.ActionType commandadvancement_action, Object... aobject) {
            CommandBase.func_152373_a(icommandlistener, (ICommand) commandadvancement, commandadvancement_action.field_193541_d + "." + this.field_193554_g + ".success", aobject);
        }

        @Nullable
        static AdvancementCommand.Mode func_193547_a(String s) {
            AdvancementCommand.Mode[] acommandadvancement_filter = values();
            int i = acommandadvancement_filter.length;

            for (int j = 0; j < i; ++j) {
                AdvancementCommand.Mode commandadvancement_filter = acommandadvancement_filter[j];

                if (commandadvancement_filter.field_193554_g.equals(s)) {
                    return commandadvancement_filter;
                }
            }

            return null;
        }

        static {
            for (int i = 0; i < values().length; ++i) {
                AdvancementCommand.Mode.field_193553_f[i] = values()[i].field_193554_g;
            }

        }
    }

    static enum ActionType {

        GRANT("grant") {;
            protected boolean func_193537_a(EntityPlayerMP entityplayer, Advancement advancement) {
                AdvancementProgress advancementprogress = entityplayer.func_192039_O().func_192747_a(advancement);

                if (advancementprogress.func_192105_a()) {
                    return false;
                } else {
                    Iterator iterator = advancementprogress.func_192107_d().iterator();

                    while (iterator.hasNext()) {
                        String s = (String) iterator.next();

                        entityplayer.func_192039_O().func_192750_a(advancement, s);
                    }

                    return true;
                }
            }

            protected boolean func_193535_a(EntityPlayerMP entityplayer, Advancement advancement, String s) {
                return entityplayer.func_192039_O().func_192750_a(advancement, s);
            }
        }, REVOKE("revoke") {;
    protected boolean func_193537_a(EntityPlayerMP entityplayer, Advancement advancement) {
        AdvancementProgress advancementprogress = entityplayer.func_192039_O().func_192747_a(advancement);

        if (!advancementprogress.func_192108_b()) {
            return false;
        } else {
            Iterator iterator = advancementprogress.func_192102_e().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                entityplayer.func_192039_O().func_192744_b(advancement, s);
            }

            return true;
        }
    }

    protected boolean func_193535_a(EntityPlayerMP entityplayer, Advancement advancement, String s) {
        return entityplayer.func_192039_O().func_192744_b(advancement, s);
    }
};

        final String field_193540_c;
        final String field_193541_d;

        private ActionType(String s) {
            this.field_193540_c = s;
            this.field_193541_d = "commands.advancement." + s;
        }

        @Nullable
        static AdvancementCommand.ActionType func_193536_a(String s) {
            AdvancementCommand.ActionType[] acommandadvancement_action = values();
            int i = acommandadvancement_action.length;

            for (int j = 0; j < i; ++j) {
                AdvancementCommand.ActionType commandadvancement_action = acommandadvancement_action[j];

                if (commandadvancement_action.field_193540_c.equals(s)) {
                    return commandadvancement_action;
                }
            }

            return null;
        }

        CommandException func_193534_a() {
            return new CommandException(this.field_193541_d + ".usage", new Object[0]);
        }

        public int func_193532_a(EntityPlayerMP entityplayer, Iterable<Advancement> iterable) {
            int i = 0;
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                Advancement advancement = (Advancement) iterator.next();

                if (this.func_193537_a(entityplayer, advancement)) {
                    ++i;
                }
            }

            return i;
        }

        protected abstract boolean func_193537_a(EntityPlayerMP entityplayer, Advancement advancement);

        protected abstract boolean func_193535_a(EntityPlayerMP entityplayer, Advancement advancement, String s);

        ActionType(String s, Object object) {
            this(s);
        }
    }
}
