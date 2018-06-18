package net.minecraft.item;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.bukkit.event.player.PlayerFishEvent;

public class ItemFishingRod extends Item {

    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
        });
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (entityhuman.fishEntity != null) {
            int i = entityhuman.fishEntity.handleHookRetraction();

            itemstack.damageItem(i, entityhuman);
            entityhuman.swingArm(enumhand);
            world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (ItemFishingRod.itemRand.nextFloat() * 0.4F + 0.8F));
        } else {
            // world.a((EntityHuman) null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.L, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.j.nextFloat() * 0.4F + 0.8F)); // CraftBukkit - moved down
            if (!world.isRemote) {
                EntityFishHook entityfishinghook = new EntityFishHook(world, entityhuman);
                int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (j > 0) {
                    entityfishinghook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (k > 0) {
                    entityfishinghook.setLuck(k);
                }

                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), null, (org.bukkit.entity.Fish) entityfishinghook.getBukkitEntity(), PlayerFishEvent.State.FISHING);
                world.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    entityhuman.fishEntity = null;
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                }
                world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.itemRand.nextFloat() * 0.4F + 0.8F));
                // CraftBukkit end

                world.spawnEntity(entityfishinghook);
            }

            entityhuman.swingArm(enumhand);
            entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public int getItemEnchantability() {
        return 1;
    }
}
