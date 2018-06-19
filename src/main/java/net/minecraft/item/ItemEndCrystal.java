package net.minecraft.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal extends Item {

    public ItemEndCrystal() {
        this.func_77655_b("end_crystal");
        this.func_77637_a(CreativeTabs.field_78031_c);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() != Blocks.field_150343_Z && iblockdata.func_177230_c() != Blocks.field_150357_h) {
            return EnumActionResult.FAIL;
        } else {
            BlockPos blockposition1 = blockposition.func_177984_a();
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (!entityhuman.func_175151_a(blockposition1, enumdirection, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos blockposition2 = blockposition1.func_177984_a();
                boolean flag = !world.func_175623_d(blockposition1) && !world.func_180495_p(blockposition1).func_177230_c().func_176200_f((IBlockAccess) world, blockposition1);

                flag |= !world.func_175623_d(blockposition2) && !world.func_180495_p(blockposition2).func_177230_c().func_176200_f((IBlockAccess) world, blockposition2);
                if (flag) {
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
                            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) (blockposition.func_177956_o() + 1), (double) ((float) blockposition.func_177952_p() + 0.5F));

                            entityendercrystal.func_184517_a(false);
                            world.func_72838_d(entityendercrystal);
                            if (world.field_73011_w instanceof WorldProviderEnd) {
                                DragonFightManager enderdragonbattle = ((WorldProviderEnd) world.field_73011_w).func_186063_s();

                                enderdragonbattle.func_186106_e();
                            }
                        }

                        itemstack.func_190918_g(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }
}
