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

    protected final ResourceLocation table;

    public LootEntryTable(ResourceLocation minecraftkey, int i, int j, LootCondition[] alootitemcondition) {
        super(i, j, alootitemcondition);
        this.table = minecraftkey;
    }

    public void addLoot(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        LootTable loottable = loottableinfo.getLootTableManager().getLootTableFromLocation(this.table);
        List list = loottable.generateLootForPools(random, loottableinfo);

        collection.addAll(list);
    }

    protected void serialize(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
        jsonobject.addProperty("name", this.table.toString());
    }

    public static LootEntryTable deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootCondition[] alootitemcondition) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "name"));

        return new LootEntryTable(minecraftkey, i, j, alootitemcondition);
    }
}
