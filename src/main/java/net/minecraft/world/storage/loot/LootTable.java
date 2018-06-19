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
import net.minecraft.server.LootTable.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class LootTable {

    private static final Logger field_186465_b = LogManager.getLogger();
    public static final LootTable field_186464_a = new LootTable(new LootPool[0]);
    private final LootPool[] field_186466_c;

    public LootTable(LootPool[] alootselector) {
        this.field_186466_c = alootselector;
    }

    public List<ItemStack> func_186462_a(Random random, LootContext loottableinfo) {
        ArrayList arraylist = Lists.newArrayList();

        if (loottableinfo.func_186496_a(this)) {
            LootPool[] alootselector = this.field_186466_c;
            int i = alootselector.length;

            for (int j = 0; j < i; ++j) {
                LootPool lootselector = alootselector[j];

                lootselector.func_186449_b(arraylist, random, loottableinfo);
            }

            loottableinfo.func_186490_b(this);
        } else {
            LootTable.field_186465_b.warn("Detected infinite loop in loot tables");
        }

        return arraylist;
    }

    public void func_186460_a(IInventory iinventory, Random random, LootContext loottableinfo) {
        List list = this.func_186462_a(random, loottableinfo);
        List list1 = this.func_186459_a(iinventory, random);

        this.func_186463_a(list, list1.size(), random);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (list1.isEmpty()) {
                LootTable.field_186465_b.warn("Tried to over-fill a container");
                return;
            }

            if (itemstack.func_190926_b()) {
                iinventory.func_70299_a(((Integer) list1.remove(list1.size() - 1)).intValue(), ItemStack.field_190927_a);
            } else {
                iinventory.func_70299_a(((Integer) list1.remove(list1.size() - 1)).intValue(), itemstack);
            }
        }

    }

    private void func_186463_a(List<ItemStack> list, int i, Random random) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (itemstack.func_190926_b()) {
                iterator.remove();
            } else if (itemstack.func_190916_E() > 1) {
                arraylist.add(itemstack);
                iterator.remove();
            }
        }

        i -= list.size();

        while (i > 0 && !arraylist.isEmpty()) {
            ItemStack itemstack1 = (ItemStack) arraylist.remove(MathHelper.func_76136_a(random, 0, arraylist.size() - 1));
            int j = MathHelper.func_76136_a(random, 1, itemstack1.func_190916_E() / 2);
            ItemStack itemstack2 = itemstack1.func_77979_a(j);

            if (itemstack1.func_190916_E() > 1 && random.nextBoolean()) {
                arraylist.add(itemstack1);
            } else {
                list.add(itemstack1);
            }

            if (itemstack2.func_190916_E() > 1 && random.nextBoolean()) {
                arraylist.add(itemstack2);
            } else {
                list.add(itemstack2);
            }
        }

        list.addAll(arraylist);
        Collections.shuffle(list, random);
    }

    private List<Integer> func_186459_a(IInventory iinventory, Random random) {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < iinventory.func_70302_i_(); ++i) {
            if (iinventory.func_70301_a(i).func_190926_b()) {
                arraylist.add(Integer.valueOf(i));
            }
        }

        Collections.shuffle(arraylist, random);
        return arraylist;
    }

    public static class a implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {

        public a() {}

        public LootTable a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "loot table");
            LootPool[] alootselector = (LootPool[]) JsonUtils.func_188177_a(jsonobject, "pools", new LootPool[0], jsondeserializationcontext, LootPool[].class);

            return new LootTable(alootselector);
        }

        public JsonElement a(LootTable loottable, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("pools", jsonserializationcontext.serialize(loottable.field_186466_c));
            return jsonobject;
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((LootTable) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
