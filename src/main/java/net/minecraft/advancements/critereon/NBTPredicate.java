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

    public static final NBTPredicate ANY = new NBTPredicate((NBTTagCompound) null);
    @Nullable
    private final NBTTagCompound tag;

    public NBTPredicate(@Nullable NBTTagCompound nbttagcompound) {
        this.tag = nbttagcompound;
    }

    public boolean test(ItemStack itemstack) {
        return this == NBTPredicate.ANY ? true : this.test((NBTBase) itemstack.getTagCompound());
    }

    public boolean test(Entity entity) {
        return this == NBTPredicate.ANY ? true : this.test((NBTBase) CommandBase.entityToNBT(entity));
    }

    public boolean test(@Nullable NBTBase nbtbase) {
        return nbtbase == null ? this == NBTPredicate.ANY : this.tag == null || NBTUtil.areNBTEquals(this.tag, nbtbase, true);
    }

    public static NBTPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(jsonelement, "nbt"));
            } catch (NBTException mojangsonparseexception) {
                throw new JsonSyntaxException("Invalid nbt tag: " + mojangsonparseexception.getMessage());
            }

            return new NBTPredicate(nbttagcompound);
        } else {
            return NBTPredicate.ANY;
        }
    }
}
