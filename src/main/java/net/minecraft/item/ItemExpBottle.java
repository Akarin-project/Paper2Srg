package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemExpBottle extends Item {

    public ItemExpBottle() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemExpBottle.itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EntityExpBottle entitythrownexpbottle = new EntityExpBottle(world, entityhuman);

            entitythrownexpbottle.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, -20.0F, 0.7F, 1.0F);
            world.spawnEntity(entitythrownexpbottle);
        }

        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
