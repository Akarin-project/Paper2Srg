package net.minecraft.item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.world.World;


public class ItemSoup extends ItemFood {

    public ItemSoup(int i) {
        super(i, false);
        this.func_77625_d(1);
    }

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        super.func_77654_b(itemstack, world, entityliving);
        return new ItemStack(Items.field_151054_z);
    }
}
