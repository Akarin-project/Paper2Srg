package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class ItemLilyPad extends ItemColored {

    public ItemLilyPad(Block block) {
        super(block, false);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        RayTraceResult movingobjectposition = this.func_77621_a(world, entityhuman, true);

        if (movingobjectposition == null) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK) {
                BlockPos blockposition = movingobjectposition.func_178782_a();

                if (!world.func_175660_a(entityhuman, blockposition) || !entityhuman.func_175151_a(blockposition.func_177972_a(movingobjectposition.field_178784_b), movingobjectposition.field_178784_b, itemstack)) {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                }

                BlockPos blockposition1 = blockposition.func_177984_a();
                IBlockState iblockdata = world.func_180495_p(blockposition);

                if (iblockdata.func_185904_a() == Material.field_151586_h && ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0 && world.func_175623_d(blockposition1)) {
                    // CraftBukkit start - special case for handling block placement with water lilies
                    org.bukkit.block.BlockState blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(world, blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
                    world.func_180501_a(blockposition1, Blocks.field_150392_bi.func_176223_P(), 11);
                    org.bukkit.event.block.BlockPlaceEvent placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, enumhand, blockstate, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                    if (placeEvent != null && (placeEvent.isCancelled() || !placeEvent.canBuild())) {
                        blockstate.update(true, false);
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    }
                    // CraftBukkit end
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition1, itemstack);
                    }

                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }

                    entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                    world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187916_gp, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }

            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
