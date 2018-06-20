package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetMetadata extends LootFunction {

    private static final Logger field_186572_a = LogManager.getLogger();
    private final RandomValueRange field_186573_b;

    public SetMetadata(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds) {
        super(alootitemcondition);
        this.field_186573_b = lootvaluebounds;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        if (itemstack.func_77984_f()) {
            SetMetadata.field_186572_a.warn("Couldn\'t set data of loot item {}", itemstack);
        } else {
            itemstack.func_77964_b(this.field_186573_b.func_186511_a(random));
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<SetMetadata> {

        protected a() {
            super(new ResourceLocation("set_data"), SetMetadata.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetMetadata lootitemfunctionsetdata, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("data", jsonserializationcontext.serialize(lootitemfunctionsetdata.field_186573_b));
        }

        public SetMetadata a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new SetMetadata(alootitemcondition, JsonUtils.func_188174_a(jsonobject, "data", jsondeserializationcontext, RandomValueRange.class));
        }

        @Override
        public SetMetadata b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
