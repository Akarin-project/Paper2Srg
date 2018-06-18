package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
// CraftBukkit end

public class ItemHangingEntity extends Item {

    private final Class<? extends EntityHanging> hangingEntityClass;

    public ItemHangingEntity(Class<? extends EntityHanging> oclass) {
        this.hangingEntityClass = oclass;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        BlockPos blockposition1 = blockposition.offset(enumdirection);

        if (enumdirection != EnumFacing.DOWN && enumdirection != EnumFacing.UP && entityhuman.canPlayerEdit(blockposition1, enumdirection, itemstack)) {
            EntityHanging entityhanging = this.createEntity(world, blockposition1, enumdirection);

            if (entityhanging != null && entityhanging.onValidSurface()) {
                if (!world.isRemote) {
                    // CraftBukkit start - fire HangingPlaceEvent
                    Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                    org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                    org.bukkit.block.BlockFace blockFace = org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection);

                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return EnumActionResult.FAIL;
                    }
                    // CraftBukkit end
                    entityhanging.playPlaceSound();
                    world.spawnEntity(entityhanging);
                }

                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Nullable
    private EntityHanging createEntity(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return (EntityHanging) (this.hangingEntityClass == EntityPainting.class ? new EntityPainting(world, blockposition, enumdirection) : (this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(world, blockposition, enumdirection) : null));
    }
}
