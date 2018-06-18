package net.minecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBoat extends Item {

    private final EntityBoat.Type type;

    public ItemBoat(EntityBoat.Type entityboat_enumboattype) {
        this.type = entityboat_enumboattype;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setUnlocalizedName("boat." + entityboat_enumboattype.getName());
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        float f = 1.0F;
        float f1 = entityhuman.prevRotationPitch + (entityhuman.rotationPitch - entityhuman.prevRotationPitch) * 1.0F;
        float f2 = entityhuman.prevRotationYaw + (entityhuman.rotationYaw - entityhuman.prevRotationYaw) * 1.0F;
        double d0 = entityhuman.prevPosX + (entityhuman.posX - entityhuman.prevPosX) * 1.0D;
        double d1 = entityhuman.prevPosY + (entityhuman.posY - entityhuman.prevPosY) * 1.0D + (double) entityhuman.getEyeHeight();
        double d2 = entityhuman.prevPosZ + (entityhuman.posZ - entityhuman.prevPosZ) * 1.0D;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.addVector((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
        RayTraceResult movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1, true);

        if (movingobjectposition == null) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            Vec3d vec3d2 = entityhuman.getLook(1.0F);
            boolean flag = false;
            List list = world.getEntitiesWithinAABBExcludingEntity(entityhuman, entityhuman.getEntityBoundingBox().expand(vec3d2.x * 5.0D, vec3d2.y * 5.0D, vec3d2.z * 5.0D).grow(1.0D));

            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.canBeCollidedWith()) {
                    AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double) entity.getCollisionBorderSize());

                    if (axisalignedbb.contains(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else if (movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else {
                // CraftBukkit start - Boat placement
                org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(entityhuman, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, movingobjectposition.getBlockPos(), movingobjectposition.sideHit, itemstack, enumhand);

                if (event.isCancelled()) {
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                }
                // CraftBukkit end
                Block block = world.getBlockState(movingobjectposition.getBlockPos()).getBlock();
                boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
                EntityBoat entityboat = new EntityBoat(world, movingobjectposition.hitVec.x, flag1 ? movingobjectposition.hitVec.y - 0.12D : movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);

                entityboat.setBoatType(this.type);
                entityboat.rotationYaw = entityhuman.rotationYaw;
                if (!world.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().grow(-0.1D)).isEmpty()) {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                } else {
                    if (!world.isRemote) {
                        if (!world.spawnEntity(entityboat)) return new ActionResult(EnumActionResult.PASS, itemstack); // CraftBukkit
                    }

                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    entityhuman.addStat(StatList.getObjectUseStats((Item) this));
                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }
        }
    }
}
