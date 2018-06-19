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

    public static final int[] field_150922_c = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye() {
        this.func_77627_a(true);
        this.func_77656_e(0);
        this.func_77637_a(CreativeTabs.field_78035_l);
    }

    public String func_77667_c(ItemStack itemstack) {
        int i = itemstack.func_77960_j();

        return super.func_77658_a() + "." + EnumDyeColor.func_176766_a(i).func_176762_d();
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            EnumDyeColor enumcolor = EnumDyeColor.func_176766_a(itemstack.func_77960_j());

            if (enumcolor == EnumDyeColor.WHITE) {
                if (func_179234_a(itemstack, world, blockposition)) {
                    if (!world.field_72995_K) {
                        world.func_175718_b(2005, blockposition, 0);
                    }

                    return EnumActionResult.SUCCESS;
                }
            } else if (enumcolor == EnumDyeColor.BROWN) {
                IBlockState iblockdata = world.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();

                if (block == Blocks.field_150364_r && iblockdata.func_177229_b(BlockOldLog.field_176301_b) == BlockPlanks.EnumType.JUNGLE) {
                    if (enumdirection == EnumFacing.DOWN || enumdirection == EnumFacing.UP) {
                        return EnumActionResult.FAIL;
                    }

                    blockposition = blockposition.func_177972_a(enumdirection);
                    if (world.func_175623_d(blockposition)) {
                        IBlockState iblockdata1 = Blocks.field_150375_by.func_180642_a(world, blockposition, enumdirection, f, f1, f2, 0, entityhuman);

                        world.func_180501_a(blockposition, iblockdata1, 10);
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.FAIL;
            }

            return EnumActionResult.PASS;
        }
    }

    public static boolean func_179234_a(ItemStack itemstack, World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() instanceof IGrowable) {
            IGrowable iblockfragileplantelement = (IGrowable) iblockdata.func_177230_c();

            if (iblockfragileplantelement.func_176473_a(world, blockposition, iblockdata, world.field_72995_K)) {
                if (!world.field_72995_K) {
                    if (iblockfragileplantelement.func_180670_a(world, world.field_73012_v, blockposition, iblockdata)) {
                        iblockfragileplantelement.func_176474_b(world, world.field_73012_v, blockposition, iblockdata);
                    }

                    itemstack.func_190918_g(1);
                }

                return true;
            }
        }

        return false;
    }

    public boolean func_111207_a(ItemStack itemstack, EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            EnumDyeColor enumcolor = EnumDyeColor.func_176766_a(itemstack.func_77960_j());

            if (!entitysheep.func_70892_o() && entitysheep.func_175509_cj() != enumcolor) {
                // CraftBukkit start
                byte bColor = (byte) enumcolor.func_176765_a();
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByWoolData(bColor));
                entitysheep.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                enumcolor = EnumDyeColor.func_176764_b((byte) event.getColor().getWoolData());
                // CraftBukkit end
                entitysheep.func_175512_b(enumcolor);
                itemstack.func_190918_g(1);
            }

            return true;
        } else {
            return false;
        }
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            for (int i = 0; i < 16; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }
}
