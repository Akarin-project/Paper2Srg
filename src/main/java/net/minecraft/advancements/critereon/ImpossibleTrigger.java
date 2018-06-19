package net.minecraft.advancements.critereon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.server.CriterionTriggerImpossible.a;
import net.minecraft.util.ResourceLocation;

public class ImpossibleTrigger implements ICriterionTrigger<CriterionTriggerImpossible.a> {

    private static final ResourceLocation field_192205_a = new ResourceLocation("impossible");

    public ImpossibleTrigger() {}

    public ResourceLocation func_192163_a() {
        return ImpossibleTrigger.field_192205_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerImpossible.a> criteriontrigger_a) {}

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerImpossible.a> criteriontrigger_a) {}

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {}

    public CriterionTriggerImpossible.a b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new CriterionTriggerImpossible.a();
    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    public static class a extends AbstractCriterionInstance {

        public a() {
            super(ImpossibleTrigger.field_192205_a);
        }
    }
}
