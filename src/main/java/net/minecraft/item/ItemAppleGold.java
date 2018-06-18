package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class ItemAppleGold extends ItemFood {

    public ItemAppleGold(int i, float f, boolean flag) {
        super(i, f, flag);
        this.setHasSubtypes(true);
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return itemstack.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        if (!world.isRemote) {
            if (itemstack.getMetadata() > 0) {
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
            } else {
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                entityhuman.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
            }
        }

    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            nonnulllist.add(new ItemStack(this));
            nonnulllist.add(new ItemStack(this, 1, 1));
        }

    }
}
