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

    private static final Logger LOGGER = LogManager.getLogger();
    private final RandomValueRange metaRange;

    public SetMetadata(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds) {
        super(alootitemcondition);
        this.metaRange = lootvaluebounds;
    }

    @Override
    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        if (itemstack.isItemStackDamageable()) {
            SetMetadata.LOGGER.warn("Couldn\'t set data of loot item {}", itemstack);
        } else {
            itemstack.setItemDamage(this.metaRange.generateInt(random));
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<SetMetadata> {

        protected a() {
            super(new ResourceLocation("set_data"), SetMetadata.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetMetadata lootitemfunctionsetdata, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("data", jsonserializationcontext.serialize(lootitemfunctionsetdata.metaRange));
        }

        public SetMetadata a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new SetMetadata(alootitemcondition, JsonUtils.deserializeClass(jsonobject, "data", jsondeserializationcontext, RandomValueRange.class));
        }

        @Override
        public SetMetadata b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
