package net.minecraft.item;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBlock extends Item {

    protected final Block field_150939_a;

    public ItemBlock(Block block) {
        this.field_150939_a = block;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        if (!block.func_176200_f((IBlockAccess) world, blockposition)) {
            blockposition = blockposition.func_177972_a(enumdirection);
        }

        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && entityhuman.func_175151_a(blockposition, enumdirection, itemstack) && world.func_190527_a(this.field_150939_a, blockposition, false, enumdirection, entityhuman)) { // Paper - Pass entityhuman instead of null
            int i = this.func_77647_b(itemstack.func_77960_j());
            IBlockState iblockdata1 = this.field_150939_a.func_180642_a(world, blockposition, enumdirection, f, f1, f2, i, entityhuman);

            if (world.func_180501_a(blockposition, iblockdata1, 11)) {
                iblockdata1 = world.func_180495_p(blockposition);
                if (iblockdata1.func_177230_c() == this.field_150939_a) {
                    func_179224_a(world, entityhuman, blockposition, itemstack);
                    this.field_150939_a.func_180633_a(world, blockposition, iblockdata1, entityhuman, itemstack);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }
                }

                SoundType soundeffecttype = this.field_150939_a.func_185467_w();

                // world.a(entityhuman, blockposition, soundeffecttype.e(), SoundCategory.BLOCKS, (soundeffecttype.a() + 1.0F) / 2.0F, soundeffecttype.b() * 0.8F); // CraftBukkit - SPIGOT-1288
                itemstack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public static boolean func_179224_a(World world, @Nullable EntityPlayer entityhuman, BlockPos blockposition, ItemStack itemstack) {
        MinecraftServer minecraftserver = world.func_73046_m();

        if (minecraftserver == null) {
            return false;
        } else {
            NBTTagCompound nbttagcompound = itemstack.func_179543_a("BlockEntityTag");

            if (nbttagcompound != null) {
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity != null) {
                    if (!world.field_72995_K && tileentity.func_183000_F() && (entityhuman == null || !entityhuman.func_189808_dh())) {
                        return false;
                    }

                    NBTTagCompound nbttagcompound1 = tileentity.func_189515_b(new NBTTagCompound());
                    NBTTagCompound nbttagcompound2 = nbttagcompound1.func_74737_b();

                    nbttagcompound1.func_179237_a(nbttagcompound);
                    nbttagcompound1.func_74768_a("x", blockposition.func_177958_n());
                    nbttagcompound1.func_74768_a("y", blockposition.func_177956_o());
                    nbttagcompound1.func_74768_a("z", blockposition.func_177952_p());
                    if (!nbttagcompound1.equals(nbttagcompound2)) {
                        tileentity.func_145839_a(nbttagcompound1);
                        tileentity.func_70296_d();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public String func_77667_c(ItemStack itemstack) {
        return this.field_150939_a.func_149739_a();
    }

    public String func_77658_a() {
        return this.field_150939_a.func_149739_a();
    }

    public CreativeTabs func_77640_w() {
        return this.field_150939_a.func_149708_J();
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            this.field_150939_a.func_149666_a(creativemodetab, nonnulllist);
        }

    }

    public Block func_179223_d() {
        return this.field_150939_a;
    }
}
