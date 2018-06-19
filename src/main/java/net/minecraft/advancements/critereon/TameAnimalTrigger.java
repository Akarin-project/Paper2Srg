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
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerTamedAnimal.a;
import net.minecraft.server.CriterionTriggerTamedAnimal.b;
import net.minecraft.util.ResourceLocation;

public class TameAnimalTrigger implements ICriterionTrigger<CriterionTriggerTamedAnimal.b> {

    private static final ResourceLocation field_193179_a = new ResourceLocation("tame_animal");
    private final Map<PlayerAdvancements, CriterionTriggerTamedAnimal.a> field_193180_b = Maps.newHashMap();

    public TameAnimalTrigger() {}

    public ResourceLocation func_192163_a() {
        return TameAnimalTrigger.field_193179_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTamedAnimal.b> criteriontrigger_a) {
        CriterionTriggerTamedAnimal.a criteriontriggertamedanimal_a = (CriterionTriggerTamedAnimal.a) this.field_193180_b.get(advancementdataplayer);

        if (criteriontriggertamedanimal_a == null) {
            criteriontriggertamedanimal_a = new CriterionTriggerTamedAnimal.a(advancementdataplayer);
            this.field_193180_b.put(advancementdataplayer, criteriontriggertamedanimal_a);
        }

        criteriontriggertamedanimal_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTamedAnimal.b> criteriontrigger_a) {
        CriterionTriggerTamedAnimal.a criteriontriggertamedanimal_a = (CriterionTriggerTamedAnimal.a) this.field_193180_b.get(advancementdataplayer);

        if (criteriontriggertamedanimal_a != null) {
            criteriontriggertamedanimal_a.b(criteriontrigger_a);
            if (criteriontriggertamedanimal_a.a()) {
                this.field_193180_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193180_b.remove(advancementdataplayer);
    }

    public CriterionTriggerTamedAnimal.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("entity"));

        return new CriterionTriggerTamedAnimal.b(criterionconditionentity);
    }

    public void func_193178_a(EntityPlayerMP entityplayer, EntityAnimal entityanimal) {
        CriterionTriggerTamedAnimal.a criteriontriggertamedanimal_a = (CriterionTriggerTamedAnimal.a) this.field_193180_b.get(entityplayer.func_192039_O());

        if (criteriontriggertamedanimal_a != null) {
            criteriontriggertamedanimal_a.a(entityplayer, entityanimal);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerTamedAnimal.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerTamedAnimal.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerTamedAnimal.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityAnimal entityanimal) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerTamedAnimal.b) criteriontrigger_a.a()).a(entityplayer, entityanimal)) {
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

        private final EntityPredicate a;

        public b(EntityPredicate criterionconditionentity) {
            super(TameAnimalTrigger.field_193179_a);
            this.a = criterionconditionentity;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityAnimal entityanimal) {
            return this.a.func_192482_a(entityplayer, entityanimal);
        }
    }
}
