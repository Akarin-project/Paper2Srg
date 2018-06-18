package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemSeedFood extends ItemFood {

    private final Block crops;
    private final Block soilId;

    public ItemSeedFood(int i, float f, Block block, Block block1) {
        super(i, f, false);
        this.crops = block;
        this.soilId = block1;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (enumdirection == EnumFacing.UP && entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack) && world.getBlockState(blockposition).getBlock() == this.soilId && world.isAirBlock(blockposition.up())) {
            world.setBlockState(blockposition.up(), this.crops.getDefaultState(), 11);
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
