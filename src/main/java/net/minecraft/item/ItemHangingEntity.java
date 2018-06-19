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

    private final Class<? extends EntityHanging> field_82811_a;

    public ItemHangingEntity(Class<? extends EntityHanging> oclass) {
        this.field_82811_a = oclass;
        this.func_77637_a(CreativeTabs.field_78031_c);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);

        if (enumdirection != EnumFacing.DOWN && enumdirection != EnumFacing.UP && entityhuman.func_175151_a(blockposition1, enumdirection, itemstack)) {
            EntityHanging entityhanging = this.func_179233_a(world, blockposition1, enumdirection);

            if (entityhanging != null && entityhanging.func_70518_d()) {
                if (!world.field_72995_K) {
                    // CraftBukkit start - fire HangingPlaceEvent
                    Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                    org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                    org.bukkit.block.BlockFace blockFace = org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection);

                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return EnumActionResult.FAIL;
                    }
                    // CraftBukkit end
                    entityhanging.func_184523_o();
                    world.func_72838_d(entityhanging);
                }

                itemstack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Nullable
    private EntityHanging func_179233_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return (EntityHanging) (this.field_82811_a == EntityPainting.class ? new EntityPainting(world, blockposition, enumdirection) : (this.field_82811_a == EntityItemFrame.class ? new EntityItemFrame(world, blockposition, enumdirection) : null));
    }
}
