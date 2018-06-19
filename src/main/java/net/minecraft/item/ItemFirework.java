package net.minecraft.item;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemFirework extends Item {

    public ItemFirework() {}

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.field_72995_K) {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);
            EntityFireworkRocket entityfireworks = new EntityFireworkRocket(world, (double) ((float) blockposition.func_177958_n() + f), (double) ((float) blockposition.func_177956_o() + f1), (double) ((float) blockposition.func_177952_p() + f2), itemstack);

            entityfireworks.spawningEntity = entityhuman.func_110124_au(); // Paper
            world.func_72838_d(entityfireworks);
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        if (entityhuman.func_184613_cA()) {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (!world.field_72995_K) {
                EntityFireworkRocket entityfireworks = new EntityFireworkRocket(world, itemstack, entityhuman);

                entityfireworks.spawningEntity = entityhuman.func_110124_au(); // Paper
                world.func_72838_d(entityfireworks);
                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    itemstack.func_190918_g(1);
                }
            }

            return new ActionResult(EnumActionResult.SUCCESS, entityhuman.func_184586_b(enumhand));
        } else {
            return new ActionResult(EnumActionResult.PASS, entityhuman.func_184586_b(enumhand));
        }
    }
}
