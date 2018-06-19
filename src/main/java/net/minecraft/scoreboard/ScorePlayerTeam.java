package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.util.text.TextFormatting;

public class ScorePlayerTeam extends Team {

    private final Scoreboard field_96677_a;
    private final String field_96675_b;
    private final Set<String> field_96676_c = Sets.newHashSet();
    private String field_96673_d;
    private String field_96674_e = "";
    private String field_96671_f = "";
    private boolean field_96672_g = true;
    private boolean field_98301_h = true;
    private Team.EnumVisible field_178778_i;
    private Team.EnumVisible field_178776_j;
    private TextFormatting field_178777_k;
    private Team.CollisionRule field_186683_l;

    public ScorePlayerTeam(Scoreboard scoreboard, String s) {
        this.field_178778_i = Team.EnumVisible.ALWAYS;
        this.field_178776_j = Team.EnumVisible.ALWAYS;
        this.field_178777_k = TextFormatting.RESET;
        this.field_186683_l = Team.CollisionRule.ALWAYS;
        this.field_96677_a = scoreboard;
        this.field_96675_b = s;
        this.field_96673_d = s;
    }

    public String func_96661_b() {
        return this.field_96675_b;
    }

    public String func_96669_c() {
        return this.field_96673_d;
    }

    public void func_96664_a(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            this.field_96673_d = s;
            this.field_96677_a.func_96538_b(this);
        }
    }

    public Collection<String> func_96670_d() {
        return this.field_96676_c;
    }

    public String func_96668_e() {
        return this.field_96674_e;
    }

    public void func_96666_b(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        } else {
            this.field_96674_e = s;
            this.field_96677_a.func_96538_b(this);
        }
    }

    public String func_96663_f() {
        return this.field_96671_f;
    }

    public void func_96662_c(String s) {
        this.field_96671_f = s;
        this.field_96677_a.func_96538_b(this);
    }

    public String func_142053_d(String s) {
        return this.func_96668_e() + s + this.func_96663_f();
    }

    public static String func_96667_a(@Nullable Team scoreboardteambase, String s) {
        return scoreboardteambase == null ? s : scoreboardteambase.func_142053_d(s);
    }

    public boolean func_96665_g() {
        return this.field_96672_g;
    }

    public void func_96660_a(boolean flag) {
        this.field_96672_g = flag;
        this.field_96677_a.func_96538_b(this);
    }

    public boolean func_98297_h() {
        return this.field_98301_h;
    }

    public void func_98300_b(boolean flag) {
        this.field_98301_h = flag;
        this.field_96677_a.func_96538_b(this);
    }

    public Team.EnumVisible func_178770_i() {
        return this.field_178778_i;
    }

    public Team.EnumVisible func_178771_j() {
        return this.field_178776_j;
    }

    public void func_178772_a(Team.EnumVisible scoreboardteambase_enumnametagvisibility) {
        this.field_178778_i = scoreboardteambase_enumnametagvisibility;
        this.field_96677_a.func_96538_b(this);
    }

    public void func_178773_b(Team.EnumVisible scoreboardteambase_enumnametagvisibility) {
        this.field_178776_j = scoreboardteambase_enumnametagvisibility;
        this.field_96677_a.func_96538_b(this);
    }

    public Team.CollisionRule func_186681_k() {
        return this.field_186683_l;
    }

    public void func_186682_a(Team.CollisionRule scoreboardteambase_enumteampush) {
        this.field_186683_l = scoreboardteambase_enumteampush;
        this.field_96677_a.func_96538_b(this);
    }

    public int func_98299_i() {
        int i = 0;

        if (this.func_96665_g()) {
            i |= 1;
        }

        if (this.func_98297_h()) {
            i |= 2;
        }

        return i;
    }

    public void func_178774_a(TextFormatting enumchatformat) {
        this.field_178777_k = enumchatformat;
    }

    public TextFormatting func_178775_l() {
        return this.field_178777_k;
    }
}
