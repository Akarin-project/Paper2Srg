package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.text.TextFormatting;

public abstract class Team {

    public Team() {}

    public boolean func_142054_a(@Nullable Team scoreboardteambase) {
        return scoreboardteambase == null ? false : this == scoreboardteambase;
    }

    public abstract String func_96661_b();

    public abstract String func_142053_d(String s);

    public abstract boolean func_96665_g();

    public abstract TextFormatting func_178775_l();

    public abstract Collection<String> func_96670_d();

    public abstract Team.EnumVisible func_178771_j();

    public abstract Team.CollisionRule func_186681_k();

    public static enum CollisionRule {

        ALWAYS("always", 0), NEVER("never", 1), HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2), HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);

        private static final Map<String, Team.CollisionRule> field_186695_g = Maps.newHashMap();
        public final String field_186693_e;
        public final int field_186694_f;

        public static String[] func_186687_a() {
            return (String[]) Team.CollisionRule.field_186695_g.keySet().toArray(new String[Team.CollisionRule.field_186695_g.size()]);
        }

        @Nullable
        public static Team.CollisionRule func_186686_a(String s) {
            return (Team.CollisionRule) Team.CollisionRule.field_186695_g.get(s);
        }

        private CollisionRule(String s, int i) {
            this.field_186693_e = s;
            this.field_186694_f = i;
        }

        static {
            Team.CollisionRule[] ascoreboardteambase_enumteampush = values();
            int i = ascoreboardteambase_enumteampush.length;

            for (int j = 0; j < i; ++j) {
                Team.CollisionRule scoreboardteambase_enumteampush = ascoreboardteambase_enumteampush[j];

                Team.CollisionRule.field_186695_g.put(scoreboardteambase_enumteampush.field_186693_e, scoreboardteambase_enumteampush);
            }

        }
    }

    public static enum EnumVisible {

        ALWAYS("always", 0), NEVER("never", 1), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2), HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

        private static final Map<String, Team.EnumVisible> field_186697_g = Maps.newHashMap();
        public final String field_178830_e;
        public final int field_178827_f;

        public static String[] func_178825_a() {
            return (String[]) Team.EnumVisible.field_186697_g.keySet().toArray(new String[Team.EnumVisible.field_186697_g.size()]);
        }

        @Nullable
        public static Team.EnumVisible func_178824_a(String s) {
            return (Team.EnumVisible) Team.EnumVisible.field_186697_g.get(s);
        }

        private EnumVisible(String s, int i) {
            this.field_178830_e = s;
            this.field_178827_f = i;
        }

        static {
            Team.EnumVisible[] ascoreboardteambase_enumnametagvisibility = values();
            int i = ascoreboardteambase_enumnametagvisibility.length;

            for (int j = 0; j < i; ++j) {
                Team.EnumVisible scoreboardteambase_enumnametagvisibility = ascoreboardteambase_enumnametagvisibility[j];

                Team.EnumVisible.field_186697_g.put(scoreboardteambase_enumnametagvisibility.field_178830_e, scoreboardteambase_enumnametagvisibility);
            }

        }
    }
}
