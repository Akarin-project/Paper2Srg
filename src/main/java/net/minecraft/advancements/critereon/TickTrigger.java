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
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<TickTrigger.b> {

    public static final ResourceLocation field_193183_a = new ResourceLocation("tick");
    private final Map<PlayerAdvancements, TickTrigger.a> field_193184_b = Maps.newHashMap();

    public TickTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return TickTrigger.field_193183_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<TickTrigger.b> criteriontrigger_a) {
        TickTrigger.a criteriontriggertick_a = this.field_193184_b.get(advancementdataplayer);

        if (criteriontriggertick_a == null) {
            criteriontriggertick_a = new TickTrigger.a(advancementdataplayer);
            this.field_193184_b.put(advancementdataplayer, criteriontriggertick_a);
        }

        criteriontriggertick_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<TickTrigger.b> criteriontrigger_a) {
        TickTrigger.a criteriontriggertick_a = this.field_193184_b.get(advancementdataplayer);

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b(criteriontrigger_a);
            if (criteriontriggertick_a.a()) {
                this.field_193184_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193184_b.remove(advancementdataplayer);
    }

    public TickTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new TickTrigger.b();
    }

    public void func_193182_a(EntityPlayerMP entityplayer) {
        TickTrigger.a criteriontriggertick_a = this.field_193184_b.get(entityplayer.func_192039_O());

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b();
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<TickTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<TickTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<TickTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void b() {
            Iterator iterator = Lists.newArrayList(this.b).iterator();

            while (iterator.hasNext()) {
                ICriterionTrigger.a criteriontrigger_a = (ICriterionTrigger.a) iterator.next();

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
