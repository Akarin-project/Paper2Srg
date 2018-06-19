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
        this.func_77637_a(CreativeTabs.field_78029_e);
        this.func_77625_d(1);
        this.func_77656_e(25);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (world.field_72995_K) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            if (entityhuman.func_184218_aH() && entityhuman.func_184187_bx() instanceof EntityPig) {
                EntityPig entitypig = (EntityPig) entityhuman.func_184187_bx();

                if (itemstack.func_77958_k() - itemstack.func_77960_j() >= 7 && entitypig.func_184762_da()) {
                    itemstack.func_77972_a(7, entityhuman);
                    if (itemstack.func_190926_b()) {
                        ItemStack itemstack1 = new ItemStack(Items.field_151112_aM);

                        itemstack1.func_77982_d(itemstack.func_77978_p());
                        return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
                    }

                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }

            entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
            return new ActionResult(EnumActionResult.PASS, itemstack);
        }
    }
}
