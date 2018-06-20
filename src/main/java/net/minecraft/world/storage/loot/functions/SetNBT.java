package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetNBT extends LootFunction {

    private final NBTTagCompound field_186570_a;

    public SetNBT(LootCondition[] alootitemcondition, NBTTagCompound nbttagcompound) {
        super(alootitemcondition);
        this.field_186570_a = nbttagcompound;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        if (nbttagcompound == null) {
            nbttagcompound = this.field_186570_a.func_74737_b();
        } else {
            nbttagcompound.func_179237_a(this.field_186570_a);
        }

        itemstack.func_77982_d(nbttagcompound);
        return itemstack;
    }

    public static class a extends LootFunction.a<SetNBT> {

        public a() {
            super(new ResourceLocation("set_nbt"), SetNBT.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetNBT lootitemfunctionsettag, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("tag", lootitemfunctionsettag.field_186570_a.toString());
        }

        public SetNBT a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            try {
                NBTTagCompound nbttagcompound = JsonToNBT.func_180713_a(JsonUtils.func_151200_h(jsonobject, "tag"));

                return new SetNBT(alootitemcondition, nbttagcompound);
            } catch (NBTException mojangsonparseexception) {
                throw new JsonSyntaxException(mojangsonparseexception);
            }
        }

        @Override
        public SetNBT b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
