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

    private final Set<EntityPlayerMP> field_186762_h = Sets.newHashSet();
    private final Set<EntityPlayerMP> field_186763_i;
    public boolean field_186764_j;

    public BossInfoServer(ITextComponent ichatbasecomponent, BossInfo.Color bossbattle_barcolor, BossInfo.Overlay bossbattle_barstyle) {
        super(MathHelper.func_188210_a(), ichatbasecomponent, bossbattle_barcolor, bossbattle_barstyle);
        this.field_186763_i = Collections.unmodifiableSet(this.field_186762_h);
        this.field_186764_j = true;
    }

    public void func_186735_a(float f) {
        if (f != this.field_186750_b) {
            super.func_186735_a(f);
            this.func_186759_a(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
        }

    }

    public BossInfo func_186741_a(boolean flag) {
        if (flag != this.field_186753_e) {
            super.func_186741_a(flag);
            this.func_186759_a(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo func_186742_b(boolean flag) {
        if (flag != this.field_186754_f) {
            super.func_186742_b(flag);
            this.func_186759_a(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo func_186743_c(boolean flag) {
        if (flag != this.field_186755_g) {
            super.func_186743_c(flag);
            this.func_186759_a(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public void func_186739_a(ITextComponent ichatbasecomponent) {
        if (!Objects.equal(ichatbasecomponent, this.field_186749_a)) {
            super.func_186739_a(ichatbasecomponent);
            this.func_186759_a(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
        }

    }

    public void func_186759_a(SPacketUpdateBossInfo.Operation packetplayoutboss_action) {
        if (this.field_186764_j) {
            SPacketUpdateBossInfo packetplayoutboss = new SPacketUpdateBossInfo(packetplayoutboss_action, this);
            Iterator iterator = this.field_186762_h.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.field_71135_a.func_147359_a(packetplayoutboss);
            }
        }

    }

    public void func_186760_a(EntityPlayerMP entityplayer) {
        if (this.field_186762_h.add(entityplayer) && this.field_186764_j) {
            entityplayer.field_71135_a.func_147359_a(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.ADD, this));
        }

    }

    public void func_186761_b(EntityPlayerMP entityplayer) {
        if (this.field_186762_h.remove(entityplayer) && this.field_186764_j) {
            entityplayer.field_71135_a.func_147359_a(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.REMOVE, this));
        }

    }

    public void func_186758_d(boolean flag) {
        if (flag != this.field_186764_j) {
            this.field_186764_j = flag;
            Iterator iterator = this.field_186762_h.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.field_71135_a.func_147359_a(new SPacketUpdateBossInfo(flag ? SPacketUpdateBossInfo.Operation.ADD : SPacketUpdateBossInfo.Operation.REMOVE, this));
            }
        }

    }

    public Collection<EntityPlayerMP> func_186757_c() {
        return this.field_186763_i;
    }
}
