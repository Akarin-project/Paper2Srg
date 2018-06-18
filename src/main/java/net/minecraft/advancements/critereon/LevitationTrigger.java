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
import net.minecraft.server.CriterionTriggerLevitation.a;
import net.minecraft.server.CriterionTriggerLevitation.b;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class LevitationTrigger implements ICriterionTrigger<CriterionTriggerLevitation.b> {

    private static final ResourceLocation ID = new ResourceLocation("levitation");
    private final Map<PlayerAdvancements, CriterionTriggerLevitation.a> listeners = Maps.newHashMap();

    public LevitationTrigger() {}

    public ResourceLocation getId() {
        return LevitationTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLevitation.b> criteriontrigger_a) {
        CriterionTriggerLevitation.a criteriontriggerlevitation_a = (CriterionTriggerLevitation.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerlevitation_a == null) {
            criteriontriggerlevitation_a = new CriterionTriggerLevitation.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerlevitation_a);
        }

        criteriontriggerlevitation_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLevitation.b> criteriontrigger_a) {
        CriterionTriggerLevitation.a criteriontriggerlevitation_a = (CriterionTriggerLevitation.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerlevitation_a != null) {
            criteriontriggerlevitation_a.b(criteriontrigger_a);
            if (criteriontriggerlevitation_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerLevitation.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        DistancePredicate criterionconditiondistance = DistancePredicate.deserialize(jsonobject.get("distance"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("duration"));

        return new CriterionTriggerLevitation.b(criterionconditiondistance, criterionconditionvalue);
    }

    public void trigger(EntityPlayerMP entityplayer, Vec3d vec3d, int i) {
        CriterionTriggerLevitation.a criteriontriggerlevitation_a = (CriterionTriggerLevitation.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerlevitation_a != null) {
            criteriontriggerlevitation_a.a(entityplayer, vec3d, i);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerLevitation.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerLevitation.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerLevitation.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, Vec3d vec3d, int i) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerLevitation.b) criteriontrigger_a.a()).a(entityplayer, vec3d, i)) {
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

        private final DistancePredicate a;
        private final MinMaxBounds b;

        public b(DistancePredicate criterionconditiondistance, MinMaxBounds criterionconditionvalue) {
            super(LevitationTrigger.ID);
            this.a = criterionconditiondistance;
            this.b = criterionconditionvalue;
        }

        public boolean a(EntityPlayerMP entityplayer, Vec3d vec3d, int i) {
            return !this.a.test(vec3d.x, vec3d.y, vec3d.z, entityplayer.posX, entityplayer.posY, entityplayer.posZ) ? false : this.b.test((float) i);
        }
    }
}
