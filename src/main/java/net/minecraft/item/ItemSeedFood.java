package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemSeedFood extends ItemFood {

    private final Block field_150908_b;
    private final Block field_82809_c;

    public ItemSeedFood(int i, float f, Block block, Block block1) {
        super(i, f, false);
        this.field_150908_b = block;
        this.field_82809_c = block1;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (enumdirection == EnumFacing.UP && entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack) && world.func_180495_p(blockposition).func_177230_c() == this.field_82809_c && world.func_175623_d(blockposition.func_177984_a())) {
            world.func_180501_a(blockposition.func_177984_a(), this.field_150908_b.func_176223_P(), 11);
            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
