package net.minecraft.advancements.critereon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.server.CriterionTriggerImpossible.a;
import net.minecraft.util.ResourceLocation;

public class ImpossibleTrigger implements ICriterionTrigger<CriterionTriggerImpossible.a> {

    private static final ResourceLocation ID = new ResourceLocation("impossible");

    public ImpossibleTrigger() {}

    public ResourceLocation getId() {
        return ImpossibleTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerImpossible.a> criteriontrigger_a) {}

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerImpossible.a> criteriontrigger_a) {}

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {}

    public CriterionTriggerImpossible.a b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new CriterionTriggerImpossible.a();
    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    public static class a extends AbstractCriterionInstance {

        public a() {
            super(ImpossibleTrigger.ID);
        }
    }
}
