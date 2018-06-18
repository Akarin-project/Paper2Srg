package net.minecraft.item;

import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow {

    public ItemTippedArrow() {}

    public EntityArrow createArrow(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, entityliving);

        entitytippedarrow.setPotionEffect(itemstack);
        return entitytippedarrow;
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            Iterator iterator = PotionType.REGISTRY.iterator();

            while (iterator.hasNext()) {
                PotionType potionregistry = (PotionType) iterator.next();

                if (!potionregistry.getEffects().isEmpty()) {
                    nonnulllist.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potionregistry));
                }
            }
        }

    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(itemstack).getNamePrefixed("tipped_arrow.effect."));
    }
}
