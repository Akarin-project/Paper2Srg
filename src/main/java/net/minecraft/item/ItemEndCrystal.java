package net.minecraft.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal extends Item {

    public ItemEndCrystal() {
        this.setUnlocalizedName("end_crystal");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        if (iblockdata.getBlock() != Blocks.OBSIDIAN && iblockdata.getBlock() != Blocks.BEDROCK) {
            return EnumActionResult.FAIL;
        } else {
            BlockPos blockposition1 = blockposition.up();
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (!entityhuman.canPlayerEdit(blockposition1, enumdirection, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos blockposition2 = blockposition1.up();
                boolean flag = !world.isAirBlock(blockposition1) && !world.getBlockState(blockposition1).getBlock().isReplaceable((IBlockAccess) world, blockposition1);

                flag |= !world.isAirBlock(blockposition2) && !world.getBlockState(blockposition2).getBlock().isReplaceable((IBlockAccess) world, blockposition2);
                if (flag) {
                    return EnumActionResult.FAIL;
                } else {
                    double d0 = (double) blockposition1.getX();
                    double d1 = (double) blockposition1.getY();
                    double d2 = (double) blockposition1.getZ();
                    List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty()) {
                        return EnumActionResult.FAIL;
                    } else {
                        if (!world.isRemote) {
                            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world, (double) ((float) blockposition.getX() + 0.5F), (double) (blockposition.getY() + 1), (double) ((float) blockposition.getZ() + 0.5F));

                            entityendercrystal.setShowBottom(false);
                            world.spawnEntity(entityendercrystal);
                            if (world.provider instanceof WorldProviderEnd) {
                                DragonFightManager enderdragonbattle = ((WorldProviderEnd) world.provider).getDragonFightManager();

                                enderdragonbattle.respawnDragon();
                            }
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }
}
