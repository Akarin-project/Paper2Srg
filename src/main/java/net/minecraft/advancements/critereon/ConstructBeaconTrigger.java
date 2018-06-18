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
import net.minecraft.server.CriterionTriggerConstructBeacon.a;
import net.minecraft.server.CriterionTriggerConstructBeacon.b;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class ConstructBeaconTrigger implements ICriterionTrigger<CriterionTriggerConstructBeacon.b> {

    private static final ResourceLocation ID = new ResourceLocation("construct_beacon");
    private final Map<PlayerAdvancements, CriterionTriggerConstructBeacon.a> listeners = Maps.newHashMap();

    public ConstructBeaconTrigger() {}

    public ResourceLocation getId() {
        return ConstructBeaconTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerConstructBeacon.b> criteriontrigger_a) {
        CriterionTriggerConstructBeacon.a criteriontriggerconstructbeacon_a = (CriterionTriggerConstructBeacon.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerconstructbeacon_a == null) {
            criteriontriggerconstructbeacon_a = new CriterionTriggerConstructBeacon.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerconstructbeacon_a);
        }

        criteriontriggerconstructbeacon_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerConstructBeacon.b> criteriontrigger_a) {
        CriterionTriggerConstructBeacon.a criteriontriggerconstructbeacon_a = (CriterionTriggerConstructBeacon.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerconstructbeacon_a != null) {
            criteriontriggerconstructbeacon_a.b(criteriontrigger_a);
            if (criteriontriggerconstructbeacon_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerConstructBeacon.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("level"));

        return new CriterionTriggerConstructBeacon.b(criterionconditionvalue);
    }

    public void trigger(EntityPlayerMP entityplayer, TileEntityBeacon tileentitybeacon) {
        CriterionTriggerConstructBeacon.a criteriontriggerconstructbeacon_a = (CriterionTriggerConstructBeacon.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerconstructbeacon_a != null) {
            criteriontriggerconstructbeacon_a.a(tileentitybeacon);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerConstructBeacon.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerConstructBeacon.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerConstructBeacon.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(TileEntityBeacon tileentitybeacon) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerConstructBeacon.b) criteriontrigger_a.a()).a(tileentitybeacon)) {
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
            super(ConstructBeaconTrigger.ID);
            this.a = criterionconditionvalue;
        }

        public boolean a(TileEntityBeacon tileentitybeacon) {
            return this.a.test((float) tileentitybeacon.getLevels());
        }
    }
}
