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

    private static final Logger field_186557_a = LogManager.getLogger();
    private final List<Enchantment> field_186558_b;

    public EnchantRandomly(LootCondition[] alootitemcondition, @Nullable List<Enchantment> list) {
        super(alootitemcondition);
        this.field_186558_b = list == null ? Collections.emptyList() : list;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        Enchantment enchantment;

        if (this.field_186558_b.isEmpty()) {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = Enchantment.field_185264_b.iterator();

            while (iterator.hasNext()) {
                Enchantment enchantment1 = (Enchantment) iterator.next();

                if (itemstack.func_77973_b() == Items.field_151122_aG || enchantment1.func_92089_a(itemstack)) {
                    arraylist.add(enchantment1);
                }
            }

            if (arraylist.isEmpty()) {
                EnchantRandomly.field_186557_a.warn("Couldn\'t find a compatible enchantment for {}", itemstack);
                return itemstack;
            }

            enchantment = (Enchantment) arraylist.get(random.nextInt(arraylist.size()));
        } else {
            enchantment = this.field_186558_b.get(random.nextInt(this.field_186558_b.size()));
        }

        int i = MathHelper.func_76136_a(random, enchantment.func_77319_d(), enchantment.func_77325_b());

        if (itemstack.func_77973_b() == Items.field_151122_aG) {
            itemstack = new ItemStack(Items.field_151134_bR);
            ItemEnchantedBook.func_92115_a(itemstack, new EnchantmentData(enchantment, i));
        } else {
            itemstack.func_77966_a(enchantment, i);
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<EnchantRandomly> {

        public a() {
            super(new ResourceLocation("enchant_randomly"), EnchantRandomly.class);
        }

        @Override
        public void a(JsonObject jsonobject, EnchantRandomly lootitemfunctionenchant, JsonSerializationContext jsonserializationcontext) {
            if (!lootitemfunctionenchant.field_186558_b.isEmpty()) {
                JsonArray jsonarray = new JsonArray();
                Iterator iterator = lootitemfunctionenchant.field_186558_b.iterator();

                while (iterator.hasNext()) {
                    Enchantment enchantment = (Enchantment) iterator.next();
                    ResourceLocation minecraftkey = Enchantment.field_185264_b.func_177774_c(enchantment);

                    if (minecraftkey == null) {
                        throw new IllegalArgumentException("Don\'t know how to serialize enchantment " + enchantment);
                    }

                    jsonarray.add(new JsonPrimitive(minecraftkey.toString()));
                }

                jsonobject.add("enchantments", jsonarray);
            }

        }

        public EnchantRandomly a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            ArrayList arraylist = Lists.newArrayList();

            if (jsonobject.has("enchantments")) {
                JsonArray jsonarray = JsonUtils.func_151214_t(jsonobject, "enchantments");
                Iterator iterator = jsonarray.iterator();

                while (iterator.hasNext()) {
                    JsonElement jsonelement = (JsonElement) iterator.next();
                    String s = JsonUtils.func_151206_a(jsonelement, "enchantment");
                    Enchantment enchantment = Enchantment.field_185264_b.func_82594_a(new ResourceLocation(s));

                    if (enchantment == null) {
                        throw new JsonSyntaxException("Unknown enchantment \'" + s + "\'");
                    }

                    arraylist.add(enchantment);
                }
            }

            return new EnchantRandomly(alootitemcondition, arraylist);
        }

        @Override
        public EnchantRandomly b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
