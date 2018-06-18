package net.minecraft.network.play.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient> {

    private SPacketPlayerListItem.Action action;
    private final List<SPacketPlayerListItem.AddPlayerData> players = Lists.newArrayList();

    public SPacketPlayerListItem() {}

    public SPacketPlayerListItem(SPacketPlayerListItem.Action packetplayoutplayerinfo_enumplayerinfoaction, EntityPlayerMP... aentityplayer) {
        this.action = packetplayoutplayerinfo_enumplayerinfoaction;
        EntityPlayerMP[] aentityplayer1 = aentityplayer;
        int i = aentityplayer.length;

        for (int j = 0; j < i; ++j) {
            EntityPlayerMP entityplayer = aentityplayer1[j];

            this.players.add(new SPacketPlayerListItem.AddPlayerData(entityplayer.getGameProfile(), entityplayer.ping, entityplayer.interactionManager.getGameType(), entityplayer.getTabListDisplayName()));
        }

    }

    public SPacketPlayerListItem(SPacketPlayerListItem.Action packetplayoutplayerinfo_enumplayerinfoaction, Iterable<EntityPlayerMP> iterable) {
        this.action = packetplayoutplayerinfo_enumplayerinfoaction;
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            this.players.add(new SPacketPlayerListItem.AddPlayerData(entityplayer.getGameProfile(), entityplayer.ping, entityplayer.interactionManager.getGameType(), entityplayer.getTabListDisplayName()));
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.action = (SPacketPlayerListItem.Action) packetdataserializer.readEnumValue(SPacketPlayerListItem.Action.class);
        int i = packetdataserializer.readVarInt();

        for (int j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            GameType enumgamemode = null;
            ITextComponent ichatbasecomponent = null;

            switch (this.action) {
            case ADD_PLAYER:
                gameprofile = new GameProfile(packetdataserializer.readUniqueId(), packetdataserializer.readString(16));
                int l = packetdataserializer.readVarInt();

                for (int i1 = 0; i1 < l; ++i1) {
                    String s = packetdataserializer.readString(32767);
                    String s1 = packetdataserializer.readString(32767);

                    if (packetdataserializer.readBoolean()) {
                        gameprofile.getProperties().put(s, new Property(s, s1, packetdataserializer.readString(32767)));
                    } else {
                        gameprofile.getProperties().put(s, new Property(s, s1));
                    }
                }

                enumgamemode = GameType.getByID(packetdataserializer.readVarInt());
                k = packetdataserializer.readVarInt();
                if (packetdataserializer.readBoolean()) {
                    ichatbasecomponent = packetdataserializer.readTextComponent();
                }
                break;

            case UPDATE_GAME_MODE:
                gameprofile = new GameProfile(packetdataserializer.readUniqueId(), (String) null);
                enumgamemode = GameType.getByID(packetdataserializer.readVarInt());
                break;

            case UPDATE_LATENCY:
                gameprofile = new GameProfile(packetdataserializer.readUniqueId(), (String) null);
                k = packetdataserializer.readVarInt();
                break;

            case UPDATE_DISPLAY_NAME:
                gameprofile = new GameProfile(packetdataserializer.readUniqueId(), (String) null);
                if (packetdataserializer.readBoolean()) {
                    ichatbasecomponent = packetdataserializer.readTextComponent();
                }
                break;

            case REMOVE_PLAYER:
                gameprofile = new GameProfile(packetdataserializer.readUniqueId(), (String) null);
            }

            this.players.add(new SPacketPlayerListItem.AddPlayerData(gameprofile, k, enumgamemode, ichatbasecomponent));
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.action);
        packetdataserializer.writeVarInt(this.players.size());
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            SPacketPlayerListItem.AddPlayerData packetplayoutplayerinfo_playerinfodata = (SPacketPlayerListItem.AddPlayerData) iterator.next();

            switch (this.action) {
            case ADD_PLAYER:
                packetdataserializer.writeUniqueId(packetplayoutplayerinfo_playerinfodata.getProfile().getId());
                packetdataserializer.writeString(packetplayoutplayerinfo_playerinfodata.getProfile().getName());
                packetdataserializer.writeVarInt(packetplayoutplayerinfo_playerinfodata.getProfile().getProperties().size());
                Iterator iterator1 = packetplayoutplayerinfo_playerinfodata.getProfile().getProperties().values().iterator();

                while (iterator1.hasNext()) {
                    Property property = (Property) iterator1.next();

                    packetdataserializer.writeString(property.getName());
                    packetdataserializer.writeString(property.getValue());
                    if (property.hasSignature()) {
                        packetdataserializer.writeBoolean(true);
                        packetdataserializer.writeString(property.getSignature());
                    } else {
                        packetdataserializer.writeBoolean(false);
                    }
                }

                packetdataserializer.writeVarInt(packetplayoutplayerinfo_playerinfodata.getGameMode().getID());
                packetdataserializer.writeVarInt(packetplayoutplayerinfo_playerinfodata.getPing());
                if (packetplayoutplayerinfo_playerinfodata.getDisplayName() == null) {
                    packetdataserializer.writeBoolean(false);
                } else {
                    packetdataserializer.writeBoolean(true);
                    packetdataserializer.writeTextComponent(packetplayoutplayerinfo_playerinfodata.getDisplayName());
                }
                break;

            case UPDATE_GAME_MODE:
                packetdataserializer.writeUniqueId(packetplayoutplayerinfo_playerinfodata.getProfile().getId());
                packetdataserializer.writeVarInt(packetplayoutplayerinfo_playerinfodata.getGameMode().getID());
                break;

            case UPDATE_LATENCY:
                packetdataserializer.writeUniqueId(packetplayoutplayerinfo_playerinfodata.getProfile().getId());
                packetdataserializer.writeVarInt(packetplayoutplayerinfo_playerinfodata.getPing());
                break;

            case UPDATE_DISPLAY_NAME:
                packetdataserializer.writeUniqueId(packetplayoutplayerinfo_playerinfodata.getProfile().getId());
                if (packetplayoutplayerinfo_playerinfodata.getDisplayName() == null) {
                    packetdataserializer.writeBoolean(false);
                } else {
                    packetdataserializer.writeBoolean(true);
                    packetdataserializer.writeTextComponent(packetplayoutplayerinfo_playerinfodata.getDisplayName());
                }
                break;

            case REMOVE_PLAYER:
                packetdataserializer.writeUniqueId(packetplayoutplayerinfo_playerinfodata.getProfile().getId());
            }
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handlePlayerListItem(this);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
    }

    public class AddPlayerData {

        private final int ping;
        private final GameType gamemode;
        private final GameProfile profile;
        private final ITextComponent displayName;

        public AddPlayerData(GameProfile gameprofile, int i, GameType enumgamemode, @Nullable ITextComponent ichatbasecomponent) {
            this.profile = gameprofile;
            this.ping = i;
            this.gamemode = enumgamemode;
            this.displayName = ichatbasecomponent;
        }

        public GameProfile getProfile() {
            return this.profile;
        }

        public int getPing() {
            return this.ping;
        }

        public GameType getGameMode() {
            return this.gamemode;
        }

        @Nullable
        public ITextComponent getDisplayName() {
            return this.displayName;
        }

        public String toString() {
            return MoreObjects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", this.displayName == null ? null : ITextComponent.Serializer.componentToJson(this.displayName)).toString();
        }
    }

    public static enum Action {

        ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;

        private Action() {}
    }
}
