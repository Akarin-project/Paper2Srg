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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class NetherTravelTrigger implements ICriterionTrigger<NetherTravelTrigger.b> {

    private static final ResourceLocation field_193169_a = new ResourceLocation("nether_travel");
    private final Map<PlayerAdvancements, NetherTravelTrigger.a> field_193170_b = Maps.newHashMap();

    public NetherTravelTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return NetherTravelTrigger.field_193169_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<NetherTravelTrigger.b> criteriontrigger_a) {
        NetherTravelTrigger.a criteriontriggernethertravel_a = this.field_193170_b.get(advancementdataplayer);

        if (criteriontriggernethertravel_a == null) {
            criteriontriggernethertravel_a = new NetherTravelTrigger.a(advancementdataplayer);
            this.field_193170_b.put(advancementdataplayer, criteriontriggernethertravel_a);
        }

        criteriontriggernethertravel_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<NetherTravelTrigger.b> criteriontrigger_a) {
        NetherTravelTrigger.a criteriontriggernethertravel_a = this.field_193170_b.get(advancementdataplayer);

        if (criteriontriggernethertravel_a != null) {
            criteriontriggernethertravel_a.b(criteriontrigger_a);
            if (criteriontriggernethertravel_a.a()) {
                this.field_193170_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193170_b.remove(advancementdataplayer);
    }

    public NetherTravelTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        LocationPredicate criterionconditionlocation = LocationPredicate.func_193454_a(jsonobject.get("entered"));
        LocationPredicate criterionconditionlocation1 = LocationPredicate.func_193454_a(jsonobject.get("exited"));
        DistancePredicate criterionconditiondistance = DistancePredicate.func_193421_a(jsonobject.get("distance"));

        return new NetherTravelTrigger.b(criterionconditionlocation, criterionconditionlocation1, criterionconditiondistance);
    }

    public void func_193168_a(EntityPlayerMP entityplayer, Vec3d vec3d) {
        NetherTravelTrigger.a criteriontriggernethertravel_a = this.field_193170_b.get(entityplayer.func_192039_O());

        if (criteriontriggernethertravel_a != null) {
            criteriontriggernethertravel_a.a(entityplayer.func_71121_q(), vec3d, entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<NetherTravelTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<NetherTravelTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<NetherTravelTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(WorldServer worldserver, Vec3d vec3d, double d0, double d1, double d2) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((NetherTravelTrigger.b) criteriontrigger_a.a()).a(worldserver, vec3d, d0, d1, d2)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
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
            super(NetherTravelTrigger.field_193169_a);
            this.a = criterionconditionlocation;
            this.b = criterionconditionlocation1;
            this.c = criterionconditiondistance;
        }

        public boolean a(WorldServer worldserver, Vec3d vec3d, double d0, double d1, double d2) {
            return !this.a.func_193452_a(worldserver, vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c) ? false : (!this.b.func_193452_a(worldserver, d0, d1, d2) ? false : this.c.func_193422_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, d0, d1, d2));
        }
    }
}
