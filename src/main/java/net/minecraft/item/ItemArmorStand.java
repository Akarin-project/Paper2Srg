package net.minecraft.item;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemArmorStand extends Item {

    public ItemArmorStand() {
        this.func_77637_a(CreativeTabs.field_78031_c);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            boolean flag = world.func_180495_p(blockposition).func_177230_c().func_176200_f((IBlockAccess) world, blockposition);
            BlockPos blockposition1 = flag ? blockposition : blockposition.func_177972_a(enumdirection);
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (!entityhuman.func_175151_a(blockposition1, enumdirection, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos blockposition2 = blockposition1.func_177984_a();
                boolean flag1 = !world.func_175623_d(blockposition1) && !world.func_180495_p(blockposition1).func_177230_c().func_176200_f((IBlockAccess) world, blockposition1);

                flag1 |= !world.func_175623_d(blockposition2) && !world.func_180495_p(blockposition2).func_177230_c().func_176200_f((IBlockAccess) world, blockposition2);
                if (flag1) {
                    return EnumActionResult.FAIL;
                } else {
                    double d0 = (double) blockposition1.func_177958_n();
                    double d1 = (double) blockposition1.func_177956_o();
                    double d2 = (double) blockposition1.func_177952_p();
                    List list = world.func_72839_b((Entity) null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty()) {
                        return EnumActionResult.FAIL;
                    } else {
                        if (!world.field_72995_K) {
                            world.func_175698_g(blockposition1);
                            world.func_175698_g(blockposition2);
                            EntityArmorStand entityarmorstand = new EntityArmorStand(world, d0 + 0.5D, d1, d2 + 0.5D);
                            float f3 = (float) MathHelper.func_76141_d((MathHelper.func_76142_g(entityhuman.field_70177_z - 180.0F) + 22.5F) / 45.0F) * 45.0F;

                            entityarmorstand.func_70012_b(d0 + 0.5D, d1, d2 + 0.5D, f3, 0.0F);
                            this.func_179221_a(entityarmorstand, world.field_73012_v);
                            ItemMonsterPlacer.func_185079_a(world, entityhuman, itemstack, (Entity) entityarmorstand);
                            world.func_72838_d(entityarmorstand);
                            world.func_184148_a((EntityPlayer) null, entityarmorstand.field_70165_t, entityarmorstand.field_70163_u, entityarmorstand.field_70161_v, SoundEvents.field_187710_m, SoundCategory.BLOCKS, 0.75F, 0.8F);
                        }

                        itemstack.func_190918_g(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    private void func_179221_a(EntityArmorStand entityarmorstand, Random random) {
        Rotations vector3f = entityarmorstand.func_175418_s();
        float f = random.nextFloat() * 5.0F;
        float f1 = random.nextFloat() * 20.0F - 10.0F;
        Rotations vector3f1 = new Rotations(vector3f.func_179415_b() + f, vector3f.func_179416_c() + f1, vector3f.func_179413_d());

        entityarmorstand.func_175415_a(vector3f1);
        vector3f = entityarmorstand.func_175408_t();
        f = random.nextFloat() * 10.0F - 5.0F;
        vector3f1 = new Rotations(vector3f.func_179415_b(), vector3f.func_179416_c() + f, vector3f.func_179413_d());
        entityarmorstand.func_175424_b(vector3f1);
    }
}
