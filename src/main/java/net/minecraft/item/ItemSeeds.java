package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemSeeds extends Item {

    private final Block field_150925_a;
    private final Block field_77838_b;

    public ItemSeeds(Block block, Block block1) {
        this.field_150925_a = block;
        this.field_77838_b = block1;
        this.func_77637_a(CreativeTabs.field_78035_l);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (enumdirection == EnumFacing.UP && entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack) && world.func_180495_p(blockposition).func_177230_c() == this.field_77838_b && world.func_175623_d(blockposition.func_177984_a())) {
            world.func_175656_a(blockposition.func_177984_a(), this.field_150925_a.func_176223_P());
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition.func_177984_a(), itemstack);
            }

            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
