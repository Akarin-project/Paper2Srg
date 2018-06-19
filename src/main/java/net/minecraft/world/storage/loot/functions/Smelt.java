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

    private static final Logger LOGGER = LogManager.getLogger();

    public Smelt(LootCondition[] alootitemcondition) {
        super(alootitemcondition);
    }

    @Override
    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        if (itemstack.isEmpty()) {
            return itemstack;
        } else {
            ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);

            if (itemstack1.isEmpty()) {
                Smelt.LOGGER.warn("Couldn\'t smelt {} because there is no smelting recipe", itemstack);
                return itemstack;
            } else {
                ItemStack itemstack2 = itemstack1.copy();

                itemstack2.setCount(itemstack.getCount());
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
