package net.minecraft.world.storage.loot.functions;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class EnchantRandomly extends LootFunction {

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<Enchantment> enchantments;

    public EnchantRandomly(LootCondition[] alootitemcondition, @Nullable List<Enchantment> list) {
        super(alootitemcondition);
        this.enchantments = list == null ? Collections.emptyList() : list;
    }

    @Override
    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        Enchantment enchantment;

        if (this.enchantments.isEmpty()) {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = Enchantment.REGISTRY.iterator();

            while (iterator.hasNext()) {
                Enchantment enchantment1 = (Enchantment) iterator.next();

                if (itemstack.getItem() == Items.BOOK || enchantment1.canApply(itemstack)) {
                    arraylist.add(enchantment1);
                }
            }

            if (arraylist.isEmpty()) {
                EnchantRandomly.LOGGER.warn("Couldn\'t find a compatible enchantment for {}", itemstack);
                return itemstack;
            }

            enchantment = (Enchantment) arraylist.get(random.nextInt(arraylist.size()));
        } else {
            enchantment = this.enchantments.get(random.nextInt(this.enchantments.size()));
        }

        int i = MathHelper.getInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());

        if (itemstack.getItem() == Items.BOOK) {
            itemstack = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantedBook.addEnchantment(itemstack, new EnchantmentData(enchantment, i));
        } else {
            itemstack.addEnchantment(enchantment, i);
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<EnchantRandomly> {

        public a() {
            super(new ResourceLocation("enchant_randomly"), EnchantRandomly.class);
        }

        @Override
        public void a(JsonObject jsonobject, EnchantRandomly lootitemfunctionenchant, JsonSerializationContext jsonserializationcontext) {
            if (!lootitemfunctionenchant.enchantments.isEmpty()) {
                JsonArray jsonarray = new JsonArray();
                Iterator iterator = lootitemfunctionenchant.enchantments.iterator();

                while (iterator.hasNext()) {
                    Enchantment enchantment = (Enchantment) iterator.next();
                    ResourceLocation minecraftkey = Enchantment.REGISTRY.getNameForObject(enchantment);

                    if (minecraftkey == null) {
                        throw new IllegalArgumentException("Don\'t know how to serialize enchantment " + enchantment);
                    }

                    jsonarray.add(new JsonPrimitive(minecraftkey.toString()));
                }

                jsonobject.add("enchantments", jsonarray);
            }

        }

        @Override
        public EnchantRandomly b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            ArrayList arraylist = Lists.newArrayList();

            if (jsonobject.has("enchantments")) {
                JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "enchantments");
                Iterator iterator = jsonarray.iterator();

                while (iterator.hasNext()) {
                    JsonElement jsonelement = (JsonElement) iterator.next();
                    String s = JsonUtils.getString(jsonelement, "enchantment");
                    Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(s));

                    if (enchantment == null) {
                        throw new JsonSyntaxException("Unknown enchantment \'" + s + "\'");
                    }

                    arraylist.add(enchantment);
                }
            }

            return new EnchantRandomly(alootitemcondition, arraylist);
        }
    }
}
