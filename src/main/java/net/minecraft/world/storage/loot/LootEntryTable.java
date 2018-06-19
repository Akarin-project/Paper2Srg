package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryTable extends LootEntry {

    protected final ResourceLocation field_186371_a;

    public LootEntryTable(ResourceLocation minecraftkey, int i, int j, LootCondition[] alootitemcondition) {
        super(i, j, alootitemcondition);
        this.field_186371_a = minecraftkey;
    }

    public void func_186363_a(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        LootTable loottable = loottableinfo.func_186497_e().func_186521_a(this.field_186371_a);
        List list = loottable.func_186462_a(random, loottableinfo);

        collection.addAll(list);
    }

    protected void func_186362_a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
        jsonobject.addProperty("name", this.field_186371_a.toString());
    }

    public static LootEntryTable func_186370_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootCondition[] alootitemcondition) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "name"));

        return new LootEntryTable(minecraftkey, i, j, alootitemcondition);
    }
}
