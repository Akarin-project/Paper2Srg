package net.minecraft.inventory;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemStackHelper {

    public static ItemStack func_188382_a(List<ItemStack> list, int i, int j) {
        return i >= 0 && i < list.size() && !((ItemStack) list.get(i)).func_190926_b() && j > 0 ? ((ItemStack) list.get(i)).func_77979_a(j) : ItemStack.field_190927_a;
    }

    public static ItemStack func_188383_a(List<ItemStack> list, int i) {
        return i >= 0 && i < list.size() ? (ItemStack) list.set(i, ItemStack.field_190927_a) : ItemStack.field_190927_a;
    }

    public static NBTTagCompound func_191282_a(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist) {
        return func_191281_a(nbttagcompound, nonnulllist, true);
    }

    public static NBTTagCompound func_191281_a(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist, boolean flag) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(i);

            if (!itemstack.func_190926_b()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.func_74774_a("Slot", (byte) i);
                itemstack.func_77955_b(nbttagcompound1);
                nbttaglist.func_74742_a(nbttagcompound1);
            }
        }

        if (!nbttaglist.func_82582_d() || flag) {
            nbttagcompound.func_74782_a("Items", nbttaglist);
        }

        return nbttagcompound;
    }

    public static void func_191283_b(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist) {
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Items", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            int j = nbttagcompound1.func_74771_c("Slot") & 255;

            if (j >= 0 && j < nonnulllist.size()) {
                nonnulllist.set(j, new ItemStack(nbttagcompound1));
            }
        }

    }
}
