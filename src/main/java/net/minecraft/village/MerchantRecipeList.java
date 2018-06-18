package net.minecraft.village;

import java.util.ArrayList;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList<MerchantRecipe> {

    public MerchantRecipeList() {}

    public MerchantRecipeList(NBTTagCompound nbttagcompound) {
        this.readRecipiesFromTags(nbttagcompound);
    }

    @Nullable
    public MerchantRecipe canRecipeBeUsed(ItemStack itemstack, ItemStack itemstack1, int i) {
        if (i > 0 && i < this.size()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            return this.areItemStacksExactlyEqual(itemstack, merchantrecipe.getItemToBuy()) && (itemstack1.isEmpty() && !merchantrecipe.hasSecondItemToBuy() || merchantrecipe.hasSecondItemToBuy() && this.areItemStacksExactlyEqual(itemstack1, merchantrecipe.getSecondItemToBuy())) && itemstack.getCount() >= merchantrecipe.getItemToBuy().getCount() && (!merchantrecipe.hasSecondItemToBuy() || itemstack1.getCount() >= merchantrecipe.getSecondItemToBuy().getCount()) ? merchantrecipe : null;
        } else {
            for (int j = 0; j < this.size(); ++j) {
                MerchantRecipe merchantrecipe1 = (MerchantRecipe) this.get(j);

                if (this.areItemStacksExactlyEqual(itemstack, merchantrecipe1.getItemToBuy()) && itemstack.getCount() >= merchantrecipe1.getItemToBuy().getCount() && (!merchantrecipe1.hasSecondItemToBuy() && itemstack1.isEmpty() || merchantrecipe1.hasSecondItemToBuy() && this.areItemStacksExactlyEqual(itemstack1, merchantrecipe1.getSecondItemToBuy()) && itemstack1.getCount() >= merchantrecipe1.getSecondItemToBuy().getCount())) {
                    return merchantrecipe1;
                }
            }

            return null;
        }
    }

    private boolean areItemStacksExactlyEqual(ItemStack itemstack, ItemStack itemstack1) {
        return ItemStack.areItemsEqual(itemstack, itemstack1) && (!itemstack1.hasTagCompound() || itemstack.hasTagCompound() && NBTUtil.areNBTEquals(itemstack1.getTagCompound(), itemstack.getTagCompound(), false));
    }

    public void writeToBuf(PacketBuffer packetdataserializer) {
        packetdataserializer.writeByte((byte) (this.size() & 255));

        for (int i = 0; i < this.size(); ++i) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            packetdataserializer.writeItemStack(merchantrecipe.getItemToBuy());
            packetdataserializer.writeItemStack(merchantrecipe.getItemToSell());
            ItemStack itemstack = merchantrecipe.getSecondItemToBuy();

            packetdataserializer.writeBoolean(!itemstack.isEmpty());
            if (!itemstack.isEmpty()) {
                packetdataserializer.writeItemStack(itemstack);
            }

            packetdataserializer.writeBoolean(merchantrecipe.isRecipeDisabled());
            packetdataserializer.writeInt(merchantrecipe.getToolUses());
            packetdataserializer.writeInt(merchantrecipe.getMaxTradeUses());
        }

    }

    public void readRecipiesFromTags(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);

            this.add(new MerchantRecipe(nbttagcompound1));
        }

    }

    public NBTTagCompound getRecipiesAsTags() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.size(); ++i) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            nbttaglist.appendTag(merchantrecipe.writeToTags());
        }

        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
