package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerTick.a;
import net.minecraft.server.CriterionTriggerTick.b;
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<CriterionTriggerTick.b> {

    public static final ResourceLocation field_193183_a = new ResourceLocation("tick");
    private final Map<PlayerAdvancements, CriterionTriggerTick.a> field_193184_b = Maps.newHashMap();

    public TickTrigger() {}

    public ResourceLocation func_192163_a() {
        return TickTrigger.field_193183_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.field_193184_b.get(advancementdataplayer);

        if (criteriontriggertick_a == null) {
            criteriontriggertick_a = new CriterionTriggerTick.a(advancementdataplayer);
            this.field_193184_b.put(advancementdataplayer, criteriontriggertick_a);
        }

        criteriontriggertick_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.field_193184_b.get(advancementdataplayer);

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b(criteriontrigger_a);
            if (criteriontriggertick_a.a()) {
                this.field_193184_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193184_b.remove(advancementdataplayer);
    }

    public CriterionTriggerTick.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new CriterionTriggerTick.b();
    }

    public void func_193182_a(EntityPlayerMP entityplayer) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.field_193184_b.get(entityplayer.func_192039_O());

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b();
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerTick.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void b() {
            Iterator iterator = Lists.newArrayList(this.b).iterator();

            while (iterator.hasNext()) {
                CriterionTrigger.a criteriontrigger_a = (CriterionTrigger.a) iterator.next();

                criteriontrigger_a.a(this.a);
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        public b() {
            super(TickTrigger.field_193183_a);
        }
    }
}
