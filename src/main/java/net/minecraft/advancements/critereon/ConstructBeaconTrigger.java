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
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class ConstructBeaconTrigger implements ICriterionTrigger<ConstructBeaconTrigger.b> {

    private static final ResourceLocation field_192181_a = new ResourceLocation("construct_beacon");
    private final Map<PlayerAdvancements, ConstructBeaconTrigger.a> field_192182_b = Maps.newHashMap();

    public ConstructBeaconTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return ConstructBeaconTrigger.field_192181_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<ConstructBeaconTrigger.b> criteriontrigger_a) {
        ConstructBeaconTrigger.a criteriontriggerconstructbeacon_a = this.field_192182_b.get(advancementdataplayer);

        if (criteriontriggerconstructbeacon_a == null) {
            criteriontriggerconstructbeacon_a = new ConstructBeaconTrigger.a(advancementdataplayer);
            this.field_192182_b.put(advancementdataplayer, criteriontriggerconstructbeacon_a);
        }

        criteriontriggerconstructbeacon_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<ConstructBeaconTrigger.b> criteriontrigger_a) {
        ConstructBeaconTrigger.a criteriontriggerconstructbeacon_a = this.field_192182_b.get(advancementdataplayer);

        if (criteriontriggerconstructbeacon_a != null) {
            criteriontriggerconstructbeacon_a.b(criteriontrigger_a);
            if (criteriontriggerconstructbeacon_a.a()) {
                this.field_192182_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192182_b.remove(advancementdataplayer);
    }

    public ConstructBeaconTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("level"));

        return new ConstructBeaconTrigger.b(criterionconditionvalue);
    }

    public void func_192180_a(EntityPlayerMP entityplayer, TileEntityBeacon tileentitybeacon) {
        ConstructBeaconTrigger.a criteriontriggerconstructbeacon_a = this.field_192182_b.get(entityplayer.func_192039_O());

        if (criteriontriggerconstructbeacon_a != null) {
            criteriontriggerconstructbeacon_a.a(tileentitybeacon);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<ConstructBeaconTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<ConstructBeaconTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<ConstructBeaconTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(TileEntityBeacon tileentitybeacon) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((ConstructBeaconTrigger.b) criteriontrigger_a.a()).a(tileentitybeacon)) {
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

        private final MinMaxBounds a;

        public b(MinMaxBounds criterionconditionvalue) {
            super(ConstructBeaconTrigger.field_192181_a);
            this.a = criterionconditionvalue;
        }

        public boolean a(TileEntityBeacon tileentitybeacon) {
            return this.a.func_192514_a(tileentitybeacon.func_191979_s());
        }
    }
}
