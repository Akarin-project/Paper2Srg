package net.minecraft.world.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class LootTable {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final LootTable EMPTY_LOOT_TABLE = new LootTable(new LootPool[0]);
    private final LootPool[] pools;

    public LootTable(LootPool[] alootselector) {
        this.pools = alootselector;
    }

    public List<ItemStack> generateLootForPools(Random random, LootContext loottableinfo) {
        ArrayList arraylist = Lists.newArrayList();

        if (loottableinfo.addLootTable(this)) {
            LootPool[] alootselector = this.pools;
            int i = alootselector.length;

            for (int j = 0; j < i; ++j) {
                LootPool lootselector = alootselector[j];

                lootselector.generateLoot(arraylist, random, loottableinfo);
            }

            loottableinfo.removeLootTable(this);
        } else {
            LootTable.LOGGER.warn("Detected infinite loop in loot tables");
        }

        return arraylist;
    }

    public void fillInventory(IInventory iinventory, Random random, LootContext loottableinfo) {
        List list = this.generateLootForPools(random, loottableinfo);
        List list1 = this.getEmptySlotsRandomized(iinventory, random);

        this.shuffleItems(list, list1.size(), random);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (list1.isEmpty()) {
                LootTable.LOGGER.warn("Tried to over-fill a container");
                return;
            }

            if (itemstack.isEmpty()) {
                iinventory.setInventorySlotContents(((Integer) list1.remove(list1.size() - 1)).intValue(), ItemStack.EMPTY);
            } else {
                iinventory.setInventorySlotContents(((Integer) list1.remove(list1.size() - 1)).intValue(), itemstack);
            }
        }

    }

    private void shuffleItems(List<ItemStack> list, int i, Random random) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 1) {
                arraylist.add(itemstack);
                iterator.remove();
            }
        }

        i -= list.size();

        while (i > 0 && !arraylist.isEmpty()) {
            ItemStack itemstack1 = (ItemStack) arraylist.remove(MathHelper.getInt(random, 0, arraylist.size() - 1));
            int j = MathHelper.getInt(random, 1, itemstack1.getCount() / 2);
            ItemStack itemstack2 = itemstack1.splitStack(j);

            if (itemstack1.getCount() > 1 && random.nextBoolean()) {
                arraylist.add(itemstack1);
            } else {
                list.add(itemstack1);
            }

            if (itemstack2.getCount() > 1 && random.nextBoolean()) {
                arraylist.add(itemstack2);
            } else {
                list.add(itemstack2);
            }
        }

        list.addAll(arraylist);
        Collections.shuffle(list, random);
    }

    private List<Integer> getEmptySlotsRandomized(IInventory iinventory, Random random) {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < iinventory.getSizeInventory(); ++i) {
            if (iinventory.getStackInSlot(i).isEmpty()) {
                arraylist.add(Integer.valueOf(i));
            }
        }

        Collections.shuffle(arraylist, random);
        return arraylist;
    }

    public static class a implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {

        public a() {}

        public LootTable a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "loot table");
            LootPool[] alootselector = JsonUtils.deserializeClass(jsonobject, "pools", new LootPool[0], jsondeserializationcontext, LootPool[].class);

            return new LootTable(alootselector);
        }

        public JsonElement a(LootTable loottable, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("pools", jsonserializationcontext.serialize(loottable.pools));
            return jsonobject;
        }

        @Override
        public JsonElement serialize(LootTable object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public LootTable deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
