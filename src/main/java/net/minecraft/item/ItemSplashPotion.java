package net.minecraft.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;


public class ItemSplashPotion extends ItemPotion {

    public ItemSplashPotion() {}

    public String func_77653_i(ItemStack itemstack) {
        return I18n.func_74838_a(PotionUtils.func_185191_c(itemstack).func_185174_b("splash_potion.effect."));
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        ItemStack itemstack1 = entityhuman.field_71075_bZ.field_75098_d ? itemstack.func_77946_l() : itemstack.func_77979_a(1);

        world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187827_fP, SoundCategory.PLAYERS, 0.5F, 0.4F / (ItemSplashPotion.field_77697_d.nextFloat() * 0.4F + 0.8F));
        if (!world.field_72995_K) {
            EntityPotion entitypotion = new EntityPotion(world, entityhuman, itemstack1);

            entitypotion.func_184538_a(entityhuman, entityhuman.field_70125_A, entityhuman.field_70177_z, -20.0F, 0.5F, 1.0F);
            world.func_72838_d(entitypotion);
        }

        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
