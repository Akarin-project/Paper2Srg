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
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = ItemMap.func_190906_a(world, entityhuman.field_70165_t, entityhuman.field_70161_v, (byte) 0, true, false);
        ItemStack itemstack1 = entityhuman.func_184586_b(enumhand);

        itemstack1.func_190918_g(1);
        if (itemstack1.func_190926_b()) {
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            if (!entityhuman.field_71071_by.func_70441_a(itemstack.func_77946_l())) {
                entityhuman.func_71019_a(itemstack, false);
            }

            entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
            return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
        }
    }
}
