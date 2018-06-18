package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemEnderPearl extends Item {

    public ItemEnderPearl() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        // CraftBukkit start - change order
        if (!world.isRemote) {
            EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, entityhuman);

            entityenderpearl.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, 0.0F, 1.5F, 1.0F);
            if (!world.spawnEntity(entityenderpearl)) {
                if (entityhuman instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory();
                }
                return new ActionResult(EnumActionResult.FAIL, itemstack);
            }
        }

        if (!entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemEnderPearl.itemRand.nextFloat() * 0.4F + 0.8F));
        entityhuman.getCooldownTracker().setCooldown(this, 20);
        // CraftBukkit end

        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
