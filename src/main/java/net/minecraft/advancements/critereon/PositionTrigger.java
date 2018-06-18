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
import net.minecraft.server.CriterionTriggerLocation.a;
import net.minecraft.server.CriterionTriggerLocation.b;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class PositionTrigger implements ICriterionTrigger<CriterionTriggerLocation.b> {

    private final ResourceLocation id;
    private final Map<PlayerAdvancements, CriterionTriggerLocation.a> listeners = Maps.newHashMap();

    public PositionTrigger(ResourceLocation minecraftkey) {
        this.id = minecraftkey;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerlocation_a == null) {
            criteriontriggerlocation_a = new CriterionTriggerLocation.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerlocation_a);
        }

        criteriontriggerlocation_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerlocation_a != null) {
            criteriontriggerlocation_a.b(criteriontrigger_a);
            if (criteriontriggerlocation_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerLocation.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        LocationPredicate criterionconditionlocation = LocationPredicate.deserialize(jsonobject);

        return new CriterionTriggerLocation.b(this.id, criterionconditionlocation);
    }

    public void trigger(EntityPlayerMP entityplayer) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerlocation_a != null) {
            criteriontriggerlocation_a.a(entityplayer.getServerWorld(), entityplayer.posX, entityplayer.posY, entityplayer.posZ);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerLocation.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(WorldServer worldserver, double d0, double d1, double d2) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerLocation.b) criteriontrigger_a.a()).a(worldserver, d0, d1, d2)) {
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

        public b(ResourceLocation minecraftkey, LocationPredicate criterionconditionlocation) {
            super(minecraftkey);
            this.a = criterionconditionlocation;
        }

        public boolean a(WorldServer worldserver, double d0, double d1, double d2) {
            return this.a.test(worldserver, d0, d1, d2);
        }
    }
}
