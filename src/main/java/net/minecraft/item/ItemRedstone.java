package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemRedstone extends Item {

    public ItemRedstone() {
        this.func_77637_a(CreativeTabs.field_78028_d);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        boolean flag = world.func_180495_p(blockposition).func_177230_c().func_176200_f((IBlockAccess) world, blockposition);
        BlockPos blockposition1 = flag ? blockposition : blockposition.func_177972_a(enumdirection);
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && entityhuman.func_175151_a(blockposition1, enumdirection, itemstack) && world.func_190527_a(world.func_180495_p(blockposition1).func_177230_c(), blockposition1, false, enumdirection, (Entity) null) && Blocks.field_150488_af.func_176196_c(world, blockposition1)) { // CraftBukkit
            world.func_175656_a(blockposition1, Blocks.field_150488_af.func_176223_P());
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition1, itemstack);
            }

            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
