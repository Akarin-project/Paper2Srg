package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class Scoreboard {

    private final Map<String, ScoreObjective> field_96545_a = Maps.newHashMap();
    private final Map<IScoreCriteria, List<ScoreObjective>> field_96543_b = Maps.newHashMap();
    private final Map<String, Map<ScoreObjective, Score>> field_96544_c = Maps.newHashMap();
    private final ScoreObjective[] field_96541_d = new ScoreObjective[19];
    private final Map<String, ScorePlayerTeam> field_96542_e = Maps.newHashMap();
    private final Map<String, ScorePlayerTeam> field_96540_f = Maps.newHashMap();
    private static String[] field_178823_g;

    public Scoreboard() {}

    @Nullable
    public ScoreObjective func_96518_b(String s) {
        return (ScoreObjective) this.field_96545_a.get(s);
    }

    public ScoreObjective func_96535_a(String s, IScoreCriteria iscoreboardcriteria) {
        if (s.length() > 16) {
            throw new IllegalArgumentException("The objective name \'" + s + "\' is too long!");
        } else {
            ScoreObjective scoreboardobjective = this.func_96518_b(s);

            if (scoreboardobjective != null) {
                throw new IllegalArgumentException("An objective with the name \'" + s + "\' already exists!");
            } else {
                scoreboardobjective = new ScoreObjective(this, s, iscoreboardcriteria);
                Object object = (List) this.field_96543_b.get(iscoreboardcriteria);

                if (object == null) {
                    object = Lists.newArrayList();
                    this.field_96543_b.put(iscoreboardcriteria, object);
                }

                ((List) object).add(scoreboardobjective);
                this.field_96545_a.put(s, scoreboardobjective);
                this.func_96522_a(scoreboardobjective);
                return scoreboardobjective;
            }
        }
    }

    public Collection<ScoreObjective> func_96520_a(IScoreCriteria iscoreboardcriteria) {
        Collection collection = (Collection) this.field_96543_b.get(iscoreboardcriteria);

        return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
    }

    public boolean func_178819_b(String s, ScoreObjective scoreboardobjective) {
        Map map = (Map) this.field_96544_c.get(s);

        if (map == null) {
            return false;
        } else {
            Score scoreboardscore = (Score) map.get(scoreboardobjective);

            return scoreboardscore != null;
        }
    }

    public Score func_96529_a(String s, ScoreObjective scoreboardobjective) {
        if (s.length() > 40) {
            throw new IllegalArgumentException("The player name \'" + s + "\' is too long!");
        } else {
            Object object = (Map) this.field_96544_c.get(s);

            if (object == null) {
                object = Maps.newHashMap();
                this.field_96544_c.put(s, object);
            }

            Score scoreboardscore = (Score) ((Map) object).get(scoreboardobjective);

            if (scoreboardscore == null) {
                scoreboardscore = new Score(this, scoreboardobjective, s);
                ((Map) object).put(scoreboardobjective, scoreboardscore);
            }

            return scoreboardscore;
        }
    }

    public Collection<Score> func_96534_i(ScoreObjective scoreboardobjective) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_96544_c.values().iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();
            Score scoreboardscore = (Score) map.get(scoreboardobjective);

            if (scoreboardscore != null) {
                arraylist.add(scoreboardscore);
            }
        }

        Collections.sort(arraylist, Score.field_96658_a);
        return arraylist;
    }

    public Collection<ScoreObjective> func_96514_c() {
        return this.field_96545_a.values();
    }

    public Collection<String> func_96526_d() {
        return this.field_96544_c.keySet();
    }

    public void func_178822_d(String s, ScoreObjective scoreboardobjective) {
        Map map;

        if (scoreboardobjective == null) {
            map = (Map) this.field_96544_c.remove(s);
            if (map != null) {
                this.func_96516_a(s);
            }
        } else {
            map = (Map) this.field_96544_c.get(s);
            if (map != null) {
                Score scoreboardscore = (Score) map.remove(scoreboardobjective);

                if (map.size() < 1) {
                    Map map1 = (Map) this.field_96544_c.remove(s);

                    if (map1 != null) {
                        this.func_96516_a(s);
                    }
                } else if (scoreboardscore != null) {
                    this.func_178820_a(s, scoreboardobjective);
                }
            }
        }

    }

    public Collection<Score> func_96528_e() {
        Collection collection = this.field_96544_c.values();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();

            arraylist.addAll(map.values());
        }

        return arraylist;
    }

    public Map<ScoreObjective, Score> func_96510_d(String s) {
        Object object = (Map) this.field_96544_c.get(s);

        if (object == null) {
            object = Maps.newHashMap();
        }

        return (Map) object;
    }

    public void func_96519_k(ScoreObjective scoreboardobjective) {
        this.field_96545_a.remove(scoreboardobjective.func_96679_b());

        for (int i = 0; i < 19; ++i) {
            if (this.func_96539_a(i) == scoreboardobjective) {
                this.func_96530_a(i, (ScoreObjective) null);
            }
        }

        List list = (List) this.field_96543_b.get(scoreboardobjective.func_96680_c());

        if (list != null) {
            list.remove(scoreboardobjective);
        }

        Iterator iterator = this.field_96544_c.values().iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();

            map.remove(scoreboardobjective);
        }

        this.func_96533_c(scoreboardobjective);
    }

    public void func_96530_a(int i, ScoreObjective scoreboardobjective) {
        this.field_96541_d[i] = scoreboardobjective;
    }

    @Nullable
    public ScoreObjective func_96539_a(int i) {
        return this.field_96541_d[i];
    }

    public ScorePlayerTeam func_96508_e(String s) {
        return (ScorePlayerTeam) this.field_96542_e.get(s);
    }

    public ScorePlayerTeam func_96527_f(String s) {
        if (s.length() > 16) {
            throw new IllegalArgumentException("The team name \'" + s + "\' is too long!");
        } else {
            ScorePlayerTeam scoreboardteam = this.func_96508_e(s);

            if (scoreboardteam != null) {
                throw new IllegalArgumentException("A team with the name \'" + s + "\' already exists!");
            } else {
                scoreboardteam = new ScorePlayerTeam(this, s);
                this.field_96542_e.put(s, scoreboardteam);
                this.func_96523_a(scoreboardteam);
                return scoreboardteam;
            }
        }
    }

    public void func_96511_d(ScorePlayerTeam scoreboardteam) {
        this.field_96542_e.remove(scoreboardteam.func_96661_b());
        Iterator iterator = scoreboardteam.func_96670_d().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.field_96540_f.remove(s);
        }

        this.func_96513_c(scoreboardteam);
    }

    public boolean func_151392_a(String s, String s1) {
        if (s.length() > 40) {
            throw new IllegalArgumentException("The player name \'" + s + "\' is too long!");
        } else if (!this.field_96542_e.containsKey(s1)) {
            return false;
        } else {
            ScorePlayerTeam scoreboardteam = this.func_96508_e(s1);

            if (this.func_96509_i(s) != null) {
                this.func_96524_g(s);
            }

            this.field_96540_f.put(s, scoreboardteam);
            scoreboardteam.func_96670_d().add(s);
            return true;
        }
    }

    public boolean func_96524_g(String s) {
        ScorePlayerTeam scoreboardteam = this.func_96509_i(s);

        if (scoreboardteam != null) {
            this.func_96512_b(s, scoreboardteam);
            return true;
        } else {
            return false;
        }
    }

    public void func_96512_b(String s, ScorePlayerTeam scoreboardteam) {
        if (this.func_96509_i(s) != scoreboardteam) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + scoreboardteam.func_96661_b() + "\'.");
        } else {
            this.field_96540_f.remove(s);
            scoreboardteam.func_96670_d().remove(s);
        }
    }

    public Collection<String> func_96531_f() {
        return this.field_96542_e.keySet();
    }

    public Collection<ScorePlayerTeam> func_96525_g() {
        return this.field_96542_e.values();
    }

    @Nullable
    public ScorePlayerTeam func_96509_i(String s) {
        return (ScorePlayerTeam) this.field_96540_f.get(s);
    }

    public void func_96522_a(ScoreObjective scoreboardobjective) {}

    public void func_96532_b(ScoreObjective scoreboardobjective) {}

    public void func_96533_c(ScoreObjective scoreboardobjective) {}

    public void func_96536_a(Score scoreboardscore) {}

    public void func_96516_a(String s) {}

    public void func_178820_a(String s, ScoreObjective scoreboardobjective) {}

    public void func_96523_a(ScorePlayerTeam scoreboardteam) {}

    public void func_96538_b(ScorePlayerTeam scoreboardteam) {}

    public void func_96513_c(ScorePlayerTeam scoreboardteam) {}

    public static String func_96517_b(int i) {
        switch (i) {
        case 0:
            return "list";

        case 1:
            return "sidebar";

        case 2:
            return "belowName";

        default:
            if (i >= 3 && i <= 18) {
                TextFormatting enumchatformat = TextFormatting.func_175744_a(i - 3);

                if (enumchatformat != null && enumchatformat != TextFormatting.RESET) {
                    return "sidebar.team." + enumchatformat.func_96297_d();
                }
            }

            return null;
        }
    }

    public static int func_96537_j(String s) {
        if ("list".equalsIgnoreCase(s)) {
            return 0;
        } else if ("sidebar".equalsIgnoreCase(s)) {
            return 1;
        } else if ("belowName".equalsIgnoreCase(s)) {
            return 2;
        } else {
            if (s.startsWith("sidebar.team.")) {
                String s1 = s.substring("sidebar.team.".length());
                TextFormatting enumchatformat = TextFormatting.func_96300_b(s1);

                if (enumchatformat != null && enumchatformat.func_175746_b() >= 0) {
                    return enumchatformat.func_175746_b() + 3;
                }
            }

            return -1;
        }
    }

    public static String[] func_178821_h() {
        if (Scoreboard.field_178823_g == null) {
            Scoreboard.field_178823_g = new String[19];

            for (int i = 0; i < 19; ++i) {
                Scoreboard.field_178823_g[i] = func_96517_b(i);
            }
        }

        return Scoreboard.field_178823_g;
    }

    public void func_181140_a(Entity entity) {
        if (entity != null && !(entity instanceof EntityPlayer) && !entity.func_70089_S()) {
            String s = entity.func_189512_bd();

            this.func_178822_d(s, (ScoreObjective) null);
            this.func_96524_g(s);
        }
    }
}
