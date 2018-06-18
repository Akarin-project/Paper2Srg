package net.minecraft.item;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemFirework extends Item {

    public ItemFirework() {}

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.isRemote) {
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);
            EntityFireworkRocket entityfireworks = new EntityFireworkRocket(world, (double) ((float) blockposition.getX() + f), (double) ((float) blockposition.getY() + f1), (double) ((float) blockposition.getZ() + f2), itemstack);

            entityfireworks.spawningEntity = entityhuman.getUniqueID(); // Paper
            world.spawnEntity(entityfireworks);
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        if (entityhuman.isElytraFlying()) {
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (!world.isRemote) {
                EntityFireworkRocket entityfireworks = new EntityFireworkRocket(world, itemstack, entityhuman);

                entityfireworks.spawningEntity = entityhuman.getUniqueID(); // Paper
                world.spawnEntity(entityfireworks);
                if (!entityhuman.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
            }

            return new ActionResult(EnumActionResult.SUCCESS, entityhuman.getHeldItem(enumhand));
        } else {
            return new ActionResult(EnumActionResult.PASS, entityhuman.getHeldItem(enumhand));
        }
    }
}
