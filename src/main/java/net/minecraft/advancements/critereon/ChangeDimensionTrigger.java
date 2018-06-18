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
import javax.annotation.Nullable;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTrigger;
import net.minecraft.server.CriterionTriggerChangedDimension;
import net.minecraft.server.CriterionTriggerChangedDimension.a;
import net.minecraft.server.CriterionTriggerChangedDimension.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

public class ChangeDimensionTrigger implements ICriterionTrigger<CriterionTriggerChangedDimension.b> {

    private static final ResourceLocation ID = new ResourceLocation("changed_dimension");
    private final Map<PlayerAdvancements, CriterionTriggerChangedDimension.a> listeners = Maps.newHashMap();

    public ChangeDimensionTrigger() {}

    @Override
    public ResourceLocation getId() {
        return ChangeDimensionTrigger.ID;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerChangedDimension.b> criteriontrigger_a) {
        CriterionTriggerChangedDimension.a criteriontriggerchangeddimension_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerchangeddimension_a == null) {
            criteriontriggerchangeddimension_a = new CriterionTriggerChangedDimension.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerchangeddimension_a);
        }

        criteriontriggerchangeddimension_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerChangedDimension.b> criteriontrigger_a) {
        CriterionTriggerChangedDimension.a criteriontriggerchangeddimension_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerchangeddimension_a != null) {
            criteriontriggerchangeddimension_a.b(criteriontrigger_a);
            if (criteriontriggerchangeddimension_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerChangedDimension.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        DimensionType dimensionmanager = jsonobject.has("from") ? DimensionType.byName(JsonUtils.getString(jsonobject, "from")) : null;
        DimensionType dimensionmanager1 = jsonobject.has("to") ? DimensionType.byName(JsonUtils.getString(jsonobject, "to")) : null;

        return new CriterionTriggerChangedDimension.b(dimensionmanager, dimensionmanager1);
    }

    public void trigger(EntityPlayerMP entityplayer, DimensionType dimensionmanager, DimensionType dimensionmanager1) {
        CriterionTriggerChangedDimension.a criteriontriggerchangeddimension_a = this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerchangeddimension_a != null) {
            criteriontriggerchangeddimension_a.a(dimensionmanager, dimensionmanager1);
        }

    }

    @Override
    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerChangedDimension.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerChangedDimension.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerChangedDimension.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(DimensionType dimensionmanager, DimensionType dimensionmanager1) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerChangedDimension.b) criteriontrigger_a.a()).a(dimensionmanager, dimensionmanager1)) {
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

        @Nullable
        private final DimensionType a;
        @Nullable
        private final DimensionType b;

        public b(@Nullable DimensionType dimensionmanager, @Nullable DimensionType dimensionmanager1) {
            super(ChangeDimensionTrigger.ID);
            this.a = dimensionmanager;
            this.b = dimensionmanager1;
        }

        public boolean a(DimensionType dimensionmanager, DimensionType dimensionmanager1) {
            return this.a != null && this.a != dimensionmanager ? false : this.b == null || this.b == dimensionmanager1;
        }
    }
}
