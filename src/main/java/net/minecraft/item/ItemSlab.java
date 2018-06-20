package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock {

    private final BlockSlab field_150949_c;
    private final BlockSlab field_179226_c;

    public ItemSlab(Block block, BlockSlab blockstepabstract, BlockSlab blockstepabstract1) {
        super(block);
        this.field_150949_c = blockstepabstract;
        this.field_179226_c = blockstepabstract1;
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    @Override
    public int func_77647_b(int i) {
        return i;
    }

    @Override
    public String func_77667_c(ItemStack itemstack) {
        return this.field_150949_c.func_150002_b(itemstack.func_77960_j());
    }

    @Override
    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack)) {
            Comparable comparable = this.field_150949_c.func_185674_a(itemstack);
            IBlockState iblockdata = world.func_180495_p(blockposition);

            if (iblockdata.func_177230_c() == this.field_150949_c) {
                IProperty iblockstate = this.field_150949_c.func_176551_l();
                Comparable comparable1 = iblockdata.func_177229_b(iblockstate);
                BlockSlab.EnumBlockHalf blockstepabstract_enumslabhalf = iblockdata.func_177229_b(BlockSlab.field_176554_a);

                if ((enumdirection == EnumFacing.UP && blockstepabstract_enumslabhalf == BlockSlab.EnumBlockHalf.BOTTOM || enumdirection == EnumFacing.DOWN && blockstepabstract_enumslabhalf == BlockSlab.EnumBlockHalf.TOP) && comparable1 == comparable) {
                    IBlockState iblockdata1 = this.func_185055_a(iblockstate, comparable1);
                    AxisAlignedBB axisalignedbb = iblockdata1.func_185890_d(world, blockposition);

                    if (axisalignedbb != Block.field_185506_k && world.func_72855_b(axisalignedbb.func_186670_a(blockposition)) && world.func_180501_a(blockposition, iblockdata1, 11)) {
                        SoundType soundeffecttype = this.field_179226_c.func_185467_w();

                        world.func_184133_a(entityhuman, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                        itemstack.func_190918_g(1);
                        if (entityhuman instanceof EntityPlayerMP) {
                            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.func_180615_a(entityhuman, itemstack, world, blockposition.func_177972_a(enumdirection), comparable) ? EnumActionResult.SUCCESS : super.func_180614_a(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    private boolean func_180615_a(EntityPlayer entityhuman, ItemStack itemstack, World world, BlockPos blockposition, Object object) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() == this.field_150949_c) {
            Comparable comparable = iblockdata.func_177229_b(this.field_150949_c.func_176551_l());

            if (comparable == object) {
                IBlockState iblockdata1 = this.func_185055_a(this.field_150949_c.func_176551_l(), comparable);
                AxisAlignedBB axisalignedbb = iblockdata1.func_185890_d(world, blockposition);

                if (axisalignedbb != Block.field_185506_k && world.func_72855_b(axisalignedbb.func_186670_a(blockposition)) && world.func_180501_a(blockposition, iblockdata1, 11)) {
                    SoundType soundeffecttype = this.field_179226_c.func_185467_w();

                    world.func_184133_a(entityhuman, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                    itemstack.func_190918_g(1);
                }

                return true;
            }
        }

        return false;
    }

    protected <T extends Comparable<T>> IBlockState func_185055_a(IProperty<T> iblockstate, Comparable<?> comparable) {
        return this.field_179226_c.func_176223_P().func_177226_a(iblockstate, (T) comparable);
    }
}
