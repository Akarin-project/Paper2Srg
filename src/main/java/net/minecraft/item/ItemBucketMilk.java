package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemBucketMilk extends Item {

    public ItemBucketMilk() {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        if (entityliving instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityliving;

            CriteriaTriggers.CONSUME_ITEM.trigger(entityplayer, itemstack);
            entityplayer.addStat(StatList.getObjectUseStats((Item) this));
        }

        if (entityliving instanceof EntityPlayer && !((EntityPlayer) entityliving).capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        if (!world.isRemote) {
            entityliving.clearActivePotions();
        }

        return itemstack.isEmpty() ? new ItemStack(Items.BUCKET) : itemstack;
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
}
