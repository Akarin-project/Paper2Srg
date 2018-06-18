package net.minecraft.item;

import java.util.Iterator;
import java.util.List;


import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class ItemLead extends Item {

    public ItemLead() {
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        Block block = world.getBlockState(blockposition).getBlock();

        if (!(block instanceof BlockFence)) {
            return EnumActionResult.PASS;
        } else {
            if (!world.isRemote) {
                attachToFence(entityhuman, world, blockposition);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public static boolean attachToFence(EntityPlayer entityhuman, World world, BlockPos blockposition) {
        EntityLeashKnot entityleash = EntityLeashKnot.getKnotForPosition(world, blockposition);
        boolean flag = false;
        double d0 = 7.0D;
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        List list = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - 7.0D, (double) j - 7.0D, (double) k - 7.0D, (double) i + 7.0D, (double) j + 7.0D, (double) k + 7.0D));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityLiving entityinsentient = (EntityLiving) iterator.next();

            if (entityinsentient.getLeashed() && entityinsentient.getLeashHolder() == entityhuman) {
                if (entityleash == null) {
                    entityleash = EntityLeashKnot.createKnot(world, blockposition);

                    // CraftBukkit start - fire HangingPlaceEvent
                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityleash.getBukkitEntity(), entityhuman != null ? (org.bukkit.entity.Player) entityhuman.getBukkitEntity() : null, world.getWorld().getBlockAt(i, j, k), org.bukkit.block.BlockFace.SELF);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        entityleash.setDead();
                        return false;
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, entityleash, entityhuman).isCancelled()) {
                    continue;
                }
                // CraftBukkit end

                entityinsentient.setLeashHolder(entityleash, true);
                flag = true;
            }
        }

        return flag;
    }
}
