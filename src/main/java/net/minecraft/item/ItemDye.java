package net.minecraft.item;


import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.entity.SheepDyeWoolEvent;

public class ItemDye extends Item {

    public static final int[] DYE_COLORS = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        int i = itemstack.getMetadata();

        return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            EnumDyeColor enumcolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

            if (enumcolor == EnumDyeColor.WHITE) {
                if (applyBonemeal(itemstack, world, blockposition)) {
                    if (!world.isRemote) {
                        world.playEvent(2005, blockposition, 0);
                    }

                    return EnumActionResult.SUCCESS;
                }
            } else if (enumcolor == EnumDyeColor.BROWN) {
                IBlockState iblockdata = world.getBlockState(blockposition);
                Block block = iblockdata.getBlock();

                if (block == Blocks.LOG && iblockdata.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                    if (enumdirection == EnumFacing.DOWN || enumdirection == EnumFacing.UP) {
                        return EnumActionResult.FAIL;
                    }

                    blockposition = blockposition.offset(enumdirection);
                    if (world.isAirBlock(blockposition)) {
                        IBlockState iblockdata1 = Blocks.COCOA.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, 0, entityhuman);

                        world.setBlockState(blockposition, iblockdata1, 10);
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.FAIL;
            }

            return EnumActionResult.PASS;
        }
    }

    public static boolean applyBonemeal(ItemStack itemstack, World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        if (iblockdata.getBlock() instanceof IGrowable) {
            IGrowable iblockfragileplantelement = (IGrowable) iblockdata.getBlock();

            if (iblockfragileplantelement.canGrow(world, blockposition, iblockdata, world.isRemote)) {
                if (!world.isRemote) {
                    if (iblockfragileplantelement.canUseBonemeal(world, world.rand, blockposition, iblockdata)) {
                        iblockfragileplantelement.grow(world, world.rand, blockposition, iblockdata);
                    }

                    itemstack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            EnumDyeColor enumcolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumcolor) {
                // CraftBukkit start
                byte bColor = (byte) enumcolor.getMetadata();
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByWoolData(bColor));
                entitysheep.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                enumcolor = EnumDyeColor.byMetadata((byte) event.getColor().getWoolData());
                // CraftBukkit end
                entitysheep.setFleeceColor(enumcolor);
                itemstack.shrink(1);
            }

            return true;
        } else {
            return false;
        }
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            for (int i = 0; i < 16; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }
}
