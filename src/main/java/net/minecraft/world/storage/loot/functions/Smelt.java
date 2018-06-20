package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class Smelt extends LootFunction {

    private static final Logger field_186574_a = LogManager.getLogger();

    public Smelt(LootCondition[] alootitemcondition) {
        super(alootitemcondition);
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        if (itemstack.func_190926_b()) {
            return itemstack;
        } else {
            ItemStack itemstack1 = FurnaceRecipes.func_77602_a().func_151395_a(itemstack);

            if (itemstack1.func_190926_b()) {
                Smelt.field_186574_a.warn("Couldn\'t smelt {} because there is no smelting recipe", itemstack);
                return itemstack;
            } else {
                ItemStack itemstack2 = itemstack1.func_77946_l();

                itemstack2.func_190920_e(itemstack.func_190916_E());
                return itemstack2;
            }
        }
    }

    public static class a extends LootFunction.a<Smelt> {

        protected a() {
            super(new ResourceLocation("furnace_smelt"), Smelt.class);
        }

        @Override
        public void a(JsonObject jsonobject, Smelt lootitemfunctionsmelt, JsonSerializationContext jsonserializationcontext) {}

        public Smelt a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new Smelt(alootitemcondition);
        }

        @Override
        public Smelt b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
