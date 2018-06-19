package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;

public class ScoreboardSaveData extends WorldSavedData {

    private static final Logger field_151481_a = LogManager.getLogger();
    private Scoreboard field_96507_a;
    private NBTTagCompound field_96506_b;

    public ScoreboardSaveData() {
        this("scoreboard");
    }

    public ScoreboardSaveData(String s) {
        super(s);
    }

    public void func_96499_a(Scoreboard scoreboard) {
        this.field_96507_a = scoreboard;
        if (this.field_96506_b != null) {
            this.func_76184_a(this.field_96506_b);
        }

    }

    public void func_76184_a(NBTTagCompound nbttagcompound) {
        if (this.field_96507_a == null) {
            this.field_96506_b = nbttagcompound;
        } else {
            this.func_96501_b(nbttagcompound.func_150295_c("Objectives", 10));
            this.func_96500_c(nbttagcompound.func_150295_c("PlayerScores", 10));
            if (nbttagcompound.func_150297_b("DisplaySlots", 10)) {
                this.func_96504_c(nbttagcompound.func_74775_l("DisplaySlots"));
            }

            if (nbttagcompound.func_150297_b("Teams", 9)) {
                this.func_96498_a(nbttagcompound.func_150295_c("Teams", 10));
            }

        }
    }

    protected void func_96498_a(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            String s = nbttagcompound.func_74779_i("Name");

            if (s.length() > 16) {
                s = s.substring(0, 16);
            }

            ScorePlayerTeam scoreboardteam = this.field_96507_a.func_96527_f(s);
            String s1 = nbttagcompound.func_74779_i("DisplayName");

            if (s1.length() > 32) {
                s1 = s1.substring(0, 32);
            }

            scoreboardteam.func_96664_a(s1);
            if (nbttagcompound.func_150297_b("TeamColor", 8)) {
                scoreboardteam.func_178774_a(TextFormatting.func_96300_b(nbttagcompound.func_74779_i("TeamColor")));
            }

            scoreboardteam.func_96666_b(nbttagcompound.func_74779_i("Prefix"));
            scoreboardteam.func_96662_c(nbttagcompound.func_74779_i("Suffix"));
            if (nbttagcompound.func_150297_b("AllowFriendlyFire", 99)) {
                scoreboardteam.func_96660_a(nbttagcompound.func_74767_n("AllowFriendlyFire"));
            }

            if (nbttagcompound.func_150297_b("SeeFriendlyInvisibles", 99)) {
                scoreboardteam.func_98300_b(nbttagcompound.func_74767_n("SeeFriendlyInvisibles"));
            }

            Team.EnumVisible scoreboardteambase_enumnametagvisibility;

            if (nbttagcompound.func_150297_b("NameTagVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = Team.EnumVisible.func_178824_a(nbttagcompound.func_74779_i("NameTagVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.func_178772_a(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.func_150297_b("DeathMessageVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = Team.EnumVisible.func_178824_a(nbttagcompound.func_74779_i("DeathMessageVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.func_178773_b(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.func_150297_b("CollisionRule", 8)) {
                Team.CollisionRule scoreboardteambase_enumteampush = Team.CollisionRule.func_186686_a(nbttagcompound.func_74779_i("CollisionRule"));

                if (scoreboardteambase_enumteampush != null) {
                    scoreboardteam.func_186682_a(scoreboardteambase_enumteampush);
                }
            }

            this.func_96502_a(scoreboardteam, nbttagcompound.func_150295_c("Players", 8));
        }

    }

    protected void func_96502_a(ScorePlayerTeam scoreboardteam, NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            this.field_96507_a.func_151392_a(nbttaglist.func_150307_f(i), scoreboardteam.func_96661_b());
        }

    }

    protected void func_96504_c(NBTTagCompound nbttagcompound) {
        for (int i = 0; i < 19; ++i) {
            if (nbttagcompound.func_150297_b("slot_" + i, 8)) {
                String s = nbttagcompound.func_74779_i("slot_" + i);
                ScoreObjective scoreboardobjective = this.field_96507_a.func_96518_b(s);

                this.field_96507_a.func_96530_a(i, scoreboardobjective);
            }
        }

    }

    protected void func_96501_b(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            IScoreCriteria iscoreboardcriteria = (IScoreCriteria) IScoreCriteria.field_96643_a.get(nbttagcompound.func_74779_i("CriteriaName"));

            if (iscoreboardcriteria != null) {
                String s = nbttagcompound.func_74779_i("Name");

                if (s.length() > 16) {
                    s = s.substring(0, 16);
                }

                ScoreObjective scoreboardobjective = this.field_96507_a.func_96535_a(s, iscoreboardcriteria);

                scoreboardobjective.func_96681_a(nbttagcompound.func_74779_i("DisplayName"));
                scoreboardobjective.func_178767_a(IScoreCriteria.EnumRenderType.func_178795_a(nbttagcompound.func_74779_i("RenderType")));
            }
        }

    }

    protected void func_96500_c(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            ScoreObjective scoreboardobjective = this.field_96507_a.func_96518_b(nbttagcompound.func_74779_i("Objective"));
            String s = nbttagcompound.func_74779_i("Name");

            if (s.length() > 40) {
                s = s.substring(0, 40);
            }

            Score scoreboardscore = this.field_96507_a.func_96529_a(s, scoreboardobjective);

            scoreboardscore.func_96647_c(nbttagcompound.func_74762_e("Score"));
            if (nbttagcompound.func_74764_b("Locked")) {
                scoreboardscore.func_178815_a(nbttagcompound.func_74767_n("Locked"));
            }
        }

    }

    public NBTTagCompound func_189551_b(NBTTagCompound nbttagcompound) {
        if (this.field_96507_a == null) {
            ScoreboardSaveData.field_151481_a.warn("Tried to save scoreboard without having a scoreboard...");
            return nbttagcompound;
        } else {
            nbttagcompound.func_74782_a("Objectives", this.func_96505_b());
            nbttagcompound.func_74782_a("PlayerScores", this.func_96503_e());
            nbttagcompound.func_74782_a("Teams", this.func_96496_a());
            this.func_96497_d(nbttagcompound);
            return nbttagcompound;
        }
    }

    protected NBTTagList func_96496_a() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.field_96507_a.func_96525_g();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();
            if (!com.destroystokyo.paper.PaperConfig.saveEmptyScoreboardTeams && scoreboardteam.func_96670_d().isEmpty()) continue; // Paper
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.func_74778_a("Name", scoreboardteam.func_96661_b());
            nbttagcompound.func_74778_a("DisplayName", scoreboardteam.func_96669_c());
            if (scoreboardteam.func_178775_l().func_175746_b() >= 0) {
                nbttagcompound.func_74778_a("TeamColor", scoreboardteam.func_178775_l().func_96297_d());
            }

            nbttagcompound.func_74778_a("Prefix", scoreboardteam.func_96668_e());
            nbttagcompound.func_74778_a("Suffix", scoreboardteam.func_96663_f());
            nbttagcompound.func_74757_a("AllowFriendlyFire", scoreboardteam.func_96665_g());
            nbttagcompound.func_74757_a("SeeFriendlyInvisibles", scoreboardteam.func_98297_h());
            nbttagcompound.func_74778_a("NameTagVisibility", scoreboardteam.func_178770_i().field_178830_e);
            nbttagcompound.func_74778_a("DeathMessageVisibility", scoreboardteam.func_178771_j().field_178830_e);
            nbttagcompound.func_74778_a("CollisionRule", scoreboardteam.func_186681_k().field_186693_e);
            NBTTagList nbttaglist1 = new NBTTagList();
            Iterator iterator1 = scoreboardteam.func_96670_d().iterator();

            while (iterator1.hasNext()) {
                String s = (String) iterator1.next();

                nbttaglist1.func_74742_a(new NBTTagString(s));
            }

            nbttagcompound.func_74782_a("Players", nbttaglist1);
            nbttaglist.func_74742_a(nbttagcompound);
        }

        return nbttaglist;
    }

    protected void func_96497_d(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        boolean flag = false;

        for (int i = 0; i < 19; ++i) {
            ScoreObjective scoreboardobjective = this.field_96507_a.func_96539_a(i);

            if (scoreboardobjective != null) {
                nbttagcompound1.func_74778_a("slot_" + i, scoreboardobjective.func_96679_b());
                flag = true;
            }
        }

        if (flag) {
            nbttagcompound.func_74782_a("DisplaySlots", nbttagcompound1);
        }

    }

    protected NBTTagList func_96505_b() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.field_96507_a.func_96514_c();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (scoreboardobjective.func_96680_c() != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.func_74778_a("Name", scoreboardobjective.func_96679_b());
                nbttagcompound.func_74778_a("CriteriaName", scoreboardobjective.func_96680_c().func_96636_a());
                nbttagcompound.func_74778_a("DisplayName", scoreboardobjective.func_96678_d());
                nbttagcompound.func_74778_a("RenderType", scoreboardobjective.func_178766_e().func_178796_a());
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    protected NBTTagList func_96503_e() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.field_96507_a.func_96528_e();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next();

            if (scoreboardscore.func_96645_d() != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.func_74778_a("Name", scoreboardscore.func_96653_e());
                nbttagcompound.func_74778_a("Objective", scoreboardscore.func_96645_d().func_96679_b());
                nbttagcompound.func_74768_a("Score", scoreboardscore.func_96652_c());
                nbttagcompound.func_74757_a("Locked", scoreboardscore.func_178816_g());
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        return nbttaglist;
    }
}
