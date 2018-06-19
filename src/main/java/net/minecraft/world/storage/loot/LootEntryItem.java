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

    protected final Item field_186368_a;
    protected final LootFunction[] field_186369_b;

    public LootEntryItem(Item item, int i, int j, LootFunction[] alootitemfunction, LootCondition[] alootitemcondition) {
        super(i, j, alootitemcondition);
        this.field_186368_a = item;
        this.field_186369_b = alootitemfunction;
    }

    public void func_186363_a(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        ItemStack itemstack = new ItemStack(this.field_186368_a);
        LootFunction[] alootitemfunction = this.field_186369_b;
        int i = alootitemfunction.length;

        for (int j = 0; j < i; ++j) {
            LootFunction lootitemfunction = alootitemfunction[j];

            if (LootConditionManager.func_186638_a(lootitemfunction.func_186554_a(), random, loottableinfo)) {
                itemstack = lootitemfunction.func_186553_a(itemstack, random, loottableinfo);
            }
        }

        if (!itemstack.func_190926_b()) {
            if (itemstack.func_190916_E() < this.field_186368_a.func_77639_j()) {
                collection.add(itemstack);
            } else {
                int k = itemstack.func_190916_E();

                while (k > 0) {
                    ItemStack itemstack1 = itemstack.func_77946_l();

                    itemstack1.func_190920_e(Math.min(itemstack.func_77976_d(), k));
                    k -= itemstack1.func_190916_E();
                    collection.add(itemstack1);
                }
            }
        }

    }

    protected void func_186362_a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
        if (this.field_186369_b != null && this.field_186369_b.length > 0) {
            jsonobject.add("functions", jsonserializationcontext.serialize(this.field_186369_b));
        }

        ResourceLocation minecraftkey = (ResourceLocation) Item.field_150901_e.func_177774_c(this.field_186368_a);

        if (minecraftkey == null) {
            throw new IllegalArgumentException("Can\'t serialize unknown item " + this.field_186368_a);
        } else {
            jsonobject.addProperty("name", minecraftkey.toString());
        }
    }

    public static LootEntryItem func_186367_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootCondition[] alootitemcondition) {
        Item item = JsonUtils.func_188180_i(jsonobject, "name");
        LootFunction[] alootitemfunction;

        if (jsonobject.has("functions")) {
            alootitemfunction = (LootFunction[]) JsonUtils.func_188174_a(jsonobject, "functions", jsondeserializationcontext, LootFunction[].class);
        } else {
            alootitemfunction = new LootFunction[0];
        }

        return new LootEntryItem(item, i, j, alootitemfunction, alootitemcondition);
    }
}
