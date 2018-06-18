package net.minecraft.inventory;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemStackHelper {

    public static ItemStack getAndSplit(List<ItemStack> list, int i, int j) {
        return i >= 0 && i < list.size() && !((ItemStack) list.get(i)).isEmpty() && j > 0 ? ((ItemStack) list.get(i)).splitStack(j) : ItemStack.EMPTY;
    }

    public static ItemStack getAndRemove(List<ItemStack> list, int i) {
        return i >= 0 && i < list.size() ? (ItemStack) list.set(i, ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    public static NBTTagCompound saveAllItems(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist) {
        return saveAllItems(nbttagcompound, nonnulllist, true);
    }

    public static NBTTagCompound saveAllItems(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist, boolean flag) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(i);

            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        if (!nbttaglist.hasNoTags() || flag) {
            nbttagcompound.setTag("Items", nbttaglist);
        }

        return nbttagcompound;
    }

    public static void loadAllItems(NBTTagCompound nbttagcompound, NonNullList<ItemStack> nonnulllist) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < nonnulllist.size()) {
                nonnulllist.set(j, new ItemStack(nbttagcompound1));
            }
        }

    }
}
