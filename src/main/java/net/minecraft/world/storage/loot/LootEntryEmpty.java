package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryEmpty extends LootEntry {

    public LootEntryEmpty(int i, int j, LootCondition[] alootitemcondition) {
        super(i, j, alootitemcondition);
    }

    public void func_186363_a(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {}

    protected void func_186362_a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {}

    public static LootEntryEmpty func_186372_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootCondition[] alootitemcondition) {
        return new LootEntryEmpty(i, j, alootitemcondition);
    }
}
