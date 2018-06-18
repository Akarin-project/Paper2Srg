package net.minecraft.item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.world.World;


public class ItemSoup extends ItemFood {

    public ItemSoup(int i) {
        super(i, false);
        this.setMaxStackSize(1);
    }

    public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        super.onItemUseFinish(itemstack, world, entityliving);
        return new ItemStack(Items.BOWL);
    }
}
