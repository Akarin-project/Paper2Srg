package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemSkull extends Item {

    private static final String[] field_82807_a = new String[] { "skeleton", "wither", "zombie", "char", "creeper", "dragon"};

    public ItemSkull() {
        this.func_77637_a(CreativeTabs.field_78031_c);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();
            boolean flag = block.func_176200_f((IBlockAccess) world, blockposition);

            if (!flag) {
                if (!world.func_180495_p(blockposition).func_185904_a().func_76220_a()) {
                    return EnumActionResult.FAIL;
                }

                blockposition = blockposition.func_177972_a(enumdirection);
            }

            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && Blocks.field_150465_bP.func_176196_c(world, blockposition)) {
                if (world.field_72995_K) {
                    return EnumActionResult.SUCCESS;
                } else {
                    // Spigot Start
                    if ( !Blocks.field_150465_bP.func_176196_c( world, blockposition ) )
                    {
                        return EnumActionResult.FAIL;
                    }
                    // Spigot End
                    world.func_180501_a(blockposition, Blocks.field_150465_bP.func_176223_P().func_177226_a(BlockSkull.field_176418_a, enumdirection), 11);
                    int i = 0;

                    if (enumdirection == EnumFacing.UP) {
                        i = MathHelper.func_76128_c((double) (entityhuman.field_70177_z * 16.0F / 360.0F) + 0.5D) & 15;
                    }

                    TileEntity tileentity = world.func_175625_s(blockposition);

                    if (tileentity instanceof TileEntitySkull) {
                        TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;

                        if (itemstack.func_77960_j() == 3) {
                            GameProfile gameprofile = null;

                            if (itemstack.func_77942_o()) {
                                NBTTagCompound nbttagcompound = itemstack.func_77978_p();

                                if (nbttagcompound.func_150297_b("SkullOwner", 10)) {
                                    gameprofile = NBTUtil.func_152459_a(nbttagcompound.func_74775_l("SkullOwner"));
                                } else if (nbttagcompound.func_150297_b("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.func_74779_i("SkullOwner"))) {
                                    gameprofile = new GameProfile((UUID) null, nbttagcompound.func_74779_i("SkullOwner"));
                                }
                            }

                            tileentityskull.func_152106_a(gameprofile);
                        } else {
                            tileentityskull.func_152107_a(itemstack.func_77960_j());
                        }

                        tileentityskull.func_145903_a(i);
                        Blocks.field_150465_bP.func_180679_a(world, blockposition, tileentityskull);
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
        }
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            for (int i = 0; i < ItemSkull.field_82807_a.length; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }

    public int func_77647_b(int i) {
        return i;
    }

    public String func_77667_c(ItemStack itemstack) {
        int i = itemstack.func_77960_j();

        if (i < 0 || i >= ItemSkull.field_82807_a.length) {
            i = 0;
        }

        return super.func_77658_a() + "." + ItemSkull.field_82807_a[i];
    }

    public String func_77653_i(ItemStack itemstack) {
        if (itemstack.func_77960_j() == 3 && itemstack.func_77942_o()) {
            if (itemstack.func_77978_p().func_150297_b("SkullOwner", 8)) {
                return I18n.func_74837_a("item.skull.player.name", new Object[] { itemstack.func_77978_p().func_74779_i("SkullOwner")});
            }

            if (itemstack.func_77978_p().func_150297_b("SkullOwner", 10)) {
                NBTTagCompound nbttagcompound = itemstack.func_77978_p().func_74775_l("SkullOwner");

                if (nbttagcompound.func_150297_b("Name", 8)) {
                    return I18n.func_74837_a("item.skull.player.name", new Object[] { nbttagcompound.func_74779_i("Name")});
                }
            }
        }

        return super.func_77653_i(itemstack);
    }

    public boolean func_179215_a(final NBTTagCompound nbttagcompound) { // Spigot - make final
        super.func_179215_a(nbttagcompound);
        if (nbttagcompound.func_150297_b("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.func_74779_i("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID) null, nbttagcompound.func_74779_i("SkullOwner"));

            // Spigot start
            TileEntitySkull.b(gameprofile, new com.google.common.base.Predicate<GameProfile>() {

                @Override
                public boolean apply(GameProfile gameprofile) {
                    nbttagcompound.func_74782_a("SkullOwner", NBTUtil.func_180708_a(new NBTTagCompound(), gameprofile));
                    return false;
                }
            }, false);
            // Spigot end
            return true;
        } else {
            // CraftBukkit start
            NBTTagList textures = nbttagcompound.func_74775_l("SkullOwner").func_74775_l("Properties").func_150295_c("textures", 10); // Safe due to method contracts
            for (int i = 0; i < textures.func_74745_c(); i++) {
                if (textures.func_150305_b(i) instanceof NBTTagCompound && !((NBTTagCompound) textures.func_150305_b(i)).func_150297_b("Signature", 8) && ((NBTTagCompound) textures.func_150305_b(i)).func_74779_i("Value").trim().isEmpty()) {
                    nbttagcompound.func_82580_o("SkullOwner");
                    break;
                }
            }
            // CraftBukkit end
            return false;
        }
    }
}
