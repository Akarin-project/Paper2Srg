package net.minecraft.item;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class ItemElytra extends Item {

    public ItemElytra() {
        this.maxStackSize = 1;
        this.setMaxDamage(432);
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter() {
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack itemstack) {
        return itemstack.getItemDamage() < itemstack.getMaxDamage() - 1;
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() == Items.LEATHER;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        EntityEquipmentSlot enumitemslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = entityhuman.getItemStackFromSlot(enumitemslot);

        if (itemstack1.isEmpty()) {
            entityhuman.setItemStackToSlot(enumitemslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
