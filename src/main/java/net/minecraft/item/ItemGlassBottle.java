package net.minecraft.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item {

    public ItemGlassBottle() {
        this.func_77637_a(CreativeTabs.field_78038_k);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        List list = world.func_175647_a(EntityAreaEffectCloud.class, entityhuman.func_174813_aQ().func_186662_g(2.0D), new Predicate() {
            public boolean a(@Nullable EntityAreaEffectCloud entityareaeffectcloud) {
                return entityareaeffectcloud != null && entityareaeffectcloud.func_70089_S() && entityareaeffectcloud.func_184494_w() instanceof EntityDragon;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityAreaEffectCloud) object);
            }
        });
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!list.isEmpty()) {
            EntityAreaEffectCloud entityareaeffectcloud = (EntityAreaEffectCloud) list.get(0);

            entityareaeffectcloud.func_184483_a(entityareaeffectcloud.func_184490_j() - 0.5F);
            world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187618_I, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return new ActionResult(EnumActionResult.SUCCESS, this.func_185061_a(itemstack, entityhuman, new ItemStack(Items.field_185157_bK)));
        } else {
            RayTraceResult movingobjectposition = this.func_77621_a(world, entityhuman, true);

            if (movingobjectposition == null) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else {
                if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK) {
                    BlockPos blockposition = movingobjectposition.func_178782_a();

                    if (!world.func_175660_a(entityhuman, blockposition) || !entityhuman.func_175151_a(blockposition.func_177972_a(movingobjectposition.field_178784_b), movingobjectposition.field_178784_b, itemstack)) {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    }

                    if (world.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h) {
                        world.func_184148_a(entityhuman, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187615_H, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, this.func_185061_a(itemstack, entityhuman, PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionTypes.field_185230_b)));
                    }
                }

                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
        }
    }

    protected ItemStack func_185061_a(ItemStack itemstack, EntityPlayer entityhuman, ItemStack itemstack1) {
        itemstack.func_190918_g(1);
        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        if (itemstack.func_190926_b()) {
            return itemstack1;
        } else {
            if (!entityhuman.field_71071_by.func_70441_a(itemstack1)) {
                entityhuman.func_71019_a(itemstack1, false);
            }

            return itemstack;
        }
    }
}
