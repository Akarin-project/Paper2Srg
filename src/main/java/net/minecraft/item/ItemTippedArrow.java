package net.minecraft.item;

import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow {

    public ItemTippedArrow() {}

    public EntityArrow func_185052_a(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, entityliving);

        entitytippedarrow.func_184555_a(itemstack);
        return entitytippedarrow;
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            Iterator iterator = PotionType.field_185176_a.iterator();

            while (iterator.hasNext()) {
                PotionType potionregistry = (PotionType) iterator.next();

                if (!potionregistry.func_185170_a().isEmpty()) {
                    nonnulllist.add(PotionUtils.func_185188_a(new ItemStack(this), potionregistry));
                }
            }
        }

    }

    public String func_77653_i(ItemStack itemstack) {
        return I18n.func_74838_a(PotionUtils.func_185191_c(itemstack).func_185174_b("tipped_arrow.effect."));
    }
}
