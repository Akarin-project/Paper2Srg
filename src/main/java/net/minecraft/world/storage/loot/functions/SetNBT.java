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

    private final NBTTagCompound tag;

    public SetNBT(LootCondition[] alootitemcondition, NBTTagCompound nbttagcompound) {
        super(alootitemcondition);
        this.tag = nbttagcompound;
    }

    @Override
    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        if (nbttagcompound == null) {
            nbttagcompound = this.tag.copy();
        } else {
            nbttagcompound.merge(this.tag);
        }

        itemstack.setTagCompound(nbttagcompound);
        return itemstack;
    }

    public static class a extends LootFunction.a<SetNBT> {

        public a() {
            super(new ResourceLocation("set_nbt"), SetNBT.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetNBT lootitemfunctionsettag, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("tag", lootitemfunctionsettag.tag.toString());
        }

        public SetNBT a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            try {
                NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(jsonobject, "tag"));

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
