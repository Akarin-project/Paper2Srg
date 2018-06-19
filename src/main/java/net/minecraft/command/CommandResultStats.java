package net.minecraft.command;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandResultStats {

    private static final int field_179676_a = CommandResultStats.Type.values().length;
    private static final String[] field_179674_b = new String[CommandResultStats.field_179676_a];
    private String[] field_179675_c;
    private String[] field_179673_d;

    public CommandResultStats() {
        this.field_179675_c = CommandResultStats.field_179674_b;
        this.field_179673_d = CommandResultStats.field_179674_b;
    }

    public void func_184932_a(MinecraftServer minecraftserver, final ICommandSender icommandlistener, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        String s = this.field_179675_c[commandobjectiveexecutor_enumcommandresult.func_179636_a()];

        if (s != null) {
            ICommandSender icommandlistener1 = new ICommandSender() {
                public String func_70005_c_() {
                    return icommandlistener.func_70005_c_();
                }

                public ITextComponent func_145748_c_() {
                    return icommandlistener.func_145748_c_();
                }

                public void func_145747_a(ITextComponent ichatbasecomponent) {
                    icommandlistener.func_145747_a(ichatbasecomponent);
                }

                public boolean func_70003_b(int i, String s) {
                    return true;
                }

                public BlockPos func_180425_c() {
                    return icommandlistener.func_180425_c();
                }

                public Vec3d func_174791_d() {
                    return icommandlistener.func_174791_d();
                }

                public World func_130014_f_() {
                    return icommandlistener.func_130014_f_();
                }

                public Entity func_174793_f() {
                    return icommandlistener.func_174793_f();
                }

                public boolean func_174792_t_() {
                    return icommandlistener.func_174792_t_();
                }

                public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                    icommandlistener.func_174794_a(commandobjectiveexecutor_enumcommandresult, i);
                }

                public MinecraftServer func_184102_h() {
                    return icommandlistener.func_184102_h();
                }
            };

            String s1;

            try {
                s1 = CommandBase.func_184891_e(minecraftserver, icommandlistener1, s);
            } catch (CommandException commandexception) {
                return;
            }

            String s2 = this.field_179673_d[commandobjectiveexecutor_enumcommandresult.func_179636_a()];

            if (s2 != null) {
                Scoreboard scoreboard = icommandlistener.func_130014_f_().func_96441_U();
                ScoreObjective scoreboardobjective = scoreboard.func_96518_b(s2);

                if (scoreboardobjective != null) {
                    if (scoreboard.func_178819_b(s1, scoreboardobjective)) {
                        Score scoreboardscore = scoreboard.func_96529_a(s1, scoreboardobjective);

                        scoreboardscore.func_96647_c(i);
                    }
                }
            }
        }
    }

    public void func_179668_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("CommandStats", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("CommandStats");
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];
                String s = commandobjectiveexecutor_enumcommandresult.func_179637_b() + "Name";
                String s1 = commandobjectiveexecutor_enumcommandresult.func_179637_b() + "Objective";

                if (nbttagcompound1.func_150297_b(s, 8) && nbttagcompound1.func_150297_b(s1, 8)) {
                    String s2 = nbttagcompound1.func_74779_i(s);
                    String s3 = nbttagcompound1.func_74779_i(s1);

                    func_179667_a(this, commandobjectiveexecutor_enumcommandresult, s2, s3);
                }
            }

        }
    }

    public void func_179670_b(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
        int i = acommandobjectiveexecutor_enumcommandresult.length;

        for (int j = 0; j < i; ++j) {
            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];
            String s = this.field_179675_c[commandobjectiveexecutor_enumcommandresult.func_179636_a()];
            String s1 = this.field_179673_d[commandobjectiveexecutor_enumcommandresult.func_179636_a()];

            if (s != null && s1 != null) {
                nbttagcompound1.func_74778_a(commandobjectiveexecutor_enumcommandresult.func_179637_b() + "Name", s);
                nbttagcompound1.func_74778_a(commandobjectiveexecutor_enumcommandresult.func_179637_b() + "Objective", s1);
            }
        }

        if (!nbttagcompound1.func_82582_d()) {
            nbttagcompound.func_74782_a("CommandStats", nbttagcompound1);
        }

    }

    public static void func_179667_a(CommandResultStats commandobjectiveexecutor, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, @Nullable String s, @Nullable String s1) {
        if (s != null && !s.isEmpty() && s1 != null && !s1.isEmpty()) {
            if (commandobjectiveexecutor.field_179675_c == CommandResultStats.field_179674_b || commandobjectiveexecutor.field_179673_d == CommandResultStats.field_179674_b) {
                commandobjectiveexecutor.field_179675_c = new String[CommandResultStats.field_179676_a];
                commandobjectiveexecutor.field_179673_d = new String[CommandResultStats.field_179676_a];
            }

            commandobjectiveexecutor.field_179675_c[commandobjectiveexecutor_enumcommandresult.func_179636_a()] = s;
            commandobjectiveexecutor.field_179673_d[commandobjectiveexecutor_enumcommandresult.func_179636_a()] = s1;
        } else {
            func_179669_a(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult);
        }
    }

    private static void func_179669_a(CommandResultStats commandobjectiveexecutor, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult) {
        if (commandobjectiveexecutor.field_179675_c != CommandResultStats.field_179674_b && commandobjectiveexecutor.field_179673_d != CommandResultStats.field_179674_b) {
            commandobjectiveexecutor.field_179675_c[commandobjectiveexecutor_enumcommandresult.func_179636_a()] = null;
            commandobjectiveexecutor.field_179673_d[commandobjectiveexecutor_enumcommandresult.func_179636_a()] = null;
            boolean flag = true;
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult1 = acommandobjectiveexecutor_enumcommandresult[j];

                if (commandobjectiveexecutor.field_179675_c[commandobjectiveexecutor_enumcommandresult1.func_179636_a()] != null && commandobjectiveexecutor.field_179673_d[commandobjectiveexecutor_enumcommandresult1.func_179636_a()] != null) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                commandobjectiveexecutor.field_179675_c = CommandResultStats.field_179674_b;
                commandobjectiveexecutor.field_179673_d = CommandResultStats.field_179674_b;
            }

        }
    }

    public void func_179671_a(CommandResultStats commandobjectiveexecutor) {
        CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
        int i = acommandobjectiveexecutor_enumcommandresult.length;

        for (int j = 0; j < i; ++j) {
            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];

            func_179667_a(this, commandobjectiveexecutor_enumcommandresult, commandobjectiveexecutor.field_179675_c[commandobjectiveexecutor_enumcommandresult.func_179636_a()], commandobjectiveexecutor.field_179673_d[commandobjectiveexecutor_enumcommandresult.func_179636_a()]);
        }

    }

    public static enum Type {

        SUCCESS_COUNT(0, "SuccessCount"), AFFECTED_BLOCKS(1, "AffectedBlocks"), AFFECTED_ENTITIES(2, "AffectedEntities"), AFFECTED_ITEMS(3, "AffectedItems"), QUERY_RESULT(4, "QueryResult");

        final int field_179639_f;
        final String field_179640_g;

        private Type(int i, String s) {
            this.field_179639_f = i;
            this.field_179640_g = s;
        }

        public int func_179636_a() {
            return this.field_179639_f;
        }

        public String func_179637_b() {
            return this.field_179640_g;
        }

        public static String[] func_179634_c() {
            String[] astring = new String[values().length];
            int i = 0;
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = values();
            int j = acommandobjectiveexecutor_enumcommandresult.length;

            for (int k = 0; k < j; ++k) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[k];

                astring[i++] = commandobjectiveexecutor_enumcommandresult.func_179637_b();
            }

            return astring;
        }

        @Nullable
        public static CommandResultStats.Type func_179635_a(String s) {
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];

                if (commandobjectiveexecutor_enumcommandresult.func_179637_b().equals(s)) {
                    return commandobjectiveexecutor_enumcommandresult;
                }
            }

            return null;
        }
    }
}
