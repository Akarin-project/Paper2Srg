package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel() {
        this.field_77777_bU = 1;
        this.func_77656_e(64);
        this.func_77637_a(CreativeTabs.field_78040_i);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        blockposition = blockposition.func_177972_a(enumdirection);
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!entityhuman.func_175151_a(blockposition, enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            if (world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a) {
                // CraftBukkit start - Store the clicked block
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), org.bukkit.event.block.BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, entityhuman).isCancelled()) {
                    itemstack.func_77972_a(1, entityhuman);
                    return EnumActionResult.PASS;
                }
                // CraftBukkit end
                world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187649_bu, SoundCategory.BLOCKS, 1.0F, ItemFlintAndSteel.field_77697_d.nextFloat() * 0.4F + 0.8F);
                world.func_180501_a(blockposition, Blocks.field_150480_ab.func_176223_P(), 11);
            }

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition, itemstack);
            }

            itemstack.func_77972_a(1, entityhuman);
            return EnumActionResult.SUCCESS;
        }
    }
}
