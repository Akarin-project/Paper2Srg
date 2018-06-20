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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class UsedEnderEyeTrigger implements ICriterionTrigger<UsedEnderEyeTrigger.b> {

    private static final ResourceLocation field_192242_a = new ResourceLocation("used_ender_eye");
    private final Map<PlayerAdvancements, UsedEnderEyeTrigger.a> field_192243_b = Maps.newHashMap();

    public UsedEnderEyeTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return UsedEnderEyeTrigger.field_192242_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<UsedEnderEyeTrigger.b> criteriontrigger_a) {
        UsedEnderEyeTrigger.a criteriontriggerusedendereye_a = this.field_192243_b.get(advancementdataplayer);

        if (criteriontriggerusedendereye_a == null) {
            criteriontriggerusedendereye_a = new UsedEnderEyeTrigger.a(advancementdataplayer);
            this.field_192243_b.put(advancementdataplayer, criteriontriggerusedendereye_a);
        }

        criteriontriggerusedendereye_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<UsedEnderEyeTrigger.b> criteriontrigger_a) {
        UsedEnderEyeTrigger.a criteriontriggerusedendereye_a = this.field_192243_b.get(advancementdataplayer);

        if (criteriontriggerusedendereye_a != null) {
            criteriontriggerusedendereye_a.b(criteriontrigger_a);
            if (criteriontriggerusedendereye_a.a()) {
                this.field_192243_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192243_b.remove(advancementdataplayer);
    }

    public UsedEnderEyeTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("distance"));

        return new UsedEnderEyeTrigger.b(criterionconditionvalue);
    }

    public void func_192239_a(EntityPlayerMP entityplayer, BlockPos blockposition) {
        UsedEnderEyeTrigger.a criteriontriggerusedendereye_a = this.field_192243_b.get(entityplayer.func_192039_O());

        if (criteriontriggerusedendereye_a != null) {
            double d0 = entityplayer.field_70165_t - blockposition.func_177958_n();
            double d1 = entityplayer.field_70161_v - blockposition.func_177952_p();

            criteriontriggerusedendereye_a.a(d0 * d0 + d1 * d1);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<UsedEnderEyeTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<UsedEnderEyeTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<UsedEnderEyeTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(double d0) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((UsedEnderEyeTrigger.b) criteriontrigger_a.a()).a(d0)) {
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
            super(UsedEnderEyeTrigger.field_192242_a);
            this.a = criterionconditionvalue;
        }

        public boolean a(double d0) {
            return this.a.func_192513_a(d0);
        }
    }
}
