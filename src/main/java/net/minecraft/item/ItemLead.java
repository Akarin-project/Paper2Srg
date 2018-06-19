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
        this.func_77637_a(CreativeTabs.field_78040_i);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        Block block = world.func_180495_p(blockposition).func_177230_c();

        if (!(block instanceof BlockFence)) {
            return EnumActionResult.PASS;
        } else {
            if (!world.field_72995_K) {
                func_180618_a(entityhuman, world, blockposition);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public static boolean func_180618_a(EntityPlayer entityhuman, World world, BlockPos blockposition) {
        EntityLeashKnot entityleash = EntityLeashKnot.func_174863_b(world, blockposition);
        boolean flag = false;
        double d0 = 7.0D;
        int i = blockposition.func_177958_n();
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p();
        List list = world.func_72872_a(EntityLiving.class, new AxisAlignedBB((double) i - 7.0D, (double) j - 7.0D, (double) k - 7.0D, (double) i + 7.0D, (double) j + 7.0D, (double) k + 7.0D));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityLiving entityinsentient = (EntityLiving) iterator.next();

            if (entityinsentient.func_110167_bD() && entityinsentient.func_110166_bE() == entityhuman) {
                if (entityleash == null) {
                    entityleash = EntityLeashKnot.func_174862_a(world, blockposition);

                    // CraftBukkit start - fire HangingPlaceEvent
                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityleash.getBukkitEntity(), entityhuman != null ? (org.bukkit.entity.Player) entityhuman.getBukkitEntity() : null, world.getWorld().getBlockAt(i, j, k), org.bukkit.block.BlockFace.SELF);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        entityleash.func_70106_y();
                        return false;
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, entityleash, entityhuman).isCancelled()) {
                    continue;
                }
                // CraftBukkit end

                entityinsentient.func_110162_b(entityleash, true);
                flag = true;
            }
        }

        return flag;
    }
}
