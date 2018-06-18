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

    private String name = "";
    private String displayName = "";
    private String prefix = "";
    private String suffix = "";
    private String nameTagVisibility;
    private String collisionRule;
    private int color;
    private final Collection<String> players;
    private int action;
    private int friendlyFlags;

    public SPacketTeams() {
        this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = -1;
        this.players = Lists.newArrayList();
    }

    public SPacketTeams(ScorePlayerTeam scoreboardteam, int i) {
        this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = -1;
        this.players = Lists.newArrayList();
        this.name = scoreboardteam.getName();
        this.action = i;
        if (i == 0 || i == 2) {
            this.displayName = scoreboardteam.getDisplayName();
            this.prefix = scoreboardteam.getPrefix();
            this.suffix = scoreboardteam.getSuffix();
            this.friendlyFlags = scoreboardteam.getFriendlyFlags();
            this.nameTagVisibility = scoreboardteam.getNameTagVisibility().internalName;
            this.collisionRule = scoreboardteam.getCollisionRule().name;
            this.color = scoreboardteam.getColor().getColorIndex();
        }

        if (i == 0) {
            this.players.addAll(scoreboardteam.getMembershipCollection());
        }

    }

    public SPacketTeams(ScorePlayerTeam scoreboardteam, Collection<String> collection, int i) {
        this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = -1;
        this.players = Lists.newArrayList();
        if (i != 3 && i != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        } else if (collection != null && !collection.isEmpty()) {
            this.action = i;
            this.name = scoreboardteam.getName();
            this.players.addAll(collection);
        } else {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.name = packetdataserializer.readString(16);
        this.action = packetdataserializer.readByte();
        if (this.action == 0 || this.action == 2) {
            this.displayName = packetdataserializer.readString(32);
            this.prefix = packetdataserializer.readString(16);
            this.suffix = packetdataserializer.readString(16);
            this.friendlyFlags = packetdataserializer.readByte();
            this.nameTagVisibility = packetdataserializer.readString(32);
            this.collisionRule = packetdataserializer.readString(32);
            this.color = packetdataserializer.readByte();
        }

        if (this.action == 0 || this.action == 3 || this.action == 4) {
            int i = packetdataserializer.readVarInt();

            for (int j = 0; j < i; ++j) {
                this.players.add(packetdataserializer.readString(40));
            }
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.name);
        packetdataserializer.writeByte(this.action);
        if (this.action == 0 || this.action == 2) {
            packetdataserializer.writeString(this.displayName);
            packetdataserializer.writeString(this.prefix);
            packetdataserializer.writeString(this.suffix);
            packetdataserializer.writeByte(this.friendlyFlags);
            packetdataserializer.writeString(this.nameTagVisibility);
            packetdataserializer.writeString(!com.destroystokyo.paper.PaperConfig.enablePlayerCollisions ? "never" : this.collisionRule); // Paper
            packetdataserializer.writeByte(this.color);
        }

        if (this.action == 0 || this.action == 3 || this.action == 4) {
            packetdataserializer.writeVarInt(this.players.size());
            Iterator iterator = this.players.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                packetdataserializer.writeString(s);
            }
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleTeams(this);
    }
}
