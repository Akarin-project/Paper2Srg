package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpade extends ItemTool {

    private static final Set<Block> field_150916_c = Sets.newHashSet(new Block[] { Blocks.field_150435_aG, Blocks.field_150346_d, Blocks.field_150458_ak, Blocks.field_150349_c, Blocks.field_150351_n, Blocks.field_150391_bh, Blocks.field_150354_m, Blocks.field_150433_aE, Blocks.field_150431_aC, Blocks.field_150425_aM, Blocks.field_185774_da, Blocks.field_192444_dS});

    public ItemSpade(Item.ToolMaterial item_enumtoolmaterial) {
        super(1.5F, -3.0F, item_enumtoolmaterial, ItemSpade.field_150916_c);
    }

    public boolean func_150897_b(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return block == Blocks.field_150431_aC ? true : block == Blocks.field_150433_aE;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (enumdirection != EnumFacing.DOWN && world.func_180495_p(blockposition.func_177984_a()).func_185904_a() == Material.field_151579_a && block == Blocks.field_150349_c) {
                IBlockState iblockdata1 = Blocks.field_185774_da.func_176223_P();

                world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187771_eN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.field_72995_K) {
                    world.func_180501_a(blockposition, iblockdata1, 11);
                    itemstack.func_77972_a(1, entityhuman);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.PASS;
            }
        }
    }
}
