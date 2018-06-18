package net.minecraft.item;

import java.util.Iterator;
import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPotion extends Item {

    public ItemPotion() {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.BREWING);
    }

    public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        EntityPlayer entityhuman = entityliving instanceof EntityPlayer ? (EntityPlayer) entityliving : null;

        if (entityhuman == null || !entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityhuman, itemstack);
        }

        if (!world.isRemote) {
            List list = PotionUtils.getEffectsFromStack(itemstack);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                if (mobeffect.getPotion().isInstant()) {
                    mobeffect.getPotion().affectEntity(entityhuman, entityhuman, entityliving, mobeffect.getAmplifier(), 1.0D);
                } else {
                    entityliving.addPotionEffect(new PotionEffect(mobeffect));
                }
            }
        }

        if (entityhuman != null) {
            entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        }

        if (entityhuman == null || !entityhuman.capabilities.isCreativeMode) {
            if (itemstack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (entityhuman != null) {
                entityhuman.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return itemstack;
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        entityhuman.setActiveHand(enumhand);
        return new ActionResult(EnumActionResult.SUCCESS, entityhuman.getHeldItem(enumhand));
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(itemstack).getNamePrefixed("potion.effect."));
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            Iterator iterator = PotionType.REGISTRY.iterator();

            while (iterator.hasNext()) {
                PotionType potionregistry = (PotionType) iterator.next();

                if (potionregistry != PotionTypes.EMPTY) {
                    nonnulllist.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potionregistry));
                }
            }
        }

    }
}
