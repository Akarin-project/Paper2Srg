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
        this.func_77201_a(nbttagcompound);
    }

    @Nullable
    public MerchantRecipe func_77203_a(ItemStack itemstack, ItemStack itemstack1, int i) {
        if (i > 0 && i < this.size()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            return this.func_181078_a(itemstack, merchantrecipe.func_77394_a()) && (itemstack1.func_190926_b() && !merchantrecipe.func_77398_c() || merchantrecipe.func_77398_c() && this.func_181078_a(itemstack1, merchantrecipe.func_77396_b())) && itemstack.func_190916_E() >= merchantrecipe.func_77394_a().func_190916_E() && (!merchantrecipe.func_77398_c() || itemstack1.func_190916_E() >= merchantrecipe.func_77396_b().func_190916_E()) ? merchantrecipe : null;
        } else {
            for (int j = 0; j < this.size(); ++j) {
                MerchantRecipe merchantrecipe1 = (MerchantRecipe) this.get(j);

                if (this.func_181078_a(itemstack, merchantrecipe1.func_77394_a()) && itemstack.func_190916_E() >= merchantrecipe1.func_77394_a().func_190916_E() && (!merchantrecipe1.func_77398_c() && itemstack1.func_190926_b() || merchantrecipe1.func_77398_c() && this.func_181078_a(itemstack1, merchantrecipe1.func_77396_b()) && itemstack1.func_190916_E() >= merchantrecipe1.func_77396_b().func_190916_E())) {
                    return merchantrecipe1;
                }
            }

            return null;
        }
    }

    private boolean func_181078_a(ItemStack itemstack, ItemStack itemstack1) {
        return ItemStack.func_179545_c(itemstack, itemstack1) && (!itemstack1.func_77942_o() || itemstack.func_77942_o() && NBTUtil.func_181123_a(itemstack1.func_77978_p(), itemstack.func_77978_p(), false));
    }

    public void func_151391_a(PacketBuffer packetdataserializer) {
        packetdataserializer.writeByte((byte) (this.size() & 255));

        for (int i = 0; i < this.size(); ++i) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            packetdataserializer.func_150788_a(merchantrecipe.func_77394_a());
            packetdataserializer.func_150788_a(merchantrecipe.func_77397_d());
            ItemStack itemstack = merchantrecipe.func_77396_b();

            packetdataserializer.writeBoolean(!itemstack.func_190926_b());
            if (!itemstack.func_190926_b()) {
                packetdataserializer.func_150788_a(itemstack);
            }

            packetdataserializer.writeBoolean(merchantrecipe.func_82784_g());
            packetdataserializer.writeInt(merchantrecipe.func_180321_e());
            packetdataserializer.writeInt(merchantrecipe.func_180320_f());
        }

    }

    public void func_77201_a(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Recipes", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);

            this.add(new MerchantRecipe(nbttagcompound1));
        }

    }

    public NBTTagCompound func_77202_a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.size(); ++i) {
            MerchantRecipe merchantrecipe = (MerchantRecipe) this.get(i);

            nbttaglist.func_74742_a(merchantrecipe.func_77395_g());
        }

        nbttagcompound.func_74782_a("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
