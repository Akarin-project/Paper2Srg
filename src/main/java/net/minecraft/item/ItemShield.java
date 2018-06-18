package net.minecraft.item;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;


public class ItemShield extends Item {

    public ItemShield() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxDamage(336);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.getSubCompound("BlockEntityTag") != null) {
            EnumDyeColor enumcolor = TileEntityBanner.getColor(itemstack);

            return I18n.translateToLocal("item.shield." + enumcolor.getUnlocalizedName() + ".name");
        } else {
            return I18n.translateToLocal("item.shield.name");
        }
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.BLOCK;
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        entityhuman.setActiveHand(enumhand);
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() == Item.getItemFromBlock(Blocks.PLANKS) ? true : super.getIsRepairable(itemstack, itemstack1);
    }
}
