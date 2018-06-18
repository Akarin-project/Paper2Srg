package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemEmptyMap extends ItemMapBase {

    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = ItemMap.setupNewMap(world, entityhuman.posX, entityhuman.posZ, (byte) 0, true, false);
        ItemStack itemstack1 = entityhuman.getHeldItem(enumhand);

        itemstack1.shrink(1);
        if (itemstack1.isEmpty()) {
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            if (!entityhuman.inventory.addItemStackToInventory(itemstack.copy())) {
                entityhuman.dropItem(itemstack, false);
            }

            entityhuman.addStat(StatList.getObjectUseStats((Item) this));
            return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
        }
    }
}
