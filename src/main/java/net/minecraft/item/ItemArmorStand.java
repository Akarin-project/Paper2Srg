package net.minecraft.item;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemArmorStand extends Item {

    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            boolean flag = world.getBlockState(blockposition).getBlock().isReplaceable((IBlockAccess) world, blockposition);
            BlockPos blockposition1 = flag ? blockposition : blockposition.offset(enumdirection);
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (!entityhuman.canPlayerEdit(blockposition1, enumdirection, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos blockposition2 = blockposition1.up();
                boolean flag1 = !world.isAirBlock(blockposition1) && !world.getBlockState(blockposition1).getBlock().isReplaceable((IBlockAccess) world, blockposition1);

                flag1 |= !world.isAirBlock(blockposition2) && !world.getBlockState(blockposition2).getBlock().isReplaceable((IBlockAccess) world, blockposition2);
                if (flag1) {
                    return EnumActionResult.FAIL;
                } else {
                    double d0 = (double) blockposition1.getX();
                    double d1 = (double) blockposition1.getY();
                    double d2 = (double) blockposition1.getZ();
                    List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty()) {
                        return EnumActionResult.FAIL;
                    } else {
                        if (!world.isRemote) {
                            world.setBlockToAir(blockposition1);
                            world.setBlockToAir(blockposition2);
                            EntityArmorStand entityarmorstand = new EntityArmorStand(world, d0 + 0.5D, d1, d2 + 0.5D);
                            float f3 = (float) MathHelper.floor((MathHelper.wrapDegrees(entityhuman.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;

                            entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f3, 0.0F);
                            this.applyRandomRotations(entityarmorstand, world.rand);
                            ItemMonsterPlacer.applyItemEntityDataToEntity(world, entityhuman, itemstack, (Entity) entityarmorstand);
                            world.spawnEntity(entityarmorstand);
                            world.playSound((EntityPlayer) null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    private void applyRandomRotations(EntityArmorStand entityarmorstand, Random random) {
        Rotations vector3f = entityarmorstand.getHeadRotation();
        float f = random.nextFloat() * 5.0F;
        float f1 = random.nextFloat() * 20.0F - 10.0F;
        Rotations vector3f1 = new Rotations(vector3f.getX() + f, vector3f.getY() + f1, vector3f.getZ());

        entityarmorstand.setHeadRotation(vector3f1);
        vector3f = entityarmorstand.getBodyRotation();
        f = random.nextFloat() * 10.0F - 5.0F;
        vector3f1 = new Rotations(vector3f.getX(), vector3f.getY() + f, vector3f.getZ());
        entityarmorstand.setBodyRotation(vector3f1);
    }
}
