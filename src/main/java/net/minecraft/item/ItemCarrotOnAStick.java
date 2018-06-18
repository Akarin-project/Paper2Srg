package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemCarrotOnAStick extends Item {

    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            if (entityhuman.isRiding() && entityhuman.getRidingEntity() instanceof EntityPig) {
                EntityPig entitypig = (EntityPig) entityhuman.getRidingEntity();

                if (itemstack.getMaxDamage() - itemstack.getMetadata() >= 7 && entitypig.boost()) {
                    itemstack.damageItem(7, entityhuman);
                    if (itemstack.isEmpty()) {
                        ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);

                        itemstack1.setTagCompound(itemstack.getTagCompound());
                        return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
                    }

                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }

            entityhuman.addStat(StatList.getObjectUseStats((Item) this));
            return new ActionResult(EnumActionResult.PASS, itemstack);
        }
    }
}
