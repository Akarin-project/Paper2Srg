package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemBed extends Item {

    public ItemBed() {
        this.func_77637_a(CreativeTabs.field_78031_c);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return EnumActionResult.SUCCESS;
        } else if (enumdirection != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();
            boolean flag = block.func_176200_f((IBlockAccess) world, blockposition);

            if (!flag) {
                blockposition = blockposition.func_177984_a();
            }

            int i = MathHelper.func_76128_c((double) (entityhuman.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumdirection1 = EnumFacing.func_176731_b(i);
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection1);
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && entityhuman.func_175151_a(blockposition1, enumdirection, itemstack)) {
                IBlockState iblockdata1 = world.func_180495_p(blockposition1);
                boolean flag1 = iblockdata1.func_177230_c().func_176200_f((IBlockAccess) world, blockposition1);
                boolean flag2 = flag || world.func_175623_d(blockposition);
                boolean flag3 = flag1 || world.func_175623_d(blockposition1);

                if (flag2 && flag3 && world.func_180495_p(blockposition.func_177977_b()).func_185896_q() && world.func_180495_p(blockposition1.func_177977_b()).func_185896_q()) {
                    IBlockState iblockdata2 = Blocks.field_150324_C.func_176223_P().func_177226_a(BlockBed.field_176471_b, Boolean.valueOf(false)).func_177226_a(BlockBed.field_185512_D, enumdirection1).func_177226_a(BlockBed.field_176472_a, BlockBed.EnumPartType.FOOT);

                    world.func_180501_a(blockposition, iblockdata2, 10);
                    world.func_180501_a(blockposition1, iblockdata2.func_177226_a(BlockBed.field_176472_a, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundeffecttype = iblockdata2.func_177230_c().func_185467_w();

                    world.func_184133_a((EntityPlayer) null, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                    TileEntity tileentity = world.func_175625_s(blockposition1);

                    if (tileentity instanceof TileEntityBed) {
                        ((TileEntityBed) tileentity).func_193051_a(itemstack);
                    }

                    TileEntity tileentity1 = world.func_175625_s(blockposition);

                    if (tileentity1 instanceof TileEntityBed) {
                        ((TileEntityBed) tileentity1).func_193051_a(itemstack);
                    }

                    world.func_175722_b(blockposition, block, false);
                    world.func_175722_b(blockposition1, iblockdata1.func_177230_c(), false);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.func_190918_g(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public String func_77667_c(ItemStack itemstack) {
        return super.func_77658_a() + "." + EnumDyeColor.func_176764_b(itemstack.func_77960_j()).func_176762_d();
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            for (int i = 0; i < 16; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }
}
