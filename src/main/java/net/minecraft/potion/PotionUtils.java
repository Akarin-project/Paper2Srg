package net.minecraft.potion;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class PotionUtils {

    public static List<PotionEffect> func_185189_a(ItemStack itemstack) {
        return func_185185_a(itemstack.func_77978_p());
    }

    public static List<PotionEffect> func_185186_a(PotionType potionregistry, Collection<PotionEffect> collection) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.addAll(potionregistry.func_185170_a());
        arraylist.addAll(collection);
        return arraylist;
    }

    public static List<PotionEffect> func_185185_a(@Nullable NBTTagCompound nbttagcompound) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.addAll(func_185187_c(nbttagcompound).func_185170_a());
        func_185193_a(nbttagcompound, (List) arraylist);
        return arraylist;
    }

    public static List<PotionEffect> func_185190_b(ItemStack itemstack) {
        return func_185192_b(itemstack.func_77978_p());
    }

    public static List<PotionEffect> func_185192_b(@Nullable NBTTagCompound nbttagcompound) {
        ArrayList arraylist = Lists.newArrayList();

        func_185193_a(nbttagcompound, (List) arraylist);
        return arraylist;
    }

    public static void func_185193_a(@Nullable NBTTagCompound nbttagcompound, List<PotionEffect> list) {
        if (nbttagcompound != null && nbttagcompound.func_150297_b("CustomPotionEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
                PotionEffect mobeffect = PotionEffect.func_82722_b(nbttagcompound1);

                if (mobeffect != null) {
                    list.add(mobeffect);
                }
            }
        }

    }

    public static int func_190932_c(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        return nbttagcompound != null && nbttagcompound.func_150297_b("CustomPotionColor", 99) ? nbttagcompound.func_74762_e("CustomPotionColor") : (func_185191_c(itemstack) == PotionTypes.field_185229_a ? 16253176 : func_185181_a((Collection) func_185189_a(itemstack)));
    }

    public static int func_185183_a(PotionType potionregistry) {
        return potionregistry == PotionTypes.field_185229_a ? 16253176 : func_185181_a((Collection) potionregistry.func_185170_a());
    }

    public static int func_185181_a(Collection<PotionEffect> collection) {
        int i = 3694022;

        if (collection.isEmpty()) {
            return 3694022;
        } else {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            int j = 0;
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                if (mobeffect.func_188418_e()) {
                    int k = mobeffect.func_188419_a().func_76401_j();
                    int l = mobeffect.func_76458_c() + 1;

                    f += (float) (l * (k >> 16 & 255)) / 255.0F;
                    f1 += (float) (l * (k >> 8 & 255)) / 255.0F;
                    f2 += (float) (l * (k >> 0 & 255)) / 255.0F;
                    j += l;
                }
            }

            if (j == 0) {
                return 0;
            } else {
                f = f / (float) j * 255.0F;
                f1 = f1 / (float) j * 255.0F;
                f2 = f2 / (float) j * 255.0F;
                return (int) f << 16 | (int) f1 << 8 | (int) f2;
            }
        }
    }

    public static PotionType func_185191_c(ItemStack itemstack) {
        return func_185187_c(itemstack.func_77978_p());
    }

    public static PotionType func_185187_c(@Nullable NBTTagCompound nbttagcompound) {
        return nbttagcompound == null ? PotionTypes.field_185229_a : PotionType.func_185168_a(nbttagcompound.func_74779_i("Potion"));
    }

    public static ItemStack func_185188_a(ItemStack itemstack, PotionType potionregistry) {
        ResourceLocation minecraftkey = (ResourceLocation) PotionType.field_185176_a.func_177774_c(potionregistry);
        NBTTagCompound nbttagcompound;

        if (potionregistry == PotionTypes.field_185229_a) {
            if (itemstack.func_77942_o()) {
                nbttagcompound = itemstack.func_77978_p();
                nbttagcompound.func_82580_o("Potion");
                if (nbttagcompound.func_82582_d()) {
                    itemstack.func_77982_d((NBTTagCompound) null);
                }
            }
        } else {
            nbttagcompound = itemstack.func_77942_o() ? itemstack.func_77978_p() : new NBTTagCompound();
            nbttagcompound.func_74778_a("Potion", minecraftkey.toString());
            itemstack.func_77982_d(nbttagcompound);
        }

        return itemstack;
    }

    public static ItemStack func_185184_a(ItemStack itemstack, Collection<PotionEffect> collection) {
        if (collection.isEmpty()) {
            return itemstack;
        } else {
            NBTTagCompound nbttagcompound = (NBTTagCompound) MoreObjects.firstNonNull(itemstack.func_77978_p(), new NBTTagCompound());
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("CustomPotionEffects", 9);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.func_74742_a(mobeffect.func_82719_a(new NBTTagCompound()));
            }

            nbttagcompound.func_74782_a("CustomPotionEffects", nbttaglist);
            itemstack.func_77982_d(nbttagcompound);
            return itemstack;
        }
    }
}
