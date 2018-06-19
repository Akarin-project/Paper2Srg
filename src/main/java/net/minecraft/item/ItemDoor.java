package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemDoor extends Item {

    private final Block field_179236_a;

    public ItemDoor(Block block) {
        this.field_179236_a = block;
        this.func_77637_a(CreativeTabs.field_78028_d);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (!block.func_176200_f((IBlockAccess) world, blockposition)) {
                blockposition = blockposition.func_177972_a(enumdirection);
            }

            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && this.field_179236_a.func_176196_c(world, blockposition)) {
                EnumFacing enumdirection1 = EnumFacing.func_176733_a((double) entityhuman.field_70177_z);
                int i = enumdirection1.func_82601_c();
                int j = enumdirection1.func_82599_e();
                boolean flag = i < 0 && f2 < 0.5F || i > 0 && f2 > 0.5F || j < 0 && f > 0.5F || j > 0 && f < 0.5F;

                func_179235_a(world, blockposition, enumdirection1, this.field_179236_a, flag);
                SoundType soundeffecttype = this.field_179236_a.func_185467_w();

                world.func_184133_a(entityhuman, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                itemstack.func_190918_g(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public static void func_179235_a(World world, BlockPos blockposition, EnumFacing enumdirection, Block block, boolean flag) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176746_e());
        BlockPos blockposition2 = blockposition.func_177972_a(enumdirection.func_176735_f());
        int i = (world.func_180495_p(blockposition2).func_185915_l() ? 1 : 0) + (world.func_180495_p(blockposition2.func_177984_a()).func_185915_l() ? 1 : 0);
        int j = (world.func_180495_p(blockposition1).func_185915_l() ? 1 : 0) + (world.func_180495_p(blockposition1.func_177984_a()).func_185915_l() ? 1 : 0);
        boolean flag1 = world.func_180495_p(blockposition2).func_177230_c() == block || world.func_180495_p(blockposition2.func_177984_a()).func_177230_c() == block;
        boolean flag2 = world.func_180495_p(blockposition1).func_177230_c() == block || world.func_180495_p(blockposition1.func_177984_a()).func_177230_c() == block;

        if ((!flag1 || flag2) && j <= i) {
            if (flag2 && !flag1 || j < i) {
                flag = false;
            }
        } else {
            flag = true;
        }

        BlockPos blockposition3 = blockposition.func_177984_a();
        boolean flag3 = world.func_175640_z(blockposition) || world.func_175640_z(blockposition3);
        IBlockState iblockdata = block.func_176223_P().func_177226_a(BlockDoor.field_176520_a, enumdirection).func_177226_a(BlockDoor.field_176521_M, flag ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).func_177226_a(BlockDoor.field_176522_N, Boolean.valueOf(flag3)).func_177226_a(BlockDoor.field_176519_b, Boolean.valueOf(flag3));

        // Spigot start - update physics after the block multi place event
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.LOWER), 3);
        world.func_180501_a(blockposition3, iblockdata.func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER), 3);
        // world.applyPhysics(blockposition, block, false);
        // world.applyPhysics(blockposition3, block, false);
        // Spigot end
    }
}
