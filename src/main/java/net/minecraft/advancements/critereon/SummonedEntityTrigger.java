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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class SummonedEntityTrigger implements ICriterionTrigger<SummonedEntityTrigger.b> {

    private static final ResourceLocation field_192232_a = new ResourceLocation("summoned_entity");
    private final Map<PlayerAdvancements, SummonedEntityTrigger.a> field_192233_b = Maps.newHashMap();

    public SummonedEntityTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return SummonedEntityTrigger.field_192232_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<SummonedEntityTrigger.b> criteriontrigger_a) {
        SummonedEntityTrigger.a criteriontriggersummonedentity_a = this.field_192233_b.get(advancementdataplayer);

        if (criteriontriggersummonedentity_a == null) {
            criteriontriggersummonedentity_a = new SummonedEntityTrigger.a(advancementdataplayer);
            this.field_192233_b.put(advancementdataplayer, criteriontriggersummonedentity_a);
        }

        criteriontriggersummonedentity_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<SummonedEntityTrigger.b> criteriontrigger_a) {
        SummonedEntityTrigger.a criteriontriggersummonedentity_a = this.field_192233_b.get(advancementdataplayer);

        if (criteriontriggersummonedentity_a != null) {
            criteriontriggersummonedentity_a.b(criteriontrigger_a);
            if (criteriontriggersummonedentity_a.a()) {
                this.field_192233_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192233_b.remove(advancementdataplayer);
    }

    public SummonedEntityTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("entity"));

        return new SummonedEntityTrigger.b(criterionconditionentity);
    }

    public void func_192229_a(EntityPlayerMP entityplayer, Entity entity) {
        SummonedEntityTrigger.a criteriontriggersummonedentity_a = this.field_192233_b.get(entityplayer.func_192039_O());

        if (criteriontriggersummonedentity_a != null) {
            criteriontriggersummonedentity_a.a(entityplayer, entity);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<SummonedEntityTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<SummonedEntityTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<SummonedEntityTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, Entity entity) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((SummonedEntityTrigger.b) criteriontrigger_a.a()).a(entityplayer, entity)) {
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

        private final EntityPredicate a;

        public b(EntityPredicate criterionconditionentity) {
            super(SummonedEntityTrigger.field_192232_a);
            this.a = criterionconditionentity;
        }

        public boolean a(EntityPlayerMP entityplayer, Entity entity) {
            return this.a.func_192482_a(entityplayer, entity);
        }
    }
}
