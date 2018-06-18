package net.minecraft.world;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class BossInfoServer extends BossInfo {

    private final Set<EntityPlayerMP> players = Sets.newHashSet();
    private final Set<EntityPlayerMP> readOnlyPlayers;
    public boolean visible;

    public BossInfoServer(ITextComponent ichatbasecomponent, BossInfo.Color bossbattle_barcolor, BossInfo.Overlay bossbattle_barstyle) {
        super(MathHelper.getRandomUUID(), ichatbasecomponent, bossbattle_barcolor, bossbattle_barstyle);
        this.readOnlyPlayers = Collections.unmodifiableSet(this.players);
        this.visible = true;
    }

    public void setPercent(float f) {
        if (f != this.percent) {
            super.setPercent(f);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
        }

    }

    public BossInfo setDarkenSky(boolean flag) {
        if (flag != this.darkenSky) {
            super.setDarkenSky(flag);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo setPlayEndBossMusic(boolean flag) {
        if (flag != this.playEndBossMusic) {
            super.setPlayEndBossMusic(flag);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo setCreateFog(boolean flag) {
        if (flag != this.createFog) {
            super.setCreateFog(flag);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public void setName(ITextComponent ichatbasecomponent) {
        if (!Objects.equal(ichatbasecomponent, this.name)) {
            super.setName(ichatbasecomponent);
            this.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
        }

    }

    public void sendUpdate(SPacketUpdateBossInfo.Operation packetplayoutboss_action) {
        if (this.visible) {
            SPacketUpdateBossInfo packetplayoutboss = new SPacketUpdateBossInfo(packetplayoutboss_action, this);
            Iterator iterator = this.players.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.connection.sendPacket(packetplayoutboss);
            }
        }

    }

    public void addPlayer(EntityPlayerMP entityplayer) {
        if (this.players.add(entityplayer) && this.visible) {
            entityplayer.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.ADD, this));
        }

    }

    public void removePlayer(EntityPlayerMP entityplayer) {
        if (this.players.remove(entityplayer) && this.visible) {
            entityplayer.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.REMOVE, this));
        }

    }

    public void setVisible(boolean flag) {
        if (flag != this.visible) {
            this.visible = flag;
            Iterator iterator = this.players.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.connection.sendPacket(new SPacketUpdateBossInfo(flag ? SPacketUpdateBossInfo.Operation.ADD : SPacketUpdateBossInfo.Operation.REMOVE, this));
            }
        }

    }

    public Collection<EntityPlayerMP> getPlayers() {
        return this.readOnlyPlayers;
    }
}
