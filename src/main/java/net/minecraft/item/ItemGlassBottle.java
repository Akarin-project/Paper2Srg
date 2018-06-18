package net.minecraft.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item {

    public ItemGlassBottle() {
        this.setCreativeTab(CreativeTabs.BREWING);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        List list = world.getEntitiesWithinAABB(EntityAreaEffectCloud.class, entityhuman.getEntityBoundingBox().grow(2.0D), new Predicate() {
            public boolean a(@Nullable EntityAreaEffectCloud entityareaeffectcloud) {
                return entityareaeffectcloud != null && entityareaeffectcloud.isEntityAlive() && entityareaeffectcloud.getOwner() instanceof EntityDragon;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityAreaEffectCloud) object);
            }
        });
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!list.isEmpty()) {
            EntityAreaEffectCloud entityareaeffectcloud = (EntityAreaEffectCloud) list.get(0);

            entityareaeffectcloud.setRadius(entityareaeffectcloud.getRadius() - 0.5F);
            world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, entityhuman, new ItemStack(Items.DRAGON_BREATH)));
        } else {
            RayTraceResult movingobjectposition = this.rayTrace(world, entityhuman, true);

            if (movingobjectposition == null) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else {
                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockposition = movingobjectposition.getBlockPos();

                    if (!world.isBlockModifiable(entityhuman, blockposition) || !entityhuman.canPlayerEdit(blockposition.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemstack)) {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    }

                    if (world.getBlockState(blockposition).getMaterial() == Material.WATER) {
                        world.playSound(entityhuman, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, entityhuman, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)));
                    }
                }

                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack itemstack, EntityPlayer entityhuman, ItemStack itemstack1) {
        itemstack.shrink(1);
        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        if (itemstack.isEmpty()) {
            return itemstack1;
        } else {
            if (!entityhuman.inventory.addItemStackToInventory(itemstack1)) {
                entityhuman.dropItem(itemstack1, false);
            }

            return itemstack;
        }
    }
}
