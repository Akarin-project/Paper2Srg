package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemEgg extends Item {

    public ItemEgg() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (ItemEgg.itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EntityEgg entityegg = new EntityEgg(world, entityhuman);

            entityegg.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(entityegg);
        }

        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
