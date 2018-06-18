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

    public static List<PotionEffect> getEffectsFromStack(ItemStack itemstack) {
        return getEffectsFromTag(itemstack.getTagCompound());
    }

    public static List<PotionEffect> mergeEffects(PotionType potionregistry, Collection<PotionEffect> collection) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.addAll(potionregistry.getEffects());
        arraylist.addAll(collection);
        return arraylist;
    }

    public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound nbttagcompound) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.addAll(getPotionTypeFromNBT(nbttagcompound).getEffects());
        addCustomPotionEffectToList(nbttagcompound, (List) arraylist);
        return arraylist;
    }

    public static List<PotionEffect> getFullEffectsFromItem(ItemStack itemstack) {
        return getFullEffectsFromTag(itemstack.getTagCompound());
    }

    public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound nbttagcompound) {
        ArrayList arraylist = Lists.newArrayList();

        addCustomPotionEffectToList(nbttagcompound, (List) arraylist);
        return arraylist;
    }

    public static void addCustomPotionEffectToList(@Nullable NBTTagCompound nbttagcompound, List<PotionEffect> list) {
        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                PotionEffect mobeffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound1);

                if (mobeffect != null) {
                    list.add(mobeffect);
                }
            }
        }

    }

    public static int getColor(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        return nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99) ? nbttagcompound.getInteger("CustomPotionColor") : (getPotionFromItem(itemstack) == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList((Collection) getEffectsFromStack(itemstack)));
    }

    public static int getPotionColor(PotionType potionregistry) {
        return potionregistry == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList((Collection) potionregistry.getEffects());
    }

    public static int getPotionColorFromEffectList(Collection<PotionEffect> collection) {
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

                if (mobeffect.doesShowParticles()) {
                    int k = mobeffect.getPotion().getLiquidColor();
                    int l = mobeffect.getAmplifier() + 1;

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

    public static PotionType getPotionFromItem(ItemStack itemstack) {
        return getPotionTypeFromNBT(itemstack.getTagCompound());
    }

    public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound nbttagcompound) {
        return nbttagcompound == null ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(nbttagcompound.getString("Potion"));
    }

    public static ItemStack addPotionToItemStack(ItemStack itemstack, PotionType potionregistry) {
        ResourceLocation minecraftkey = (ResourceLocation) PotionType.REGISTRY.getNameForObject(potionregistry);
        NBTTagCompound nbttagcompound;

        if (potionregistry == PotionTypes.EMPTY) {
            if (itemstack.hasTagCompound()) {
                nbttagcompound = itemstack.getTagCompound();
                nbttagcompound.removeTag("Potion");
                if (nbttagcompound.hasNoTags()) {
                    itemstack.setTagCompound((NBTTagCompound) null);
                }
            }
        } else {
            nbttagcompound = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
            nbttagcompound.setString("Potion", minecraftkey.toString());
            itemstack.setTagCompound(nbttagcompound);
        }

        return itemstack;
    }

    public static ItemStack appendEffects(ItemStack itemstack, Collection<PotionEffect> collection) {
        if (collection.isEmpty()) {
            return itemstack;
        } else {
            NBTTagCompound nbttagcompound = (NBTTagCompound) MoreObjects.firstNonNull(itemstack.getTagCompound(), new NBTTagCompound());
            NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 9);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.appendTag(mobeffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
            itemstack.setTagCompound(nbttagcompound);
            return itemstack;
        }
    }
}
