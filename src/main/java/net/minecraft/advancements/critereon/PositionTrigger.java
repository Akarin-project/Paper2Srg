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

    private final ResourceLocation field_192217_a;
    private final Map<PlayerAdvancements, CriterionTriggerLocation.a> field_192218_b = Maps.newHashMap();

    public PositionTrigger(ResourceLocation minecraftkey) {
        this.field_192217_a = minecraftkey;
    }

    public ResourceLocation func_192163_a() {
        return this.field_192217_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.field_192218_b.get(advancementdataplayer);

        if (criteriontriggerlocation_a == null) {
            criteriontriggerlocation_a = new CriterionTriggerLocation.a(advancementdataplayer);
            this.field_192218_b.put(advancementdataplayer, criteriontriggerlocation_a);
        }

        criteriontriggerlocation_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerLocation.b> criteriontrigger_a) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.field_192218_b.get(advancementdataplayer);

        if (criteriontriggerlocation_a != null) {
            criteriontriggerlocation_a.b(criteriontrigger_a);
            if (criteriontriggerlocation_a.a()) {
                this.field_192218_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192218_b.remove(advancementdataplayer);
    }

    public CriterionTriggerLocation.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        LocationPredicate criterionconditionlocation = LocationPredicate.func_193454_a(jsonobject);

        return new CriterionTriggerLocation.b(this.field_192217_a, criterionconditionlocation);
    }

    public void func_192215_a(EntityPlayerMP entityplayer) {
        CriterionTriggerLocation.a criteriontriggerlocation_a = (CriterionTriggerLocation.a) this.field_192218_b.get(entityplayer.func_192039_O());

        if (criteriontriggerlocation_a != null) {
            criteriontriggerlocation_a.a(entityplayer.func_71121_q(), entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            return this.a.func_193452_a(worldserver, d0, d1, d2);
        }
    }
}
