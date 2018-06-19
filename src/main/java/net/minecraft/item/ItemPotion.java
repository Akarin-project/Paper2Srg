package net.minecraft.item;

import java.util.Iterator;
import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPotion extends Item {

    public ItemPotion() {
        this.func_77625_d(1);
        this.func_77637_a(CreativeTabs.field_78038_k);
    }

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        EntityPlayer entityhuman = entityliving instanceof EntityPlayer ? (EntityPlayer) entityliving : null;

        if (entityhuman == null || !entityhuman.field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
        }

        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193138_y.func_193148_a((EntityPlayerMP) entityhuman, itemstack);
        }

        if (!world.field_72995_K) {
            List list = PotionUtils.func_185189_a(itemstack);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                if (mobeffect.func_188419_a().func_76403_b()) {
                    mobeffect.func_188419_a().func_180793_a(entityhuman, entityhuman, entityliving, mobeffect.func_76458_c(), 1.0D);
                } else {
                    entityliving.func_70690_d(new PotionEffect(mobeffect));
                }
            }
        }

        if (entityhuman != null) {
            entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        }

        if (entityhuman == null || !entityhuman.field_71075_bZ.field_75098_d) {
            if (itemstack.func_190926_b()) {
                return new ItemStack(Items.field_151069_bo);
            }

            if (entityhuman != null) {
                entityhuman.field_71071_by.func_70441_a(new ItemStack(Items.field_151069_bo));
            }
        }

        return itemstack;
    }

    public int func_77626_a(ItemStack itemstack) {
        return 32;
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        entityhuman.func_184598_c(enumhand);
        return new ActionResult(EnumActionResult.SUCCESS, entityhuman.func_184586_b(enumhand));
    }

    public String func_77653_i(ItemStack itemstack) {
        return I18n.func_74838_a(PotionUtils.func_185191_c(itemstack).func_185174_b("potion.effect."));
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            Iterator iterator = PotionType.field_185176_a.iterator();

            while (iterator.hasNext()) {
                PotionType potionregistry = (PotionType) iterator.next();

                if (potionregistry != PotionTypes.field_185229_a) {
                    nonnulllist.add(PotionUtils.func_185188_a(new ItemStack(this), potionregistry));
                }
            }
        }

    }
}
