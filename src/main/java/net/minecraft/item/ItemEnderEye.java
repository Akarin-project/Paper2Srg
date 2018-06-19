package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


public class ItemEnderEye extends Item {

    public ItemEnderEye() {
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack) && iblockdata.func_177230_c() == Blocks.field_150378_br && !((Boolean) iblockdata.func_177229_b(BlockEndPortalFrame.field_176507_b)).booleanValue()) {
            if (world.field_72995_K) {
                return EnumActionResult.SUCCESS;
            } else {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(true)), 2);
                world.func_175666_e(blockposition, Blocks.field_150378_br);
                itemstack.func_190918_g(1);

                for (int i = 0; i < 16; ++i) {
                    double d0 = (double) ((float) blockposition.func_177958_n() + (5.0F + ItemEnderEye.field_77697_d.nextFloat() * 6.0F) / 16.0F);
                    double d1 = (double) ((float) blockposition.func_177956_o() + 0.8125F);
                    double d2 = (double) ((float) blockposition.func_177952_p() + (5.0F + ItemEnderEye.field_77697_d.nextFloat() * 6.0F) / 16.0F);
                    double d3 = 0.0D;
                    double d4 = 0.0D;
                    double d5 = 0.0D;

                    world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_193781_bp, SoundCategory.BLOCKS, 1.0F, 1.0F);
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = BlockEndPortalFrame.func_185661_e().func_177681_a(world, blockposition);

                if (shapedetector_shapedetectorcollection != null) {
                    BlockPos blockposition1 = shapedetector_shapedetectorcollection.func_181117_a().func_177982_a(-3, 0, -3);

                    for (int j = 0; j < 3; ++j) {
                        for (int k = 0; k < 3; ++k) {
                            world.func_180501_a(blockposition1.func_177982_a(j, 0, k), Blocks.field_150384_bq.func_176223_P(), 2);
                        }
                    }

                    world.func_175669_a(1038, blockposition1.func_177982_a(1, 0, 1), 0);
                }

                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        RayTraceResult movingobjectposition = this.func_77621_a(world, entityhuman, false);

        if (movingobjectposition != null && movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK && world.func_180495_p(movingobjectposition.func_178782_a()).func_177230_c() == Blocks.field_150378_br) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            entityhuman.func_184598_c(enumhand);
            if (!world.field_72995_K) {
                BlockPos blockposition = ((WorldServer) world).func_72863_F().func_180513_a(world, "Stronghold", new BlockPos(entityhuman), false);

                if (blockposition != null) {
                    EntityEnderEye entityendersignal = new EntityEnderEye(world, entityhuman.field_70165_t, entityhuman.field_70163_u + (double) (entityhuman.field_70131_O / 2.0F), entityhuman.field_70161_v);

                    entityendersignal.func_180465_a(blockposition);
                    world.func_72838_d(entityendersignal);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192132_l.func_192239_a((EntityPlayerMP) entityhuman, blockposition);
                    }

                    world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187528_aR, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemEnderEye.field_77697_d.nextFloat() * 0.4F + 0.8F));
                    world.func_180498_a((EntityPlayer) null, 1003, new BlockPos(entityhuman), 0);
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }

                    entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
    }
}
