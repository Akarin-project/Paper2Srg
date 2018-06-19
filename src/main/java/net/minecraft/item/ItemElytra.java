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
        this.field_77777_bU = 1;
        this.func_77656_e(432);
        this.func_77637_a(CreativeTabs.field_78029_e);
        this.func_185043_a(new ResourceLocation("broken"), new IItemPropertyGetter() {
        });
        BlockDispenser.field_149943_a.func_82595_a(this, ItemArmor.field_96605_cw);
    }

    public static boolean func_185069_d(ItemStack itemstack) {
        return itemstack.func_77952_i() < itemstack.func_77958_k() - 1;
    }

    public boolean func_82789_a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.func_77973_b() == Items.field_151116_aA;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        EntityEquipmentSlot enumitemslot = EntityLiving.func_184640_d(itemstack);
        ItemStack itemstack1 = entityhuman.func_184582_a(enumitemslot);

        if (itemstack1.func_190926_b()) {
            entityhuman.func_184201_a(enumitemslot, itemstack.func_77946_l());
            itemstack.func_190920_e(0);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
