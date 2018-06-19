package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class SPacketTeams implements Packet<INetHandlerPlayClient> {

    private String field_149320_a = "";
    private String field_149318_b = "";
    private String field_149319_c = "";
    private String field_149316_d = "";
    private String field_179816_e;
    private String field_186976_f;
    private int field_179815_f;
    private final Collection<String> field_149317_e;
    private int field_149314_f;
    private int field_149315_g;

    public SPacketTeams() {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_186976_f = Team.CollisionRule.ALWAYS.field_186693_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
    }

    public SPacketTeams(ScorePlayerTeam scoreboardteam, int i) {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_186976_f = Team.CollisionRule.ALWAYS.field_186693_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        this.field_149320_a = scoreboardteam.func_96661_b();
        this.field_149314_f = i;
        if (i == 0 || i == 2) {
            this.field_149318_b = scoreboardteam.func_96669_c();
            this.field_149319_c = scoreboardteam.func_96668_e();
            this.field_149316_d = scoreboardteam.func_96663_f();
            this.field_149315_g = scoreboardteam.func_98299_i();
            this.field_179816_e = scoreboardteam.func_178770_i().field_178830_e;
            this.field_186976_f = scoreboardteam.func_186681_k().field_186693_e;
            this.field_179815_f = scoreboardteam.func_178775_l().func_175746_b();
        }

        if (i == 0) {
            this.field_149317_e.addAll(scoreboardteam.func_96670_d());
        }

    }

    public SPacketTeams(ScorePlayerTeam scoreboardteam, Collection<String> collection, int i) {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_186976_f = Team.CollisionRule.ALWAYS.field_186693_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        if (i != 3 && i != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        } else if (collection != null && !collection.isEmpty()) {
            this.field_149314_f = i;
            this.field_149320_a = scoreboardteam.func_96661_b();
            this.field_149317_e.addAll(collection);
        } else {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149320_a = packetdataserializer.func_150789_c(16);
        this.field_149314_f = packetdataserializer.readByte();
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            this.field_149318_b = packetdataserializer.func_150789_c(32);
            this.field_149319_c = packetdataserializer.func_150789_c(16);
            this.field_149316_d = packetdataserializer.func_150789_c(16);
            this.field_149315_g = packetdataserializer.readByte();
            this.field_179816_e = packetdataserializer.func_150789_c(32);
            this.field_186976_f = packetdataserializer.func_150789_c(32);
            this.field_179815_f = packetdataserializer.readByte();
        }

        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            int i = packetdataserializer.func_150792_a();

            for (int j = 0; j < i; ++j) {
                this.field_149317_e.add(packetdataserializer.func_150789_c(40));
            }
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149320_a);
        packetdataserializer.writeByte(this.field_149314_f);
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            packetdataserializer.func_180714_a(this.field_149318_b);
            packetdataserializer.func_180714_a(this.field_149319_c);
            packetdataserializer.func_180714_a(this.field_149316_d);
            packetdataserializer.writeByte(this.field_149315_g);
            packetdataserializer.func_180714_a(this.field_179816_e);
            packetdataserializer.func_180714_a(!com.destroystokyo.paper.PaperConfig.enablePlayerCollisions ? "never" : this.field_186976_f); // Paper
            packetdataserializer.writeByte(this.field_179815_f);
        }

        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            packetdataserializer.func_150787_b(this.field_149317_e.size());
            Iterator iterator = this.field_149317_e.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                packetdataserializer.func_180714_a(s);
            }
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147247_a(this);
    }
}
