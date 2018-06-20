package net.minecraft.advancements.critereon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;

public class ImpossibleTrigger implements ICriterionTrigger<ImpossibleTrigger.a> {

    private static final ResourceLocation field_192205_a = new ResourceLocation("impossible");

    public ImpossibleTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return ImpossibleTrigger.field_192205_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<ImpossibleTrigger.a> criteriontrigger_a) {}

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<ImpossibleTrigger.a> criteriontrigger_a) {}

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {}

    public ImpossibleTrigger.a b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new ImpossibleTrigger.a();
    }

    @Override
    public a func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    public static class a extends AbstractCriterionInstance {

        public a() {
            super(ImpossibleTrigger.field_192205_a);
        }
    }
}
