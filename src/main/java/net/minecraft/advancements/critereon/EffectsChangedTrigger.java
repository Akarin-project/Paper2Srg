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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class EffectsChangedTrigger implements ICriterionTrigger<EffectsChangedTrigger.b> {

    private static final ResourceLocation field_193154_a = new ResourceLocation("effects_changed");
    private final Map<PlayerAdvancements, EffectsChangedTrigger.a> field_193155_b = Maps.newHashMap();

    public EffectsChangedTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return EffectsChangedTrigger.field_193154_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<EffectsChangedTrigger.b> criteriontrigger_a) {
        EffectsChangedTrigger.a criteriontriggereffectschanged_a = (EffectsChangedTrigger.a) this.field_193155_b.get(advancementdataplayer);

        if (criteriontriggereffectschanged_a == null) {
            criteriontriggereffectschanged_a = new EffectsChangedTrigger.a(advancementdataplayer);
            this.field_193155_b.put(advancementdataplayer, criteriontriggereffectschanged_a);
        }

        criteriontriggereffectschanged_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<EffectsChangedTrigger.b> criteriontrigger_a) {
        EffectsChangedTrigger.a criteriontriggereffectschanged_a = (EffectsChangedTrigger.a) this.field_193155_b.get(advancementdataplayer);

        if (criteriontriggereffectschanged_a != null) {
            criteriontriggereffectschanged_a.b(criteriontrigger_a);
            if (criteriontriggereffectschanged_a.a()) {
                this.field_193155_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193155_b.remove(advancementdataplayer);
    }

    public EffectsChangedTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        MobEffectsPredicate criterionconditionmobeffect = MobEffectsPredicate.func_193471_a(jsonobject.get("effects"));

        return new EffectsChangedTrigger.b(criterionconditionmobeffect);
    }

    public void func_193153_a(EntityPlayerMP entityplayer) {
        EffectsChangedTrigger.a criteriontriggereffectschanged_a = (EffectsChangedTrigger.a) this.field_193155_b.get(entityplayer.func_192039_O());

        if (criteriontriggereffectschanged_a != null) {
            criteriontriggereffectschanged_a.a(entityplayer);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<EffectsChangedTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<EffectsChangedTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<EffectsChangedTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((EffectsChangedTrigger.b) criteriontrigger_a.a()).a(entityplayer)) {
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

        private final MobEffectsPredicate a;

        public b(MobEffectsPredicate criterionconditionmobeffect) {
            super(EffectsChangedTrigger.field_193154_a);
            this.a = criterionconditionmobeffect;
        }

        public boolean a(EntityPlayerMP entityplayer) {
            return this.a.func_193472_a(entityplayer);
        }
    }
}
