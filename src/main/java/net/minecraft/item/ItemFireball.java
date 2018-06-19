package net.minecraft.item;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemFireball extends Item {

    public ItemFireball() {
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return EnumActionResult.SUCCESS;
        } else {
            blockposition = blockposition.func_177972_a(enumdirection);
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (!entityhuman.func_175151_a(blockposition, enumdirection, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                if (world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a) {
                    // CraftBukkit start - fire BlockIgniteEvent
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), org.bukkit.event.block.BlockIgniteEvent.IgniteCause.FIREBALL, entityhuman).isCancelled()) {
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }
                        return EnumActionResult.PASS;
                    }
                    // CraftBukkit end
                    world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187616_bj, SoundCategory.BLOCKS, 1.0F, (ItemFireball.field_77697_d.nextFloat() - ItemFireball.field_77697_d.nextFloat()) * 0.2F + 1.0F);
                    world.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                }

                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    itemstack.func_190918_g(1);
                }

                return EnumActionResult.SUCCESS;
            }
        }
    }
}
