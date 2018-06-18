package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


public class ItemEnderEye extends Item {

    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack) && iblockdata.getBlock() == Blocks.END_PORTAL_FRAME && !((Boolean) iblockdata.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
            if (world.isRemote) {
                return EnumActionResult.SUCCESS;
            } else {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(true)), 2);
                world.updateComparatorOutputLevel(blockposition, Blocks.END_PORTAL_FRAME);
                itemstack.shrink(1);

                for (int i = 0; i < 16; ++i) {
                    double d0 = (double) ((float) blockposition.getX() + (5.0F + ItemEnderEye.itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d1 = (double) ((float) blockposition.getY() + 0.8125F);
                    double d2 = (double) ((float) blockposition.getZ() + (5.0F + ItemEnderEye.itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d3 = 0.0D;
                    double d4 = 0.0D;
                    double d5 = 0.0D;

                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = BlockEndPortalFrame.getOrCreatePortalShape().match(world, blockposition);

                if (shapedetector_shapedetectorcollection != null) {
                    BlockPos blockposition1 = shapedetector_shapedetectorcollection.getFrontTopLeft().add(-3, 0, -3);

                    for (int j = 0; j < 3; ++j) {
                        for (int k = 0; k < 3; ++k) {
                            world.setBlockState(blockposition1.add(j, 0, k), Blocks.END_PORTAL.getDefaultState(), 2);
                        }
                    }

                    world.playBroadcastSound(1038, blockposition1.add(1, 0, 1), 0);
                }

                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        RayTraceResult movingobjectposition = this.rayTrace(world, entityhuman, false);

        if (movingobjectposition != null && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            entityhuman.setActiveHand(enumhand);
            if (!world.isRemote) {
                BlockPos blockposition = ((WorldServer) world).getChunkProvider().getNearestStructurePos(world, "Stronghold", new BlockPos(entityhuman), false);

                if (blockposition != null) {
                    EntityEnderEye entityendersignal = new EntityEnderEye(world, entityhuman.posX, entityhuman.posY + (double) (entityhuman.height / 2.0F), entityhuman.posZ);

                    entityendersignal.moveTowards(blockposition);
                    world.spawnEntity(entityendersignal);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger((EntityPlayerMP) entityhuman, blockposition);
                    }

                    world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemEnderEye.itemRand.nextFloat() * 0.4F + 0.8F));
                    world.playEvent((EntityPlayer) null, 1003, new BlockPos(entityhuman), 0);
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    entityhuman.addStat(StatList.getObjectUseStats((Item) this));
                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
    }
}
