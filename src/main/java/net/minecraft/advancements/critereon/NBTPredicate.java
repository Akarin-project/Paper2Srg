package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;

public class NBTPredicate {

    public static final NBTPredicate field_193479_a = new NBTPredicate((NBTTagCompound) null);
    @Nullable
    private final NBTTagCompound field_193480_b;

    public NBTPredicate(@Nullable NBTTagCompound nbttagcompound) {
        this.field_193480_b = nbttagcompound;
    }

    public boolean func_193478_a(ItemStack itemstack) {
        return this == NBTPredicate.field_193479_a ? true : this.func_193477_a((NBTBase) itemstack.func_77978_p());
    }

    public boolean func_193475_a(Entity entity) {
        return this == NBTPredicate.field_193479_a ? true : this.func_193477_a((NBTBase) CommandBase.func_184887_a(entity));
    }

    public boolean func_193477_a(@Nullable NBTBase nbtbase) {
        return nbtbase == null ? this == NBTPredicate.field_193479_a : this.field_193480_b == null || NBTUtil.func_181123_a(this.field_193480_b, nbtbase, true);
    }

    public static NBTPredicate func_193476_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = JsonToNBT.func_180713_a(JsonUtils.func_151206_a(jsonelement, "nbt"));
            } catch (NBTException mojangsonparseexception) {
                throw new JsonSyntaxException("Invalid nbt tag: " + mojangsonparseexception.getMessage());
            }

            return new NBTPredicate(nbttagcompound);
        } else {
            return NBTPredicate.field_193479_a;
        }
    }
}
