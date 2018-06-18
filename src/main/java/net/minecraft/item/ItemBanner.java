package net.minecraft.item;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock {

    public ItemBanner() {
        super(Blocks.STANDING_BANNER);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        boolean flag = iblockdata.getBlock().isReplaceable((IBlockAccess) world, blockposition);

        if (enumdirection != EnumFacing.DOWN && (iblockdata.getMaterial().isSolid() || flag) && (!flag || enumdirection == EnumFacing.UP)) {
            blockposition = blockposition.offset(enumdirection);
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && Blocks.STANDING_BANNER.canPlaceBlockAt(world, blockposition)) {
                if (world.isRemote) {
                    return EnumActionResult.SUCCESS;
                } else {
                    blockposition = flag ? blockposition.down() : blockposition;
                    if (enumdirection == EnumFacing.UP) {
                        int i = MathHelper.floor((double) ((entityhuman.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                        world.setBlockState(blockposition, Blocks.STANDING_BANNER.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
                    } else {
                        world.setBlockState(blockposition, Blocks.WALL_BANNER.getDefaultState().withProperty(BlockWallSign.FACING, enumdirection), 3);
                    }

                    TileEntity tileentity = world.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntityBanner) {
                        ((TileEntityBanner) tileentity).setItemValues(itemstack, false);
                    }

                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        String s = "item.banner.";
        EnumDyeColor enumcolor = getBaseColor(itemstack);

        s = s + enumcolor.getUnlocalizedName() + ".name";
        return I18n.translateToLocal(s);
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            EnumDyeColor[] aenumcolor = EnumDyeColor.values();
            int i = aenumcolor.length;

            for (int j = 0; j < i; ++j) {
                EnumDyeColor enumcolor = aenumcolor[j];

                nonnulllist.add(makeBanner(enumcolor, (NBTTagList) null));
            }
        }

    }

    public static ItemStack makeBanner(EnumDyeColor enumcolor, @Nullable NBTTagList nbttaglist) {
        ItemStack itemstack = new ItemStack(Items.BANNER, 1, enumcolor.getDyeDamage());

        if (nbttaglist != null && !nbttaglist.hasNoTags()) {
            itemstack.getOrCreateSubCompound("BlockEntityTag").setTag("Patterns", nbttaglist.copy());
        }

        return itemstack;
    }

    public CreativeTabs getCreativeTab() {
        return CreativeTabs.DECORATIONS;
    }

    public static EnumDyeColor getBaseColor(ItemStack itemstack) {
        return EnumDyeColor.byDyeDamage(itemstack.getMetadata() & 15);
    }
}
