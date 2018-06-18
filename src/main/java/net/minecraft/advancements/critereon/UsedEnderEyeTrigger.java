package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerUsedEnderEye.a;
import net.minecraft.server.CriterionTriggerUsedEnderEye.b;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class UsedEnderEyeTrigger implements ICriterionTrigger<CriterionTriggerUsedEnderEye.b> {

    private static final ResourceLocation ID = new ResourceLocation("used_ender_eye");
    private final Map<PlayerAdvancements, CriterionTriggerUsedEnderEye.a> listeners = Maps.newHashMap();

    public UsedEnderEyeTrigger() {}

    public ResourceLocation getId() {
        return UsedEnderEyeTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerUsedEnderEye.b> criteriontrigger_a) {
        CriterionTriggerUsedEnderEye.a criteriontriggerusedendereye_a = (CriterionTriggerUsedEnderEye.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerusedendereye_a == null) {
            criteriontriggerusedendereye_a = new CriterionTriggerUsedEnderEye.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerusedendereye_a);
        }

        criteriontriggerusedendereye_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerUsedEnderEye.b> criteriontrigger_a) {
        CriterionTriggerUsedEnderEye.a criteriontriggerusedendereye_a = (CriterionTriggerUsedEnderEye.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerusedendereye_a != null) {
            criteriontriggerusedendereye_a.b(criteriontrigger_a);
            if (criteriontriggerusedendereye_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerUsedEnderEye.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("distance"));

        return new CriterionTriggerUsedEnderEye.b(criterionconditionvalue);
    }

    public void trigger(EntityPlayerMP entityplayer, BlockPos blockposition) {
        CriterionTriggerUsedEnderEye.a criteriontriggerusedendereye_a = (CriterionTriggerUsedEnderEye.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerusedendereye_a != null) {
            double d0 = entityplayer.posX - (double) blockposition.getX();
            double d1 = entityplayer.posZ - (double) blockposition.getZ();

            criteriontriggerusedendereye_a.a(d0 * d0 + d1 * d1);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerUsedEnderEye.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerUsedEnderEye.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerUsedEnderEye.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(double d0) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerUsedEnderEye.b) criteriontrigger_a.a()).a(d0)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(this.a);
                }
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        private final MinMaxBounds a;

        public b(MinMaxBounds criterionconditionvalue) {
            super(UsedEnderEyeTrigger.ID);
            this.a = criterionconditionvalue;
        }

        public boolean a(double d0) {
            return this.a.testSquare(d0);
        }
    }
}
