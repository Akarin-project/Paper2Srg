package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.text.TextFormatting;

public abstract class Team {

    public Team() {}

    public boolean isSameTeam(@Nullable Team scoreboardteambase) {
        return scoreboardteambase == null ? false : this == scoreboardteambase;
    }

    public abstract String getName();

    public abstract String formatString(String s);

    public abstract boolean getAllowFriendlyFire();

    public abstract TextFormatting getColor();

    public abstract Collection<String> getMembershipCollection();

    public abstract Team.EnumVisible getDeathMessageVisibility();

    public abstract Team.CollisionRule getCollisionRule();

    public static enum CollisionRule {

        ALWAYS("always", 0), NEVER("never", 1), HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2), HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);

        private static final Map<String, Team.CollisionRule> nameMap = Maps.newHashMap();
        public final String name;
        public final int id;

        public static String[] getNames() {
            return (String[]) Team.CollisionRule.nameMap.keySet().toArray(new String[Team.CollisionRule.nameMap.size()]);
        }

        @Nullable
        public static Team.CollisionRule getByName(String s) {
            return (Team.CollisionRule) Team.CollisionRule.nameMap.get(s);
        }

        private CollisionRule(String s, int i) {
            this.name = s;
            this.id = i;
        }

        static {
            Team.CollisionRule[] ascoreboardteambase_enumteampush = values();
            int i = ascoreboardteambase_enumteampush.length;

            for (int j = 0; j < i; ++j) {
                Team.CollisionRule scoreboardteambase_enumteampush = ascoreboardteambase_enumteampush[j];

                Team.CollisionRule.nameMap.put(scoreboardteambase_enumteampush.name, scoreboardteambase_enumteampush);
            }

        }
    }

    public static enum EnumVisible {

        ALWAYS("always", 0), NEVER("never", 1), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2), HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

        private static final Map<String, Team.EnumVisible> nameMap = Maps.newHashMap();
        public final String internalName;
        public final int id;

        public static String[] getNames() {
            return (String[]) Team.EnumVisible.nameMap.keySet().toArray(new String[Team.EnumVisible.nameMap.size()]);
        }

        @Nullable
        public static Team.EnumVisible getByName(String s) {
            return (Team.EnumVisible) Team.EnumVisible.nameMap.get(s);
        }

        private EnumVisible(String s, int i) {
            this.internalName = s;
            this.id = i;
        }

        static {
            Team.EnumVisible[] ascoreboardteambase_enumnametagvisibility = values();
            int i = ascoreboardteambase_enumnametagvisibility.length;

            for (int j = 0; j < i; ++j) {
                Team.EnumVisible scoreboardteambase_enumnametagvisibility = ascoreboardteambase_enumnametagvisibility[j];

                Team.EnumVisible.nameMap.put(scoreboardteambase_enumnametagvisibility.internalName, scoreboardteambase_enumnametagvisibility);
            }

        }
    }
}
