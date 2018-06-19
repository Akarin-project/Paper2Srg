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

    private SPacketPlayerListItem.Action field_179770_a;
    private final List<SPacketPlayerListItem.AddPlayerData> field_179769_b = Lists.newArrayList();

    public SPacketPlayerListItem() {}

    public SPacketPlayerListItem(SPacketPlayerListItem.Action packetplayoutplayerinfo_enumplayerinfoaction, EntityPlayerMP... aentityplayer) {
        this.field_179770_a = packetplayoutplayerinfo_enumplayerinfoaction;
        EntityPlayerMP[] aentityplayer1 = aentityplayer;
        int i = aentityplayer.length;

        for (int j = 0; j < i; ++j) {
            EntityPlayerMP entityplayer = aentityplayer1[j];

            this.field_179769_b.add(new SPacketPlayerListItem.AddPlayerData(entityplayer.func_146103_bH(), entityplayer.field_71138_i, entityplayer.field_71134_c.func_73081_b(), entityplayer.func_175396_E()));
        }

    }

    public SPacketPlayerListItem(SPacketPlayerListItem.Action packetplayoutplayerinfo_enumplayerinfoaction, Iterable<EntityPlayerMP> iterable) {
        this.field_179770_a = packetplayoutplayerinfo_enumplayerinfoaction;
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            this.field_179769_b.add(new SPacketPlayerListItem.AddPlayerData(entityplayer.func_146103_bH(), entityplayer.field_71138_i, entityplayer.field_71134_c.func_73081_b(), entityplayer.func_175396_E()));
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179770_a = (SPacketPlayerListItem.Action) packetdataserializer.func_179257_a(SPacketPlayerListItem.Action.class);
        int i = packetdataserializer.func_150792_a();

        for (int j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            GameType enumgamemode = null;
            ITextComponent ichatbasecomponent = null;

            switch (this.field_179770_a) {
            case ADD_PLAYER:
                gameprofile = new GameProfile(packetdataserializer.func_179253_g(), packetdataserializer.func_150789_c(16));
                int l = packetdataserializer.func_150792_a();

                for (int i1 = 0; i1 < l; ++i1) {
                    String s = packetdataserializer.func_150789_c(32767);
                    String s1 = packetdataserializer.func_150789_c(32767);

                    if (packetdataserializer.readBoolean()) {
                        gameprofile.getProperties().put(s, new Property(s, s1, packetdataserializer.func_150789_c(32767)));
                    } else {
                        gameprofile.getProperties().put(s, new Property(s, s1));
                    }
                }

                enumgamemode = GameType.func_77146_a(packetdataserializer.func_150792_a());
                k = packetdataserializer.func_150792_a();
                if (packetdataserializer.readBoolean()) {
                    ichatbasecomponent = packetdataserializer.func_179258_d();
                }
                break;

            case UPDATE_GAME_MODE:
                gameprofile = new GameProfile(packetdataserializer.func_179253_g(), (String) null);
                enumgamemode = GameType.func_77146_a(packetdataserializer.func_150792_a());
                break;

            case UPDATE_LATENCY:
                gameprofile = new GameProfile(packetdataserializer.func_179253_g(), (String) null);
                k = packetdataserializer.func_150792_a();
                break;

            case UPDATE_DISPLAY_NAME:
                gameprofile = new GameProfile(packetdataserializer.func_179253_g(), (String) null);
                if (packetdataserializer.readBoolean()) {
                    ichatbasecomponent = packetdataserializer.func_179258_d();
                }
                break;

            case REMOVE_PLAYER:
                gameprofile = new GameProfile(packetdataserializer.func_179253_g(), (String) null);
            }

            this.field_179769_b.add(new SPacketPlayerListItem.AddPlayerData(gameprofile, k, enumgamemode, ichatbasecomponent));
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_179770_a);
        packetdataserializer.func_150787_b(this.field_179769_b.size());
        Iterator iterator = this.field_179769_b.iterator();

        while (iterator.hasNext()) {
            SPacketPlayerListItem.AddPlayerData packetplayoutplayerinfo_playerinfodata = (SPacketPlayerListItem.AddPlayerData) iterator.next();

            switch (this.field_179770_a) {
            case ADD_PLAYER:
                packetdataserializer.func_179252_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getId());
                packetdataserializer.func_180714_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getName());
                packetdataserializer.func_150787_b(packetplayoutplayerinfo_playerinfodata.func_179962_a().getProperties().size());
                Iterator iterator1 = packetplayoutplayerinfo_playerinfodata.func_179962_a().getProperties().values().iterator();

                while (iterator1.hasNext()) {
                    Property property = (Property) iterator1.next();

                    packetdataserializer.func_180714_a(property.getName());
                    packetdataserializer.func_180714_a(property.getValue());
                    if (property.hasSignature()) {
                        packetdataserializer.writeBoolean(true);
                        packetdataserializer.func_180714_a(property.getSignature());
                    } else {
                        packetdataserializer.writeBoolean(false);
                    }
                }

                packetdataserializer.func_150787_b(packetplayoutplayerinfo_playerinfodata.func_179960_c().func_77148_a());
                packetdataserializer.func_150787_b(packetplayoutplayerinfo_playerinfodata.func_179963_b());
                if (packetplayoutplayerinfo_playerinfodata.func_179961_d() == null) {
                    packetdataserializer.writeBoolean(false);
                } else {
                    packetdataserializer.writeBoolean(true);
                    packetdataserializer.func_179256_a(packetplayoutplayerinfo_playerinfodata.func_179961_d());
                }
                break;

            case UPDATE_GAME_MODE:
                packetdataserializer.func_179252_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getId());
                packetdataserializer.func_150787_b(packetplayoutplayerinfo_playerinfodata.func_179960_c().func_77148_a());
                break;

            case UPDATE_LATENCY:
                packetdataserializer.func_179252_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getId());
                packetdataserializer.func_150787_b(packetplayoutplayerinfo_playerinfodata.func_179963_b());
                break;

            case UPDATE_DISPLAY_NAME:
                packetdataserializer.func_179252_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getId());
                if (packetplayoutplayerinfo_playerinfodata.func_179961_d() == null) {
                    packetdataserializer.writeBoolean(false);
                } else {
                    packetdataserializer.writeBoolean(true);
                    packetdataserializer.func_179256_a(packetplayoutplayerinfo_playerinfodata.func_179961_d());
                }
                break;

            case REMOVE_PLAYER:
                packetdataserializer.func_179252_a(packetplayoutplayerinfo_playerinfodata.func_179962_a().getId());
            }
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147256_a(this);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("action", this.field_179770_a).add("entries", this.field_179769_b).toString();
    }

    public class AddPlayerData {

        private final int field_179966_b;
        private final GameType field_179967_c;
        private final GameProfile field_179964_d;
        private final ITextComponent field_179965_e;

        public AddPlayerData(GameProfile gameprofile, int i, GameType enumgamemode, @Nullable ITextComponent ichatbasecomponent) {
            this.field_179964_d = gameprofile;
            this.field_179966_b = i;
            this.field_179967_c = enumgamemode;
            this.field_179965_e = ichatbasecomponent;
        }

        public GameProfile func_179962_a() {
            return this.field_179964_d;
        }

        public int func_179963_b() {
            return this.field_179966_b;
        }

        public GameType func_179960_c() {
            return this.field_179967_c;
        }

        @Nullable
        public ITextComponent func_179961_d() {
            return this.field_179965_e;
        }

        public String toString() {
            return MoreObjects.toStringHelper(this).add("latency", this.field_179966_b).add("gameMode", this.field_179967_c).add("profile", this.field_179964_d).add("displayName", this.field_179965_e == null ? null : ITextComponent.Serializer.func_150696_a(this.field_179965_e)).toString();
        }
    }

    public static enum Action {

        ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;

        private Action() {}
    }
}
