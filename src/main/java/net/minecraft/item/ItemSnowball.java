package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemSnowball extends Item {

    public ItemSnowball() {
        this.field_77777_bU = 16;
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        // CraftBukkit start - moved down
        /*
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        world.a((EntityHuman) null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.hp, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemSnowball.j.nextFloat() * 0.4F + 0.8F));
        */
        if (!world.field_72995_K) {
            EntitySnowball entitysnowball = new EntitySnowball(world, entityhuman);

            entitysnowball.func_184538_a(entityhuman, entityhuman.field_70125_A, entityhuman.field_70177_z, 0.0F, 1.5F, 1.0F);
            if (world.func_72838_d(entitysnowball)) {
                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    itemstack.func_190918_g(1);
                }

                world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187797_fA, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemSnowball.field_77697_d.nextFloat() * 0.4F + 0.8F));
            } else if (entityhuman instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory();
            }
        }
        // CraftBukkit end

        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
