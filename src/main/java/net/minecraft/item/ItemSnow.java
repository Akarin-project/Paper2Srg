package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemSnow extends ItemBlock {

    public ItemSnow(Block block) {
        super(block);
        this.func_77656_e(0);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && entityhuman.func_175151_a(blockposition, enumdirection, itemstack)) {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();
            BlockPos blockposition1 = blockposition;

            if ((enumdirection != EnumFacing.UP || block != this.field_150939_a) && !block.func_176200_f((IBlockAccess) world, blockposition)) {
                blockposition1 = blockposition.func_177972_a(enumdirection);
                iblockdata = world.func_180495_p(blockposition1);
                block = iblockdata.func_177230_c();
            }

            if (block == this.field_150939_a) {
                int i = ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue();

                if (i < 8) {
                    IBlockState iblockdata1 = iblockdata.func_177226_a(BlockSnow.field_176315_a, Integer.valueOf(i + 1));
                    AxisAlignedBB axisalignedbb = iblockdata1.func_185890_d(world, blockposition1);

                    if (axisalignedbb != Block.field_185506_k && world.func_72855_b(axisalignedbb.func_186670_a(blockposition1)) && world.func_180501_a(blockposition1, iblockdata1, 10)) {
                        SoundType soundeffecttype = this.field_150939_a.func_185467_w();

                        world.func_184133_a(entityhuman, blockposition1, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                        if (entityhuman instanceof EntityPlayerMP) {
                            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                        }

                        itemstack.func_190918_g(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return super.func_180614_a(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public int func_77647_b(int i) {
        return i;
    }
}
