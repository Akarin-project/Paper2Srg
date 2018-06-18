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
import net.minecraft.server.CriterionTriggerNetherTravel.a;
import net.minecraft.server.CriterionTriggerNetherTravel.b;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class NetherTravelTrigger implements ICriterionTrigger<CriterionTriggerNetherTravel.b> {

    private static final ResourceLocation ID = new ResourceLocation("nether_travel");
    private final Map<PlayerAdvancements, CriterionTriggerNetherTravel.a> listeners = Maps.newHashMap();

    public NetherTravelTrigger() {}

    public ResourceLocation getId() {
        return NetherTravelTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerNetherTravel.b> criteriontrigger_a) {
        CriterionTriggerNetherTravel.a criteriontriggernethertravel_a = (CriterionTriggerNetherTravel.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggernethertravel_a == null) {
            criteriontriggernethertravel_a = new CriterionTriggerNetherTravel.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggernethertravel_a);
        }

        criteriontriggernethertravel_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerNetherTravel.b> criteriontrigger_a) {
        CriterionTriggerNetherTravel.a criteriontriggernethertravel_a = (CriterionTriggerNetherTravel.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggernethertravel_a != null) {
            criteriontriggernethertravel_a.b(criteriontrigger_a);
            if (criteriontriggernethertravel_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerNetherTravel.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        LocationPredicate criterionconditionlocation = LocationPredicate.deserialize(jsonobject.get("entered"));
        LocationPredicate criterionconditionlocation1 = LocationPredicate.deserialize(jsonobject.get("exited"));
        DistancePredicate criterionconditiondistance = DistancePredicate.deserialize(jsonobject.get("distance"));

        return new CriterionTriggerNetherTravel.b(criterionconditionlocation, criterionconditionlocation1, criterionconditiondistance);
    }

    public void trigger(EntityPlayerMP entityplayer, Vec3d vec3d) {
        CriterionTriggerNetherTravel.a criteriontriggernethertravel_a = (CriterionTriggerNetherTravel.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggernethertravel_a != null) {
            criteriontriggernethertravel_a.a(entityplayer.getServerWorld(), vec3d, entityplayer.posX, entityplayer.posY, entityplayer.posZ);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerNetherTravel.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerNetherTravel.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerNetherTravel.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(WorldServer worldserver, Vec3d vec3d, double d0, double d1, double d2) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerNetherTravel.b) criteriontrigger_a.a()).a(worldserver, vec3d, d0, d1, d2)) {
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

        private final LocationPredicate a;
        private final LocationPredicate b;
        private final DistancePredicate c;

        public b(LocationPredicate criterionconditionlocation, LocationPredicate criterionconditionlocation1, DistancePredicate criterionconditiondistance) {
            super(NetherTravelTrigger.ID);
            this.a = criterionconditionlocation;
            this.b = criterionconditionlocation1;
            this.c = criterionconditiondistance;
        }

        public boolean a(WorldServer worldserver, Vec3d vec3d, double d0, double d1, double d2) {
            return !this.a.test(worldserver, vec3d.x, vec3d.y, vec3d.z) ? false : (!this.b.test(worldserver, d0, d1, d2) ? false : this.c.test(vec3d.x, vec3d.y, vec3d.z, d0, d1, d2));
        }
    }
}
