package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemBlockSpecial extends Item {

    public final Block field_150935_a; // PAIL: private->public

    public ItemBlockSpecial(Block block) {
        this.field_150935_a = block;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        if (block == Blocks.field_150431_aC && ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() < 1) {
            enumdirection = EnumFacing.UP;
        } else if (!block.func_176200_f((IBlockAccess) world, blockposition)) {
            blockposition = blockposition.func_177972_a(enumdirection);
        }

        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && world.func_190527_a(this.field_150935_a, blockposition, false, enumdirection, (Entity) null)) {
            IBlockState iblockdata1 = this.field_150935_a.func_180642_a(world, blockposition, enumdirection, f, f1, f2, 0, entityhuman);

            if (!world.func_180501_a(blockposition, iblockdata1, 11)) {
                return EnumActionResult.FAIL;
            } else {
                iblockdata1 = world.func_180495_p(blockposition);
                if (iblockdata1.func_177230_c() == this.field_150935_a) {
                    ItemBlock.func_179224_a(world, entityhuman, blockposition, itemstack);
                    iblockdata1.func_177230_c().func_180633_a(world, blockposition, iblockdata1, entityhuman, itemstack);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }
                }

                SoundType soundeffecttype = this.field_150935_a.func_185467_w();

                world.func_184133_a(entityhuman, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                itemstack.func_190918_g(1);
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
