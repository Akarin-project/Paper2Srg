package net.minecraft.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;


public class ItemLingeringPotion extends ItemPotion {

    public ItemLingeringPotion() {}

    public String getItemStackDisplayName(ItemStack itemstack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(itemstack).getNamePrefixed("lingering_potion.effect."));
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        ItemStack itemstack1 = entityhuman.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);

        world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_LINGERINGPOTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemLingeringPotion.itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EntityPotion entitypotion = new EntityPotion(world, entityhuman, itemstack1);

            entitypotion.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntity(entitypotion);
        }

        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
