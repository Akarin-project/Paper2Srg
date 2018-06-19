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
        super(Blocks.field_180393_cK);
        this.field_77777_bU = 16;
        this.func_77637_a(CreativeTabs.field_78031_c);
        this.func_77627_a(true);
        this.func_77656_e(0);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        boolean flag = iblockdata.func_177230_c().func_176200_f((IBlockAccess) world, blockposition);

        if (enumdirection != EnumFacing.DOWN && (iblockdata.func_185904_a().func_76220_a() || flag) && (!flag || enumdirection == EnumFacing.UP)) {
            blockposition = blockposition.func_177972_a(enumdirection);
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && Blocks.field_180393_cK.func_176196_c(world, blockposition)) {
                if (world.field_72995_K) {
                    return EnumActionResult.SUCCESS;
                } else {
                    blockposition = flag ? blockposition.func_177977_b() : blockposition;
                    if (enumdirection == EnumFacing.UP) {
                        int i = MathHelper.func_76128_c((double) ((entityhuman.field_70177_z + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                        world.func_180501_a(blockposition, Blocks.field_180393_cK.func_176223_P().func_177226_a(BlockStandingSign.field_176413_a, Integer.valueOf(i)), 3);
                    } else {
                        world.func_180501_a(blockposition, Blocks.field_180394_cL.func_176223_P().func_177226_a(BlockWallSign.field_176412_a, enumdirection), 3);
                    }

                    TileEntity tileentity = world.func_175625_s(blockposition);

                    if (tileentity instanceof TileEntityBanner) {
                        ((TileEntityBanner) tileentity).func_175112_a(itemstack, false);
                    }

                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.func_190918_g(1);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public String func_77653_i(ItemStack itemstack) {
        String s = "item.banner.";
        EnumDyeColor enumcolor = func_179225_h(itemstack);

        s = s + enumcolor.func_176762_d() + ".name";
        return I18n.func_74838_a(s);
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            EnumDyeColor[] aenumcolor = EnumDyeColor.values();
            int i = aenumcolor.length;

            for (int j = 0; j < i; ++j) {
                EnumDyeColor enumcolor = aenumcolor[j];

                nonnulllist.add(func_190910_a(enumcolor, (NBTTagList) null));
            }
        }

    }

    public static ItemStack func_190910_a(EnumDyeColor enumcolor, @Nullable NBTTagList nbttaglist) {
        ItemStack itemstack = new ItemStack(Items.field_179564_cE, 1, enumcolor.func_176767_b());

        if (nbttaglist != null && !nbttaglist.func_82582_d()) {
            itemstack.func_190925_c("BlockEntityTag").func_74782_a("Patterns", nbttaglist.func_74737_b());
        }

        return itemstack;
    }

    public CreativeTabs func_77640_w() {
        return CreativeTabs.field_78031_c;
    }

    public static EnumDyeColor func_179225_h(ItemStack itemstack) {
        return EnumDyeColor.func_176766_a(itemstack.func_77960_j() & 15);
    }
}
