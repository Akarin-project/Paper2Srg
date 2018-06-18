package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootEntryItem extends LootEntry {

    protected final Item item;
    protected final LootFunction[] functions;

    public LootEntryItem(Item item, int i, int j, LootFunction[] alootitemfunction, LootCondition[] alootitemcondition) {
        super(i, j, alootitemcondition);
        this.item = item;
        this.functions = alootitemfunction;
    }

    public void addLoot(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        ItemStack itemstack = new ItemStack(this.item);
        LootFunction[] alootitemfunction = this.functions;
        int i = alootitemfunction.length;

        for (int j = 0; j < i; ++j) {
            LootFunction lootitemfunction = alootitemfunction[j];

            if (LootConditionManager.testAllConditions(lootitemfunction.getConditions(), random, loottableinfo)) {
                itemstack = lootitemfunction.apply(itemstack, random, loottableinfo);
            }
        }

        if (!itemstack.isEmpty()) {
            if (itemstack.getCount() < this.item.getItemStackLimit()) {
                collection.add(itemstack);
            } else {
                int k = itemstack.getCount();

                while (k > 0) {
                    ItemStack itemstack1 = itemstack.copy();

                    itemstack1.setCount(Math.min(itemstack.getMaxStackSize(), k));
                    k -= itemstack1.getCount();
                    collection.add(itemstack1);
                }
            }
        }

    }

    protected void serialize(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
        if (this.functions != null && this.functions.length > 0) {
            jsonobject.add("functions", jsonserializationcontext.serialize(this.functions));
        }

        ResourceLocation minecraftkey = (ResourceLocation) Item.REGISTRY.getNameForObject(this.item);

        if (minecraftkey == null) {
            throw new IllegalArgumentException("Can\'t serialize unknown item " + this.item);
        } else {
            jsonobject.addProperty("name", minecraftkey.toString());
        }
    }

    public static LootEntryItem deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootCondition[] alootitemcondition) {
        Item item = JsonUtils.getItem(jsonobject, "name");
        LootFunction[] alootitemfunction;

        if (jsonobject.has("functions")) {
            alootitemfunction = (LootFunction[]) JsonUtils.deserializeClass(jsonobject, "functions", jsondeserializationcontext, LootFunction[].class);
        } else {
            alootitemfunction = new LootFunction[0];
        }

        return new LootEntryItem(item, i, j, alootitemfunction, alootitemcondition);
    }
}
