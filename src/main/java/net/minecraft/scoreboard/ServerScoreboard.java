package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard {

    private final MinecraftServer field_96555_a;
    private final Set<ScoreObjective> field_96553_b = Sets.newHashSet();
    private Runnable[] field_186685_c = new Runnable[0];

    public ServerScoreboard(MinecraftServer minecraftserver) {
        this.field_96555_a = minecraftserver;
    }

    public void func_96536_a(Score scoreboardscore) {
        super.func_96536_a(scoreboardscore);
        if (this.field_96553_b.contains(scoreboardscore.func_96645_d())) {
            this.sendAll(new SPacketUpdateScore(scoreboardscore));
        }

        this.func_96551_b();
    }

    public void func_96516_a(String s) {
        super.func_96516_a(s);
        this.sendAll(new SPacketUpdateScore(s));
        this.func_96551_b();
    }

    public void func_178820_a(String s, ScoreObjective scoreboardobjective) {
        super.func_178820_a(s, scoreboardobjective);
        this.sendAll(new SPacketUpdateScore(s, scoreboardobjective));
        this.func_96551_b();
    }

    public void func_96530_a(int i, ScoreObjective scoreboardobjective) {
        ScoreObjective scoreboardobjective1 = this.func_96539_a(i);

        super.func_96530_a(i, scoreboardobjective);
        if (scoreboardobjective1 != scoreboardobjective && scoreboardobjective1 != null) {
            if (this.func_96552_h(scoreboardobjective1) > 0) {
                this.sendAll(new SPacketDisplayObjective(i, scoreboardobjective));
            } else {
                this.func_96546_g(scoreboardobjective1);
            }
        }

        if (scoreboardobjective != null) {
            if (this.field_96553_b.contains(scoreboardobjective)) {
                this.sendAll(new SPacketDisplayObjective(i, scoreboardobjective));
            } else {
                this.func_96549_e(scoreboardobjective);
            }
        }

        this.func_96551_b();
    }

    public boolean func_151392_a(String s, String s1) {
        if (super.func_151392_a(s, s1)) {
            ScorePlayerTeam scoreboardteam = this.func_96508_e(s1);

            this.sendAll(new SPacketTeams(scoreboardteam, Arrays.asList(new String[] { s}), 3));
            this.func_96551_b();
            return true;
        } else {
            return false;
        }
    }

    public void func_96512_b(String s, ScorePlayerTeam scoreboardteam) {
        super.func_96512_b(s, scoreboardteam);
        this.sendAll(new SPacketTeams(scoreboardteam, Arrays.asList(new String[] { s}), 4));
        this.func_96551_b();
    }

    public void func_96522_a(ScoreObjective scoreboardobjective) {
        super.func_96522_a(scoreboardobjective);
        this.func_96551_b();
    }

    public void func_96532_b(ScoreObjective scoreboardobjective) {
        super.func_96532_b(scoreboardobjective);
        if (this.field_96553_b.contains(scoreboardobjective)) {
            this.sendAll(new SPacketScoreboardObjective(scoreboardobjective, 2));
        }

        this.func_96551_b();
    }

    public void func_96533_c(ScoreObjective scoreboardobjective) {
        super.func_96533_c(scoreboardobjective);
        if (this.field_96553_b.contains(scoreboardobjective)) {
            this.func_96546_g(scoreboardobjective);
        }

        this.func_96551_b();
    }

    public void func_96523_a(ScorePlayerTeam scoreboardteam) {
        super.func_96523_a(scoreboardteam);
        this.sendAll(new SPacketTeams(scoreboardteam, 0));
        this.func_96551_b();
    }

    public void func_96538_b(ScorePlayerTeam scoreboardteam) {
        super.func_96538_b(scoreboardteam);
        this.sendAll(new SPacketTeams(scoreboardteam, 2));
        this.func_96551_b();
    }

    public void func_96513_c(ScorePlayerTeam scoreboardteam) {
        super.func_96513_c(scoreboardteam);
        this.sendAll(new SPacketTeams(scoreboardteam, 1));
        this.func_96551_b();
    }

    public void func_186684_a(Runnable runnable) {
        this.field_186685_c = (Runnable[]) Arrays.copyOf(this.field_186685_c, this.field_186685_c.length + 1);
        this.field_186685_c[this.field_186685_c.length - 1] = runnable;
    }

    protected void func_96551_b() {
        Runnable[] arunnable = this.field_186685_c;
        int i = arunnable.length;

        for (int j = 0; j < i; ++j) {
            Runnable runnable = arunnable[j];

            runnable.run();
        }

    }

    public List<Packet<?>> func_96550_d(ScoreObjective scoreboardobjective) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.add(new SPacketScoreboardObjective(scoreboardobjective, 0));

        for (int i = 0; i < 19; ++i) {
            if (this.func_96539_a(i) == scoreboardobjective) {
                arraylist.add(new SPacketDisplayObjective(i, scoreboardobjective));
            }
        }

        Iterator iterator = this.func_96534_i(scoreboardobjective).iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next();

            arraylist.add(new SPacketUpdateScore(scoreboardscore));
        }

        return arraylist;
    }

    public void func_96549_e(ScoreObjective scoreboardobjective) {
        List list = this.func_96550_d(scoreboardobjective);
        Iterator iterator = this.field_96555_a.func_184103_al().func_181057_v().iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() != this) continue; // CraftBukkit - Only players on this board
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                Packet packet = (Packet) iterator1.next();

                entityplayer.field_71135_a.func_147359_a(packet);
            }
        }

        this.field_96553_b.add(scoreboardobjective);
    }

    public List<Packet<?>> func_96548_f(ScoreObjective scoreboardobjective) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.add(new SPacketScoreboardObjective(scoreboardobjective, 1));

        for (int i = 0; i < 19; ++i) {
            if (this.func_96539_a(i) == scoreboardobjective) {
                arraylist.add(new SPacketDisplayObjective(i, scoreboardobjective));
            }
        }

        return arraylist;
    }

    public void func_96546_g(ScoreObjective scoreboardobjective) {
        List list = this.func_96548_f(scoreboardobjective);
        Iterator iterator = this.field_96555_a.func_184103_al().func_181057_v().iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() != this) continue; // CraftBukkit - Only players on this board
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                Packet packet = (Packet) iterator1.next();

                entityplayer.field_71135_a.func_147359_a(packet);
            }
        }

        this.field_96553_b.remove(scoreboardobjective);
    }

    public int func_96552_h(ScoreObjective scoreboardobjective) {
        int i = 0;

        for (int j = 0; j < 19; ++j) {
            if (this.func_96539_a(j) == scoreboardobjective) {
                ++i;
            }
        }

        return i;
    }

    // CraftBukkit start - Send to players
    private void sendAll(Packet packet) {
        for (EntityPlayerMP entityplayer : (List<EntityPlayerMP>) this.field_96555_a.func_184103_al().field_72404_b) {
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() == this) {
                entityplayer.field_71135_a.func_147359_a(packet);
            }
        }
    }
    // CraftBukkit end
}
