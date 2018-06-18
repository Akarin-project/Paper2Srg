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

    private static final int NUM_RESULT_TYPES = CommandResultStats.Type.values().length;
    private static final String[] STRING_RESULT_TYPES = new String[CommandResultStats.NUM_RESULT_TYPES];
    private String[] entitiesID;
    private String[] objectives;

    public CommandResultStats() {
        this.entitiesID = CommandResultStats.STRING_RESULT_TYPES;
        this.objectives = CommandResultStats.STRING_RESULT_TYPES;
    }

    public void setCommandStatForSender(MinecraftServer minecraftserver, final ICommandSender icommandlistener, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        String s = this.entitiesID[commandobjectiveexecutor_enumcommandresult.getTypeID()];

        if (s != null) {
            ICommandSender icommandlistener1 = new ICommandSender() {
                public String getName() {
                    return icommandlistener.getName();
                }

                public ITextComponent getDisplayName() {
                    return icommandlistener.getDisplayName();
                }

                public void sendMessage(ITextComponent ichatbasecomponent) {
                    icommandlistener.sendMessage(ichatbasecomponent);
                }

                public boolean canUseCommand(int i, String s) {
                    return true;
                }

                public BlockPos getPosition() {
                    return icommandlistener.getPosition();
                }

                public Vec3d getPositionVector() {
                    return icommandlistener.getPositionVector();
                }

                public World getEntityWorld() {
                    return icommandlistener.getEntityWorld();
                }

                public Entity getCommandSenderEntity() {
                    return icommandlistener.getCommandSenderEntity();
                }

                public boolean sendCommandFeedback() {
                    return icommandlistener.sendCommandFeedback();
                }

                public void setCommandStat(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                    icommandlistener.setCommandStat(commandobjectiveexecutor_enumcommandresult, i);
                }

                public MinecraftServer getServer() {
                    return icommandlistener.getServer();
                }
            };

            String s1;

            try {
                s1 = CommandBase.getEntityName(minecraftserver, icommandlistener1, s);
            } catch (CommandException commandexception) {
                return;
            }

            String s2 = this.objectives[commandobjectiveexecutor_enumcommandresult.getTypeID()];

            if (s2 != null) {
                Scoreboard scoreboard = icommandlistener.getEntityWorld().getScoreboard();
                ScoreObjective scoreboardobjective = scoreboard.getObjective(s2);

                if (scoreboardobjective != null) {
                    if (scoreboard.entityHasObjective(s1, scoreboardobjective)) {
                        Score scoreboardscore = scoreboard.getOrCreateScore(s1, scoreboardobjective);

                        scoreboardscore.setScorePoints(i);
                    }
                }
            }
        }
    }

    public void readStatsFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("CommandStats", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("CommandStats");
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];
                String s = commandobjectiveexecutor_enumcommandresult.getTypeName() + "Name";
                String s1 = commandobjectiveexecutor_enumcommandresult.getTypeName() + "Objective";

                if (nbttagcompound1.hasKey(s, 8) && nbttagcompound1.hasKey(s1, 8)) {
                    String s2 = nbttagcompound1.getString(s);
                    String s3 = nbttagcompound1.getString(s1);

                    setScoreBoardStat(this, commandobjectiveexecutor_enumcommandresult, s2, s3);
                }
            }

        }
    }

    public void writeStatsToNBT(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
        int i = acommandobjectiveexecutor_enumcommandresult.length;

        for (int j = 0; j < i; ++j) {
            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];
            String s = this.entitiesID[commandobjectiveexecutor_enumcommandresult.getTypeID()];
            String s1 = this.objectives[commandobjectiveexecutor_enumcommandresult.getTypeID()];

            if (s != null && s1 != null) {
                nbttagcompound1.setString(commandobjectiveexecutor_enumcommandresult.getTypeName() + "Name", s);
                nbttagcompound1.setString(commandobjectiveexecutor_enumcommandresult.getTypeName() + "Objective", s1);
            }
        }

        if (!nbttagcompound1.hasNoTags()) {
            nbttagcompound.setTag("CommandStats", nbttagcompound1);
        }

    }

    public static void setScoreBoardStat(CommandResultStats commandobjectiveexecutor, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, @Nullable String s, @Nullable String s1) {
        if (s != null && !s.isEmpty() && s1 != null && !s1.isEmpty()) {
            if (commandobjectiveexecutor.entitiesID == CommandResultStats.STRING_RESULT_TYPES || commandobjectiveexecutor.objectives == CommandResultStats.STRING_RESULT_TYPES) {
                commandobjectiveexecutor.entitiesID = new String[CommandResultStats.NUM_RESULT_TYPES];
                commandobjectiveexecutor.objectives = new String[CommandResultStats.NUM_RESULT_TYPES];
            }

            commandobjectiveexecutor.entitiesID[commandobjectiveexecutor_enumcommandresult.getTypeID()] = s;
            commandobjectiveexecutor.objectives[commandobjectiveexecutor_enumcommandresult.getTypeID()] = s1;
        } else {
            removeScoreBoardStat(commandobjectiveexecutor, commandobjectiveexecutor_enumcommandresult);
        }
    }

    private static void removeScoreBoardStat(CommandResultStats commandobjectiveexecutor, CommandResultStats.Type commandobjectiveexecutor_enumcommandresult) {
        if (commandobjectiveexecutor.entitiesID != CommandResultStats.STRING_RESULT_TYPES && commandobjectiveexecutor.objectives != CommandResultStats.STRING_RESULT_TYPES) {
            commandobjectiveexecutor.entitiesID[commandobjectiveexecutor_enumcommandresult.getTypeID()] = null;
            commandobjectiveexecutor.objectives[commandobjectiveexecutor_enumcommandresult.getTypeID()] = null;
            boolean flag = true;
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult1 = acommandobjectiveexecutor_enumcommandresult[j];

                if (commandobjectiveexecutor.entitiesID[commandobjectiveexecutor_enumcommandresult1.getTypeID()] != null && commandobjectiveexecutor.objectives[commandobjectiveexecutor_enumcommandresult1.getTypeID()] != null) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                commandobjectiveexecutor.entitiesID = CommandResultStats.STRING_RESULT_TYPES;
                commandobjectiveexecutor.objectives = CommandResultStats.STRING_RESULT_TYPES;
            }

        }
    }

    public void addAllStats(CommandResultStats commandobjectiveexecutor) {
        CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = CommandResultStats.Type.values();
        int i = acommandobjectiveexecutor_enumcommandresult.length;

        for (int j = 0; j < i; ++j) {
            CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];

            setScoreBoardStat(this, commandobjectiveexecutor_enumcommandresult, commandobjectiveexecutor.entitiesID[commandobjectiveexecutor_enumcommandresult.getTypeID()], commandobjectiveexecutor.objectives[commandobjectiveexecutor_enumcommandresult.getTypeID()]);
        }

    }

    public static enum Type {

        SUCCESS_COUNT(0, "SuccessCount"), AFFECTED_BLOCKS(1, "AffectedBlocks"), AFFECTED_ENTITIES(2, "AffectedEntities"), AFFECTED_ITEMS(3, "AffectedItems"), QUERY_RESULT(4, "QueryResult");

        final int typeID;
        final String typeName;

        private Type(int i, String s) {
            this.typeID = i;
            this.typeName = s;
        }

        public int getTypeID() {
            return this.typeID;
        }

        public String getTypeName() {
            return this.typeName;
        }

        public static String[] getTypeNames() {
            String[] astring = new String[values().length];
            int i = 0;
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = values();
            int j = acommandobjectiveexecutor_enumcommandresult.length;

            for (int k = 0; k < j; ++k) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[k];

                astring[i++] = commandobjectiveexecutor_enumcommandresult.getTypeName();
            }

            return astring;
        }

        @Nullable
        public static CommandResultStats.Type getTypeByName(String s) {
            CommandResultStats.Type[] acommandobjectiveexecutor_enumcommandresult = values();
            int i = acommandobjectiveexecutor_enumcommandresult.length;

            for (int j = 0; j < i; ++j) {
                CommandResultStats.Type commandobjectiveexecutor_enumcommandresult = acommandobjectiveexecutor_enumcommandresult[j];

                if (commandobjectiveexecutor_enumcommandresult.getTypeName().equals(s)) {
                    return commandobjectiveexecutor_enumcommandresult;
                }
            }

            return null;
        }
    }
}
