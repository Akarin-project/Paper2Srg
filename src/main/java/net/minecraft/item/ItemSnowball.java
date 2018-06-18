package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemSnowball extends Item {

    public ItemSnowball() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        // CraftBukkit start - moved down
        /*
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        world.a((EntityHuman) null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.hp, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemSnowball.j.nextFloat() * 0.4F + 0.8F));
        */
        if (!world.isRemote) {
            EntitySnowball entitysnowball = new EntitySnowball(world, entityhuman);

            entitysnowball.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, 0.0F, 1.5F, 1.0F);
            if (world.spawnEntity(entitysnowball)) {
                if (!entityhuman.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }

                world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemSnowball.itemRand.nextFloat() * 0.4F + 0.8F));
            } else if (entityhuman instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory();
            }
        }
        // CraftBukkit end

        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
